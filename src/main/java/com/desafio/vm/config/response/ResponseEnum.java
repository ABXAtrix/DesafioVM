package com.desafio.vm.config.response;

import org.springframework.http.HttpStatus;

import lombok.Getter;

/**
 * Enumeração que padroniza as respostas da API do sistema WMS.
 * 
 * <p>
 * Define mensagens e códigos HTTP padrão para os diferentes cenários de
 * resposta da API, garantindo consistência em toda a aplicação.
 * </p>
 * 
 * <p>
 * Cada constante representa um tipo específico de resposta que pode ser
 * retornada pelas operações da API, associando uma mensagem descritiva ao
 * status HTTP apropriado.
 * </p>
 */

@Getter
public enum ResponseEnum {

	/**
	 * Operação genérica realizada com sucesso
	 */
	SUCESSO("Operação realizada com sucesso", HttpStatus.OK),

	/**
	 * Resposta para quando uma lista de itens é retornada com sucesso
	 */
	LISTA_OBTIDA("Lista obtida com sucesso", HttpStatus.OK),

	/**
	 * Resposta para quando um registro específico é encontrado
	 */
	REGISTRO_ENCONTRADO("Registro encontrado com sucesso", HttpStatus.OK),

	/**
	 * Resposta para criação bem-sucedida de um registro
	 */
	REGISTRO_CRIADO("Registro criado com sucesso", HttpStatus.CREATED),

	/**
	 * Resposta para atualização bem-sucedida de um registro
	 */
	REGISTRO_ATUALIZADO("Registro atualizado com sucesso", HttpStatus.OK),

	/**
	 * Resposta para exclusão bem-sucedida de um registro
	 */
	REGISTRO_EXCLUIDO("Registro excluído com sucesso", HttpStatus.NO_CONTENT),

	/**
	 * Resposta para quando um registro solicitado não é encontrado
	 */
	REGISTRO_NAO_ENCONTRADO("Registro não encontrado", HttpStatus.NOT_FOUND),

	/**
	 * Resposta genérica para erros inesperados
	 */
	ERROR("Erro ao realizar a requisição, favor contatar o administrador", HttpStatus.BAD_REQUEST),

	/**
	 * Resposta específica para quando um produto possui estoque vinculado
	 */
	PRODUTO_ESTOQUE("Produto possui estoque vinculado!", HttpStatus.NO_CONTENT);

	/**
	 * Mensagem descritiva associada ao tipo de resposta
	 */
	private final String message;

	/**
	 * Status HTTP associado ao tipo de resposta
	 */
	private final HttpStatus status;

	/**
	 * Construtor da enumeração
	 * 
	 * @param message Mensagem descritiva da resposta
	 * @param status  Status HTTP correspondente
	 */
	ResponseEnum(String message, HttpStatus status) {
		this.message = message;
		this.status = status;
	}

}
