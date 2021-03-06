package com.cromey.access;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static java.util.Collections.emptyList;

import java.util.Objects;

class TokenAuthenticationService {
	static final long EXPIRATIONTIME = 864_000_000; // 10 days
	static final String SECRET = "ThisIsASecret";
	static final String TOKEN_PREFIX = "Bearer";
	static final String AUTHORIZATION_HEADER = "Authorization";
	static final String SUB_HEADER = "ID";

	static void addAuthentication(HttpServletResponse res, String username) {
		try {
			Algorithm algorithm = Algorithm.HMAC256("secret");
			String token = JWT.create().withClaim("sub", "1").withIssuer("auth0").sign(algorithm);
			res.addHeader(AUTHORIZATION_HEADER, TOKEN_PREFIX + " " + token);
		} catch (JWTVerificationException exception) {
			System.out.println(exception.getMessage());
		}
	}

	static Authentication getAuthentication(HttpServletRequest request) {
		String token = request.getHeader(AUTHORIZATION_HEADER);
		String id = request.getHeader(SUB_HEADER);
		if (token != null) {
			// parse the token.
			try {
				Algorithm algorithm = Algorithm.HMAC256("secret");
				JWTVerifier verifier = JWT.require(algorithm).withIssuer("auth0").build(); // Reusable verifier instance
				DecodedJWT jwt = verifier.verify(token);
				// if()
				if (Objects.nonNull(jwt.getSubject())) {
					if(!jwt.getSubject().equalsIgnoreCase(id)) 
						return null;
					return new UsernamePasswordAuthenticationToken(jwt.getClaim("sub"), null, emptyList());
				} else {
					return null;
				}
			} catch (JWTVerificationException exception) {
				System.out.println(exception.getMessage());
			}

		}
		return null;
	}
}