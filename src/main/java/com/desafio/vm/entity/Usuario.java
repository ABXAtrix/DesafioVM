package com.desafio.vm.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Entidade que representa um Usuário no sistema. Responsável por armazenar as
 * credenciais de acesso e gerenciar o vínculo com as máquinas virtuais para
 * controle do limite de capacidade.
 */

@Data
@Table(name = "USUARIO")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Usuario {

	/** Identificador único do usuário gerado automaticamente. */
	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/** Nome do usuário. Requisito para exibição em logs e tarefas. */
	@Column(name = "NOME")
	@NotBlank(message = "O nome é obrigatório")
	private String nome;

	/** E-mail para login. Validado conforme o formato padrão de e-mail. */
	@NotBlank(message = "O e-mail é obrigatório")
	@Email(message = "Formato de e-mail inválido")
	@Column(unique = true)
	private String email;

	/** Senha criptografada para autenticação no sistema. */
	@Column(name = "SENHA")
	@NotBlank(message = "A senha é obrigatória")
	private String senha;

	/** Se as VMs forem vinculadas ao usuário para o limite de 5 */
	@OneToMany(mappedBy = "usuario") /** Indica que um usuário pertence a várias VMs. Máximo de 5 */
	@ToString.Exclude
	@JsonIgnore
	private List<VirtualMachine> maquinas;

}
