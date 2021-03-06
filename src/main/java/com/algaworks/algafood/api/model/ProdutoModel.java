package com.algaworks.algafood.api.model;

import java.math.BigDecimal;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Relation(collectionRelation = "produtos")
@Getter
@Setter
public class ProdutoModel extends RepresentationModel<ProdutoModel> {

	@Schema(description = "ID do produto", example = "1")
	private Long id;
	
	@Schema(description = "Nome do produto", example = "Porco com molho agridoce")
	private String nome;

	@Schema(description = "Descrição do produto", example = "Deliciosa carne suína ao molho especial")
	private String descricao;

	@Schema(description = "Preço do produto", example = "1")
	private BigDecimal preco;

	@Schema(description = "Se o produto está disponível", example = "true")
	private Boolean ativo;
}
