package com.desafio.vm.config.security.interceptor;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.desafio.vm.config.security.UserDetail;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Filtro de interceptação de requisições. Verifica a presença e validade do
 * Token JWT no cabeçalho Authorization.
 */
@Component
public class JwtInterceptorFilter extends OncePerRequestFilter {

	private final JwtService jwtService;

	public JwtInterceptorFilter(JwtService jwtService) {
		this.jwtService = jwtService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		// Extrai o Header
		final String authHeader = request.getHeader("Authorization");

		/**
		 * Se não houver Header ou não começar com Bearer, apenas segue o fluxo. O
		 * Spring Security (SecurityConfig) decidirá depois se essa rota exigia ou não o
		 * token.
		 */
		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			filterChain.doFilter(request, response);
			return;
		}

		final String jwt = authHeader.substring(7);

		/** Verifica se já não há alguém autenticado no contexto */
		if (SecurityContextHolder.getContext().getAuthentication() == null) {

			try {
				/** Valida o token e recupera o UserDetail */
				UserDetail userDetail = jwtService.validateTokenAndGetUser(jwt);

				if (userDetail != null) {
					UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userDetail, null,
							userDetail.getAuthorities());

					auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

					/** Define o usuário como autenticado */
					SecurityContextHolder.getContext().setAuthentication(auth);
				}
			} catch (Exception e) {
				/**
				 * Se o token for inválido, não faz nada. O SecurityConfig barrará a requisição
				 * nas rotas protegidas (403).
				 */
				logger.error("Erro ao validar token JWT: " + e.getMessage());
			}
		}

		filterChain.doFilter(request, response);
	}
}