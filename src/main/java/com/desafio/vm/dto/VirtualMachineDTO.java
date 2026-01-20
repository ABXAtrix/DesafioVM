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

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class VirtualMachineDTO {

	private Long id;

	private String nome;

	private Integer cpu;

	private BigDecimal memoria;

	private BigDecimal disco;

	private LocalDateTime dataCriacao;

	private StatusVmEnum status;

}
