package com.prathab.data.mongodb;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class MongoClientService {
  private static final String MONGODB_HOST = "localhost";
  private static final int MONGODB_PORT = 27017;

  private static MongoClient sMongoClient = new MongoClient(MONGODB_HOST, MONGODB_PORT);

  static MongoClient getMongoClient() {
    return sMongoClient;
  }

  public static MongoDatabase getDatabase(String database) {
    return sMongoClient.getDatabase(database);
  }

  public static MongoCollection<Document> getCollection(String databaseName,
      String collectionName) {
    return getDatabase(databaseName).getCollection(collectionName);
  }

  @Override
  protected void finalize() throws Throwable {
    super.finalize();
    sMongoClient.close();
  }
}
