package com.desafio.vm.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.desafio.vm.config.response.DesafioVmApiResponse;
import com.desafio.vm.config.response.ResponseBuilder;
import com.desafio.vm.config.response.ResponseEnum;
import com.desafio.vm.entity.Tarefa;
import com.desafio.vm.service.TarefaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/tarefas")
@Tag(name = "Tarefas", description = "API para gestão de tarefas no sistema")
public class TarefaController {

	@Autowired
	private TarefaService service;

	/**
	 * Recupera o histórico de atividades. A lógica de permissão * 
	 * @return Lista de Tarefas (logs)
	 */
	@Operation(summary = "Listar histórico de atividades", description = "Retorna os logs de ações realizadas em VMs. Admins visualizam tudo, usuários comuns apenas suas ações.")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "Histórico obtido com sucesso"),
			@ApiResponse(responseCode = "403", description = "Acesso negado ou Token inválido") })
	@GetMapping
	public ResponseEntity<DesafioVmApiResponse<List<Tarefa>>> getAllLogs() {
		try {
			// O service já retorna a lista ordenada e filtrada por permissão
			List<Tarefa> lista = service.listarTarefasPorPermissao();
			return ResponseBuilder.build(ResponseEnum.LISTA_OBTIDA, lista);
		} catch (Exception e) {
			return ResponseBuilder.handleException(e);
		}
	}
}