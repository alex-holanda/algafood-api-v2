package com.algaworks.algafoodapi.domain.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Data
@Entity
@Table(name = "item_pedido")
public class ItemPedido {

	@EqualsAndHashCode.Include
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private Integer quantidade;
	
	@Column(name = "preco_unitario")
	private BigDecimal precoUnitario;
	
	@Column(name = "preco_total")
	private BigDecimal precoTotal;
	
	private String observacao;
	
	@ManyToOne
	@JoinColumn(name = "produto_id")
	private Produto produto;
	
	@ManyToOne
	@JoinColumn(name = "pedido_id")
	private Pedido pedido;
	
	public void calcularPrecoTotal() {
		var precoUnitario = getPrecoUnitario();
		var quantidade = getQuantidade();
		
		if (precoUnitario == null) {
			precoUnitario = BigDecimal.ZERO;
		}
		
		if (quantidade == null) {
			quantidade = 0;
		}
		
		setPrecoTotal(precoUnitario.multiply(new BigDecimal(quantidade)));
	}
}
