package com.prathab.data.postgresql.services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.testng.annotations.Test;

public class TestPostgresqlConnectivity {

  @Test
  public void testPostgresqlConnection() {

    System.out.println("-------- PostgreSQL JDBC Connection Testing --------");

    try {
      Class.forName("org.postgresql.Driver");
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
      System.out.println("Postgresql java driver cannot be found");
    }
    Connection connection = null;

    try {
      connection = DriverManager.getConnection(
          "jdbc:postgresql://localhost:5432/shopping", "shopping_user",
          "shopping_user");
    } catch (SQLException e) {
      e.printStackTrace();
    }

    if (connection != null) {
      System.out.println("You made it, take control your database now!");
    } else {
      System.out.println("Failed to make connection!");
    }
  }
}
