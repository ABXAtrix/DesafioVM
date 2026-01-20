package com.desafio.vm.config.security;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.desafio.vm.config.security.interceptor.JwtInterceptorFilter;

/**
 * Classe de configuração de segurança do Spring Security. Define políticas de
 * acesso, gerenciamento de sessão stateless e a ordem dos filtros de
 * autenticação.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

	/**
	 * Provedor customizado que valida credenciais (e-mail/senha) no banco de dados.
	 */
	@Autowired
	private AuthenticationProviderManager authenticationProviderManager;

	/** Filtro interceptador que valida o Token JWT em cada requisição. */
	@Autowired
	private JwtInterceptorFilter jwtInterceptorFilter;

	/**
	 * Configuração da corrente de filtros de segurança (Security Filter Chain).
	 * * @param http Objeto para configurar a segurança baseada em requisições HTTP.
	 * 
	 * @return A instância de SecurityFilterChain configurada.
	 * @throws Exception Caso ocorra erro na configuração.
	 */
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
				// Configura o CORS com as definições padrão (permitindo integração com
				// Angular/Frontend)
				.cors(withDefaults())

				.csrf(csrf -> csrf.disable())

				// Define a política de sessão como STATELESS (a API não armazena estado do
				// usuário no servidor)
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

				// Registra o provedor de autenticação customizado que criamos
				.authenticationProvider(authenticationProviderManager)

				// Configura regras de autorização de rotas
				.authorizeHttpRequests(auth -> auth
						// Libera todos os endpoints sob /api/auth/** (Registro e Login)
						.requestMatchers("/api/auth/**").permitAll()
						// Exige autenticação para qualquer outra requisição na API
						.anyRequest().authenticated())

				.addFilterBefore(jwtInterceptorFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

	/**
	 * Expõe o AuthenticationManager como um Bean para ser utilizado no
	 * AuthenticationController. * @param config Configuração de autenticação do
	 * Spring.
	 * 
	 * @return O gerenciador de autenticação configurado.
	 * @throws Exception Caso ocorra erro ao recuperar o gerenciador.
	 */
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}
}