package com.desafio.vm.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * DTO que representa a estrutura de dados do Usuário para comunicação externa.
 * Inclui a lista de máquinas virtuais vinculadas para controle e monitoramento.
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class UsuarioDTO {

	/** ID único do usuário. */
	private Long id;

	/** Nome do usuário. */
	private String nome;

	/** E-mail utilizado como identificador no sistema. */
	private String email;

	/**
	 * * Senha do usuário. WRITE_ONLY garante que a senha seja recebida no cadastro,
	 * mas nunca enviada de volta nas respostas da API por segurança.
	 */
	@JsonProperty(access = Access.WRITE_ONLY)
	private String senha;

	/** Lista de máquinas virtuais pertencentes ao usuário. */
	private List<VirtualMachineDTO> maquinas;

}
