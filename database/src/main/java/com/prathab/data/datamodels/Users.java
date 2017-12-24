package com.prathab.data.datamodels;

import javax.xml.bind.annotation.XmlRootElement;

import com.prathab.data.base.dbmodel.DbObject;

@XmlRootElement
public class Users implements DbObject {
	private String id;
	private String name;
	private String mobile;
	private String email;
	private String password;

	public Users() {
	}

	public Users(String name, String mobile, String email, String password, String id) {
		this.name = name;
		this.mobile = mobile;
		this.email = email;
		this.password = password;
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
