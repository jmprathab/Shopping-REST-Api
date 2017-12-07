package com.prathab.data.mysql.services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.prathab.data.base.DbObjectSpec;
import com.prathab.data.base.exception.DbException;
import com.prathab.data.base.result.DeleteResult;
import com.prathab.data.base.result.InsertResult;
import com.prathab.data.base.result.ReadResult;
import com.prathab.data.datamodels.Users;
import com.prathab.data.mysql.services.MysqlAccountsService;

public class TestMysqlConnectivity {

	@Test(priority = 0)
	public void testMysqlConnection() {

		System.out.println("---------- Mysql JDBC Connection Testing ----------");

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.out.println("Mysql java driver cannot be found");
		}
		Connection connection = null;

		try {
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/shopping", "shopping_user",
					"shopping_user");
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
	public void testMysqlInsert() {
		System.out.println("Testing Mysql Insert operation");

		MysqlAccountsService service = new MysqlAccountsService();
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
	public void testMysqlRead() {
		System.out.println("Testing Mysql Read operation");

		MysqlAccountsService service = new MysqlAccountsService();
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
			Assert.fail("Mysql Insert Read failed", e);
		}
	}

	@Test(priority = 3)
	public void testMysqlUpdate() {
		System.out.println("Testing Mysql Update operation");
	}

	@Test(priority = 4)
	public void testMysqlDelete() {
		System.out.println("Testing Mysql Delete operation");

		MysqlAccountsService service = new MysqlAccountsService();
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
}
