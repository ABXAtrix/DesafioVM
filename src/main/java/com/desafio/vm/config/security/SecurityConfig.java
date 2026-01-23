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

import com.desafio.vm.config.security.interceptor.JwtInterceptorFilter;

/**
 * Classe de configuração de segurança do Spring Security. Define políticas de
 * acesso, gerenciamento de sessão stateless e a ordem dos filtros de
 * autenticação.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Autowired
	private AuthenticationProviderManager authenticationProviderManager;

	@Autowired
	private JwtInterceptorFilter jwtInterceptorFilter;

	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.cors(withDefaults()).csrf(csrf -> csrf.disable())
				// 1. Define a política de sessão como STATELESS (essencial para JWT)
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

				// 2. Configura o provedor de autenticação customizado
				.authenticationProvider(authenticationProviderManager)

				// 3. Regras de Autorização (A ordem importa!)
				.authorizeHttpRequests(auth -> auth
						// Libera o Swagger e a documentação JSON
						.requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html", "/webjars/**")
						.permitAll()

						// Libera Login e Registro
						.requestMatchers("/api/auth/**").permitAll()

						// Bloqueia TODO o resto. Qualquer nova rota criada estará protegida por padrão.
						.anyRequest().authenticated())

				// 4. Adiciona o filtro JWT antes do filtro de autenticação padrão do Spring
				.addFilterBefore(jwtInterceptorFilter,
						org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class);

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