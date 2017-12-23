package com.prathab.data.constants;

public class DBConstants {

	/**
	 * Database constants
	 */
	public static final String DB_DATABASE_NAME = "Shopping";

	/**
	 * Collection/Table names
	 */
	public static final String DB_COLLECTION_USERS = "users";
	public static final String DB_COLLECTION_PRODUCTS = "products";
	public static final String DB_COLLECTION_CART = "carts";

	/**
	 * Users collection/table column names
	 */
	public static final String DB_COLLECTION_USERS_ID = "users_id";
	public static final String DB_COLLECTION_USERS_NAME = "name";
	public static final String DB_COLLECTION_USERS_EMAIL = "email";
	public static final String DB_COLLECTION_USERS_MOBILE = "mobile";
	public static final String DB_COLLECTION_USERS_PASSWORD = "password";

	/**
	 * Cart collection/table column names
	 */
	public static final String DB_COLLECTION_CARTS_ID = "carts_id";
	public static final String DB_COLLECTION_CARTS_PRODUCT_ID = "products_id";
	public static final String DB_COLLECTION_CARTS_USERS_ID = "users_id";
	public static final String DB_COLLECTION_CARTS_QUANTITY = "quantity";

	/**
	 * Products collection/table column names
	 */
	public static final String DB_COLLECTION_PRODUCTS_ID = "products_id";
	public static final String DB_COLLECTION_PRODUCTS_NAME = "name";
	public static final String DB_COLLECTION_PRODUCTS_PRICE = "price";
	public static final String DB_COLLECTION_PRODUCTS_RATING = "rating";
	public static final String DB_COLLECTION_PRODUCTS_IMAGES = "images";
	public static final String DB_COLLECTION_PRODUCTS_TAGS = "tags";
	public static final String DB_COLLECTION_PRODUCTS_DESCRIPTION = "description";
}
