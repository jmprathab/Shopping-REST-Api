package com.prathab.data.datamodels;

import com.prathab.data.base.dbmodel.DbObject;

/**
 * @author jmprathab
 *
 */
public class Carts implements DbObject {
	private int productsId;
	private int quantity;
	private int usersId;
	private String usersMobile;

	public int getUsersId() {
		return usersId;
	}

	public void setUsersId(int usersId) {
		this.usersId = usersId;
	}

	public Carts() {
		super();
	}

	public Carts(int productsId, int quantity, int usersId, String usersMobile) {
		super();
		this.productsId = productsId;
		this.quantity = quantity;
		this.usersId = usersId;
		this.usersMobile = usersMobile;
	}

	@Override
	public String toString() {
		return "Carts [productsId=" + productsId + ", quantity=" + quantity + ", usersId=" + usersId + ", usersMobile="
				+ usersMobile + "]";
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

	public String getUsersMobile() {
		return usersMobile;
	}

	public void setUsersMobile(String usersMobile) {
		this.usersMobile = usersMobile;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
}
