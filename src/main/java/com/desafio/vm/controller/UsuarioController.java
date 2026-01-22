package com.desafio.vm.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.desafio.vm.config.response.DesafioVmApiResponse;
import com.desafio.vm.config.response.ResponseBuilder;
import com.desafio.vm.config.response.ResponseEnum;
import com.desafio.vm.config.security.SecurityUtil;
import com.desafio.vm.dto.UsuarioDTO;
import com.desafio.vm.mapper.UsuarioMapper;
import com.desafio.vm.service.UsuarioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/usuarios")
@Tag(name = "Usuário", description = "API para gestão de usuários do sistema")
public class UsuarioController {

	@Autowired
	private UsuarioService service;

	@Autowired
	private UsuarioMapper mapper;

	/**
	 * Recupera a lista completa de usuários cadastrados no sistema. * @return
	 * ResponseEntity contendo a resposta com a lista de UsuarioDTO
	 */

	@Operation(summary = "Listar todos os usuários", description = "Retorna a lista completa de usuários cadastrados. Requer autenticação.")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "Lista obtida com sucesso"),
			@ApiResponse(responseCode = "403", description = "Acesso negado - Token inválido") })
	@GetMapping
	public ResponseEntity<DesafioVmApiResponse<List<UsuarioDTO>>> getAll() {
		try {
			List<UsuarioDTO> lista = service.findAll().stream().map(mapper::toDto).collect(Collectors.toList());
			return ResponseBuilder.build(ResponseEnum.LISTA_OBTIDA, lista);
		} catch (Exception e) {
			return ResponseBuilder.handleException(e);
		}
	}

	/**
	 * Realiza uma busca personalizada de usuários baseada em filtros dinâmicos.
	 * * @param filter DTO contendo os campos para filtragem
	 * 
	 * @return Lista de usuários que atendem aos critérios informados
	 */

	@Operation(summary = "Filtrar usuários por critérios", description = "Busca usuários com base em atributos como nome ou e-mail enviados no corpo da requisição.")
	@ApiResponse(responseCode = "200", description = "Busca realizada com sucesso")
	@PostMapping("/filtro")
	public ResponseEntity<DesafioVmApiResponse<List<UsuarioDTO>>> getAllFiltered(@RequestBody UsuarioDTO filter) {
		try {
			List<UsuarioDTO> lista = service.findAllFiltered(mapper.toEntity(filter)).stream().map(mapper::toDto)
					.collect(Collectors.toList());
			return ResponseBuilder.build(ResponseEnum.LISTA_OBTIDA, lista);
		} catch (Exception e) {
			return ResponseBuilder.handleException(e);
		}
	}

	/**
	 * Lista usuários utilizando paginação e filtros dinâmicos.
	 * 
	 * @param filter DTO contendo os critérios de busca
	 * @return Página de usuários filtrada
	 */

	@Operation(summary = "Listar usuários com paginação e filtro", description = "Retorna uma página de usuários. Permite controlar o tamanho da página e a ordenação.")
	@ApiResponse(responseCode = "200", description = "Página retornada com sucesso")
	@PostMapping("/paginas")
	public ResponseEntity<DesafioVmApiResponse<Page<UsuarioDTO>>> getAllPaginated(Pageable pageable,
			@RequestBody UsuarioDTO filter) {
		try {
			Page<UsuarioDTO> pagina = service.findAllPaginated(pageable, mapper.toEntity(filter)).map(mapper::toDto);
			return ResponseBuilder.build(ResponseEnum.LISTA_OBTIDA, pagina);
		} catch (Exception e) {
			return ResponseBuilder.handleException(e);
		}
	}

	/**
	 * Busca um usuário específico através de seu identificador único. * @param id
	 * Identificador do usuário
	 * 
	 * @return Detalhes do usuário ou resposta de registro não encontrado
	 */

	@Operation(summary = "Buscar usuário por ID", description = "Recupera os detalhes de um usuário específico através do seu identificador numérico.")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "Usuário encontrado"),
			@ApiResponse(responseCode = "404", description = "Usuário não localizado") })
	@GetMapping("/{id}")
	public ResponseEntity<DesafioVmApiResponse<UsuarioDTO>> getById(@PathVariable Long id) {
		try {
			return service.findById(id).map(mapper::toDto)
					.map(dto -> ResponseBuilder.build(ResponseEnum.REGISTRO_ENCONTRADO, dto))
					.orElseGet(() -> ResponseBuilder.build(ResponseEnum.REGISTRO_NAO_ENCONTRADO, new UsuarioDTO()));
		} catch (Exception e) {
			return ResponseBuilder.handleException(e);
		}
	}

	/**
	 * Permite que o usuário autenticado atualize seus próprios dados cadastrais. O
	 * ID é recuperado automaticamente do contexto de segurança. * @param dto Dados
	 * atualizados do usuário
	 * 
	 * @return UsuarioDTO com as informações atualizadas
	 */

	@Operation(summary = "Atualizar meu próprio perfil", description = "Atualiza os dados do usuário que está logado. "
			+ "O sistema identifica o usuário automaticamente pelo Token JWT. Nome deve ter no mínimo 3 caracteres e senha deve ter no mínimo 6 caracteres .")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "Perfil atualizado com sucesso"),
			@ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos") })
	@PutMapping("/me")
	public ResponseEntity<DesafioVmApiResponse<UsuarioDTO>> updateSelf(@Valid @RequestBody UsuarioDTO dto) {
		try {
			Long id = SecurityUtil.getCurrentUserId();
			UsuarioDTO updated = mapper.toDto(service.update(id, mapper.toEntity(dto)));
			return ResponseBuilder.build(ResponseEnum.REGISTRO_ATUALIZADO, updated);
		} catch (Exception e) {
			return ResponseBuilder.handleException(e);
		}
	}

	/**
	 * Remove um usuário do sistema. * @param id Identificador do usuário a ser
	 * excluído
	 * 
	 * @return Resposta vazia com status de sucesso
	 */

	@Operation(summary = "Excluir um usuário do sistema", description = "Remove um usuário e seus vínculos do banco de dados.")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "Usuário removido com sucesso"),
			@ApiResponse(responseCode = "404", description = "Usuário não encontrado para exclusão") })
	@DeleteMapping("/{id}")
	public ResponseEntity<DesafioVmApiResponse<Void>> delete(@PathVariable Long id) {
		try {
			service.delete(id);
			return ResponseBuilder.build(ResponseEnum.REGISTRO_EXCLUIDO);
		} catch (Exception e) {
			return ResponseBuilder.handleException(e);
		}
	}
}