package com.prathab.data.mysql.services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import com.prathab.data.base.DbObjectSpec;
import com.prathab.data.base.DbService;
import com.prathab.data.base.dbmodel.DbObject;
import com.prathab.data.base.exception.DbException;
import com.prathab.data.base.result.DeleteResult;
import com.prathab.data.base.result.InsertResult;
import com.prathab.data.base.result.ReadResult;
import com.prathab.data.base.result.UpdateResult;
import com.prathab.data.constants.DBConstants;
import com.prathab.data.datamodels.Users;

public class MysqlAccountsService implements DbService{

	@Override
	public ReadResult read(DbObjectSpec spec) throws DbException {
		Users inputSpec = (Users) spec.getObject();
	    String mobile = inputSpec.getMobile();
	    String email = inputSpec.getEmail();
	    
	    Connection connection = null;
	    Users fetchedUsers = null;

	    String nonNullValue = null;
	    String nonNullKey = null;
	    if (mobile != null) {
	      nonNullValue = mobile;
	      nonNullKey = DBConstants.DB_COLLECTION_USERS_MOBILE;
	    } else if (email != null) {
	      nonNullValue = email;
	      nonNullKey = DBConstants.DB_COLLECTION_USERS_EMAIL;
	    } else {
	      assert false;
	    }
	    try {
		    Class.forName("com.mysql.cj.jdbc.Driver");
		    
		    connection = DriverManager.getConnection(
			          "jdbc:mysql://localhost:3306/shopping", "shopping_user",
			          "shopping_user");
		    
		    Statement statement=connection.createStatement();  
		    ResultSet rs=statement.executeQuery("select * from users where "+ nonNullKey + "=" + nonNullValue);  
		    while(rs.next()) { 
			    	fetchedUsers = new Users();
			    	fetchedUsers.setName(rs.getString(DBConstants.DB_COLLECTION_USERS_NAME));
			    	fetchedUsers.setMobile(rs.getString(DBConstants.DB_COLLECTION_USERS_MOBILE));
			    	fetchedUsers.setPassword(rs.getString(DBConstants.DB_COLLECTION_USERS_PASSWORD));
		    }
	    }catch(Exception e){
	    		System.out.println(e);
	    	}

	    return new ReadResult(fetchedUsers);
	}

	@Override
	public DeleteResult delete(DbObjectSpec spec) throws DbException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InsertResult insert(DbObject object) throws DbException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UpdateResult update(DbObjectSpec spec) throws DbException {
		// TODO Auto-generated method stub
		return null;
	}
	
}
