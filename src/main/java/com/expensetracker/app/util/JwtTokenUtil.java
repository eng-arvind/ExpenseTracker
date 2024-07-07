package com.expensetracker.app.util;

import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtTokenUtil {
	
	 private static final String SECRET = "638CBE3A90E0303BF3808F40F95A7F02A24B4B5D029C954CF553F79E9EF1DC0384BE681C249F1223F6B55AA21DC070914834CA22C8DD98E14A872CA010091ACC";
	 private static final long VALIDITY = TimeUnit.MINUTES.toMillis(30);

	public String generateToken(UserDetails userDetails) {
		  Map<String, String> claims = new HashMap<>();		
		return Jwts.builder()
		.claims(claims)
		.subject(userDetails.getUsername())
		 .issuedAt(Date.from(Instant.now()))
		 .expiration(Date.from(Instant.now().plusMillis(VALIDITY)))
		 .signWith(getSigningKey())
		.compact();
		
	}
	
	private SecretKey getSigningKey() {
        byte[] keyBytes = Base64.getDecoder().decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }
	
	public String getUsernameFromToken(String jwtToken) {
		Claims claims =  getClaimFromToken(jwtToken);
		return claims.getSubject();
	}
	
	private Claims getClaimFromToken(String token) {
		return Jwts.parser()
		.verifyWith(getSigningKey())
		.build()
		.parseSignedClaims(token)
		.getPayload();
	}

	public boolean isTokenvalidate(String jwtToken) {
		Claims claims =  getClaimFromToken(jwtToken);
		return  claims.getExpiration().after(Date.from(Instant.now()));
	}

//	private boolean isTokenExpired(String jwtToken) {
//		Claims claims = getClaimFromToken(jwtToken);
//		return claims.getExpiration().after(Date.from(Instant.now()));
//	}

}
