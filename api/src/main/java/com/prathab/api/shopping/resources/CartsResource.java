package com.prathab.api.shopping.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import com.prathab.data.base.DbObjectSpec;
import com.prathab.data.base.DbService;
import com.prathab.data.base.exception.DbException;
import com.prathab.data.base.result.DeleteResult;
import com.prathab.data.base.result.InsertResult;
import com.prathab.data.base.result.UpdateResult;
import com.prathab.data.datamodels.Carts;
import com.prathab.data.mysql.services.MysqlCartsService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(value = "/carts", description = "Operations on Users carts")
@Path("/carts")
public class CartsResource {

	private static final DbService mDbService = new MysqlCartsService();

	@ApiOperation(value = "Adds a product to cart", notes = "Add to cart requires Users ID, Products ID and Quantity")
	@ApiResponses(value = {
			@ApiResponse(code = 400, message = "Input fields are empty or null or incorrect, or if there is any error in input data"),
			@ApiResponse(code = 200, message = "Account was created successfully") })
	@POST
	@Path("/add")
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response addToCart(@Context UriInfo uriInfo,
			@ApiParam(value = "Should contain Products ID, Users ID, quantity", required = true) Carts carts) {

		int quantity = carts.getQuantity();

		if (quantity <= 0) {
			quantity = 1;
		}

		InsertResult result = null;
		try {
			result = mDbService.insert(carts);
		} catch (DbException e) {
			e.printStackTrace();
			return Response.status(Status.BAD_REQUEST).build();
		}

		if (result.isSuccessful()) {
			return Response.ok().build();
		}

		return Response.status(Status.BAD_REQUEST).build();
	}
	
	@ApiOperation(value = "Adds a product to cart", notes = "Add to cart requires Users ID, Products ID and Quantity")
	@ApiResponses(value = {
			@ApiResponse(code = 400, message = "Input fields are empty or null or incorrect, or if there is any error in input data"),
			@ApiResponse(code = 200, message = "Account was created successfully") })
	@POST
	@Path("/delete")
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response deleteFromCart(@Context UriInfo uriInfo,
			@ApiParam(value = "Should contain Products ID, Users ID", required = true) Carts carts) {

		DeleteResult result = null;
		DbObjectSpec spec = new DbObjectSpec(carts);
		
		try {
			result = mDbService.delete(spec);
		} catch (DbException e) {
			e.printStackTrace();
			return Response.status(Status.BAD_REQUEST).build();
		}

		if (result.isSuccessful()) {
			return Response.ok().build();
		}

		return Response.status(Status.BAD_REQUEST).build();
	}
	
	@ApiOperation(value = "Adds a product to cart", notes = "Add to cart requires Users ID, Products ID and Quantity")
	@ApiResponses(value = {
			@ApiResponse(code = 400, message = "Input fields are empty or null or incorrect, or if there is any error in input data"),
			@ApiResponse(code = 200, message = "Account was created successfully") })
	@POST
	@Path("/update")
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response updateCart(@Context UriInfo uriInfo,
			@ApiParam(value = "Should contain Products ID, Users ID, quantity", required = true) Carts carts) {

		UpdateResult result = null;
		DbObjectSpec spec = new DbObjectSpec(carts);
		
		try {
			result = mDbService.update(spec);
		} catch (DbException e) {
			e.printStackTrace();
			return Response.status(Status.BAD_REQUEST).build();
		}

		if (result.isSuccessful()) {
			return Response.ok().build();
		}

		return Response.status(Status.BAD_REQUEST).build();
	}
}
