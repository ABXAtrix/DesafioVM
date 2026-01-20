package com.desafio.vm.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Provedor de autenticação customizado. Esta classe é responsável por validar
 * se a combinação de e-mail e senha fornecida pelo usuário condiz com os dados
 * armazenados no banco.
 */
@Component
public class AuthenticationProviderManager implements AuthenticationProvider {

	@Autowired
	private UserDetailService userDetailsService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String email = authentication.getName();
		String password = authentication.getCredentials().toString();

		// Busca o usuário no banco via UserDetailService
		UserDetail userDetails = userDetailsService.loadUserByUsername(email);

		// Valida a senha usando o utilitário de criptografia
		if (!passwordEncoder.matches(password, userDetails.getPassword())) {
			throw new BadCredentialsException("E-mail ou senha inválidos");
		}

		// Retorna o token de autenticação preenchido (Usuário autenticado com sucesso)
		return new UsernamePasswordAuthenticationToken(userDetails, null, // Por segurança, não mantemos a senha em
																			// memória após a autenticação
				userDetails.getAuthorities());
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
	}
}