package com.desafio.vm.exceptions;


/**
 * Exceção personalizada para erros de aplicação no sistema DesafioVm.
 * 
 * <p>
 * Esta classe representa erros específicos da lógica de negócios da aplicação,
 * sendo utilizada para tratar situações excepcionais de forma controlada.
 * </p>
 * 
 * <p>
 * Características principais:
 * <ul>
 * <li>Herda de RuntimeException (não checada)</li>
 * <li>Permite encadeamento de exceções</li>
 * <li>Fornece mensagens claras para tratamento de erros</li>
 * <li>Método adicional para obtenção da mensagem</li>
 * </ul>
 * </p>
 */
public class AplicacaoException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * Cria uma nova exceção com mensagem específica.
	 * 
	 * @param message Mensagem descritiva do erro
	 */
	public AplicacaoException(String message) {
		super(message);
	}

	/**
	 * Cria uma nova exceção com mensagem e causa específicas.
	 * 
	 * @param message Mensagem descritiva do erro
	 * @param cause   Exceção original que causou o erro
	 */
	public AplicacaoException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Retorna a mensagem de erro associada à exceção.
	 * 
	 * @return Mensagem descritiva do erro
	 */
	public String getMensagem() {
		return getMessage();
	}

}
