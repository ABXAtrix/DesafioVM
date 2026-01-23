package com.desafio.vm.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.desafio.vm.entity.Tarefa;

@Repository
public interface TarefaRepository extends JpaRepository<Tarefa, Long>, JpaSpecificationExecutor<Tarefa> {

	List<Tarefa> findByUsuarioId(Long usuarioId, Sort sort);

	@Modifying
	@Query("UPDATE Tarefa t SET t.virtualMachine = null WHERE t.virtualMachine.id = :vmId")
	void desvincularVm(@Param("vmId") Long vmId);

}
