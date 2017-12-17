package com.prathab.data.mongodb.services;

import com.mongodb.client.MongoCollection;
import com.prathab.data.base.DbObjectSpec;
import com.prathab.data.base.DbService;
import com.prathab.data.base.dbmodel.DbObject;
import com.prathab.data.base.result.DeleteResult;
import com.prathab.data.base.result.InsertResult;
import com.prathab.data.base.result.ReadResult;
import com.prathab.data.base.result.UpdateResult;
import com.prathab.data.constants.DBConstants;
import com.prathab.data.datamodels.Users;

import org.bson.Document;

import static com.mongodb.client.model.Filters.eq;
import static com.prathab.data.constants.DBConstants.DB_COLLECTION_USERS_MOBILE;
import static com.prathab.data.constants.DBConstants.DB_COLLECTION_USERS_NAME;
import static com.prathab.data.constants.DBConstants.DB_COLLECTION_USERS_PASSWORD;

public class MongoDBAccountsService implements DbService {

	private static MongoCollection<Document> mUsersCollection = MongoClientService.getUsersCollection();

	@Override
	public ReadResult read(DbObjectSpec spec) {
		Users inputSpec = (Users) spec.getObject();
		String mobile = inputSpec.getMobile();
		String email = inputSpec.getEmail();

		String nonNullValue = null;
		String nonNullKey = null;
		if (mobile != null) {
			nonNullValue = mobile;
			nonNullKey = DBConstants.DB_COLLECTION_USERS_MOBILE;
		} else if (email != null) {
			nonNullValue = email;
			nonNullKey = DBConstants.DB_COLLECTION_USERS_EMAIL;
		} else {
			assert false;
		}

		Document fetchedDocument = mUsersCollection.find(eq(nonNullKey, nonNullValue)).first();

		Users fetchedUsers = new Users();
		fetchedUsers.setName(fetchedDocument.getString(DBConstants.DB_COLLECTION_USERS_NAME));
		fetchedUsers.setMobile(fetchedDocument.getString(DBConstants.DB_COLLECTION_USERS_MOBILE));
		fetchedUsers.setPassword(fetchedDocument.getString(DBConstants.DB_COLLECTION_USERS_PASSWORD));

		return new ReadResult(fetchedUsers);
	}

	@Override
	public DeleteResult delete(DbObjectSpec spec) {
		Users inputSpec = (Users) spec.getObject();
		String mobile = inputSpec.getMobile();
		String email = inputSpec.getEmail();

		String nonNullValue = null;
		String nonNullKey = null;
		if (mobile != null) {
			nonNullValue = mobile;
			nonNullKey = DBConstants.DB_COLLECTION_USERS_MOBILE;
		} else if (email != null) {
			nonNullValue = email;
			nonNullKey = DBConstants.DB_COLLECTION_USERS_EMAIL;
		} else {
			assert false;
		}

		Document deleteUsers = new Document(nonNullKey, nonNullValue);
		DeleteResult deleteResult = new DeleteResult();

		try {
			mUsersCollection.deleteOne(deleteUsers);
		} catch (Exception e) {
			System.out.println("Cannot delete user : " + e.getMessage());
			deleteResult.setSuccessful(false);
		}

		return deleteResult;
	}

	@Override
	public InsertResult insert(DbObject object) {
		Users inputUsers = (Users) object;

		Document newUser = new Document(DB_COLLECTION_USERS_NAME, inputUsers.getName());
		newUser.append(DB_COLLECTION_USERS_MOBILE, inputUsers.getMobile());
		newUser.append(DB_COLLECTION_USERS_PASSWORD, inputUsers.getPassword());

		InsertResult insertResult = new InsertResult();

		try {
			mUsersCollection.insertOne(newUser);
		} catch (Exception e) {
			System.out.println("Cannot insert user : " + e.getMessage());
			insertResult.setSuccessful(false);
		}

		return insertResult;
	}

	@Override
	public UpdateResult update(DbObjectSpec spec) {
		// TODO implement this
		assert false;
		return null;
	}
}
