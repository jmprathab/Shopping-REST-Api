package com.prathab.data.base.utils;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.dbcp2.BasicDataSource;

import com.prathab.data.mysql.services.MysqlConfiguration;

public class DatabaseUtils {
	private static BasicDataSource dataSource;

	private DatabaseUtils() {
		// Private constructor
	}

	private static BasicDataSource getDataSource() {

		if (dataSource == null) {
			BasicDataSource ds = new BasicDataSource();

			// TODO change this and remove mysql dependency
			ds.setDriverClassName(MysqlConfiguration.DRIVER_CLASS);
			ds.setUrl(MysqlConfiguration.CONNECTION_STRING);
			ds.setUsername(MysqlConfiguration.USER_NAME);
			ds.setPassword(MysqlConfiguration.PASSWORD);

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
