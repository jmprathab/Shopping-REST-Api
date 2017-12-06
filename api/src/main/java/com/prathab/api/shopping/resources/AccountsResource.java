package com.prathab.api.shopping.resources;

import static com.prathab.api.shopping.constants.HttpConstants.HTTP_PARAM_EMAIL;
import static com.prathab.api.shopping.constants.JwtConstants.JWT_CLAIM_MOBILE;
import static com.prathab.api.shopping.constants.JwtConstants.JWT_CLAIM_NAME;
import static com.prathab.api.shopping.constants.JwtConstants.JWT_CLAIM_USERS_TYPE;
import static com.prathab.api.shopping.utility.JwtUtility.createAndGetJWT;

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

import org.mindrot.jbcrypt.BCrypt;

import com.prathab.api.shopping.constants.HttpConstants;
import com.prathab.api.shopping.utility.Validators;
import com.prathab.data.base.DbObjectSpec;
import com.prathab.data.base.DbService;
import com.prathab.data.base.exception.DbException;
import com.prathab.data.base.result.DeleteResult;
import com.prathab.data.base.result.InsertResult;
import com.prathab.data.datamodels.Users;
import com.prathab.data.mysql.services.MysqlAccountsService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(value = "/accounts", description = "Operations on User's account")
@Path("/accounts")
public class AccountsResource {

  private static final DbService mDbAccountsService = new MysqlAccountsService();

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

    boolean isUserDataValid = validateUserData(users);

    String validatedMobile = Validators.getInternationalPhoneNumber(users.getMobile());

    if (validatedMobile == null) {
      isUserDataValid = false;
    }
    users.setMobile(validatedMobile);

    if (isUserDataValid) {

      Users readSpec = new Users();
      readSpec.setMobile(users.getMobile());

      DbObjectSpec spec = new DbObjectSpec(readSpec);

      Users fetchedUsers = null;
      try {
        fetchedUsers = (Users) mDbAccountsService.read(spec).getDbObject();
      } catch (DbException e) {
        e.printStackTrace();
      }

      if (fetchedUsers != null) {
        return Response.status(Status.CONFLICT).build();
      }

      // Create a new account
      String hashedPassword = BCrypt.hashpw(users.getPassword(), BCrypt.gensalt(12));

      Users newAccountUsers = new Users();
      newAccountUsers.setName(users.getName());
      newAccountUsers.setMobile(users.getMobile());
      newAccountUsers.setPassword(hashedPassword);

      InsertResult insertResult = null;
      try {
        insertResult = mDbAccountsService.insert(newAccountUsers);
      } catch (DbException e) {
        e.printStackTrace();
      }

      if (!insertResult.isSuccessful()) {
        return Response.status(Status.BAD_REQUEST).build();
      }

      HashMap<String, String> claims = new HashMap<>();
      claims.put(JWT_CLAIM_NAME, users.getName());
      claims.put(JWT_CLAIM_MOBILE, users.getMobile());
      claims.put(JWT_CLAIM_USERS_TYPE, "User");

      String jwt = createAndGetJWT(claims);

      if (jwt != null) {
        return Response.ok().header(HttpConstants.HTTP_HEADER_TOKEN, jwt).build();
      }
      return Response.status(Status.BAD_REQUEST).build();
    }
    return Response.status(Status.BAD_REQUEST).build();
  }

  private boolean validateUserData(Users users) {
    return !Validators.isStringEmpty(users.getName())
        && !Validators.isStringEmpty(users.getMobile())
        && !Validators.isStringEmpty(users.getPassword());
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
      @ApiParam(value = "Should contain mobile, password", required = true) Users inputUsers) {

    System.out.println(inputUsers.getMobile() + " " + inputUsers.getPassword());

    boolean isUserDataValid = true;

    if (Validators.isStringEmpty(inputUsers.getMobile())
        || Validators.isStringEmpty(inputUsers.getPassword())) {

      isUserDataValid = false;
    }

    String validatedMobile = Validators.getInternationalPhoneNumber(inputUsers.getMobile());

    if (validatedMobile == null) {
      isUserDataValid = false;
    }

    inputUsers.setMobile(validatedMobile);

    if (isUserDataValid) {
      Users readSpec = new Users();
      readSpec.setMobile(inputUsers.getMobile());

      DbObjectSpec dbSpec = new DbObjectSpec(readSpec);

      Users fetchedUsers = null;
      try {
        fetchedUsers = (Users) mDbAccountsService.read(dbSpec).getDbObject();
      } catch (DbException e) {
        e.printStackTrace();
      }

      if (fetchedUsers == null
          || !BCrypt.checkpw(inputUsers.getPassword(),
          fetchedUsers.getPassword())) {

        return Response.status(Status.UNAUTHORIZED).build();
      }

      inputUsers.setName(fetchedUsers.getName());

      HashMap<String, String> claims = new HashMap<>();
      claims.put(JWT_CLAIM_NAME, inputUsers.getName());
      claims.put(JWT_CLAIM_MOBILE, inputUsers.getMobile());
      claims.put(JWT_CLAIM_USERS_TYPE, "User");

      System.out.println(
          inputUsers.getName() + " " + inputUsers.getMobile() + " " + inputUsers.getPassword());

      String jwt = createAndGetJWT(claims);

      if (jwt != null) {
        return Response.ok(inputUsers, MediaType.APPLICATION_JSON_TYPE)
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

      Users users = new Users();
      users.setEmail(email);

      Users forgotUsersSpec = new Users();
      forgotUsersSpec.setEmail(email);
      DbObjectSpec forgotSpec = new DbObjectSpec(forgotUsersSpec);
      Users fetchedUsers = null;
      try {
        fetchedUsers = (Users) mDbAccountsService.read(forgotSpec).getDbObject();
      } catch (DbException e) {
        e.printStackTrace();
      }

      if (fetchedUsers == null) {
        return Response.status(Status.BAD_REQUEST).build();
      }

      String to = fetchedUsers.getEmail();
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

    String validatedMobile = Validators.getInternationalPhoneNumber(users.getMobile());
    if (validatedMobile == null) {
      isUserDataValid = false;
    }
    users.setMobile(validatedMobile);

    if (isUserDataValid) {

      Users readUsersSpec = new Users();
      readUsersSpec.setMobile(users.getMobile());
      DbObjectSpec readSpec = new DbObjectSpec(readUsersSpec);

      Users fetchedUsers = null;
      try {
        fetchedUsers = (Users) mDbAccountsService.read(readSpec).getDbObject();
      } catch (DbException e) {
        e.printStackTrace();
      }

      if (fetchedUsers == null
          || !BCrypt.checkpw(users.getPassword(), fetchedUsers.getPassword())) {

        return Response.status(Status.UNAUTHORIZED).build();
      }

      Users deleteUsersSpec = new Users();
      deleteUsersSpec.setMobile(users.getMobile());
      DbObjectSpec deleteSpec = new DbObjectSpec(deleteUsersSpec);

      DeleteResult deleteResult = null;
      try {
        deleteResult = mDbAccountsService.delete(deleteSpec);
      } catch (DbException e) {
        e.printStackTrace();
      }
      if (deleteResult.isSuccessful()) {
        return Response.ok().build();
      }
    }
    return Response.status(Status.UNAUTHORIZED).build();
  }
}
