package com.prathab.api.shopping.constants;

public class JwtConstants {

  /**
   * JWT Constants
   */
  // TODO Change JWT_SECRET value
  public static final String JWT_SECRET = "secret";
  public static final String JWT_ISSUER = "shopping.com";

  /**
   * JWT Claim Constants
   */
  public static final String JWT_CLAIM_ID = "id";
  public static final String JWT_CLAIM_NAME = "name";
  public static final String JWT_CLAIM_MOBILE = "mobile";
  public static final String JWT_CLAIM_USERS_TYPE = "users_type";
}
