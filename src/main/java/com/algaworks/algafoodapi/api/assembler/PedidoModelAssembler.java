package com.algaworks.algafoodapi.api.assembler;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.algaworks.algafoodapi.api.model.PedidoModel;
import com.algaworks.algafoodapi.api.model.PedidoResumoModel;
import com.algaworks.algafoodapi.domain.model.Pedido;

@Component
public class PedidoModelAssembler {

	@Autowired
	private ModelMapper mapper;
	
	public PedidoResumoModel toResumoModel(Pedido pedido) {
		return mapper.map(pedido, PedidoResumoModel.class);
	}
	
	public PedidoModel toModel(Pedido pedido) {
		return mapper.map(pedido, PedidoModel.class);
	}
	
	public List<PedidoResumoModel> toCollectionModel(List<Pedido> pedidos) {
		return pedidos.stream().map(pedido -> toResumoModel(pedido)).collect(Collectors.toList());
	}
}
