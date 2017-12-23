package com.prathab.data.base;

import com.prathab.data.base.dbmodel.DbObject;

/**
 * Use this class for doing select/update operation on DbService
 */
public class DbObjectSpec {
	private DbObject object;

	public DbObjectSpec(DbObject object) {
		this.object = object;
	}

	public DbObject getObject() {
		return object;
	}

	public void setObject(DbObject object) {
		this.object = object;
	}
}
