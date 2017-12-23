package com.prathab.data.base;

import com.prathab.data.base.dbmodel.DbObject;
import com.prathab.data.base.exception.DbException;
import com.prathab.data.base.result.DeleteResult;
import com.prathab.data.base.result.InsertResult;
import com.prathab.data.base.result.ReadResult;
import com.prathab.data.base.result.UpdateResult;

/**
 * Base class which api layer uses to interact with DB
 * 
 * @author jmprathab
 *
 */
public interface DbService {

	ReadResult read(DbObjectSpec spec) throws DbException;

	DeleteResult delete(DbObjectSpec spec) throws DbException;

	InsertResult insert(DbObject object) throws DbException;

	UpdateResult update(DbObjectSpec spec) throws DbException;
}
