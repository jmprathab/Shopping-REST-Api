package com.prathab.api.shopping.resources;

import com.prathab.data.datamodels.Products;
import com.prathab.data.services.ProductsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.ArrayList;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

@Api(value = "/products", description = "Operations on Products")
@Path("/products")
public class ProductsResource {

  @ApiOperation(
      value = "Returns a list of products",
      notes = "Can pass an optional page and limit params")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "Fetched a list of products")})
  @GET
  @Path("/")
  @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
  @Produces({MediaType.APPLICATION_JSON})
  public Response fetchProducts(@Context UriInfo uriInfo, @QueryParam("page") int page,
      @QueryParam("limit") int limit) {

    ArrayList<Products> productsList = ProductsService.getProducts(page, limit);

    GenericEntity<ArrayList<Products>> entity =
        new GenericEntity<ArrayList<Products>>(productsList) {
        };

    return Response.ok().entity(entity).build();
  }
}
