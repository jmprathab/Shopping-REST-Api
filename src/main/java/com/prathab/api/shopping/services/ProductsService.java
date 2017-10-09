package com.prathab.api.shopping.services;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.prathab.api.shopping.datamodels.Products;
import java.util.ArrayList;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import org.bson.Document;
import org.bson.types.ObjectId;

import static com.prathab.api.shopping.constants.DBConstants.DB_COLLECTION_PRODUCTS;
import static com.prathab.api.shopping.constants.DBConstants.DB_COLLECTION_PRODUCTS_DESCRIPTION;
import static com.prathab.api.shopping.constants.DBConstants.DB_COLLECTION_PRODUCTS_ID;
import static com.prathab.api.shopping.constants.DBConstants.DB_COLLECTION_PRODUCTS_IMAGES;
import static com.prathab.api.shopping.constants.DBConstants.DB_COLLECTION_PRODUCTS_NAME;
import static com.prathab.api.shopping.constants.DBConstants.DB_COLLECTION_PRODUCTS_PRICE;
import static com.prathab.api.shopping.constants.DBConstants.DB_COLLECTION_PRODUCTS_RATING;
import static com.prathab.api.shopping.constants.DBConstants.DB_COLLECTION_PRODUCTS_TAGS;
import static com.prathab.api.shopping.constants.DBConstants.DB_DATABASE_NAME;

public class ProductsService {
  public static Response fetchProducts(UriInfo uriInfo, int page, int limit) {
    if (page < 0 || page > 1000) {
      page = 0;
    }
    if (limit <= 0 || limit > 50) {
      limit = 50;
    }

    ArrayList<Products> productsList = new ArrayList<>();
    MongoCollection<Document> collection = MongoClientService
        .getCollection(DB_DATABASE_NAME, DB_COLLECTION_PRODUCTS);

    MongoCursor<Document> fetchedDocument =
        collection.find().skip(page > 0 ? ((page - 1) * limit) : 0).limit(limit).iterator();

    try {
      while (fetchedDocument.hasNext()) {
        Document current = fetchedDocument.next();
        ObjectId objectId = current.getObjectId(DB_COLLECTION_PRODUCTS_ID);
        String id = objectId.toString();
        String name = current.getString(DB_COLLECTION_PRODUCTS_NAME);
        String price = current.getString(DB_COLLECTION_PRODUCTS_PRICE);
        int rating = current.getInteger(DB_COLLECTION_PRODUCTS_RATING);

        ArrayList<String> imagesBasicDBList =
            (ArrayList<String>) current.get(DB_COLLECTION_PRODUCTS_IMAGES);
        ArrayList<String> images = new ArrayList<>();
        for (String anImagesBasicDBList : imagesBasicDBList) {
          images.add(anImagesBasicDBList);
        }

        ArrayList<String> tagsBasicDBList =
            (ArrayList<String>) current.get(DB_COLLECTION_PRODUCTS_TAGS);
        ArrayList<String> tags = new ArrayList<>();
        for (String aTagsBasicDBList : tagsBasicDBList) {
          tags.add(aTagsBasicDBList);
        }

        String description = current.getString(DB_COLLECTION_PRODUCTS_DESCRIPTION);

        Products products = new Products(id, name, price, rating, images, tags, description);

        productsList.add(products);

        System.out.println(products.toString());
      }
    } finally {
      fetchedDocument.close();
    }

    GenericEntity<ArrayList<Products>> entity =
        new GenericEntity<ArrayList<Products>>(productsList) {
        };

    return Response.ok().entity(entity).build();
  }
}
