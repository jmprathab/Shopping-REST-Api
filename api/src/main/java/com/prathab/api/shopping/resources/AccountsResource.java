package com.prathab.api.shopping.resources;

import com.prathab.api.shopping.constants.HttpConstants;
import com.prathab.api.shopping.utility.Validators;
import com.prathab.data.constants.DBConstants;
import com.prathab.data.datamodels.Users;
import com.prathab.data.services.AccountsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.HashMap;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;
import org.bson.Document;
import org.mindrot.jbcrypt.BCrypt;

import static com.prathab.api.shopping.constants.HttpConstants.HTTP_PARAM_EMAIL;
import static com.prathab.api.shopping.constants.JwtConstants.JWT_CLAIM_MOBILE;
import static com.prathab.api.shopping.constants.JwtConstants.JWT_CLAIM_NAME;
import static com.prathab.api.shopping.constants.JwtConstants.JWT_CLAIM_USERS_TYPE;
import static com.prathab.api.shopping.utility.JwtUtility.createAndGetJWT;
import static com.prathab.data.constants.DBConstants.DB_COLLECTION_USERS_EMAIL;
import static com.prathab.data.constants.DBConstants.DB_COLLECTION_USERS_MOBILE;
import static com.prathab.data.constants.DBConstants.DB_COLLECTION_USERS_NAME;
import static com.prathab.data.constants.DBConstants.DB_COLLECTION_USERS_PASSWORD;

@Api(value = "/accounts", description = "Operations on User's account")
@Path("/accounts")
public class AccountsResource {

  @ApiOperation(
      value = "Create a new User Account",
      notes = "Create Account requires name, mobile and password")
  @ApiResponses(value = {
      @ApiResponse(code = 400, message = "Input fields are empty or null or incorrect"),
      @ApiResponse(code = 409, message = "Account already exists"),
      @ApiResponse(code = 200, message = "Account was created successfully")})
  @POST
  @Path("/create")
  @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
  public Response createNewAccount(@Context UriInfo uriInfo,
      @ApiParam(value = "Should contain name, mobile, password", required = true) Users users) {

    boolean isUserDataValid = true;
    if (Validators.isStringEmpty(users.getName())
        || Validators.isStringEmpty(users.getMobile())
        || Validators.isStringEmpty(users.getPassword())) {

      isUserDataValid = false;
    }

    String validatedMobile = Validators.getInternationalPhoneNumber(users.getMobile());
    if (validatedMobile == null) {
      isUserDataValid = false;
    }
    users.setMobile(validatedMobile);
    if (isUserDataValid) {
      Document fetchedDocument =
          AccountsService.fetch(DBConstants.DB_COLLECTION_USERS_MOBILE, users.getMobile());

      if (fetchedDocument != null) {
        return Response.status(Status.CONFLICT).build();
      }

      // Create a new account
      String hashedPassword = BCrypt.hashpw(users.getPassword(), BCrypt.gensalt(12));

      Document newUser = new Document(DB_COLLECTION_USERS_NAME, users.getName());
      newUser.append(DB_COLLECTION_USERS_MOBILE, users.getMobile());
      newUser.append(DB_COLLECTION_USERS_PASSWORD, hashedPassword);

      try {
        AccountsService.insert(newUser);
      } catch (Exception e) {
        return Response.status(Status.BAD_REQUEST).build();
      }

      HashMap<String, String> claims = new HashMap<>();
      claims.put(JWT_CLAIM_NAME, users.getName());
      claims.put(JWT_CLAIM_MOBILE, users.getMobile());
      claims.put(JWT_CLAIM_USERS_TYPE, Users.UsersTypes.USER);

      String jwt = createAndGetJWT(claims);

      if (jwt != null) {
        return Response.ok().header(HttpConstants.HTTP_HEADER_TOKEN, jwt).build();
      }
      return Response.status(Status.BAD_REQUEST).build();
    }
    return Response.status(Status.BAD_REQUEST).build();
  }

