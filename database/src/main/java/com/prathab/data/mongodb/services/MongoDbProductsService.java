package com.prathab.data.mongodb.services;

import static com.prathab.data.constants.DBConstants.DB_COLLECTION_PRODUCTS_DESCRIPTION;
import static com.prathab.data.constants.DBConstants.DB_COLLECTION_PRODUCTS_ID;
import static com.prathab.data.constants.DBConstants.DB_COLLECTION_PRODUCTS_IMAGES;
import static com.prathab.data.constants.DBConstants.DB_COLLECTION_PRODUCTS_NAME;
import static com.prathab.data.constants.DBConstants.DB_COLLECTION_PRODUCTS_PRICE;
import static com.prathab.data.constants.DBConstants.DB_COLLECTION_PRODUCTS_RATING;
import static com.prathab.data.constants.DBConstants.DB_COLLECTION_PRODUCTS_TAGS;

import java.util.ArrayList;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.prathab.data.datamodels.Products;

public class MongoDbProductsService {
	public static ArrayList<Products> getProducts(int page, int limit) {
		if (page < 0 || page > 1000) {
			page = 0;
		}
		if (limit <= 0 || limit > 50) {
			limit = 50;
		}

		ArrayList<Products> productsList = new ArrayList<>();
		MongoCollection<Document> collection = MongoClientService.getProductsCollection();

		MongoCursor<Document> fetchedDocument = collection.find().skip(page > 0 ? ((page - 1) * limit) : 0).limit(limit)
				.iterator();

		try {
			while (fetchedDocument.hasNext()) {
				Document current = fetchedDocument.next();
				ObjectId objectId = current.getObjectId(DB_COLLECTION_PRODUCTS_ID);
				String id = objectId.toString();
				String name = current.getString(DB_COLLECTION_PRODUCTS_NAME);
				String price = current.getString(DB_COLLECTION_PRODUCTS_PRICE);
				int rating = current.getInteger(DB_COLLECTION_PRODUCTS_RATING);

				ArrayList<String> imagesBasicDBList = (ArrayList<String>) current.get(DB_COLLECTION_PRODUCTS_IMAGES);
				ArrayList<String> images = new ArrayList<>();
				images.addAll(imagesBasicDBList);

				ArrayList<String> tagsBasicDBList = (ArrayList<String>) current.get(DB_COLLECTION_PRODUCTS_TAGS);
				ArrayList<String> tags = new ArrayList<>();
				tags.addAll(tagsBasicDBList);

				String description = current.getString(DB_COLLECTION_PRODUCTS_DESCRIPTION);

				Products products = new Products(id, name, price, rating, images, tags, description);

				productsList.add(products);

				System.out.println(products.toString());
			}
		} finally {
			fetchedDocument.close();
		}

		return productsList;
	}
}
