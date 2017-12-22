package com.prathab.data.mysql.services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import com.prathab.data.base.DbCartsService;
import com.prathab.data.base.DbObjectSpec;
import com.prathab.data.base.dbmodel.DbObject;
import com.prathab.data.base.exception.DbException;
import com.prathab.data.base.result.DeleteResult;
import com.prathab.data.base.result.InsertResult;
import com.prathab.data.base.result.ReadResult;
import com.prathab.data.base.result.UpdateResult;
import com.prathab.data.constants.DBConstants;
import com.prathab.data.datamodels.Carts;

public class MysqlCartsService implements DbCartsService {

	@Override
	public ReadResult read(DbObjectSpec spec) throws DbException {
		Carts inputSpec = (Carts) spec.getObject();
		int usersId = inputSpec.getUsersId();
		int productsId = inputSpec.getProductsId();

		Connection connection = null;

		try {
			Class.forName(MysqlConfiguration.DRIVER_CLASS);
		} catch (Exception e) {
			System.out.println("Mysql Driver not found");
		}

		Carts fetchedCarts = null;
		try {
			connection = DriverManager.getConnection(MysqlConfiguration.CONNECTION_STRING, MysqlConfiguration.USER_NAME,
					MysqlConfiguration.PASSWORD);

			Statement statement = connection.createStatement();
			String query = "select * from carts where  users_id =" + usersId + " and products_id =" + productsId + ";";
			System.out.println("Mysql : Read : " + query);

			ResultSet rs = statement.executeQuery(query);
			while (rs.next()) {
				fetchedCarts = new Carts();
				fetchedCarts.setProductsId(rs.getInt(DBConstants.DB_COLLECTION_CARTS_PRODUCT_ID));
				fetchedCarts.setUsersId(rs.getInt(DBConstants.DB_COLLECTION_CARTS_USERS_ID));
				fetchedCarts.setQuantity(rs.getInt(DBConstants.DB_COLLECTION_CARTS_QUANTITY));
			}
		} catch (Exception e) {
			System.out.println(e);
			ReadResult result = new ReadResult(null);
			result.setSuccessful(false);
			return result;
		}

		ReadResult result = new ReadResult(fetchedCarts);
		result.setSuccessful(true);
		return result;
	}

	@Override
	public DeleteResult delete(DbObjectSpec spec) throws DbException {
		Carts inputSpec = (Carts) spec.getObject();
		int usersId = inputSpec.getUsersId();
		int productsId = inputSpec.getProductsId();

		Connection connection = null;
		DeleteResult deleteResult = new DeleteResult();
		try {
			Class.forName(MysqlConfiguration.DRIVER_CLASS);
		} catch (Exception e) {
			System.out.println("Mysql Driver not found");
		}

		try {
			connection = DriverManager.getConnection(MysqlConfiguration.CONNECTION_STRING, MysqlConfiguration.USER_NAME,
					MysqlConfiguration.PASSWORD);

			Statement statement = connection.createStatement();
			String query = "delete from carts where  users_id =" + usersId + " and products_id =" + productsId + ";";
			System.out.println("Mysql : Delete : " + query);

			int numRows = statement.executeUpdate(query);

			deleteResult.setNumOfRows(numRows);
			deleteResult.setSuccessful(true);

		} catch (Exception e) {
			System.out.println("Cannot delete Carts : " + e.getMessage());
			deleteResult.setSuccessful(false);
		}

		return deleteResult;
	}

	@Override
	public InsertResult insert(DbObject object) throws DbException {
		Carts carts = (Carts) object;

		int productsId = carts.getProductsId();
		int usersId = 1; // TODO clean this
		int quantity = carts.getQuantity();

		Connection connection = null;

		InsertResult insertResult = new InsertResult();

		try {
			Class.forName(MysqlConfiguration.DRIVER_CLASS);

			connection = DriverManager.getConnection(MysqlConfiguration.CONNECTION_STRING, MysqlConfiguration.USER_NAME,
					MysqlConfiguration.PASSWORD);

			String query = "insert into carts (users_id, products_id, quantity) values (?,?,?)";
			System.out.println("Mysql : Insert : " + query);

			PreparedStatement statement = connection.prepareStatement(query);
			statement.setInt(1, usersId);
			statement.setInt(2, productsId);
			statement.setInt(3, quantity);

			int numRows = statement.executeUpdate();

			insertResult.setNumOfRows(numRows);
			insertResult.setSuccessful(true);

		} catch (Exception e) {
			System.out.println("Cannot insert user : " + e.getMessage());
			insertResult.setSuccessful(false);
		}
		return insertResult;
	}

	@Override
	public UpdateResult update(DbObjectSpec spec) throws DbException {

		Carts carts = (Carts) spec.getObject();
		int productsId = carts.getProductsId();
		int usersId = carts.getUsersId();
		int quantity = carts.getQuantity();

		Connection connection = null;

		UpdateResult updateResult = new UpdateResult();

		boolean isObjectInDb = false;

		ReadResult readResult = read(spec);
		isObjectInDb = readResult.isSuccessful();

		if (!isObjectInDb) {
			UpdateResult result = new UpdateResult();
			InsertResult insertResult = insert(carts);
			result.setSuccessful(insertResult.isSuccessful());
			result.setNumOfRows(insertResult.getNumOfRows());
			return result;
		}

		try {
			Class.forName(MysqlConfiguration.DRIVER_CLASS);

			connection = DriverManager.getConnection(MysqlConfiguration.CONNECTION_STRING, MysqlConfiguration.USER_NAME,
					MysqlConfiguration.PASSWORD);

			String query = "update carts set quantity=? where products_id=? and users_id=?;";

			System.out.println("Mysql : Update : " + query);

			PreparedStatement statement = connection.prepareStatement(query);
			statement.setInt(1, quantity);
			statement.setInt(2, productsId);
			statement.setInt(3, usersId);

			int numRows = statement.executeUpdate();

			updateResult.setNumOfRows(numRows);
			updateResult.setSuccessful(true);

		} catch (Exception e) {
			System.out.println("Cannot insert user : " + e.getMessage());
			updateResult.setSuccessful(false);
		}
		return updateResult;
	}

	@Override
	public UpdateResult checkout(DbObjectSpec spec) throws DbException {
		Carts carts = (Carts) spec.getObject();
		
		int usersId = carts.getUsersId();

		Connection connection = null;

		UpdateResult updateResult = new UpdateResult();
		
		try {
			Class.forName(MysqlConfiguration.DRIVER_CLASS);

			connection = DriverManager.getConnection(MysqlConfiguration.CONNECTION_STRING, MysqlConfiguration.USER_NAME,
					MysqlConfiguration.PASSWORD);
			
			connection.setAutoCommit(false);

			String query = "update products p,carts c set p.quantity=p.quantity-c.quantity where p.products_id in (select products_id from carts where c.users_id=?);";

			System.out.println("Mysql : Checkout : " + query);

			PreparedStatement statement = connection.prepareStatement(query);
			statement.setInt(1, usersId);

			int numRows = statement.executeUpdate();
			
			String deleteCartsQuery = "delete from carts where users_id = ?;";
			PreparedStatement deleteCartsStatement = connection.prepareStatement(deleteCartsQuery);
			deleteCartsStatement.setInt(1, usersId);
			deleteCartsStatement.executeUpdate();
			
			connection.commit();
			connection.close();
			
			updateResult.setNumOfRows(numRows);
			updateResult.setSuccessful(true);

		} catch (Exception e) {
			System.out.println("Carts checkout failed : " + e.getMessage());
			updateResult.setSuccessful(false);
		}
		return updateResult;
	}

}
