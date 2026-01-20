package com.desafio.vm.config.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.desafio.vm.entity.Usuario;

/**
 * Utilitário para recuperar informações do usuário autenticado.
 */
@Component
public class SecurityUtil {

	/**
	 * Recupera o ID do usuário que está autenticado na requisição atual. * @return
	 * Long ID do usuário.
	 * 
	 * @throws RuntimeException se o usuário não estiver autenticado.
	 */
	public static Long getCurrentUserId() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication == null || !authentication.isAuthenticated()) {
			throw new RuntimeException("Usuário não autenticado no contexto de segurança.");
		}

		Object principal = authentication.getPrincipal();

		if (principal instanceof UserDetail) {
			return ((UserDetail) principal).getUsuario().getId();
		}

		throw new RuntimeException("Dados do usuário logado estão em formato inválido.");
	}

	/**
	 * Recupera o objeto Usuario completo contido no UserDetail da sessão. Evita uma
	 * consulta extra ao banco de dados se os dados já estiverem no contexto.
	 * * @return Usuario entidade completa.
	 */
	public static Usuario getUsuarioLogado() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication != null && authentication.getPrincipal() instanceof UserDetail userDetail) {
			return userDetail.getUsuario();
		}

		throw new RuntimeException("Não foi possível recuperar o usuário da sessão.");
	}
}