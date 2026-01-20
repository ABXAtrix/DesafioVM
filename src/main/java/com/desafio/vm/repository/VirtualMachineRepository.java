package com.desafio.vm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.desafio.vm.entity.VirtualMachine;

@Repository
public interface VirtualMachineRepository
		extends JpaRepository<VirtualMachine, Long>, JpaSpecificationExecutor<VirtualMachine> {

}
