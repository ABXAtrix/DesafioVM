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
import com.desafio.vm.dto.VirtualMachineDTO;
import com.desafio.vm.mapper.VirtualMachineMapper;
import com.desafio.vm.service.VirtualMachineService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/vms")
@Tag(name = "VirtualMachine", description = "API para gerenciamento de Máquinas Virtuais")
public class VirtualMachineController {

	@Autowired
	private VirtualMachineService service;

	@Autowired
	private VirtualMachineMapper mapper;

	/**
	 * Lista todas as máquinas virtuais de todos os usuários do sistema.
	 * 
	 * @return Lista completa de VirtualMachineDTO
	 */

	@Operation(summary = "Listar todas as máquinas do sistema", description = "Retorna todas as VMs cadastradas sem filtro de usuário.")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "Lista completa obtida com sucesso"),
			@ApiResponse(responseCode = "403", description = "Acesso negado") })
	@GetMapping("/all")
	public ResponseEntity<DesafioVmApiResponse<List<VirtualMachineDTO>>> getAllSystemWide() {
		try {
			List<VirtualMachineDTO> lista = service.findAllAdmin().stream().map(mapper::toDto)
					.collect(Collectors.toList());
			return ResponseBuilder.build(ResponseEnum.LISTA_OBTIDA, lista);
		} catch (Exception e) {
			return ResponseBuilder.handleException(e);
		}
	}

	/**
	 * Recupera as máquinas virtuais pertencentes ao usuário autenticado. Essencial
	 * para o cálculo do limite de 5 máquinas por usuário.
	 * 
	 * @return Lista de VMs do usuário logado
	 */

	@Operation(summary = "Listar todas as máquinas virtuais do usuário", description = "Retorna apenas as máquinas pertencentes ao usuário autenticado via Token JWT.")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "Lista do usuário obtida com sucesso"),
			@ApiResponse(responseCode = "403", description = "Token inválido ou expirado") })
	@GetMapping
	public ResponseEntity<DesafioVmApiResponse<List<VirtualMachineDTO>>> getAll() {
		try {
			List<VirtualMachineDTO> lista = service.findAllFiltered(null).stream().map(mapper::toDto)
					.collect(Collectors.toList());
			return ResponseBuilder.build(ResponseEnum.LISTA_OBTIDA, lista);
		} catch (Exception e) {
			return ResponseBuilder.handleException(e);
		}
	}

	/**
	 * Filtra as máquinas virtuais com base em critérios dinâmicos.
	 * 
	 * @param filter DTO com campos de busca (ex: filtrar por status ou nome)
	 * @return Lista filtrada de VMs
	 */

	@Operation(summary = "Filtrar máquinas virtuais", description = "Busca máquinas que correspondam aos atributos enviados no corpo da requisição (ex: filtrar por nome ou status).")
	@ApiResponse(responseCode = "200", description = "Filtro aplicado com sucesso")
	@PostMapping("/filtro")
	public ResponseEntity<DesafioVmApiResponse<List<VirtualMachineDTO>>> getAllFiltered(
			@RequestBody VirtualMachineDTO filter) {
		try {
			List<VirtualMachineDTO> lista = service.findAllFiltered(mapper.toEntity(filter)).stream().map(mapper::toDto)
					.collect(Collectors.toList());
			return ResponseBuilder.build(ResponseEnum.LISTA_OBTIDA, lista);
		} catch (Exception e) {
			return ResponseBuilder.handleException(e);
		}
	}

	/**
	 * Lista as máquinas virtuais de forma paginada.
	 * 
	 * @param pageable Configuração de página e tamanho
	 * @param filter   Critérios de filtro para a busca paginada
	 * @return Página de VirtualMachineDTO
	 */

	@Operation(summary = "Listar máquinas virtuais paginadas", description = "Retorna os resultados divididos em páginas. Requer parâmetros de paginação e filtros opcionais.")
	@PostMapping("/paginas")
	public ResponseEntity<DesafioVmApiResponse<Page<VirtualMachineDTO>>> getAllPaginated(Pageable pageable,
			@RequestBody VirtualMachineDTO filter) {
		try {
			Page<VirtualMachineDTO> pagina = service.findAllPaginated(pageable, mapper.toEntity(filter))
					.map(mapper::toDto);
			return ResponseBuilder.build(ResponseEnum.LISTA_OBTIDA, pagina);
		} catch (Exception e) {
			return ResponseBuilder.handleException(e);
		}
	}

	/**
	 * Busca os detalhes de uma máquina virtual específica pelo ID.
	 * 
	 * @param id Identificador único da VM
	 * @return Detalhes da VM solicitada
	 */

	@Operation(summary = "Buscar máquina virtual por ID", description = "Retorna os detalhes técnicos de uma única VM.")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "Registro encontrado"),
			@ApiResponse(responseCode = "404", description = "Máquina não encontrada no banco de dados") })
	@GetMapping("/{id}")
	public ResponseEntity<DesafioVmApiResponse<VirtualMachineDTO>> getById(@PathVariable Long id) {
		try {
			return service.findById(id).map(mapper::toDto)
					.map(dto -> ResponseBuilder.build(ResponseEnum.REGISTRO_ENCONTRADO, dto)).orElseGet(
							() -> ResponseBuilder.build(ResponseEnum.REGISTRO_NAO_ENCONTRADO, new VirtualMachineDTO()));
		} catch (Exception e) {
			return ResponseBuilder.handleException(e);
		}
	}

	/**
	 * Cria uma nova máquina virtual. O Service deve validar o limite máximo de 5
	 * máquinas antes da persistência.
	 * 
	 * @param dto Dados da nova VM
	 * @return VM criada com dados e registrados gerados
	 */

	@Operation(summary = "Cadastrar nova máquina virtual", description = "Cria uma nova VM. Valida se o usuário já atingiu o limite máximo de 5 máquinas virtuais.")
	@ApiResponses({ @ApiResponse(responseCode = "201", description = "VM criada com sucesso"),
			@ApiResponse(responseCode = "400", description = "Limite de 5 máquinas atingido ou dados inválidos") })
	@PostMapping
	public ResponseEntity<DesafioVmApiResponse<VirtualMachineDTO>> create(@Valid @RequestBody VirtualMachineDTO dto) {
		try {
			VirtualMachineDTO created = mapper.toDto(service.save(mapper.toEntity(dto)));
			return ResponseBuilder.build(ResponseEnum.REGISTRO_CRIADO, created);
		} catch (Exception e) {
			return ResponseBuilder.handleException(e);
		}
	}

	/**
	 * Atualiza os dados de uma VM existente. ID e Data de Criação são imutáveis.
	 * 
	 * @param ID  da VM que será editada
	 * @param dto Novos dados (CPU, Memória, Disco, Nome)
	 * @return VM atualizada
	 */

	@Operation(summary = "Atualizar dados da máquina virtual", description = "Permite editar CPU, Memória, Disco e Nome. O ID e a Data de Criação permanecem inalterados.")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "Dados atualizados com sucesso"),
			@ApiResponse(responseCode = "404", description = "VM não encontrada para atualização") })
	@PutMapping("/{id}")
	public ResponseEntity<DesafioVmApiResponse<VirtualMachineDTO>> update(@PathVariable Long id,
			@Valid @RequestBody VirtualMachineDTO dto) {
		try {
			VirtualMachineDTO updated = mapper.toDto(service.update(id, mapper.toEntity(dto)));
			return ResponseBuilder.build(ResponseEnum.REGISTRO_ATUALIZADO, updated);
		} catch (Exception e) {
			return ResponseBuilder.handleException(e);
		}
	}

	/**
	 * Remove uma máquina virtual do sistema.
	 * 
	 * @param ID da VM para exclusão
	 * @return Resposta de sucesso sem corpo
	 */

	@Operation(summary = "Excluir máquina virtual", description = "Remove a VM do banco de dados através do seu identificador.")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "VM excluída com sucesso"),
			@ApiResponse(responseCode = "404", description = "VM não encontrada") })
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