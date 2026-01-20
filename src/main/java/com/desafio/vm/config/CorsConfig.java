package com.desafio.vm.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * Classe de configuração CORS (Cross-Origin Resource Sharing) para a aplicação.
 * 
 * <p>
 * Define as políticas de compartilhamento de recursos entre origens diferentes,
 * permitindo que a API seja acessada por frontends em domínios específicos.
 * </p>
 * 
 * <p>
 * Configurações principais:
 * <ul>
 * <li>Origins permitidos (frontends autorizados)</li>
 * <li>Métodos HTTP permitidos</li>
 * <li>Headers permitidos</li>
 * <li>Suporte a credenciais</li>
 * </ul>
 * </p>
 */

@Configuration
public class CorsConfig {

	/**
	 * Configura e retorna a fonte de configurações CORS.
	 * 
	 * <p>
	 * As configurações incluem:
	 * <ul>
	 * <li>Origins permitidos: domínios dos frontends e ambiente local</li>
	 * <li>Todos os métodos HTTP principais (GET, POST, PUT, DELETE, OPTIONS)</li>
	 * <li>Todos os headers permitidos</li>
	 * <li>Suporte a credenciais (cookies, autenticação HTTP)</li>
	 * </ul>
	 * </p>
	 * 
	 * <p>
	 * A configuração é aplicada a todos os endpoints da aplicação ("/**").
	 * </p>
	 * 
	 * @return Fonte de configuração CORS pronta para uso pelo Spring Security
	 */
	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration config = new CorsConfiguration();
		// Domínios autorizados para acessar a API
		config.setAllowedOrigins(List.of("http://localhost:8080/", "http://localhost:3000"));
		// Métodos HTTP permitidos
		config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
		// Todos os headers permitidos
		config.setAllowedHeaders(List.of("*"));
		// Permite envio de credenciais (cookies, autenticação)
		config.setAllowCredentials(true);

		// Aplica a configuração a todos os endpoints
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", config);

		return source;
	}

}
