package com.prathab.data.base;

import com.prathab.data.base.result.ReadBulkResult;
import com.prathab.data.datamodels.Products;

/**
 * Products service interface, use this if you want to do bulk read and any
 * other opeartion which {@link DbService} does not provide
 * 
 * @author jmprathab
 *
 */
public interface DbProductsService extends DbService {
	public ReadBulkResult<Products> readProducts(int page, int limit);
}
