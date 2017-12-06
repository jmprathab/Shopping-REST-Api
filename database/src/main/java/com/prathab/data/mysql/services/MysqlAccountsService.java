package com.prathab.data.mysql.services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import com.prathab.data.base.DbObjectSpec;
import com.prathab.data.base.DbService;
import com.prathab.data.base.dbmodel.DbObject;
import com.prathab.data.base.exception.DbException;
import com.prathab.data.base.result.DeleteResult;
import com.prathab.data.base.result.InsertResult;
import com.prathab.data.base.result.ReadResult;
import com.prathab.data.base.result.UpdateResult;
import com.prathab.data.constants.DBConstants;
import com.prathab.data.datamodels.Users;

public class MysqlAccountsService implements DbService {

	@Override
	public ReadResult read(DbObjectSpec spec) throws DbException {
		Users inputSpec = (Users) spec.getObject();
		String mobile = inputSpec.getMobile();
		String email = inputSpec.getEmail();

		Connection connection = null;
		Users fetchedUsers = null;

		String nonNullValue = null;
		String nonNullKey = null;
		if (mobile != null) {
			nonNullValue = mobile;
			nonNullKey = DBConstants.DB_COLLECTION_USERS_MOBILE;
		} else if (email != null) {
			nonNullValue = email;
			nonNullKey = DBConstants.DB_COLLECTION_USERS_EMAIL;
		} else {
			assert false;
		}
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");

			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/shopping", "shopping_user",
					"shopping_user");

			Statement statement = connection.createStatement();
			String query = "select * from users where " + nonNullKey + "=" + nonNullValue;
			System.out.println("Mysql : Read : " + query);

			ResultSet rs = statement.executeQuery(query);
			while (rs.next()) {
				fetchedUsers = new Users();
				fetchedUsers.setName(rs.getString(DBConstants.DB_COLLECTION_USERS_NAME));
				fetchedUsers.setMobile(rs.getString(DBConstants.DB_COLLECTION_USERS_MOBILE));
				fetchedUsers.setPassword(rs.getString(DBConstants.DB_COLLECTION_USERS_PASSWORD));
			}
		} catch (Exception e) {
			System.out.println(e);
		}

		return new ReadResult(fetchedUsers);
	}

	@Override
	public DeleteResult delete(DbObjectSpec spec) throws DbException {

		Users inputSpec = (Users) spec.getObject();
		String mobile = inputSpec.getMobile();
		String email = inputSpec.getEmail();
		Connection connection = null;
		DeleteResult deleteResult = new DeleteResult();

		String nonNullValue = null;
		String nonNullKey = null;
		if (mobile != null) {
			nonNullValue = mobile;
			nonNullKey = DBConstants.DB_COLLECTION_USERS_MOBILE;
		} else if (email != null) {
			nonNullValue = email;
			nonNullKey = DBConstants.DB_COLLECTION_USERS_EMAIL;
		} else {
			assert false;
		}

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");

			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/shopping", "shopping_user",
					"shopping_user");

			Statement statement = connection.createStatement();
			String query = "delete from users where " + nonNullKey + "=" + nonNullValue;
			System.out.println("Mysql : Delete : " + query);

			int numRows = statement.executeUpdate(query);

			deleteResult.setNumOfRows(numRows);
			deleteResult.setSuccessful(true);

		} catch (Exception e) {
			System.out.println("Cannot delete user : " + e.getMessage());
			deleteResult.setSuccessful(false);
		}

		return deleteResult;
	}

	@Override
	public InsertResult insert(DbObject object) throws DbException {
		Users inputUsers = (Users) object;
		String name = inputUsers.getName();
		String mobile = inputUsers.getMobile();
		String password = inputUsers.getPassword();
		Connection connection = null;

		InsertResult insertResult = new InsertResult();

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");

			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/shopping", "shopping_user",
					"shopping_user");

			String query = "insert into users (name, mobile, password) values (?,?,?)";
			System.out.println("Mysql : Delete : " + query);

			PreparedStatement statement = connection.prepareStatement(query);
			statement.setString(1, name);
			statement.setString(2, mobile);
			statement.setString(3, password);

			int numRows = statement.executeUpdate(query);

			insertResult.setNumOfRows(numRows);
			insertResult.setSuccessful(true);

		} catch (Exception e) {
			System.out.println("Cannot delete user : " + e.getMessage());
			insertResult.setSuccessful(false);
		}
		return insertResult;
	}

	@Override
	public UpdateResult update(DbObjectSpec spec) throws DbException {

		return null;
	}

}
