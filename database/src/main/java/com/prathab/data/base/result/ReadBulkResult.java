package com.prathab.data.base.result;

import java.util.List;

import com.prathab.data.base.dbmodel.DbObject;

/**
 * Use this class if we want to return a List of DbObjects from a JDBC call
 * 
 * @author jmprathab
 *
 */
public class ReadBulkResult<T extends DbObject> extends DbResult {
	private List<T> dbObject;

	public List<T> getDbObject() {
		return dbObject;
	}

	public void setDbObject(List<T> dbObject) {
		this.dbObject = dbObject;
	}

	public ReadBulkResult(List<T> dbObject) {
		this.dbObject = dbObject;
	}

	public ReadBulkResult() {
	}
}
