package com.desafio.vm.config.response;

import org.springframework.http.ResponseEntity;

import com.desafio.vm.exceptions.AplicacaoException;

/**
 * Classe utilitária para construção padronizada de respostas da API WMS.
 * 
 * <p>
 * Fornece métodos estáticos para criar objetos ResponseEntity consistentes
 * utilizando a estrutura WmsApiResponse e os enumeradores de ResponseEnum.
 * </p>
 * 
 * <p>
 * Esta classe centraliza a lógica de construção de respostas, incluindo
 * tratamento de sucesso, erros e exceções de forma consistente em toda a
 * aplicação.
 * </p>
 */
public class ResponseBuilder {

	/**
	 * Constrói uma resposta com dados utilizando um ResponseEnum padrão.
	 * 
	 * @param <T>          Tipo genérico dos dados de resposta
	 * @param responseEnum Enum que define o status e mensagem padrão
	 * @param data         Dados a serem incluídos na resposta
	 * @return ResponseEntity contendo a estrutura padronizada de resposta
	 */
	public static <T> ResponseEntity<DesafioVmApiResponse<T>> build(ResponseEnum responseEnum, T data) {
		DesafioVmApiResponse<T> response = new DesafioVmApiResponse<>(responseEnum, data);
		return ResponseEntity.status(responseEnum.getStatus()).body(response);
	}

	/**
	 * Constrói uma resposta sem dados (apenas status e mensagem).
	 * 
	 * @param responseEnum Enum que define o status e mensagem padrão
	 * @return ResponseEntity sem corpo de dados
	 */
	public static ResponseEntity<DesafioVmApiResponse<Void>> build(ResponseEnum responseEnum) {
		DesafioVmApiResponse<Void> response = new DesafioVmApiResponse<>(responseEnum, null);
		return ResponseEntity.status(responseEnum.getStatus()).body(response);
	}

	/**
	 * Constrói uma resposta com dados e mensagem customizada.
	 * 
	 * @param <T>           Tipo genérico dos dados de resposta
	 * @param responseEnum  Enum que define o status base
	 * @param data          Dados a serem incluídos na resposta
	 * @param customMessage Mensagem personalizada (usa a padrão do enum se null)
	 * @return ResponseEntity com mensagem customizada
	 */
	public static <T> ResponseEntity<DesafioVmApiResponse<T>> build(ResponseEnum responseEnum, T data,
			String customMessage) {
		DesafioVmApiResponse<T> response = new DesafioVmApiResponse<>(responseEnum.getStatus().value(),
				customMessage != null ? customMessage : responseEnum.getMessage(), data);
		return ResponseEntity.status(responseEnum.getStatus()).body(response);
	}

	/**
	 * Constrói uma resposta de erro com mensagem customizada sem dados.
	 * 
	 * @param responseEnum  Enum que define o status base (geralmente um erro)
	 * @param customMessage Mensagem personalizada (usa a padrão do enum se null)
	 * @return ResponseEntity de erro sem corpo de dados
	 */
	public static ResponseEntity<DesafioVmApiResponse<Void>> build(ResponseEnum responseEnum, String customMessage) {
		DesafioVmApiResponse<Void> response = new DesafioVmApiResponse<>(responseEnum.getStatus().value(),
				customMessage != null ? customMessage : responseEnum.getMessage(), null);
		return ResponseEntity.status(responseEnum.getStatus()).body(response);
	}

	/**
	 * Tratamento padronizado de exceções com tipo genérico.
	 * 
	 * @param <T> Tipo do dado da resposta (pode ser Void para sem dados)
	 * @param e   Exceção ocorrida
	 * @return ResponseEntity com estrutura de erro padronizada
	 */
	public static <T> ResponseEntity<DesafioVmApiResponse<T>> handleException(Exception e) {
		if (e instanceof AplicacaoException) {
			return build(ResponseEnum.ERROR, null, ((AplicacaoException) e).getMensagem());
		}
		return build(ResponseEnum.ERROR, null, "Erro interno no processamento da requisição");
	}

	/**
	 * Versão simplificada do tratamento de exceções sem tipo genérico.
	 * 
	 * @param e Exceção ocorrida
	 * @return ResponseEntity com estrutura de erro padronizada sem dados
	 */
	public static ResponseEntity<DesafioVmApiResponse<Void>> handleExceptionSimple(Exception e) {
		if (e instanceof AplicacaoException) {
			return build(ResponseEnum.ERROR, ((AplicacaoException) e).getMensagem());
		}
		return build(ResponseEnum.ERROR, "Erro interno no processamento da requisição");
	}
}
