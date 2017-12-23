package com.prathab.data.base.utils;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.dbcp2.BasicDataSource;

import com.prathab.data.base.DbConfiguration;
import com.prathab.data.mysql.services.MysqlDbConfiguration;

public class DatabaseUtils {
	private static BasicDataSource dataSource;
	static DbConfiguration mDbConfiguration = new MysqlDbConfiguration();

	private DatabaseUtils() {
		// Private constructor
	}

	private static BasicDataSource getDataSource() {

		if (dataSource == null) {
			BasicDataSource ds = new BasicDataSource();
			// TODO change this and remove mysql dependency
			ds.setDriverClassName(mDbConfiguration.getDriverClass());
			ds.setUrl(mDbConfiguration.getConnectionString());
			ds.setUsername(mDbConfiguration.getDbUserName());
			ds.setPassword(mDbConfiguration.getDbPassword());

			ds.setMinIdle(5);
			ds.setMaxIdle(10);
			ds.setMaxOpenPreparedStatements(100);

			dataSource = ds;
		}
		return dataSource;
	}

	public static Connection getConnection() throws SQLException {
		return getDataSource().getConnection();
	}

}
