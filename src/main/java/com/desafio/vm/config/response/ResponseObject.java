package com.desafio.vm.config.response;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * Classe que representa um objeto básico de resposta utilizado no sistema
 * DesafioVm. Contém informações sobre o status/código da mensagem de retorno.
 * 
 * Esta classe é frequentemente utilizada como base para respostas simples da
 * API ou pode ser estendida para casos mais complexos.
 */
@Data
@Getter
@Setter
public class ResponseObject {

	/**
	 * Código ou identificador da mensagem de resposta. Geralmente utilizado para
	 * padronizar os tipos de retorno da API, podendo representar tanto mensagens de
	 * sucesso quanto de erro.
	 */
	private String codeMessage;

}