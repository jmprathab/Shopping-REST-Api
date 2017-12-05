package com.prathab.data.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.testng.annotations.Test;

public class TestMysqlConnectivity {
	
	  @Test
	  public void testMysqlConnection() {

	    System.out.println("-------- Mysql JDBC Connection Testing --------");

	    try {
	      Class.forName("com.mysql.cj.jdbc.Driver");
	    } catch (ClassNotFoundException e) {
	      e.printStackTrace();
	      System.out.println("Mysql java driver cannot be found");
	    }
	    Connection connection = null;

	    try {
	      connection = DriverManager.getConnection(
	          "jdbc:mysql://localhost:3306/shopping", "shopping_user",
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

}
