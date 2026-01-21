package com.desafio.vm.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.desafio.vm.enums.StatusVmEnum;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entidade que representa uma Máquina Virtual (VM). Contém especificações de
 * hardware (CPU, Memória, Disco) e o estado atual da máquina.
 */

@Getter
@Setter
@Data
@Table(name = "VIRTUAL_MACHINE")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VirtualMachine {

	/** Identificador único da VM gerado pela base de dados. */
	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/** Nome da máquina virtual (mínimo 5 caracteres). */
	@Column(name = "NOME")
	@NotBlank(message = "O nome é obrigatório")
	@Size(min = 5, message = "O nome deve ter no mínimo 5 caracteres")
	private String nome;

	/** Quantidade de núcleos de CPU (deve ser maior que zero). */
	@Min(value = 1, message = "A CPU deve ter pelo menos 1 núcleo")
	@Positive
	@Column(name = "CPU")
	@NotNull(message = "A quantidade de núcleos da CPU é obrigatório")
	private Integer cpu;

	/** Quantidade de memória RAM em GB (deve ser maior que zero). */
	@Min(1)
	@Positive(message = "A memória deve ser maior que zero")
	@Column(name = "MEMORIA")
	@NotNull(message = "A quantidade de memória RAM é obrigatória")
	private BigDecimal memoria;

	/** Tamanho do disco em GB (deve ser maior que zero). */
	@Min(1)
	@Positive(message = "O disco deve ser maior que zero")
	@Column(name = "DISCO")
	@NotNull(message = "A quantidade de memória do disco")
	private BigDecimal disco;

	/** Data e hora de criação da VM, gerada automaticamente pelo sistema. */
	@Column(name = "DATA_CRIACAO")
	private LocalDateTime dataCriacao;

	/** Estado atual da VM (START, STOP, SUSPEND). */
	@Column(name = "STATUS")
	@Enumerated(EnumType.STRING)
	private StatusVmEnum status;

	/**
	 * * Relacionamento com o usuário proprietário. Chave estrangeira utilizada para
	 * controle de acesso e limites.
	 */
	@ManyToOne /** Indica que muitas VMs pertencem a um único usuário. */
	@JoinColumn(name = "USUARIO_ID") /** Nome da coluna que será a chave estrangeira no banco. */
	private Usuario usuario;

}
