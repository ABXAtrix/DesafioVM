package com.desafio.vm.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.desafio.vm.entity.Usuario;
import com.desafio.vm.repositroy.UsuarioRepository;

/**
 * Service responsável pela integração entre o Spring Security e o banco de
 * dados. Implementa a interface {@link UserDetailsService} para carregar os
 * dados do usuário durante o processo de autenticação.
 */
@Service
public class UserDetailService implements UserDetailsService {

	@Autowired
	private UsuarioRepository usuarioRepository;

	/**
	 * Localiza um usuário no banco de dados através do e-mail fornecido. Este
	 * método é utilizado internamente pelo Spring Security para validar o login.
	 * * @param email O e-mail do usuário submetido no formulário de login.
	 * 
	 * @return Um objeto {@link UserDetail} contendo as informações do usuário para
	 *         a sessão.
	 * @throws UsernameNotFoundException Caso o e-mail não exista na base de dados.
	 */
	@Override
	public UserDetail loadUserByUsername(String email) throws UsernameNotFoundException {
		// O requisito define o e-mail como identificador único para autenticação.
		Usuario usuario = usuarioRepository.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("Usuário com e-mail " + email + " não encontrado"));

		return new UserDetail(usuario);
	}

	/**
	 * Recupera o objeto {@link Usuario} completo do banco de dados referente ao
	 * usuário autenticado na requisição atual via Token JWT. * @return O objeto
	 * {@link Usuario} persistido.
	 * 
	 * @throws RuntimeException Caso ocorra algum erro na recuperação dos dados.
	 */
	public Usuario getUsuarioLogado() {
		String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		return usuarioRepository.findByEmail(email)
				.orElseThrow(() -> new RuntimeException("Erro ao recuperar usuário logado no banco de dados."));
	}
}