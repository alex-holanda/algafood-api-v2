package com.algaworks.algafoodapi.api.model.input;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PermissaoInput {

	@NotBlank
	private String nome;
	
	@NotBlank
	private String descricao;

	public void setNome(String nome) {
		this.nome = nome.replaceAll(" ", "_").toUpperCase();
	}
	
}
