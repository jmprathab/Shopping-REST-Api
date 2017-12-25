package com.prathab.api.shopping.resources;

import java.util.ArrayList;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.prathab.api.shopping.constants.HttpConstants;
import com.prathab.api.shopping.utility.JwtUtility;
import com.prathab.data.base.DbProductsService;
import com.prathab.data.base.result.ReadBulkResult;
import com.prathab.data.datamodels.Products;
import com.prathab.data.mysql.services.MysqlProductsService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(value = "/products", description = "Operations on Products")
@Path("/products")
public class ProductsResource {

	private static final DbProductsService mDbProductsService = new MysqlProductsService();

	@ApiOperation(value = "Returns a list of products", notes = "Can pass an optional page and limit params")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Fetched a list of products") })
	@GET
	@Path("/")
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	public Response fetchProducts(@HeaderParam(HttpConstants.HTTP_HEADER_TOKEN) String token,
			@QueryParam("page") int page, @QueryParam("limit") int limit) {

		if (!JwtUtility.validateJWT(token)) {
			return Response.status(Status.UNAUTHORIZED).build();
		}

		ReadBulkResult<Products> readBulkResult = mDbProductsService.readProducts(page, limit);
		ArrayList<Products> productsList = (ArrayList<Products>) readBulkResult.getDbObject();

		GenericEntity<ArrayList<Products>> entity = new GenericEntity<ArrayList<Products>>(productsList) {
		};

		return Response.ok().entity(entity).build();
	}
}
