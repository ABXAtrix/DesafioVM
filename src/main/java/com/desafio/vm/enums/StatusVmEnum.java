package com.desafio.vm.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StatusVmEnum {

	START("START"), 
	STOP("STOP"),
	SUSPEND("SUSPEND");

	private final String StatusVm;

}
