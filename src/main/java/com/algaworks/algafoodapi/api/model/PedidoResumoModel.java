package com.algaworks.algafoodapi.api.model;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PedidoResumoModel {

	private String codigo;

	private BigDecimal subtotal;

	private BigDecimal taxaFrete;

	private BigDecimal valorTotal;

	private String status;

	private OffsetDateTime dataCriacao;

	private UsuarioModel cliente;
	
	private RestauranteResumoModel restaurante;
}