  @ApiOperation(
      value = "Login to the existing User account",
      notes = "Login requires mobile and password")
  @ApiResponses(value = {
      @ApiResponse(code = 401, message = "Input fields are empty or null or mobile number or password is incorrect"),
      @ApiResponse(code = 200, message = "Account was created successfully")})
  @POST
  @Path("/login")
  @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
  @Produces(MediaType.APPLICATION_JSON)
  public Response loginTheUser(@Context UriInfo uriInfo,
      @ApiParam(value = "Should contain mobile, password", required = true) Users users) {

    System.out.println(users.getMobile() + " " + users.getPassword());

    boolean isUserDataValid = true;
    if (Validators.isStringEmpty(users.getMobile())
        || Validators.isStringEmpty(users.getPassword())) {

      isUserDataValid = false;
    }

    String validatedMobile;
    validatedMobile = Validators.getInternationalPhoneNumber(users.getMobile());
    if (validatedMobile == null) {
      isUserDataValid = false;
    }

    users.setMobile(validatedMobile);
    if (isUserDataValid) {
      Document fetchedDocument =
          AccountsService.fetch(DBConstants.DB_COLLECTION_USERS_EMAIL, users.getMobile());

      if (fetchedDocument == null
          || !BCrypt.checkpw(users.getPassword(),
          fetchedDocument.getString(DB_COLLECTION_USERS_PASSWORD))) {

        return Response.status(Status.UNAUTHORIZED).build();
      }

      users.setName(fetchedDocument.getString(DB_COLLECTION_USERS_NAME));

      HashMap<String, String> claims = new HashMap<>();
      claims.put(JWT_CLAIM_NAME, users.getName());
      claims.put(JWT_CLAIM_MOBILE, users.getMobile());
      claims.put(JWT_CLAIM_USERS_TYPE, Users.UsersTypes.USER);

      System.out.println(users.getName() + " " + users.getMobile() + " " + users.getPassword());

      String jwt = createAndGetJWT(claims);

      if (jwt != null) {
        return Response.ok(users, MediaType.APPLICATION_JSON_TYPE)
            .header(HttpConstants.HTTP_HEADER_TOKEN, jwt)
            .build();
      }
      return Response.status(Status.BAD_REQUEST).build();
    }
    return Response.status(Status.UNAUTHORIZED).build();
  }

  @ApiOperation(
      value = "Reset Account Password",
      notes = "Forgot Password requires email")
  @ApiResponses(value = {
      @ApiResponse(code = 401, message = "Input fields are empty or user did not update email for their account"),
      @ApiResponse(code = 200, message = "Successfully sent email to registered email address")})
  @POST
  @Path("/forgot")
  @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
  public Response forgotPassword(@Context UriInfo uriInfo,
      @ApiParam(value = "Email id of User", required = true)
      @FormParam(HTTP_PARAM_EMAIL) String email) {

    boolean isUserDataValid = true;
    if (!Validators.isEmailAddressValid(email)) {
      isUserDataValid = false;
    }
    if (isUserDataValid) {
      Users users = new Users.Builder()
          .setEmail(email).build();
      Document fetchedDocument =
          AccountsService.fetch(DBConstants.DB_COLLECTION_USERS_EMAIL, users.getEmail());

      if (fetchedDocument == null) {
        return Response.status(Status.BAD_REQUEST).build();
      }

      String to = fetchedDocument.getString(DB_COLLECTION_USERS_EMAIL);
      String from = "admin@shopping.com";

      Properties properties = System.getProperties();
      properties.setProperty("mail.smtp.host", "localhost");

      Session session = Session.getDefaultInstance(properties);

      try {
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(from));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
        message.setSubject("Reset Password for your Shopping account");
        message.setText("You will be getting an email from us shortly to reset your password");
        Transport.send(message);
      } catch (MessagingException e) {
        return Response.status(Status.UNAUTHORIZED).build();
      }
      //TODO Send correct link to reset password
      return Response.ok().build();
    }
    return Response.status(Status.UNAUTHORIZED).build();
  }

  @ApiOperation(
      value = "Delete User's account",
      notes = "Delete account requires mobile and password")
  @ApiResponses(value = {
      @ApiResponse(code = 400, message = "Input fields are empty or Mobile or password incorrect"),
      @ApiResponse(code = 200, message = "Successfully deleted account")})
  @POST
  @Path("/delete")
  @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
  public Response deleteAccount(@Context UriInfo uriInfo,
      @ApiParam(value = "Should contain mobile, password", required = true) Users users) {

    boolean isUserDataValid = true;
    if (Validators.isStringEmpty(users.getMobile())
        || Validators.isStringEmpty(users.getPassword())) {

      isUserDataValid = false;
    }

    String validatedMobile;
    validatedMobile = Validators.getInternationalPhoneNumber(users.getMobile());
    if (validatedMobile == null) {
      isUserDataValid = false;
    }
    users.setMobile(validatedMobile);

    if (isUserDataValid) {
      Document fetchedDocument =
          AccountsService.fetch(DBConstants.DB_COLLECTION_USERS_MOBILE, users.getMobile());
      if (fetchedDocument == null
          || !BCrypt.checkpw(users.getPassword(),
          fetchedDocument.getString(DB_COLLECTION_USERS_PASSWORD))) {

        return Response.status(Status.UNAUTHORIZED).build();
      }

      AccountsService.delete(fetchedDocument);

      return Response.ok().build();
    }
    return Response.status(Status.UNAUTHORIZED).build();
  }
}
