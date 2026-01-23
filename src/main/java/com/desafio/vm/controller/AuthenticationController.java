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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Autenticação", description = "API para conseguir entrar no sistema e sair")
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

	@Operation(summary = "Registrar novo usuário", description = "Cria um novo usuário. "
			+ "Nome deve ter no mínimo 3 caracteres e senha deve ter no mínimo 6 caracteres .")
	@ApiResponse(responseCode = "201", description = "Usuário criado com sucesso")
	@ApiResponse(responseCode = "400", description = "E-mail já cadastrado ou dados inválidos")
	@PostMapping("/registrar")
	public ResponseEntity<?> registrar(@Valid @RequestBody Usuario usuario) {
		// 1. Validação de e-mail duplicado
		if (usuarioRepository.findByEmail(usuario.getEmail()).isPresent()) {
			return ResponseEntity.badRequest().body(Map.of("message", "E-mail já cadastrado"));
		}

		// 2. Criptografia da senha
		usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));

		// 3. Cargo padrão de Usuário, caso o usuário esqueça de preencher o campo
		if (usuario.getCargo() == null) {
			usuario.setCargo(com.desafio.vm.enums.Cargos.USUARIO);
		}

		Usuario salvo = usuarioRepository.save(usuario);
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(Map.of("message", "Usuário criado com sucesso", "id", salvo.getId(), "cargo", salvo.getCargo()));
	}

	/**
	 * Valida as credenciais do usuário e gera um token JWT. Atende ao requisito de
	 * login com validação de formato de e-mail através do LoginDTO.
	 * 
	 * @param loginDTO Contém e-mail e senha informados.
	 * @return Objeto contendo o token JWT, e-mail e ID do usuário autenticado.
	 */

	@Operation(summary = "Realizar Login", description = "Valida as credenciais e retorna um Token JWT para acesso aos demais endpoints.")
	@ApiResponse(responseCode = "200", description = "Login realizado com sucesso. Retorna o token JWT.")
	@ApiResponse(responseCode = "401", description = "Credenciais inválidas")
	@PostMapping("/obterToken")
	public ResponseEntity<?> login(@Valid @RequestBody LoginDTO loginDTO) {
		try {
			Authentication auth = authenticationManager
					.authenticate(new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getSenha()));

			UserDetail user = (UserDetail) auth.getPrincipal();
			String token = jwtUtil.generateToken(user);

			return ResponseEntity.ok(Map.of("token", token));
		} catch (BadCredentialsException e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Credenciais inválidas"));
		}
	}

	/**
	 * Invalida a sessão do usuário. Embora o JWT seja stateless, este endpoint
	 * garante a limpeza do SecurityContext.
	 */

	@Operation(summary = "Realizar Logout", description = "Invalida o contexto de segurança atual do servidor.")
	@ApiResponse(responseCode = "200", description = "Logout processado com sucesso")
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

	@Operation(summary = "Obter perfil atual", description = "Retorna os dados do usuário logado baseando-se no Token JWT enviado no Header.")
	@ApiResponse(responseCode = "200", description = "Dados do perfil retornados com sucesso")
	@ApiResponse(responseCode = "403", description = "Token inválido ou ausente")
	@GetMapping("/me")
	public ResponseEntity<?> getMe(@AuthenticationPrincipal UserDetail userDetail) {
		if (userDetail == null) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("message", "Não autorizado"));
		}

		Usuario u = userDetail.getUsuario();
		return ResponseEntity.ok(Map.of("id", u.getId(), "nome", u.getNome(), "email", u.getEmail()));
	}
}