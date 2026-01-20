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

	@Operation(summary = "Listar todas as máquinas do sistema")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "Lista completa obtida com sucesso") })
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

	@Operation(summary = "Listar todas as máquinas virtuais do usuário")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "Lista obtida com sucesso"),
			@ApiResponse(responseCode = "400", description = "Erro ao obter a lista") })
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

	@Operation(summary = "Filtrar máquinas virtuais")
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

	@Operation(summary = "Listar máquinas virtuais paginadas")
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

	@Operation(summary = "Buscar máquina virtual por ID")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "Registro encontrado"),
			@ApiResponse(responseCode = "404", description = "Registro não encontrado") })
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

	@Operation(summary = "Cadastrar nova máquina virtual")
	@PostMapping
	public ResponseEntity<DesafioVmApiResponse<VirtualMachineDTO>> create(@Valid @RequestBody VirtualMachineDTO dto) {
		try {
			VirtualMachineDTO created = mapper.toDto(service.save(mapper.toEntity(dto)));
			return ResponseBuilder.build(ResponseEnum.REGISTRO_CRIADO, created);
		} catch (Exception e) {
			return ResponseBuilder.handleException(e);
		}
	}

	@Operation(summary = "Atualizar dados da máquina virtual")
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

	@Operation(summary = "Excluir máquina virtual")
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