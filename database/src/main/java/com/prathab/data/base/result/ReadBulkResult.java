package com.prathab.data.base.result;

import java.util.List;

import com.prathab.data.base.dbmodel.DbObject;
/**
 * Use this class if we want to return a List of DbObjects from a JDBC call
 * @author jmprathab
 *
 */
public class ReadBulkResult {
	private List<DbObject> dbObject;

	public List<DbObject> getDbObject() {
		return dbObject;
	}

	public void setDbObject(List<DbObject> dbObject) {
		this.dbObject = dbObject;
	}

	public ReadBulkResult(List<DbObject> dbObject) {
		super();
		this.dbObject = dbObject;
	}	
}
