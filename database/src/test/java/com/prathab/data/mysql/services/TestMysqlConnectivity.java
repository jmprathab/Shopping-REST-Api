package com.prathab.data.mysql.services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.prathab.data.base.DbConfiguration;
import com.prathab.data.base.DbObjectSpec;
import com.prathab.data.base.exception.DbException;
import com.prathab.data.base.result.DeleteResult;
import com.prathab.data.base.result.InsertResult;
import com.prathab.data.base.result.ReadResult;
import com.prathab.data.base.result.UpdateResult;
import com.prathab.data.datamodels.Carts;
import com.prathab.data.datamodels.Products;
import com.prathab.data.datamodels.Users;

@Test
public class TestMysqlConnectivity {

	DbConfiguration mDbConfiguration = new MysqlDbConfiguration();

	@Test(priority = 0)
	public void testMysqlConnection() {

		System.out.println("---------- Mysql JDBC Connection Testing ----------");

		try {
			Class.forName(mDbConfiguration.getDriverClass());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.out.println("Mysql java driver cannot be found");
		}
		Connection connection = null;

		try {
			connection = DriverManager.getConnection(mDbConfiguration.getConnectionString(),
					mDbConfiguration.getDbUserName(), mDbConfiguration.getDbPassword());
		} catch (SQLException e) {
			e.printStackTrace();
		}

		if (connection != null) {
			System.out.println("Connection successful");
		} else {
			System.out.println("Failed to make connection!");
		}
	}

	@Test(priority = 1)
	public void testMysqlUsersInsert() {
		System.out.println("Testing Mysql Insert operation");

		MysqlUsersService service = new MysqlUsersService();
		Users users = new Users();
		users.setName("Shopping_Test_User");
		users.setMobile("8110081100");
		users.setPassword("password");
		try {
			InsertResult result = service.insert(users);
			Assert.assertEquals(result.getNumOfRows(), 1);
			Assert.assertTrue(result.isSuccessful());
		} catch (DbException e) {
			Assert.fail("Mysql Insert test failed", e);
		}
	}

	@Test(priority = 2)
	public void testMysqlProudctsInsert() {
		System.out.println("Testing Mysql Products Insert operation");
		System.out.println("Mysql Products Insert is not implemented, so skipping the test");

		MysqlProductsService service = new MysqlProductsService();
		Products products = new Products();
		products.setId("1");
		products.setName("Shopping_Test_Product");
		products.setPrice("1000");
		products.setDescription("This is a test row inserted by testng test case to test Mysql Products operation");
		products.setRating(5);
		// TODO add quantity here
		try {
			InsertResult result = service.insert(products);
			Assert.assertEquals(result.getNumOfRows(), 1);
			Assert.assertTrue(result.isSuccessful());
		} catch (DbException e) {
			Assert.fail("Mysql Products Insert test failed", e);
		}
	}

	@Test(priority = 3)
	public void testMysqlCartsInsert() throws DbException {
		System.out.println("Testing Mysql Carts Insert operation");

		MysqlCartsService service = new MysqlCartsService();
		Carts carts = new Carts();
		carts.setProductsId(1);
		carts.setUsersId(1);
		carts.setQuantity(1);

		try {
			InsertResult result = service.insert(carts);
			Assert.assertEquals(result.getNumOfRows(), 1);
			Assert.assertTrue(result.isSuccessful());
		} catch (DbException e) {
			Assert.fail("Mysql Carts Insert test failed", e);
		}
	}

	@Test(priority = 4)
	public void testMysqlUsersRead() {
		System.out.println("Testing Mysql Read operation");

		MysqlUsersService service = new MysqlUsersService();
		Users users = new Users();
		users.setMobile("8110081100");
		users.setPassword("password");
		DbObjectSpec spec = new DbObjectSpec(users);
		try {
			ReadResult result = service.read(spec);
			Users fetchedUsers = (Users) result.getDbObject();

			Assert.assertTrue(fetchedUsers.getName().equals("Shopping_Test_User"));
			Assert.assertTrue(fetchedUsers.getMobile().equals("8110081100"));
			Assert.assertTrue(fetchedUsers.getPassword().equals("password"));
			// Assert.assertTrue(result.isSuccessful());
		} catch (DbException e) {
			Assert.fail("Mysql Users Read failed", e);
		}
	}

	@Test(priority = 5)
	public void testMysqlUsersUpdate() {
		System.out.println("Testing Mysql Update operation");
	}

	@Test(priority = 6)
	public void testMysqlCartsUpdate() {
		System.out.println("Testing Mysql Carts Update operation");

		MysqlCartsService service = new MysqlCartsService();
		Carts carts = new Carts();
		carts.setProductsId(1);
		carts.setUsersId(1);
		carts.setQuantity(1);
		DbObjectSpec spec = new DbObjectSpec(carts);
		try {
			UpdateResult result = service.update(spec);

			Assert.assertEquals(result.getNumOfRows(), 1);
			Assert.assertTrue(result.isSuccessful());
		} catch (DbException e) {
			Assert.fail("Mysql Carts Update test failed", e);
		}
	}

	@Test(priority = 7)
	public void testMysqlCartsCheckout() {
		System.out.println("Testing Mysql Carts Checkout operation");

		MysqlCartsService service = new MysqlCartsService();
		Carts carts = new Carts();
		carts.setUsersId(1);
		DbObjectSpec spec = new DbObjectSpec(carts);
		try {
			UpdateResult result = service.checkout(spec);

			Assert.assertEquals(result.getNumOfRows(), 1);
			Assert.assertTrue(result.isSuccessful());
		} catch (DbException e) {
			Assert.fail("Mysql Carts Checkout test failed", e);
		}
	}

	@Test(priority = 8)
	public void testMysqUsersDelete() {
		System.out.println("Testing Mysql Users Delete operation");

		MysqlUsersService service = new MysqlUsersService();
		Users users = new Users();
		users.setMobile("8110081100");
		DbObjectSpec spec = new DbObjectSpec(users);
		try {
			DeleteResult result = service.delete(spec);
			Assert.assertEquals(result.getNumOfRows(), 1);
			Assert.assertTrue(result.isSuccessful());
		} catch (DbException e) {
			Assert.fail("Mysql Delete test failed", e);
		}
	}

	@Test(priority = 9)
	public void testMysqlProductsDelete() {
		System.out.println("Testing Mysql Products Delete operation");

		MysqlProductsService service = new MysqlProductsService();
		Products products = new Products();
		products.setId("1");

		DbObjectSpec spec = new DbObjectSpec(products);
		try {
			DeleteResult result = service.delete(spec);
			Assert.assertEquals(result.getNumOfRows(), 1);
			Assert.assertTrue(result.isSuccessful());
		} catch (DbException e) {
			Assert.fail("Mysql Products Delete test failed", e);
		}
	}

	@Test(priority = 10)
	public void testMysqlCartsDelete() {
		System.out.println("Testing Mysql Carts Delete operation");

		MysqlCartsService service = new MysqlCartsService();
		Carts carts = new Carts();
		carts.setProductsId(1);
		carts.setUsersId(1);

		DbObjectSpec spec = new DbObjectSpec(carts);
		try {
			DeleteResult result = service.delete(spec);

			Assert.assertEquals(result.getNumOfRows(), 1);
			Assert.assertTrue(result.isSuccessful());
		} catch (DbException e) {
			Assert.fail("Mysql Carts Delete test failed", e);
		}
	}
}
