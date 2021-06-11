package com.algaworks.algafood.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.api.AlgaLinks;
import com.algaworks.algafood.api.model.RootEntryPointModel;
import com.algaworks.algafood.core.security.AlgaSecurity;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class RootEntryPointController {

	@Autowired
	private AlgaLinks algaLinks;
	
	@Autowired
	private AlgaSecurity algaSecurity;
	
	@GetMapping
	public ResponseEntity<RootEntryPointModel> root() {
		var rootEntryPointModel = new RootEntryPointModel();
		
		if (algaSecurity.podeConsultarCozinhas()) {
			rootEntryPointModel.add(algaLinks.linkToCozinhas("cozinhas"));
		}
		
		if (algaSecurity.podeBuscarPedidos()) {
			rootEntryPointModel.add(algaLinks.linkToPedidos("pedidos"));
		}
		
		if (algaSecurity.podeConsultarRestaurantes()) {
			rootEntryPointModel.add(algaLinks.linkToRestaurantes("restaurantes"));
		}
		
		if (algaSecurity.podeConsultarUsuariosGruposPermissoes()) {
			rootEntryPointModel.add(algaLinks.linkToGrupos("grupos"));
			rootEntryPointModel.add(algaLinks.linkToUsuarios("usuarios"));
			rootEntryPointModel.add(algaLinks.linkToPermissoes("permissoes"));
		}
		
		if (algaSecurity.podeConsultarFormasPagamento()) {
			rootEntryPointModel.add(algaLinks.linkToFormasPagamento("formas-pagamento"));
		}
		
		if (algaSecurity.podeConsultarCidades()) {
			rootEntryPointModel.add(algaLinks.linkToCidades("cidades"));
		}
		
		if (algaSecurity.podeConsultarCozinhas()) {
			rootEntryPointModel.add(algaLinks.linkToEstados("estados"));
		}
		
		if (algaSecurity.podeConsultarEstatisticas()) {
			rootEntryPointModel.add(algaLinks.linkToEstatisticas("estatisticas"));
		}
		
		return ResponseEntity.ok(rootEntryPointModel);
	}
}
