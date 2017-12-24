package com.prathab.api.shopping.utility;

import static com.prathab.api.shopping.constants.JwtConstants.JWT_ISSUER;
import static com.prathab.api.shopping.constants.JwtConstants.JWT_SECRET;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.prathab.api.shopping.constants.JwtConstants;

public class JwtUtility {

	private static Algorithm algorithm = null;
	private static JWTVerifier verifier = null;

	static {
		try {
			algorithm = Algorithm.HMAC256(JWT_SECRET);
			verifier = JWT.require(algorithm).withIssuer(JWT_ISSUER).build();
		} catch (IllegalArgumentException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	public static String createAndGetJWT(HashMap<String, String> claims) {
		// Calculate expiry date
		Calendar calendar = Calendar.getInstance();
		Date currentDate = calendar.getTime();
		calendar.add(Calendar.MONTH, 1);
		Date expiryDate = calendar.getTime();

		String jwt = null;
		try {
			JWTCreator.Builder builder = JWT.create().withIssuer(JWT_ISSUER).withIssuedAt(currentDate);
			// Add Claims to JWT
			for (Map.Entry<String, String> claim : claims.entrySet()) {
				builder.withClaim(claim.getKey(), claim.getValue());
			}

			jwt = builder.withExpiresAt(expiryDate).sign(algorithm);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jwt;
	}

	public static boolean validateJWT(String jwt) {
		try {
			verifier.verify(jwt);
		} catch (JWTVerificationException exception) {
			return false;
		}
		return true;
	}

	private static String getClaim(String token, String name) {
		DecodedJWT jwt = null;
		try {
			jwt = JWT.decode(token);
		} catch (JWTDecodeException exception) {
			return null;
		}
		return jwt.getClaim(name).asString();
	}

	public static String getMobile(String token) {
		String mobile = getClaim(token, JwtConstants.JWT_CLAIM_MOBILE);
		mobile = Validators.getInternationalPhoneNumber(mobile);
		return mobile;
	}
}
