package com.desafio.vm.dto;

import java.time.LocalDateTime;

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
public class TarefaDTO {

	/** ID único da tarefa. */
	private Long id;

	/** Relacionamento com quem fez a ação. */
	private UsuarioDTO usuario;

	/** Máquina que o usuário fez a ação. */
	private VirtualMachineDTO virtualMachine;

	/** Guarda o nome da VM deletada. */
	private String nomeMaquina;

	/** Ações disponiveis: "START", "STOP", "CREATE", "DELETE". */
	private String acao;

	/** Data e hora que o usuario fez as ações. */
	private LocalDateTime dataHora;

}
