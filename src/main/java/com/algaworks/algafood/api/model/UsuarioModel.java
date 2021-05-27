package com.algaworks.algafood.api.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsuarioModel {

	@Schema(description = "ID do usuário", example = "1")
	private Long id;
	
	@Schema(description = "Nome do usuário", example = "Thai Gourmet")
	private String nome;
	
	@Schema(description = "E-mail do usuário", example = "thai.gourmet@gmail.com")
	private String email;
}