package com.algaworks.algafoodapi.domain.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FluxoPedidoService {
	
	@Autowired
	private PedidoService pedidoService;
	
	@Transactional
	public void confirmar(String codigoPedido) {
		var pedido = pedidoService.buscar(codigoPedido);
		
		pedido.confirmar();
	}
	
	@Transactional
	public void entregar(String codigoPedido) {
		var pedido = pedidoService.buscar(codigoPedido);
		
		pedido.entregar();
	}
	
	@Transactional
	public void cancelar(String codigoPedido) {
		var pedido = pedidoService.buscar(codigoPedido);
		
		pedido.cancelar();
	}
}
