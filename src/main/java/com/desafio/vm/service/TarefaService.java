package com.desafio.vm.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.desafio.vm.entity.Tarefa;
import com.desafio.vm.entity.Usuario;
import com.desafio.vm.entity.VirtualMachine;
import com.desafio.vm.repository.TarefaRepository;

import lombok.RequiredArgsConstructor;

/**
 * Serviço responsável pelo registro automático de log das tarefas executadas.
 */

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TarefaService {

	@Autowired
	private TarefaRepository repository;
	
	/**
     * Registra uma nova tarefa no histórico. 
     * O nome da máquina é salvo como snapshot para persistir mesmo após exclusão da VM.
     */
    @Transactional
    public void registrar(Usuario usuario, VirtualMachine vm, String acao) {
        Tarefa tarefa = Tarefa.builder()
                .usuario(usuario)
                .virtualMachine(vm)
                .nomeMaquina(vm.getNome()) // Captura o nome no momento da ação [cite: 56]
                .acao(acao) // Ex: "START", "STOP", "DELETE" [cite: 21]
                .dataHora(LocalDateTime.now()) // [cite: 55]
                .build();
        repository.save(tarefa);
    }

    /**
     * Retorna o histórico completo ordenado pela data mais recente.
     */
    public List<Tarefa> findAll() {
        return repository.findAll(Sort.by(Sort.Direction.DESC, "dataHora"));
    }
}
