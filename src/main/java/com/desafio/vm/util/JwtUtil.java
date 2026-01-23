package com.desafio.vm.util;

import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.desafio.vm.config.security.UserDetail;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;

@Component
public class JwtUtil {

	@Value("${jwt.expiration}")
	private long expirationTime;

	@Value("${jwt.secret}")
	private String secret;

	private SecretKey secretKey;

	@PostConstruct
	public void init() {
		byte[] decodedKey = Base64.getDecoder().decode(this.secret);
		this.secretKey = Keys.hmacShaKeyFor(decodedKey);
	}

	public String generateToken(UserDetail userDetail) {
		Map<String, Object> claims = new HashMap<>();
		claims.put("cargo", userDetail.getUsuario().getCargo().name());
		claims.put("nome", userDetail.getUsuario().getNome());

		return Jwts.builder().setClaims(claims).setSubject(userDetail.getUsername())
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + expirationTime)).signWith(secretKey).compact();
	}

	public Claims extractAllClaims(String token) {
		return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody();
	}
}