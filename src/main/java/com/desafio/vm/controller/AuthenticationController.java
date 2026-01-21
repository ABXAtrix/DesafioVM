package com.desafio.vm.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.desafio.vm.config.security.UserDetail;
import com.desafio.vm.dto.LoginDTO;
import com.desafio.vm.entity.Usuario;
import com.desafio.vm.repository.UsuarioRepository;
import com.desafio.vm.util.JwtUtil;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private JwtUtil jwtUtil;

	/**
	 * Cria um novo usuário no sistema. A senha é criptografada via BCrypt antes da
	 * persistência para garantir a segurança.
	 * 
	 * @param usuario Objeto contendo nome, e-mail e senha.
	 * @return Resposta de sucesso ou erro caso o e-mail já exista.
	 */

	@PostMapping("/registrar")
	public ResponseEntity<?> registrar(@Valid @RequestBody Usuario usuario) {
		if (usuarioRepository.findByEmail(usuario.getEmail()).isPresent()) {
			return ResponseEntity.badRequest().body(Map.of("message", "E-mail já cadastrado"));
		}

		usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));

		Usuario salvo = usuarioRepository.save(usuario);
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(Map.of("message", "Usuário criado com sucesso", "id", salvo.getId()));
	}

	/**
	 * Valida as credenciais do usuário e gera um token JWT. Atende ao requisito de
	 * login com validação de formato de e-mail através do LoginDTO.
	 * 
	 * @param loginDTO Contém e-mail e senha informados.
	 * @return Objeto contendo o token JWT, e-mail e ID do usuário autenticado.
	 */

	@PostMapping("/login")
	public ResponseEntity<?> login(@Valid @RequestBody LoginDTO loginDTO) {
		try {
			Authentication auth = authenticationManager
					.authenticate(new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getSenha()));

			UserDetail user = (UserDetail) auth.getPrincipal();
			String token = jwtUtil.generateToken(user.getUsername());

			return ResponseEntity
					.ok(Map.of("token", token, "email", user.getUsername(), "userId", user.getUsuario().getId()));
		} catch (BadCredentialsException e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Credenciais inválidas"));
		}
	}

	/**
	 * Invalida a sessão do usuário. Embora o JWT seja stateless, este endpoint
	 * garante a limpeza do SecurityContext.
	 */

	@PostMapping("/logout")
	public ResponseEntity<?> logout(jakarta.servlet.http.HttpServletRequest request,
			jakarta.servlet.http.HttpServletResponse response) {

		Authentication auth = org.springframework.security.core.context.SecurityContextHolder.getContext()
				.getAuthentication();
		if (auth != null) {
			new org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler().logout(request,
					response, auth);
		}

		return ResponseEntity.ok(Map.of("message", "Logout realizado com sucesso."));
	}

	/**
	 * Retorna as informações do perfil do usuário logado através da extração do
	 * token JWT.
	 * 
	 * @param userDetail Detalhes do usuário recuperados do SecurityContext.
	 * @return Dados simplificados do perfil (ID, Nome e E-mail).
	 */

	@GetMapping("/me")
	public ResponseEntity<?> getMe(@AuthenticationPrincipal UserDetail userDetail) {
		if (userDetail == null) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("message", "Não autorizado"));
		}

		Usuario u = userDetail.getUsuario();
		return ResponseEntity.ok(Map.of("id", u.getId(), "nome", u.getNome(), "email", u.getEmail()));
	}
}