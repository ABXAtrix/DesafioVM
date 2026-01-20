package com.desafio.vm.entity;

import java.util.List;

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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Data
@Table(name = "USUARIO")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Usuario {

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "NOME")
	@NotBlank(message = "O nome é obrigatório")
	private String nome;

	@NotBlank(message = "O e-mail é obrigatório")
	@Email(message = "Formato de e-mail inválido")
	@Column(unique = true)
	private String email;

	@Column(name = "SENHA")
	@NotBlank(message = "A senha é obrigatória")
	private String senha;

	// Se as VMs forem vinculadas ao usuário para o limite de 5:
	@OneToMany(mappedBy = "usuario")
	private List<VirtualMachine> maquinas;

}
