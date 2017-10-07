package com.prathab.api.shopping.resources;

import com.prathab.api.shopping.datamodels.Users;
import com.prathab.api.shopping.services.AccountsService;
import com.prathab.api.shopping.utility.Validators;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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

import static com.prathab.api.shopping.constants.HttpConstants.HTTP_PARAM_EMAIL;

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
    String validatedMobile;
    validatedMobile = Validators.getInternationalPhoneNumber(users.getMobile());
    if (validatedMobile == null) {
      isUserDataValid = false;
    }
    users.setMobile(validatedMobile);
    if (isUserDataValid) {
      return AccountsService.createNewAccount(uriInfo, users);
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
      return AccountsService.loginTheUser(uriInfo, users);
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
      return AccountsService.forgotPassword(uriInfo, users);
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
      return AccountsService.deleteAccount(uriInfo, users);
    }
    return Response.status(Status.UNAUTHORIZED).build();
  }
}
