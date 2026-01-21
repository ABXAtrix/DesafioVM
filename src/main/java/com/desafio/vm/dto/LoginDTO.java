package com.desafio.vm.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * DTO para transporte de credenciais durante a autenticação. Implementa
 * validações de entrada via Spring Validation para segurança inicial.
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Data
public class LoginDTO {

	/**
	 * E-mail do usuário para autenticação. Validado através da anotação de Email
	 * para garantir formato correto.
	 */
	@NotBlank(message = "O e-mail é obrigatório")
	@Email(message = "E-mail inválido")
	private String email;

	/** Senha do usuário. Campo obrigatório para processamento do login. */
	@NotBlank(message = "A senha é obrigatória")
	private String senha;

}
