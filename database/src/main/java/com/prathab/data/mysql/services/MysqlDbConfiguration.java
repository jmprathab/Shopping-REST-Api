package com.prathab.data.mysql.services;

import com.prathab.data.base.DbConfiguration;

public final class MysqlDbConfiguration implements DbConfiguration {
	private static final String DRIVER_CLASS = "org.mariadb.jdbc.Driver";
	private static final String CONNECTION_STRING = "jdbc:mariadb://localhost:3306/shopping";
	private static final String USER_NAME = "shopping_user";
	private static final String PASSWORD = "shopping_user";

	@Override
	public String getDriverClass() {
		return DRIVER_CLASS;
	}

	@Override
	public String getConnectionString() {
		return CONNECTION_STRING;
	}

	@Override
	public String getDbUserName() {
		return USER_NAME;
	}

	@Override
	public String getDbPassword() {
		return PASSWORD;
	}
}
