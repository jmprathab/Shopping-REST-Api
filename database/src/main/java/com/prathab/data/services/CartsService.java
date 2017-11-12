package com.prathab.data.services;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.DeleteResult;
import com.prathab.data.constants.DBConstants;
import com.prathab.data.mongodb.MongoClientService;
import java.util.ArrayList;
import org.bson.Document;
import org.bson.types.ObjectId;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.elemMatch;
import static com.mongodb.client.model.Filters.eq;

public class CartsService {

  private static final MongoCollection<Document> mUsersCollection =
      MongoClientService.getUsersCollection();

  public static Document fetch(String mobile, String productId) {
    Document users =
        mUsersCollection.find(eq(DBConstants.DB_COLLECTION_USERS_MOBILE, mobile)).first();

    ArrayList cart = users.get(DBConstants.DB_COLLECTION_USERS_CART, ArrayList.class);
    for (Object aCart : cart) {
      Document item = (Document) aCart;
      if (item.get(DBConstants.DB_COLLECTION_USERS_CART_PRODUCT_ID, ObjectId.class).toHexString()
          .contentEquals(productId)) {
        return item;
      }
    }
    return null;
  }

  public static DeleteResult delete(String mobile, String productId) {
    Document users =
        mUsersCollection.find(eq(DBConstants.DB_COLLECTION_USERS_MOBILE, mobile)).first();

    assert users != null;

    String findProductId = "{ productId: ObjectId('" + productId + "') }";

    mUsersCollection.deleteOne(and(eq(DBConstants.DB_COLLECTION_USERS_MOBILE, mobile),
        elemMatch(DBConstants.DB_COLLECTION_USERS_CART, Document.parse(findProductId))));

    return null;
  }

  public static void insert(String mobile, Document cartItem) throws Exception {
    mUsersCollection.updateOne(eq(DBConstants.DB_COLLECTION_USERS_MOBILE, mobile),
        Updates.addToSet(DBConstants.DB_COLLECTION_USERS_CART, cartItem),
        new UpdateOptions().upsert(true).bypassDocumentValidation(true));

    //mUsersCollection.insertOne(cartItem);
  }
}
