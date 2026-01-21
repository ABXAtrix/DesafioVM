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

	@Operation(summary = "Listar todos os usuários")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "Lista obtida com sucesso"),
			@ApiResponse(responseCode = "400", description = "Erro ao obter a lista") })
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

	@Operation(summary = "Filtrar usuários por critérios")
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

	@Operation(summary = "Listar usuários com paginação e filtro")
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

	@Operation(summary = "Buscar usuário por ID")
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

	@Operation(summary = "Atualizar meu próprio perfil")
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

	@Operation(summary = "Excluir um usuário do sistema")
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