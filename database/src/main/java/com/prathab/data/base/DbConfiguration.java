package com.prathab.data.base;

/**
 * @author jmprathab
 *
 */
public interface DbConfiguration {
	public String getDriverClass();

	public String getConnectionString();

	public String getDbUserName();

	public String getDbPassword();
}
