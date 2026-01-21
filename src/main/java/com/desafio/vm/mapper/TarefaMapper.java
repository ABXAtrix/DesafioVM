package com.desafio.vm.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.desafio.vm.dto.TarefaDTO;
import com.desafio.vm.entity.Tarefa;

@Mapper(componentModel = "spring", uses = { UsuarioMapper.class, VirtualMachineMapper.class })
public interface TarefaMapper extends GenericMapper<Tarefa, TarefaDTO> {

	/**
	 * Converte uma entidade {@link Tarefa} em um {@link TarefaDTO}.
	 * 
	 * @param entity Entidade Tarefa a ser convertida
	 * @return DTO correspondente à entidade
	 */
	@Override
	TarefaDTO toDto(Tarefa entity);

	/**
	 * Converte um {@link TarefaDTO} em uma entidade {@link Tarefa}.
	 * 
	 * @param dto DTO a ser convertido
	 * @return Entidade correspondente ao DTO
	 */
	@Override
	Tarefa toEntity(TarefaDTO dto);

	/**
	 * Converte uma lista de entidades {@link Tarefa} em uma lista de
	 * {@link TarefaDTO}.
	 * 
	 * @param entities Lista de entidades a serem convertidas
	 * @return Lista de DTOs correspondentes
	 */
	@Override
	List<TarefaDTO> toListDto(List<Tarefa> entities);

	/**
	 * Converte uma lista de {@link TarefaDTO} em uma lista de entidades
	 * {@link Tarefa}.
	 * 
	 * @param dtos Lista de DTOs a serem convertidos
	 * @return Lista de entidades correspondentes
	 */
	@Override
	List<Tarefa> toListEntity(List<TarefaDTO> dtos);

}
