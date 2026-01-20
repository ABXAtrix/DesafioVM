package com.desafio.vm.service;

import static com.desafio.vm.config.security.SecurityUtil.getCurrentUserId;
import static com.desafio.vm.config.security.SecurityUtil.getUsuarioLogado;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.desafio.vm.entity.VirtualMachine;
import com.desafio.vm.exceptions.AplicacaoException;
import com.desafio.vm.repository.VirtualMachineRepository;

import lombok.RequiredArgsConstructor;

/**
 * Serviço para gerenciar Máquinas Virtuais, garantindo que todas as operações
 * sejam realizadas no contexto do usuário logado.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VirtualMachineService {

	@Autowired
	private VirtualMachineRepository repository;

	/**
	 * Valida se a VM possui campos obrigatórios.
	 */
	private void validateVM(VirtualMachine obj) {
		if (obj == null)
			throw new AplicacaoException("Objeto Máquina Virtual não pode ser nulo");

		if (obj.getNome() == null || obj.getNome().trim().isEmpty())
			throw new AplicacaoException("Nome da VM não pode ser nulo ou vazio");

		if (obj.getCpu() == null || obj.getCpu() <= 0)
			throw new AplicacaoException("Quantidade de CPU deve ser maior que zero");

		if (obj.getMemoria() == null || obj.getMemoria().compareTo(java.math.BigDecimal.ZERO) <= 0)
			throw new AplicacaoException("Quantidade de Memória deve ser maior que zero");
	}
	
	/**
	 * Busca todas as VMs de todos os usuários.
	 */
	public List<VirtualMachine> findAllAdmin() {
	    return repository.findAll();
	}

	/**
	 * Busca todas as VMs do usuário logado com suporte a filtros dinâmicos.
	 */
	public List<VirtualMachine> findAllFiltered(VirtualMachine filter) {
		Specification<VirtualMachine> spec = Specification.where(null);

		// Restrição fundamental: Apenas dados do usuário logado
		spec = spec.and((root, query, cb) -> cb.equal(root.get("usuario").get("id"), getCurrentUserId()));

		if (filter != null) {
			if (filter.getNome() != null && !filter.getNome().trim().isEmpty())
				spec = spec.and((root, query, cb) -> cb.like(cb.lower(root.get("nome")),
						"%" + filter.getNome().toLowerCase() + "%"));

			if (filter.getStatus() != null)
				spec = spec.and((root, query, cb) -> cb.equal(root.get("status"), filter.getStatus()));
		}

		return repository.findAll(spec);
	}

	/**
	 * Busca VMs paginadas para o usuário logado.
	 */
	public Page<VirtualMachine> findAllPaginated(Pageable pageable, VirtualMachine filter) {
		Specification<VirtualMachine> spec = Specification.where(null);

		spec = spec.and((root, query, cb) -> cb.equal(root.get("usuario").get("id"), getCurrentUserId()));

		if (filter != null && filter.getNome() != null && !filter.getNome().trim().isEmpty()) {
			spec = spec.and((root, query, cb) -> cb.like(cb.lower(root.get("nome")),
					"%" + filter.getNome().toLowerCase() + "%"));
		}

		return repository.findAll(spec, pageable);
	}

	/**
	 * Busca VM pelo ID, validando propriedade do usuário.
	 */
	public Optional<VirtualMachine> findById(Long id) {
		return repository.findById(id)
				.filter(vm -> vm.getUsuario() != null && vm.getUsuario().getId().equals(getCurrentUserId()));
	}

	/**
	 * Salva uma nova VM associando automaticamente ao usuário logado.
	 */
	/**
	 * Salva uma nova VM associando automaticamente ao usuário logado. Limita a
	 * criação a no máximo 5 máquinas por usuário.
	 */
	@Transactional
	public VirtualMachine save(VirtualMachine obj) {
		validateVM(obj);

		// Regra de Negócio: Limite de 5 VMs
		long totalVmsDoUsuario = this.count();
		if (totalVmsDoUsuario >= 5) {
			throw new AplicacaoException("Limite atingido: Cada usuário pode possuir no máximo 5 máquinas virtuais.");
		}

		obj.setUsuario(getUsuarioLogado());

		if (obj.getId() != null && obj.getId() == 0) {
			obj.setId(null);
		}

		if (obj.getDataCriacao() == null) {
			obj.setDataCriacao(LocalDateTime.now());
		}

		return repository.save(obj);
	}

	/**
	 * Atualiza uma VM existente, garantindo que pertença ao usuário logado.
	 */
	@Transactional
	public VirtualMachine update(Long id, VirtualMachine updated) {
		return repository.findById(id).map(existing -> {
			// 1. Validação de Propriedade
			if (existing.getUsuario() == null || !existing.getUsuario().getId().equals(getCurrentUserId()))
				throw new AplicacaoException("Acesso negado: esta máquina pertence a outro usuário.");

			// 2. Bloqueio de alteração de ID (Se o ID enviado no corpo for diferente do ID
			// da URL)
			if (updated.getId() != null && !updated.getId().equals(id)) {
				throw new AplicacaoException("Não é permitido alterar o ID de um registro existente.");
			}

			// 3. Bloqueio de alteração da Data de Criação
			if (updated.getDataCriacao() != null && !updated.getDataCriacao().equals(existing.getDataCriacao())) {
				throw new AplicacaoException("A data de criação não pode ser alterada.");
			}

			validateVM(updated);

			// Atualiza apenas o que é permitido
			existing.setNome(updated.getNome());
			existing.setCpu(updated.getCpu());
			existing.setMemoria(updated.getMemoria());
			existing.setDisco(updated.getDisco());
			existing.setStatus(updated.getStatus());

			return repository.save(existing);
		}).orElseThrow(() -> new AplicacaoException("VM não encontrada com id: " + id));
	}

	/**
	 * Exclui a VM apenas se pertencer ao usuário logado.
	 */
	@Transactional
	public void delete(Long id) {
		VirtualMachine vm = repository.findById(id)
				.orElseThrow(() -> new AplicacaoException("Máquina não encontrada com id: " + id));

		if (vm.getUsuario() == null || !vm.getUsuario().getId().equals(getCurrentUserId()))
			throw new AplicacaoException("Acesso negado para exclusão.");

		repository.deleteById(id);
	}

	/**
	 * Conta o total de máquinas do usuário.
	 */
	public long count() {
		return repository.count((root, query, cb) -> cb.equal(root.get("usuario").get("id"), getCurrentUserId()));
	}
}