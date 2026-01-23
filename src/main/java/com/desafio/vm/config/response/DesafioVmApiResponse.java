package com.desafio.vm.config.response;

import lombok.Data;

/**
 * Classe que representa uma resposta padrão da API DesafioVm . Esta classe é
 * genérica e pode encapsular qualquer tipo de dado na resposta.
 *
 * @param <T> Tipo genérico que representa o tipo dos dados que serão retornados
 *            na resposta
 */
@Data
public class DesafioVmApiResponse<T> {

	/**
	 * Código de mensagem que identifica o tipo de resposta ou erro
	 */
	private String codeMessage;

	/**
	 * Dados retornados na resposta. Pode ser qualquer tipo definido pelo genérico T
	 */
	private T dados;

	/**
	 * Código HTTP ou código de status da resposta
	 */
	private int code;

	/**
	 * Construtor principal para criar uma resposta da API DesafioVm
	 *
	 * @param code        Código HTTP ou de status da resposta
	 * @param codeMessage Mensagem descritiva do status
	 * @param dados       Dados a serem incluídos na resposta
	 */
	public DesafioVmApiResponse(int code, String codeMessage, T dados) {
		this.code = code;
		this.codeMessage = codeMessage;
		this.dados = dados;
	}

	/**
	 * Construtor alternativo que utiliza um enum ResponseEnum para padronizar as
	 * respostas
	 *
	 * @param responseEnum Enum que contém o status e mensagem padrão
	 * @param dados        Dados a serem incluídos na resposta
	 */
	public DesafioVmApiResponse(ResponseEnum responseEnum, T dados) {
		this(responseEnum.getStatus().value(), responseEnum.getMessage(), dados);
	}

}
