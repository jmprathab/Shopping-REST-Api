package com.prathab.data.services;

import com.mongodb.client.MongoCollection;
import com.prathab.data.datamodels.Users;
import com.prathab.data.mongodb.MongoClientService;
import org.bson.Document;

import static com.mongodb.client.model.Filters.eq;
import static com.prathab.data.constants.DBConstants.DB_COLLECTION_USERS;
import static com.prathab.data.constants.DBConstants.DB_COLLECTION_USERS_EMAIL;
import static com.prathab.data.constants.DBConstants.DB_COLLECTION_USERS_MOBILE;
import static com.prathab.data.constants.DBConstants.DB_DATABASE_NAME;

public class AccountsService {

  public static Document loginTheUser(Users users) {
    MongoCollection<Document> collection = MongoClientService
        .getCollection(DB_DATABASE_NAME, DB_COLLECTION_USERS);

    Document fetchedDocument = collection
        .find(eq(DB_COLLECTION_USERS_MOBILE, users.getMobile()))
        .first();

    return fetchedDocument;
  }

  public static Document createNewAccount(Users users) {
    MongoCollection<Document> collection =
        MongoClientService.getCollection(DB_DATABASE_NAME, DB_COLLECTION_USERS);

    Document fetchedDocument =
        collection.find(eq(DB_COLLECTION_USERS_MOBILE, users.getMobile())).first();

    return fetchedDocument;
  }

  public static Document forgotPassword(Users users) {
    MongoCollection<Document> collection =
        MongoClientService.getCollection(DB_DATABASE_NAME, DB_COLLECTION_USERS);
    Document fetchedDocument =
        collection.find(eq(DB_COLLECTION_USERS_EMAIL, users.getEmail())).first();

    return fetchedDocument;
  }

  public static Document deleteAccount(Users users) {
    MongoCollection<Document> collection = MongoClientService
        .getCollection(DB_DATABASE_NAME, DB_COLLECTION_USERS);

    Document fetchedDocument = collection
        .find(eq(DB_COLLECTION_USERS_MOBILE, users.getMobile()))
        .first();

    return fetchedDocument;
  }
}
