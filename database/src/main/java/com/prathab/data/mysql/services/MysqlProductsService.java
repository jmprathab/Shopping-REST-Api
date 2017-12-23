package com.prathab.data.mysql.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.prathab.data.base.DbObjectSpec;
import com.prathab.data.base.DbProductsService;
import com.prathab.data.base.dbmodel.DbObject;
import com.prathab.data.base.exception.DbException;
import com.prathab.data.base.result.DeleteResult;
import com.prathab.data.base.result.InsertResult;
import com.prathab.data.base.result.ReadBulkResult;
import com.prathab.data.base.result.ReadResult;
import com.prathab.data.base.result.UpdateResult;
import com.prathab.data.base.utils.DatabaseUtils;
import com.prathab.data.constants.DBConstants;
import com.prathab.data.datamodels.Products;

public class MysqlProductsService implements DbProductsService {

	@Override
	public ReadResult read(DbObjectSpec spec) throws DbException {
		return null;
	}

	@Override
	public DeleteResult delete(DbObjectSpec spec) throws DbException {
		return null;
	}

	@Override
	public InsertResult insert(DbObject object) throws DbException {
		return null;
	}

	@Override
	public UpdateResult update(DbObjectSpec spec) throws DbException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ReadBulkResult<Products> readProducts(int page, int limit) {
		if (page < 0 || page > 1000) {
			page = 0;
		}
		if (limit <= 0 || limit > 50) {
			limit = 50;
		}

		ArrayList<Products> productsList = new ArrayList<>();

		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet rs = null;

		ReadBulkResult<Products> readBulkResult = new ReadBulkResult<Products>();

		try {
			connection = DatabaseUtils.getConnection();
		} catch (Exception e) {
			System.out.println("Cannot fetch connection from Pool : " + e.getMessage());
			return readBulkResult;
		}
		try {
			String query = "SELECT * FROM products LIMIT ? OFFSET ?;";
			System.out.println("Mysql : Products : BulkRead : " + query);

			statement = connection.prepareStatement(query);
			statement.setInt(1, limit);
			statement.setInt(2, page);

			rs = statement.executeQuery();

			while (rs.next()) {
				Products fetchedProducts = new Products();
				fetchedProducts.setId(rs.getString(DBConstants.DB_COLLECTION_PRODUCTS_ID));
				fetchedProducts.setName(rs.getString(DBConstants.DB_COLLECTION_PRODUCTS_NAME));
				fetchedProducts.setDescription(rs.getString(DBConstants.DB_COLLECTION_PRODUCTS_DESCRIPTION));
				fetchedProducts.setPrice(rs.getString(DBConstants.DB_COLLECTION_PRODUCTS_PRICE));
				fetchedProducts.setRating(rs.getInt(DBConstants.DB_COLLECTION_PRODUCTS_RATING));
				productsList.add(fetchedProducts);
			}
			readBulkResult.setSuccessful(true);
		} catch (Exception e) {
			System.out.println("Cannot bulk read products : " + e.getMessage());
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

		readBulkResult.setDbObject(productsList);
		return readBulkResult;
	}

}
