package com.prathab.api.shopping.services;

import com.mongodb.client.MongoCollection;
import com.prathab.api.shopping.constants.HttpConstants;
import com.prathab.api.shopping.datamodels.Users;
import java.util.HashMap;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;
import org.bson.Document;
import org.mindrot.jbcrypt.BCrypt;

import static com.mongodb.client.model.Filters.eq;
import static com.prathab.api.shopping.constants.DBConstants.DB_COLLECTION_USERS;
import static com.prathab.api.shopping.constants.DBConstants.DB_COLLECTION_USERS_EMAIL;
import static com.prathab.api.shopping.constants.DBConstants.DB_COLLECTION_USERS_MOBILE;
import static com.prathab.api.shopping.constants.DBConstants.DB_COLLECTION_USERS_NAME;
import static com.prathab.api.shopping.constants.DBConstants.DB_COLLECTION_USERS_PASSWORD;
import static com.prathab.api.shopping.constants.DBConstants.DB_DATABASE_NAME;
import static com.prathab.api.shopping.constants.JwtConstants.JWT_CLAIM_MOBILE;
import static com.prathab.api.shopping.constants.JwtConstants.JWT_CLAIM_NAME;
import static com.prathab.api.shopping.constants.JwtConstants.JWT_CLAIM_USERS_TYPE;
import static com.prathab.api.shopping.utility.JwtUtility.createAndGetJWT;

public class AccountsService {

  public static Response loginTheUser(UriInfo uriInfo, Users users) {
    MongoCollection<Document> collection = MongoClientService
        .getCollection(DB_DATABASE_NAME, DB_COLLECTION_USERS);

    Document fetchedDocument = collection
        .find(eq(DB_COLLECTION_USERS_MOBILE, users.getMobile()))
        .first();

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

  public static Response createNewAccount(UriInfo uriInfo, Users users) {
    MongoCollection<Document> collection =
        MongoClientService.getCollection(DB_DATABASE_NAME, DB_COLLECTION_USERS);
    Document fetchedDocument =
        collection.find(eq(DB_COLLECTION_USERS_MOBILE, users.getMobile())).first();

    if (fetchedDocument != null) {
      return Response.status(Status.CONFLICT).build();
    }

    // Create a new account
    String hashedPassword = BCrypt.hashpw(users.getPassword(), BCrypt.gensalt(12));

    Document newUser = new Document(DB_COLLECTION_USERS_NAME, users.getName());
    newUser.append(DB_COLLECTION_USERS_MOBILE, users.getMobile());
    newUser.append(DB_COLLECTION_USERS_PASSWORD, hashedPassword);

    try {
      collection.insertOne(newUser);
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

  public static Response forgotPassword(UriInfo uriInfo, Users users) {
    MongoCollection<Document> collection =
        MongoClientService.getCollection(DB_DATABASE_NAME, DB_COLLECTION_USERS);
    Document fetchedDocument =
        collection.find(eq(DB_COLLECTION_USERS_EMAIL, users.getEmail())).first();

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

  public static Response deleteAccount(UriInfo uriInfo, Users users) {
    MongoCollection<Document> collection = MongoClientService
        .getCollection(DB_DATABASE_NAME, DB_COLLECTION_USERS);

    Document fetchedDocument = collection
        .find(eq(DB_COLLECTION_USERS_MOBILE, users.getMobile()))
        .first();

    if (fetchedDocument == null
        || !BCrypt.checkpw(users.getPassword(),
        fetchedDocument.getString(DB_COLLECTION_USERS_PASSWORD))) {

      return Response.status(Status.UNAUTHORIZED).build();
    }
    collection.deleteOne(fetchedDocument);
    return Response.ok().build();
  }
}
