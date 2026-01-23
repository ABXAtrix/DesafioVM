package com.desafio.vm.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.desafio.vm.dto.VirtualMachineDTO;
import com.desafio.vm.entity.VirtualMachine;

@Mapper(componentModel = "spring")
public interface VirtualMachineMapper extends GenericMapper<VirtualMachine, VirtualMachineDTO> {

	/**
	 * Converte uma entidade {@link VirtualMachine} em um {@link VirtualMachineDTO}.
	 * 
	 * @param entity Entidade VirtualMachine a ser convertida
	 * @return DTO correspondente à entidade
	 */
	@Override
	@Mapping(source = "usuario.id", target = "usuarioId")
	VirtualMachineDTO toDto(VirtualMachine entity);

	/**
	 * Converte um {@link VirtualMachineDTO} em uma entidade {@link VirtualMachine}.
	 * 
	 * @param dto DTO a ser convertido
	 * @return Entidade correspondente ao DTO
	 */
	@Override
	@Mapping(source = "usuarioId", target = "usuario.id")
	@Mapping(target = "usuario.nome", ignore = true)
	@Mapping(target = "usuario.email", ignore = true)
	@Mapping(target = "usuario.senha", ignore = true)
	@Mapping(target = "usuario.maquinas", ignore = true)
	VirtualMachine toEntity(VirtualMachineDTO dto);

	/**
	 * Converte uma lista de entidades {@link VirtualMachine} em uma lista de
	 * {@link VirtualMachineDTO}.
	 * 
	 * @param entities Lista de entidades a serem convertidas
	 * @return Lista de DTOs correspondentes
	 */
	@Override
	List<VirtualMachineDTO> toListDto(List<VirtualMachine> entities);

	/**
	 * Converte uma lista de {@link VirtualMachineDTO} em uma lista de entidades
	 * {@link VirtualMachine}.
	 * 
	 * @param dtos Lista de DTOs a serem convertidos
	 * @return Lista de entidades correspondentes
	 */
	@Override
	List<VirtualMachine> toListEntity(List<VirtualMachineDTO> dtos);

}
