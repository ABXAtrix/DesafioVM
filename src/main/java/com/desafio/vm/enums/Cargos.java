package com.desafio.vm.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Cargos {
	
	USUARIO("USUARIO"),
	ADMIN("ADMIN");
	
	private final String roles;

}
