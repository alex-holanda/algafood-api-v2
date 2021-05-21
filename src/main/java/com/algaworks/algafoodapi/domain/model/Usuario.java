package com.algaworks.algafoodapi.domain.model;

import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "usuario")
public class Usuario {

	@EqualsAndHashCode.Include
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String nome;
	
	private String email;
	
	private String senha;
	
	@CreationTimestamp
	@Column(name = "data_cadastro")
	private OffsetDateTime dataCadastro;
	
//	@UpdateTimestamp
//	@Column(name = "data_atualizacao")
//	private OffsetDateTime dataAtualizacao;
	
	@ManyToMany
	@JoinTable(name = "usuario_grupo",
			joinColumns = @JoinColumn(name = "usuario_id"),
			inverseJoinColumns = @JoinColumn(name = "grupo_id"))
	private Set<Grupo> grupos = new HashSet<>();
	
	public boolean isNovo() {
		return getId() == null;
	}
	
	public boolean associar(Grupo grupo) {
		return getGrupos().add(grupo);
	}
	
	public boolean desassociar(Grupo grupo) {
		return getGrupos().remove(grupo);
	}
}
