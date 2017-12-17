package com.prathab.api.shopping.resources;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@Test
public class TestProductsResource {

	private final String ENDPOINT = "http://localhost:8080/shopping/api";

	final OkHttpClient mOkHttpClient = new OkHttpClient.Builder().connectTimeout(15, TimeUnit.SECONDS)
			.readTimeout(15, TimeUnit.SECONDS).writeTimeout(15, TimeUnit.SECONDS).build();

	@BeforeTest
	public void setup() {
		System.out.println("----------Testing ProductsResource----------");
	}

	@Test(priority = 0)
	public void testProductsReadBulk() throws IOException {
		System.out.println("Testing Products resource fetch bulk");

		HttpUrl.Builder urlBuilder = HttpUrl.parse(ENDPOINT + "/products").newBuilder();
		urlBuilder.addQueryParameter("page", "1");
		urlBuilder.addQueryParameter("limit", "50");

		Request request = new Request.Builder().url(urlBuilder.build()).get().build();

		Response response = mOkHttpClient.newCall(request).execute();

		Assert.assertEquals(response.code(), 200);
		//Assert.assertTrue(response.header("Token") != null);
	}
}
