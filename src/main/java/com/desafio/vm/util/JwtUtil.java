package com.desafio.vm.util;

import java.util.Base64;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

	private final long EXPIRATION_TIME = 86400000; // 1 dia em ms

	private final String SECRET = "w3lE7k+6o9Qp2KDFRW5U3zvKnR3f7Xz+VwbD/JajxgI=";

	private final SecretKey SECRET_KEY;

	public JwtUtil() {
		byte[] decodedKey = Base64.getDecoder().decode(SECRET);
		this.SECRET_KEY = Keys.hmacShaKeyFor(decodedKey);
	}

	public String generateToken(String username) {
		return Jwts.builder().setSubject(username).setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)).signWith(SECRET_KEY).compact();
	}

	public Claims extractAllClaims(String token) {
		return Jwts.parserBuilder().setSigningKey(SECRET_KEY).build().parseClaimsJws(token).getBody();
	}

}
