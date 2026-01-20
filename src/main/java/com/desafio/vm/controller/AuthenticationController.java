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
	 * ROTA DE REGISTRO: Permite criar um novo usuário. A senha é criptografada
	 * antes de salvar no banco.
	 */
	@PostMapping("/registrar")
	public ResponseEntity<?> registrar(@Valid @RequestBody Usuario usuario) {
		if (usuarioRepository.findByEmail(usuario.getEmail()).isPresent()) {
			return ResponseEntity.badRequest().body(Map.of("message", "E-mail já cadastrado"));
		}

		// Criptografa a senha com BCrypt (definido no SecurityConfig)
		usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));

		Usuario salvo = usuarioRepository.save(usuario);
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(Map.of("message", "Usuário criado com sucesso", "id", salvo.getId()));
	}

	/**
	 * ROTA DE LOGIN: Autentica e retorna o Token JWT.
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
	 * ROTA ME: Retorna os dados do usuário logado através do Token.
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