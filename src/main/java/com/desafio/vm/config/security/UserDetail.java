package com.desafio.vm.config.security;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.desafio.vm.entity.Usuario;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * Classe que atua como um adaptador entre a entidade Usuario e o Spring
 * Security. Implementa UserDetails para fornecer as informações necessárias de
 * autenticação e autorização.
 */
@Getter
@Setter
@Data
public class UserDetail implements UserDetails {

	private static final long serialVersionUID = 1L;

	/**
	 * Entidade de usuário proveniente do banco de dados.
	 */
	private final Usuario usuario;

	/**
	 * Define as permissões (roles) do usuário.
	 * 
	 * @return Uma coleção contendo a autoridade "ROLE_USER" por padrão.
	 */
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// Para o desafio, vamos considerar que todo usuário logado tem permissão de
		// "USER"
		return Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
	}

	/**
	 * Recupera a senha criptografada do usuário.
	 * 
	 * @return String contendo o hash da senha.
	 */
	@Override
	public String getPassword() {
		return usuario.getSenha();
	}

	/**
	 * Define o identificador principal do usuário para o Spring Security.
	 * 
	 * @return O e-mail do usuário, conforme requisito de login por e-mail.
	 */
	@Override
	public String getUsername() {
		return usuario.getEmail();
	}

	/**
	 * Indica se a conta do usuário expirou.
	 * 
	 * @return true para indicar que a conta permanece válida.
	 */
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	/**
	 * Indica se o usuário está bloqueado ou desbloqueado.
	 * 
	 * @return true para indicar que a conta não está bloqueada.
	 */
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	/**
	 * Indica se as credenciais (senha) expiraram.
	 * 
	 * @return true para indicar que as credenciais são válidas.
	 */
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	/**
	 * Indica se o usuário está habilitado ou desabilitado.
	 * 
	 * @return true para indicar que o usuário está ativo.
	 */
	@Override
	public boolean isEnabled() {
		return true;
	}

	/**
	 * Método auxiliar para facilitar o acesso ao ID do banco de dados sem a
	 * necessidade de navegar manualmente pelo objeto Usuario.
	 * 
	 * @return Long contendo o ID do usuário.
	 */
	public Long getId() {
		return usuario.getId();
	}
}