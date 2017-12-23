package com.prathab.data.base.result;

import com.prathab.data.base.dbmodel.DbObject;

public class ReadResult extends DbResult {
	private DbObject dbObject;

	public ReadResult(DbObject dbObject) {
		this.dbObject = dbObject;
	}

	public DbObject getDbObject() {
		return dbObject;
	}

	public void setDbObject(DbObject dbObject) {
		this.dbObject = dbObject;
	}
}
