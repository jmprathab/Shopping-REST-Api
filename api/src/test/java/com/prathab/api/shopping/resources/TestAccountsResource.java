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
public class TestAccountsResource {
	private final String ENDPOINT = "http://localhost:8080/shopping/api";

	final OkHttpClient mOkHttpClient = new OkHttpClient.Builder().connectTimeout(15, TimeUnit.SECONDS)
			.readTimeout(15, TimeUnit.SECONDS).writeTimeout(15, TimeUnit.SECONDS).build();

	@BeforeTest
	public void setup() {
		System.out.println("----------Testing AccountsResource----------");
	}

	@Test(priority = 0)
	public void testAccountsCreate() throws IOException {
		System.out.println("Testing testAccountsResource Create");

		JsonObject jsonObject = Json.createObjectBuilder().add("name", "Shopping_Api_Test_User")
				.add("mobile", "8110081100").add("password", "password").build();

		RequestBody requestBody = RequestBody.create(MediaType.parse(HttpConstants.HTTP_JSON_MEDIA_TYPE),
				jsonObject.toString());

		Request request = new Request.Builder().url(ENDPOINT + "/accounts/create").post(requestBody).build();

		Response response = mOkHttpClient.newCall(request).execute();

		Assert.assertEquals(response.code(), 200);
		Assert.assertTrue(response.header(HttpConstants.HTTP_HEADER_TOKEN) != null);
	}

	@Test(priority = 1)
	public void testAccountsLogin() throws IOException {
		System.out.println("Testing testAccountsResource Login");

		JsonObject jsonObject = Json.createObjectBuilder().add("mobile", "8110081100").add("password", "password")
				.build();

		RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),
				jsonObject.toString());

		Request request = new Request.Builder().url(ENDPOINT + "/accounts/login").post(requestBody).build();

		Response response = mOkHttpClient.newCall(request).execute();

		Assert.assertEquals(response.code(), 200);
		Assert.assertTrue(response.header(HttpConstants.HTTP_HEADER_TOKEN) != null);
	}

	@Test(priority = 2)
	public void testAccountsForgot() {
		System.out.println("Testing testAccountsResource Forgot");

	}

	@Test(priority = 3)
	public void testAccountsDelete() {
		System.out.println("Testing testAccountsResource Delete");
	}
}
