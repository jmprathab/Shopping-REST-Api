package com.prathab.data.mongodb.services;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.prathab.data.constants.DBConstants;

public class MongoClientService {
	private static final String MONGODB_HOST = "localhost";
	private static final int MONGODB_PORT = 27017;

	private static MongoClient sMongoClient = new MongoClient(MONGODB_HOST, MONGODB_PORT);

	private MongoClientService() {
	}

	private static MongoDatabase getDatabase() {
		return sMongoClient.getDatabase(DBConstants.DB_DATABASE_NAME);
	}

	public static MongoCollection<Document> getProductsCollection() {
		return getDatabase().getCollection(DBConstants.DB_COLLECTION_PRODUCTS);
	}

	public static MongoCollection<Document> getUsersCollection() {
		return getDatabase().getCollection(DBConstants.DB_COLLECTION_USERS);
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		sMongoClient.close();
	}
}
