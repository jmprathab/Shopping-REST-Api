package com.prathab.data.mysql.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.prathab.data.base.DbObjectSpec;
import com.prathab.data.base.DbService;
import com.prathab.data.base.dbmodel.DbObject;
import com.prathab.data.base.exception.DbException;
import com.prathab.data.base.result.DeleteResult;
import com.prathab.data.base.result.InsertResult;
import com.prathab.data.base.result.ReadResult;
import com.prathab.data.base.result.UpdateResult;
import com.prathab.data.base.utils.DatabaseUtils;
import com.prathab.data.constants.DBConstants;
import com.prathab.data.datamodels.Users;

public class MysqlAccountsService implements DbService {

	@Override
	public ReadResult read(DbObjectSpec spec) throws DbException {
		Users inputSpec = (Users) spec.getObject();
		String mobile = inputSpec.getMobile();
		String email = inputSpec.getEmail();

		Connection connection = null;
		Statement statement = null;
		ResultSet rs = null;

		ReadResult readResult = new ReadResult();
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
			connection = DatabaseUtils.getConnection();
		} catch (Exception e) {
			System.out.println("Cannot fetch connection from Pool : " + e.getMessage());
			return readResult;
		}

		try {
			String query = "SELECT * FROM USERS WHERE " + nonNullKey + "='" + nonNullValue + "'";
			System.out.println("Mysql : Accounts : Read : " + query);

			statement = connection.createStatement();
			rs = statement.executeQuery(query);

			while (rs.next()) {
				fetchedUsers = new Users();
				fetchedUsers.setName(rs.getString(DBConstants.DB_COLLECTION_USERS_NAME));
				fetchedUsers.setMobile(rs.getString(DBConstants.DB_COLLECTION_USERS_MOBILE));
				fetchedUsers.setPassword(rs.getString(DBConstants.DB_COLLECTION_USERS_PASSWORD));
			}
			readResult.setSuccessful(true);
		} catch (Exception e) {
			System.out.println("Cannot read user : " + e.getMessage());
		} finally {
			if (connection != null)
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			if (statement != null)
				try {
					statement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}

		readResult.setDbObject(fetchedUsers);
		return readResult;
	}

	@Override
	public DeleteResult delete(DbObjectSpec spec) throws DbException {
		Users inputSpec = (Users) spec.getObject();
		String mobile = inputSpec.getMobile();
		String email = inputSpec.getEmail();

		Connection connection = null;
		Statement statement = null;

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
			connection = DatabaseUtils.getConnection();
		} catch (Exception e) {
			System.out.println("Cannot fetch connection from Pool : " + e.getMessage());
			return deleteResult;
		}

		try {
			String query = "DELETE FROM USERS WHERE " + nonNullKey + "=" + nonNullValue;
			System.out.println("Mysql : Accounts : Delete : " + query);

			statement = connection.createStatement();
			int numRows = statement.executeUpdate(query);

			deleteResult.setNumOfRows(numRows);
			deleteResult.setSuccessful(true);

		} catch (Exception e) {
			System.out.println("Cannot delete user : " + e.getMessage());
			deleteResult.setSuccessful(false);
		} finally {
			if (connection != null)
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			if (statement != null)
				try {
					statement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
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
		PreparedStatement statement = null;

		InsertResult insertResult = new InsertResult();

		try {
			connection = DatabaseUtils.getConnection();
		} catch (Exception e) {
			System.out.println("Cannot fetch connection from Pool : " + e.getMessage());
			return insertResult;
		}

		try {
			String query = "INSERT INTO USERS (name, mobile, password) values (?,?,?)";
			System.out.println("Mysql : Accounts : Insert : " + query);

			statement = connection.prepareStatement(query);
			statement.setString(1, name);
			statement.setString(2, mobile);
			statement.setString(3, password);

			int numRows = statement.executeUpdate();

			insertResult.setNumOfRows(numRows);
			insertResult.setSuccessful(true);

		} catch (Exception e) {
			System.out.println("Cannot insert user : " + e.getMessage());
			insertResult.setSuccessful(false);
		} finally {
			if (connection != null)
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			if (statement != null)
				try {
					statement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
		return insertResult;
	}

	@Override
	public UpdateResult update(DbObjectSpec spec) throws DbException {
		UpdateResult result = new UpdateResult();
		System.out.println("Mysql Update operation is not implemented");
		return result;
	}

}
