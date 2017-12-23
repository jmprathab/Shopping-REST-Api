package com.prathab.data.base;

import com.prathab.data.base.exception.DbException;
import com.prathab.data.base.result.UpdateResult;

/**
 * Carts service interface which exposes checkout method
 * 
 * @author jmprathab
 *
 */
public interface DbCartsService extends DbService {

	public UpdateResult checkout(DbObjectSpec spec) throws DbException;

}
