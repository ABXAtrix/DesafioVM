package com.desafio.vm.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.desafio.vm.entity.Usuario;
import com.desafio.vm.exceptions.AplicacaoException;
import com.desafio.vm.repository.UsuarioRepository;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * Serviço para operações relacionadas ao Usuario. Contém lógica de negócio para
 * CRUD e filtros dinâmicos.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UsuarioService {

	@Autowired
	private UsuarioRepository repository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	/**
	 * Valida se o objeto usuário é nulo.
	 */
	private void validateUsuario(Usuario obj) {
		if (obj == null) {
			throw new AplicacaoException("O objeto Usuário não pode ser nulo.");
		}
	}

	/**
	 * Busca usuários com filtros dinâmicos usando Specifications. Filtra por nome e
	 * e-mail (case-insensitive).
	 */
	public List<Usuario> findAllFiltered(Usuario filter) {
		Specification<Usuario> spec = createSpecification(filter);
		return repository.findAll(spec);
	}

	/**
	 * Retorna uma página de usuários filtrados.
	 */
	public Page<Usuario> findAllPaginated(Pageable pageable, Usuario filter) {
		Specification<Usuario> spec = createSpecification(filter);
		return repository.findAll(spec, pageable);
	}

	/**
	 * Cria a lógica de Specification baseada nos campos da sua entidade.
	 */
	private Specification<Usuario> createSpecification(Usuario filter) {
		return (root, query, cb) -> {
			if (filter == null)
				return null;

			var predicates = new java.util.ArrayList<jakarta.persistence.criteria.Predicate>();

			if (filter.getNome() != null && !filter.getNome().isEmpty()) {
				predicates.add(cb.like(cb.lower(root.get("nome")), "%" + filter.getNome().toLowerCase() + "%"));
			}
			if (filter.getEmail() != null && !filter.getEmail().isEmpty()) {
				predicates.add(cb.like(cb.lower(root.get("email")), "%" + filter.getEmail().toLowerCase() + "%"));
			}

			return cb.and(predicates.toArray(new jakarta.persistence.criteria.Predicate[0]));
		};
	}

	public List<Usuario> findAll() {
		return repository.findAll();
	}

	public Optional<Usuario> findById(Long id) {
		return repository.findById(id);
	}

	/**
	 * Salva um novo usuário com senha criptografada.
	 */
	@Transactional
	public Usuario save(@Valid Usuario obj) {
		validateUsuario(obj);

		if (repository.findByEmail(obj.getEmail()).isPresent()) {
			throw new AplicacaoException("Já existe um usuário cadastrado com este e-mail.");
		}

		// Criptografa a senha antes de persistir
		obj.setSenha(passwordEncoder.encode(obj.getSenha()));

		return repository.save(obj);
	}

	@Transactional
	public void delete(Long id) {
		if (!repository.existsById(id)) {
			throw new AplicacaoException("Usuário não encontrado para exclusão.");
		}
		repository.deleteById(id);
	}

	/**
	 * Atualiza os dados do usuário. Se uma nova senha for fornecida, ela é
	 * criptografada.
	 */
	@Transactional
	public Usuario update(Long id, @Valid Usuario updated) {
		return repository.findById(id).map(existing -> {
			existing.setNome(updated.getNome());
			existing.setEmail(updated.getEmail());

			// Verifica se a senha foi alterada para re-criptografar
			if (updated.getSenha() != null && !updated.getSenha().trim().isEmpty()) {
				existing.setSenha(passwordEncoder.encode(updated.getSenha()));
			}

			return repository.save(existing);
		}).orElseThrow(() -> new AplicacaoException("Usuário não encontrado com id: " + id));
	}
}