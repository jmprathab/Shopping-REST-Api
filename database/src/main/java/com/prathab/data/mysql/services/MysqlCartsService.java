package com.prathab.data.mysql.services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import com.prathab.data.base.DbObjectSpec;
import com.prathab.data.base.DbService;
import com.prathab.data.base.dbmodel.DbObject;
import com.prathab.data.base.exception.DbException;
import com.prathab.data.base.result.DeleteResult;
import com.prathab.data.base.result.InsertResult;
import com.prathab.data.base.result.ReadResult;
import com.prathab.data.base.result.UpdateResult;
import com.prathab.data.datamodels.Carts;

public class MysqlCartsService implements DbService{

	@Override
	public ReadResult read(DbObjectSpec spec) throws DbException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DeleteResult delete(DbObjectSpec spec) throws DbException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InsertResult insert(DbObject object) throws DbException {
		Carts carts = (Carts) object;
		
		int productsId = carts.getProductsId();
		int usersId = 1; //TODO clean this
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
		// TODO Auto-generated method stub
		return null;
	}

}
