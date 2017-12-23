package com.prathab.data.mysql.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.prathab.data.base.DbCartsService;
import com.prathab.data.base.DbObjectSpec;
import com.prathab.data.base.dbmodel.DbObject;
import com.prathab.data.base.exception.DbException;
import com.prathab.data.base.result.DeleteResult;
import com.prathab.data.base.result.InsertResult;
import com.prathab.data.base.result.ReadResult;
import com.prathab.data.base.result.UpdateResult;
import com.prathab.data.base.utils.DatabaseUtils;
import com.prathab.data.constants.DBConstants;
import com.prathab.data.datamodels.Carts;

public class MysqlCartsService implements DbCartsService {

	@Override
	public ReadResult read(DbObjectSpec spec) throws DbException {
		Carts inputSpec = (Carts) spec.getObject();
		int usersId = inputSpec.getUsersId();
		int productsId = inputSpec.getProductsId();

		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet rs = null;

		ReadResult readResult = new ReadResult();

		try {
			connection = DatabaseUtils.getConnection();
		} catch (Exception e) {
			System.out.println("Cannot fetch connection from Pool : " + e.getMessage());
		}

		Carts fetchedCarts = null;
		try {
			String query = "SELECT * FROM CARTS WHERE users_id =? AND products_id =?;";
			System.out.println("Mysql : Carts : Read : " + query);

			statement = connection.prepareStatement(query);
			statement.setInt(1, usersId);
			statement.setInt(2, productsId);

			rs = statement.executeQuery();
			while (rs.next()) {
				fetchedCarts = new Carts();
				fetchedCarts.setProductsId(rs.getInt(DBConstants.DB_COLLECTION_CARTS_PRODUCT_ID));
				fetchedCarts.setUsersId(rs.getInt(DBConstants.DB_COLLECTION_CARTS_USERS_ID));
				fetchedCarts.setQuantity(rs.getInt(DBConstants.DB_COLLECTION_CARTS_QUANTITY));
			}
			readResult.setSuccessful(true);
		} catch (Exception e) {
			System.out.println("Cannot read from cart : " + e.getMessage());
			readResult.setSuccessful(false);
			return readResult;
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

		ReadResult result = new ReadResult(fetchedCarts);
		result.setSuccessful(true);
		return result;
	}

	@Override
	public DeleteResult delete(DbObjectSpec spec) throws DbException {
		Carts inputSpec = (Carts) spec.getObject();
		int usersId = inputSpec.getUsersId();

		Connection connection = null;
		PreparedStatement statement = null;

		DeleteResult deleteResult = new DeleteResult();

		try {
			connection = DatabaseUtils.getConnection();
		} catch (Exception e) {
			System.out.println("Cannot fetch connection from Pool : " + e.getMessage());
			return deleteResult;
		}

		try {
			String query = "DELETE FROM CARTS WHERE  users_id=?;";
			System.out.println("Mysql : Carts : Delete : " + query);

			statement = connection.prepareStatement(query);
			statement.setInt(1, usersId);

			int numRows = statement.executeUpdate();

			deleteResult.setNumOfRows(numRows);
			deleteResult.setSuccessful(true);

		} catch (Exception e) {
			System.out.println("Cannot delete from carts : " + e.getMessage());
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
		Carts carts = (Carts) object;

		int productsId = carts.getProductsId();
		int usersId = carts.getUsersId();
		int quantity = carts.getQuantity();

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
			String query = "insert into carts (users_id, products_id, quantity) values (?,?,?)";
			System.out.println("Mysql : Insert : " + query);

			statement = connection.prepareStatement(query);
			statement.setInt(1, usersId);
			statement.setInt(2, productsId);
			statement.setInt(3, quantity);

			int numRows = statement.executeUpdate();

			insertResult.setNumOfRows(numRows);
			insertResult.setSuccessful(true);
		} catch (Exception e) {
			System.out.println("Cannot insert into Carts : " + e.getMessage());
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

		Carts carts = (Carts) spec.getObject();
		int productsId = carts.getProductsId();
		int usersId = carts.getUsersId();
		int quantity = carts.getQuantity();

		Connection connection = null;
		PreparedStatement statement = null;

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
			connection = DatabaseUtils.getConnection();
		} catch (Exception e) {
			System.out.println("Cannot fetch connection from Pool : " + e.getMessage());
			return updateResult;
		}

		try {
			String query = "UPDATE CARTS SET quantity = ? WHERE products_id=? AND users_id=?;";
			System.out.println("Mysql : Carts : Update : " + query);

			statement = connection.prepareStatement(query);
			statement.setInt(1, quantity);
			statement.setInt(2, productsId);
			statement.setInt(3, usersId);

			int numRows = statement.executeUpdate();

			updateResult.setNumOfRows(numRows);
			updateResult.setSuccessful(true);

		} catch (Exception e) {
			System.out.println("Cannot update Carts : " + e.getMessage());
			updateResult.setSuccessful(false);
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
		return updateResult;
	}

	@Override
	public UpdateResult checkout(DbObjectSpec spec) throws DbException {
		Carts carts = (Carts) spec.getObject();

		int usersId = carts.getUsersId();

		Connection connection = null;
		PreparedStatement statement = null;

		UpdateResult updateResult = new UpdateResult();

		try {
			connection = DatabaseUtils.getConnection();
		} catch (Exception e) {
			System.out.println("Cannot fetch connection from Pool : " + e.getMessage());
			return updateResult;
		}

		try {
			connection.setAutoCommit(false);

			String query = "UPDATE products p,carts c SET p.quantity=p.quantity-c.quantity WHERE p.products_id IN (SELECT products_id FROM carts WHERE c.users_id=?);";
			System.out.println("Mysql : Carts : Checkout : " + query);

			statement = connection.prepareStatement(query);
			statement.setInt(1, usersId);

			int numRows = statement.executeUpdate();
			assert (numRows > 0);

			String deleteCartsQuery = "DELETE FROM CARTS WHERE users_id = ?;";
			System.out.println("Mysql : Carts : Checkout : DeleteCarts : " + query);

			PreparedStatement deleteStatement = connection.prepareStatement(deleteCartsQuery);
			deleteStatement.setInt(1, usersId);

			int numRowsDeleted = deleteStatement.executeUpdate();
			assert (numRowsDeleted > 0);

			connection.commit();
			
			if (deleteStatement != null) {
				try {
					deleteStatement.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			updateResult.setNumOfRows(numRowsDeleted);
			updateResult.setSuccessful(true);

		} catch (Exception e) {
			System.out.println("Carts checkout failed : " + e.getMessage());
			updateResult.setSuccessful(false);
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
		return updateResult;
	}

}
