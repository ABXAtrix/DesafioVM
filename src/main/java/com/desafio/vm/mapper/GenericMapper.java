package com.desafio.vm.mapper;

import java.util.List;

/**
 * Interface genérica para mappers que convertem entre entidades e DTOs.
 * 
 * @param <E> Tipo da entidade
 * @param <D> Tipo do DTO
 */
public interface GenericMapper<E, D> {

	/**
	 * Converte uma entidade para DTO
	 * 
	 * @param entity A entidade a ser convertida
	 * @return O DTO correspondente
	 */
	D toDto(E entity);

	/**
	 * Converte um DTO para entidade
	 * 
	 * @param dto O DTO a ser convertido
	 * @return A entidade correspondente
	 */
	E toEntity(D dto);

	/**
	 * Converte uma lista de entidades para lista de DTOs
	 * 
	 * @param entities Lista de entidades
	 * @return Lista de DTOs correspondentes
	 */
	List<D> toListDto(List<E> entities);

	/**
	 * Converte uma lista de DTOs para lista de entidades
	 * 
	 * @param dtos Lista de DTOs
	 * @return Lista de entidades correspondentes
	 */
	List<E> toListEntity(List<D> dtos);

}
