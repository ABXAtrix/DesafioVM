package com.desafio.vm.service;

import static com.desafio.vm.config.security.SecurityUtil.getCurrentUserId;
import static com.desafio.vm.config.security.SecurityUtil.getUsuarioLogado;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.desafio.vm.entity.Tarefa;
import com.desafio.vm.entity.Usuario;
import com.desafio.vm.entity.VirtualMachine;
import com.desafio.vm.enums.Cargos;
import com.desafio.vm.repository.TarefaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TarefaService {

	@Autowired
	private TarefaRepository repository;

	/**
	 * Registra a tarefa. Usado internamente pelo VirtualMachineService.
	 */
	@Transactional
	public void registrar(Usuario usuario, VirtualMachine vm, String acao) {
		Tarefa.TarefaBuilder builder = Tarefa.builder().usuario(usuario).nomeMaquina(vm.getNome()).acao(acao)
				.dataHora(LocalDateTime.now());

		if (!"DELETE".equalsIgnoreCase(acao)) {
			builder.virtualMachine(vm);
		}

		repository.save(builder.build());
	}

	/**
	 * Busca as tarefas baseada na hierarquia do usuário logado.
	 */
	public List<Tarefa> listarTarefasPorPermissao() {
		Usuario logado = getUsuarioLogado();
		Sort sort = Sort.by(Sort.Direction.DESC, "dataHora");

		if (logado.getCargo() == Cargos.ADMIN) {
			return repository.findAll(sort);
		}

		/** Para usuários comuns, filtramos estritamente pelo ID dele */
		return repository.findByUsuarioId(getCurrentUserId(), sort);
	}

	@Transactional
	public void desvincularTarefasDaVm(Long vmId) {
		repository.desvincularVm(vmId);
	}
}