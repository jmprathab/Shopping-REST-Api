package com.prathab.data.base;

import com.prathab.data.base.result.ReadBulkResult;
import com.prathab.data.datamodels.Products;

public interface DbProductsService extends DbService {
	public ReadBulkResult<Products> readProducts(int page, int limit);
}
