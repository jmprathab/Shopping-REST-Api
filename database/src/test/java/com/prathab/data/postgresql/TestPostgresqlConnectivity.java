package com.prathab.data.postgresql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import org.testng.annotations.Test;

public class TestPostgresqlConnectivity {

  @Test
  public void testPostgresqlInsert() {

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
          "jdbc:postgresql://127.0.0.1:5432/shopping", "shopping_user",
          "jaihanuman");
    } catch (SQLException e) {
      e.printStackTrace();
    }

    if (connection != null) {
      System.out.println("You made it, take control your database now!");
    } else {
      System.out.println("Failed to make connection!");
      return;
    }

    Statement statement = connection.createStatement();
    try {
      statement.execute("select * from  users")
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}
