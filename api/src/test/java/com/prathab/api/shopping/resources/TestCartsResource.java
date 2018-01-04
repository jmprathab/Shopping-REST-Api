package com.prathab.api.shopping.resources;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.json.Json;
import javax.json.JsonObject;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.prathab.api.shopping.constants.HttpConstants;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@Test
public class TestCartsResource {

	private final String ENDPOINT = "http://localhost:8080/shopping/api";
	private final String JWT_TOKEN = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJ3d3cucHJhdGhhYi5zaG9wcGluZy5jb20iLCJuYW1lIjoiU2hvcHBpbmdfQXBpX1Rlc3RfVXNlciIsIm1vYmlsZSI6Iis5MSA4MTEwMCA4MTEwMCIsInVzZXJzX3R5cGUiOiJVc2VyIiwiZXhwIjoxNTE3Njc2MDI1LCJpYXQiOjE1MTQ5OTc2MjV9.7Eym8kkkvsjZffvS4abtSe3J3KdVSnWBL1LXOXRfrdY";

	final OkHttpClient mOkHttpClient = new OkHttpClient.Builder().connectTimeout(15, TimeUnit.SECONDS)
			.readTimeout(15, TimeUnit.SECONDS).writeTimeout(15, TimeUnit.SECONDS).build();

	@BeforeTest
	public void setup() {
		System.out.println("----------Testing CartsResource----------");
	}

	@Test(priority = 0)
	public void testCartsAdd() throws IOException {
		System.out.println("Testing testCartsResource Add");

		JsonObject jsonObject = Json.createObjectBuilder().add("productsId", 2).add("usersId", 1).add("quantity", 1)
				.build();

		RequestBody requestBody = RequestBody.create(MediaType.parse(HttpConstants.HTTP_JSON_MEDIA_TYPE),
				jsonObject.toString());

		Request request = new Request.Builder().url(ENDPOINT + "/carts").header(HttpConstants.HTTP_HEADER_TOKEN, JWT_TOKEN).post(requestBody).build();

		Response response = mOkHttpClient.newCall(request).execute();
		Assert.assertEquals(response.code(), 200);
	}

	@Test(priority = 1)
	public void testCartsUpdate() throws IOException {
		System.out.println("Testing testCartsResource Update");

		JsonObject jsonObject = Json.createObjectBuilder().add("productsId", 2).add("usersId", 1).add("quantity", 2)
				.build();

		RequestBody requestBody = RequestBody.create(MediaType.parse(HttpConstants.HTTP_JSON_MEDIA_TYPE),
				jsonObject.toString());

		Request request = new Request.Builder().url(ENDPOINT + "/carts/update").header(HttpConstants.HTTP_HEADER_TOKEN, JWT_TOKEN).post(requestBody).build();

		Response response = mOkHttpClient.newCall(request).execute();

		Assert.assertEquals(response.code(), 200);
	}

	@Test(priority = 2)
	public void testCartsCheckout() throws IOException {
		System.out.println("Testing testCartsResource Checkout");

		JsonObject jsonObject = Json.createObjectBuilder().add("usersId", 1).build();

		RequestBody requestBody = RequestBody.create(MediaType.parse(HttpConstants.HTTP_JSON_MEDIA_TYPE),
				jsonObject.toString());

		Request request = new Request.Builder().url(ENDPOINT + "/carts/checkout").header(HttpConstants.HTTP_HEADER_TOKEN, JWT_TOKEN).post(requestBody).build();

		Response response = mOkHttpClient.newCall(request).execute();

		Assert.assertEquals(response.code(), 200);
	}
	
	@Test(priority = 3)
	public void testCartsDelete() throws IOException {
		System.out.println("Testing testCartsResource Delete");

		JsonObject jsonObject = Json.createObjectBuilder().add("usersId", 1).add("productsId",1).build();

		RequestBody requestBody = RequestBody.create(MediaType.parse(HttpConstants.HTTP_JSON_MEDIA_TYPE),
				jsonObject.toString());

		Request request = new Request.Builder().url(ENDPOINT + "/carts/delete").header(HttpConstants.HTTP_HEADER_TOKEN, JWT_TOKEN).post(requestBody).build();

		Response response = mOkHttpClient.newCall(request).execute();

		Assert.assertEquals(response.code(), 200);
	}
}