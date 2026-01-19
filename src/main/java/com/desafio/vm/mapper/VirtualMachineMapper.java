package com.desafio.vm.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.desafio.vm.dto.VirtualMachineDTO;
import com.desafio.vm.entity.VirtualMachine;

@Mapper(componentModel = "spring")
public interface VirtualMachineMapper extends GenericMapper<VirtualMachine, VirtualMachineDTO> {

	/**
	 * Converte uma entidade {@link VirtualMachine} em um {@link VirtualMachineDTO}.
	 * 
	 * @param entity Entidade MvEmbarque a ser convertida
	 * @return DTO correspondente à entidade
	 */
	@Override
	VirtualMachineDTO toDto(VirtualMachine entity);

	/**
	 * Converte um {@link VirtualMachineDTO} em uma entidade {@link VirtualMachine}.
	 * 
	 * @param dto DTO a ser convertido
	 * @return Entidade correspondente ao DTO
	 */
	@Override
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
