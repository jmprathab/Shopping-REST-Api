package com.prathab.data.mysql.services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
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
import com.prathab.data.constants.DBConstants;
import com.prathab.data.datamodels.Products;

public class MysqlProductsService implements DbProductsService {

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
		// TODO Auto-generated method stub
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

		try {
			Class.forName(MysqlConfiguration.DRIVER_CLASS);
		} catch (Exception e) {
			System.out.println("Mysql Driver not found");
		}

		try {
			connection = DriverManager.getConnection(MysqlConfiguration.CONNECTION_STRING, MysqlConfiguration.USER_NAME,
					MysqlConfiguration.PASSWORD);

			Statement statement = connection.createStatement();
			String query = "select * from products limit " + limit + " offset " + page + ";";
			System.out.println("Mysql : BulkRead : " + query);

			ResultSet rs = statement.executeQuery(query);

			while (rs.next()) {
				Products fetchedProducts = new Products();
				fetchedProducts.setId(rs.getString(DBConstants.DB_COLLECTION_PRODUCTS_ID));
				fetchedProducts.setName(rs.getString(DBConstants.DB_COLLECTION_PRODUCTS_NAME));
				fetchedProducts.setDescription(rs.getString(DBConstants.DB_COLLECTION_PRODUCTS_DESCRIPTION));
				fetchedProducts.setPrice(rs.getString(DBConstants.DB_COLLECTION_PRODUCTS_PRICE));
				fetchedProducts.setRating(rs.getInt(DBConstants.DB_COLLECTION_PRODUCTS_RATING));
				productsList.add(fetchedProducts);
			}
		} catch (Exception e) {
			System.out.println(e);
			assert false;
			return null;
		}
		
		ReadBulkResult<Products> readBulkResult = new ReadBulkResult<Products>(productsList);
		return readBulkResult;
	}

}
