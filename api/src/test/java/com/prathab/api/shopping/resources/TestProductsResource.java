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
	}

	@Test(priority = 1)
	public void stressTestProductsReadBulk() throws IOException {
		System.out.println("Stress Testing Products resource fetch bulk");

		Runnable runnable = new Runnable() {

			@Override
			public void run() {
				try {
					testStressProductsReadBulk();
				} catch (Exception e) {
					e.printStackTrace();
					Assert.assertTrue(false, "Stress test failed");
				}
			}

		};

		for (int i = 0; i < 10; i++) {
			new Thread(runnable).start();
		}
	}

	public void testStressProductsReadBulk() throws IOException, Exception {
		OkHttpClient okHttpClient = new OkHttpClient.Builder().connectTimeout(15, TimeUnit.SECONDS)
				.readTimeout(15, TimeUnit.SECONDS).writeTimeout(15, TimeUnit.SECONDS).build();

		HttpUrl.Builder urlBuilder = HttpUrl.parse(ENDPOINT + "/products").newBuilder();
		urlBuilder.addQueryParameter("page", "1");
		urlBuilder.addQueryParameter("limit", "50");

		Request request = new Request.Builder().url(urlBuilder.build()).get().build();

		Response response = okHttpClient.newCall(request).execute();

		if (response.code() != 200) {
			System.out.println("Product bulk read failed : " + Thread.currentThread().getName());
			throw new Exception();
		}
		Assert.assertEquals(response.code(), 200);
		System.out.println("Success : " + Thread.currentThread().getName());

	}
}
