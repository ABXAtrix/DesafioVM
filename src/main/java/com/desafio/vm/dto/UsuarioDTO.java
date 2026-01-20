package com.desafio.vm.dto;

import java.util.List;

import com.desafio.vm.entity.VirtualMachine;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class UsuarioDTO {

	private Long id;

	private String nome;

	private String email;

	// Esta anotação faz com que a senha seja aceita no POST/PUT,
	// mas NUNCA apareça no GET (retorno da API)
	@JsonProperty(access = Access.WRITE_ONLY)
	private String senha;

	private List<VirtualMachine> maquinas;

}
