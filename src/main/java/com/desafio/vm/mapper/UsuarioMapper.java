package com.desafio.vm.mapper;

import java.util.List;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.desafio.vm.dto.UsuarioDTO;
import com.desafio.vm.entity.Usuario;

@Mapper(componentModel = "spring", uses = { VirtualMachineMapper.class })
public interface UsuarioMapper extends GenericMapper<Usuario, UsuarioDTO> {

	/**
	 * Converte uma entidade {@link Usuario} em um {@link UsuarioDTO}.
	 * 
	 * @param entity Entidade Usuario a ser convertida
	 * @return DTO correspondente à entidade
	 */
	@Override
	UsuarioDTO toDto(Usuario entity);

	/**
	 * Converte um {@link UsuarioDTO} em uma entidade {@link Usuario}.
	 * 
	 * @param dto DTO a ser convertido
	 * @return Entidade correspondente ao DTO
	 */
	@Override
	@Mapping(target = "maquinas", source = "maquinas")
	Usuario toEntity(UsuarioDTO dto);

	@AfterMapping
	default void linkMaquinas(@MappingTarget Usuario usuario) {
		if (usuario.getMaquinas() != null) {
			usuario.getMaquinas().forEach(maquina -> maquina.setUsuario(usuario));
		}
	}

	/**
	 * Converte uma lista de entidades {@link Usuario} em uma lista de
	 * {@link UsuarioDTO}.
	 * 
	 * @param entities Lista de entidades a serem convertidas
	 * @return Lista de DTOs correspondentes
	 */
	@Override
	List<UsuarioDTO> toListDto(List<Usuario> entities);

	/**
	 * Converte uma lista de {@link UsuarioDTO} em uma lista de entidades
	 * {@link Usuario}.
	 * 
	 * @param dtos Lista de DTOs a serem convertidos
	 * @return Lista de entidades correspondentes
	 */
	@Override
	List<Usuario> toListEntity(List<UsuarioDTO> dtos);

}
