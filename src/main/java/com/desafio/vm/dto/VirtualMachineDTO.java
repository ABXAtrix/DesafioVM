package com.desafio.vm.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.desafio.vm.enums.StatusVmEnum;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * DTO para representação de Máquinas Virtuais.
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class VirtualMachineDTO {

	/** Identificador da máquina. Necessário para operações de edição e exclusão. */
	private Long id;

	/** Nome da VM. Requisito mínimo de 5 caracteres. */
	private String nome;

	/** Quantidade de núcleos de CPU da VM. */
	private Integer cpu;

	/** Quantidade de memória RAM em GB. */
	private BigDecimal memoria;

	/** Espaço de armazenamento em disco em GB. */
	private BigDecimal disco;

	/** Data de criação registrada automaticamente pelo sistema. */
	private LocalDateTime dataCriacao;

	/** Estado atual da VM (START, STOP, SUSPEND). */
	private StatusVmEnum status;

	/** ID do proprietário. Utilizado para validar o limite de 5 VMs por conta. */
	private Long usuarioId;

}
