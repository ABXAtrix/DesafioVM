package com.desafio.vm.config.security.interceptor;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.desafio.vm.config.security.UserDetail;
import com.desafio.vm.entity.Usuario;
import com.desafio.vm.repository.UsuarioRepository;
import com.desafio.vm.util.JwtUtil;

import io.jsonwebtoken.Claims;

/**
 * Serviço responsável pela lógica de validação e extração de dados do Token
 * JWT.
 */
@Service
public class JwtService {

	@Autowired
	private UsuarioRepository repository;

	@Autowired
	private JwtUtil jwtUtil;

	/**
	 * Extrai o e-mail (subject) de dentro do token.
	 */
	public String extractEmail(String token) {
		return extractAllClaims(token).getSubject();
	}

	/**
	 * Verifica se o token não expirou e é válido.
	 */
	public boolean isTokenValid(String token) {
		try {
			Claims claims = extractAllClaims(token);
			return claims.getExpiration().after(new Date());
		} catch (Exception e) {
			return false;
		}
	}

	private Claims extractAllClaims(String token) {
		return jwtUtil.extractAllClaims(token);
	}

	/**
	 * Valida o token e retorna o UserDetail para preencher o contexto de segurança.
	 */
	public UserDetail validateTokenAndGetUser(String token) {
		if (!isTokenValid(token)) {
			return null;
		}

		String email = extractEmail(token);
		Usuario usuario = buscarUsuarioPorEmail(email);

		if (usuario == null) {
			return null;
		}

		return new UserDetail(usuario);
	}

	private Usuario buscarUsuarioPorEmail(String email) {
		return repository.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com o e-mail: " + email));
	}
}