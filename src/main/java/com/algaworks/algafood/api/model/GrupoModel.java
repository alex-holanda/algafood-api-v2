package com.algaworks.algafood.api.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GrupoModel {

	@Schema(description = "ID do grupo", example = "1")
	private Long id;
	
	@Schema(description = "Nome do grupo", example = "Vendedor")
	private String nome;
}
