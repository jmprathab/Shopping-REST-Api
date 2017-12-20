package com.prathab.data.datamodels;

import com.prathab.data.base.dbmodel.DbObject;

public class Carts implements DbObject {
	private int productsId;
	private int quantity;
	private int usersId;

	public int getUsersId() {
		return usersId;
	}

	public void setUsersId(int usersId) {
		this.usersId = usersId;
	}

	public Carts() {
		super();
	}

	public Carts(int productsId, int quantity, int usersId) {
		super();
		this.productsId = productsId;
		this.quantity = quantity;
		this.usersId = usersId;
	}

	@Override
	public String toString() {
		return "Carts [productsId=" + productsId + ", quantity=" + quantity + ", usersId=" + usersId + "]";
	}

	public int getProductsId() {
		return productsId;
	}

	public void setProductsId(int productsId) {
		this.productsId = productsId;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
}
