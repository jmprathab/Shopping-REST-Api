package com.prathab.api.shopping.utility;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.prathab.api.shopping.constants.JwtConstants.JWT_ISSUER;
import static com.prathab.api.shopping.constants.JwtConstants.JWT_SECRET;

public class JwtUtility {

  public static String createAndGetJWT(HashMap<String, String> claims) {
    // Calculate expiry date
    Calendar calendar = Calendar.getInstance();
    Date currentDate = calendar.getTime();
    calendar.add(Calendar.MONTH, 1);
    Date expiryDate = calendar.getTime();

    String jwt = null;
    try {
      Algorithm algorithm = Algorithm.HMAC256(JWT_SECRET);
      JWTCreator.Builder builder = JWT.create()
          .withIssuer(JWT_ISSUER)
          .withIssuedAt(currentDate);

      //Add Claims to JWT
      for (Map.Entry<String, String> claim : claims.entrySet()) {
        builder.withClaim(claim.getKey(), claim.getValue());
      }

      jwt = builder.withExpiresAt(expiryDate).sign(algorithm);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return jwt;
  }
}
