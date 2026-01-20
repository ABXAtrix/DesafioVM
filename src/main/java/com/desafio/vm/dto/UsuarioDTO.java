package com.desafio.vm.dto;

import java.util.List;

import com.desafio.vm.entity.VirtualMachine;

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

	private String senha;

	private List<VirtualMachine> maquinas;

}
