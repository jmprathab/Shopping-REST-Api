package com.prathab.data.base;

import com.prathab.data.base.result.ReadBulkResult;

public interface DbProductsService extends DbService{
	public ReadBulkResult readProducts(int page, int limit);
}
