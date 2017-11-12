package com.prathab.data;

import com.prathab.data.constants.DBConstants;
import com.prathab.data.services.CartsService;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.testng.annotations.Test;

public class TestCartsService {

  @Test
  public void testCartsServiceDelete() {
    CartsService.delete("+91 81100 31139", "59cbfaab50abfd5945ccac10");
  }

  @Test
  public void testCartsServiceInsert() throws Exception {

    Document cartItem = new Document(DBConstants.DB_COLLECTION_USERS_CART_PRODUCT_ID,
        new ObjectId("59cbfaab50abfd5945ccac10"));
    cartItem.append(DBConstants.DB_COLLECTION_USERS_CART_QUANTITY, 1);

    CartsService.fetch("+91 81100 31139", "59cbfaab50abfd5945ccac10");
    CartsService.insert("+91 81100 31139", cartItem);

    Document cartItem2 = new Document(DBConstants.DB_COLLECTION_USERS_CART_PRODUCT_ID,
        new ObjectId("59cbfaab50abfd5945ccac1e"));
    cartItem2.append(DBConstants.DB_COLLECTION_USERS_CART_QUANTITY, 1);
    CartsService.insert("+91 81100 31139", cartItem2);
    CartsService.delete("+91 81100 31139", "59cbfaab50abfd5945ccac1e");
  }
}
