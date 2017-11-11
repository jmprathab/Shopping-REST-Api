package com.prathab.data.services;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.DeleteResult;
import com.prathab.data.mongodb.MongoClientService;
import org.bson.Document;

import static com.mongodb.client.model.Filters.eq;

public class AccountsService {

  private static MongoCollection<Document> mUsersCollection =
      MongoClientService.getUsersCollection();

  public static Document fetch(String property, String value) {
    return mUsersCollection.find(eq(property, value)).first();
  }

  public static DeleteResult delete(Document document) {
    return mUsersCollection.deleteOne(document);
  }

  public static void insert(Document newUser) throws Exception {
    mUsersCollection.insertOne(newUser);
  }
}
