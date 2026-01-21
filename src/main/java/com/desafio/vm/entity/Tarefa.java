package com.desafio.vm.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Table(name = "TAREFAS")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tarefa {

	/** Identificador único da tarefa gerado automaticamente. */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Long id;

	/** Relacionamento com quem fez a ação. */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "usuario_id", nullable = false)
	private Usuario usuario;

	/** Máquina que o usuário fez a ação. */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "virtual_machine_id", nullable = true)
	private VirtualMachine virtualMachine;

	/** Guarda o nome da VM deletada. */
	@Column(name = "NOME_MAQUINA")
	private String nomeMaquina;

	/** Ações disponiveis: "START", "STOP", "CREATE", "DELETE". */
	@Column(name = "ACAO")
	private String acao;

	/** Data e hora que o usuario fez as ações. */
	@Column(name = "DATA_HORA")
	private LocalDateTime dataHora;

}
