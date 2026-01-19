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
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Data
@Table(name = "VIRTUAL_MACHINE")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VirtualMachine {

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "NOME")
	private String nome;

	@Column(name = "CPU")
	private BigDecimal cpu;

	@Column(name = "MEMORIA")
	private BigDecimal memoria;

	@Column(name = "DISCO")
	private BigDecimal disco;

	@Column(name = "DATA_CRIACAO")
	private LocalDateTime dataCriação;

	@Column(name = "STATUS")
	@Enumerated(EnumType.STRING)
	private StatusVmEnum status;

}
