package com.algaworks.algafood.api.controller;

import java.math.BigDecimal;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.api.AlgaLinks;
import com.algaworks.algafood.api.assembler.RestauranteModelAssembler;
import com.algaworks.algafood.api.assembler.RestauranteResumoModelAssembler;
import com.algaworks.algafood.api.disassembler.RestauranteInputDisassembler;
import com.algaworks.algafood.api.model.RestauranteModel;
import com.algaworks.algafood.api.model.RestauranteResumoModel;
import com.algaworks.algafood.api.model.input.RestauranteInput;
import com.algaworks.algafood.api.openapi.controller.RestauranteControllerOpenAPI;
import com.algaworks.algafood.api.util.ApiUtils;
import com.algaworks.algafood.core.security.CheckSecurity;
import com.algaworks.algafood.domain.service.RestauranteService;

@RestController
@RequestMapping(path = "/restaurantes", produces = MediaType.APPLICATION_JSON_VALUE)
public class RestauranteController implements RestauranteControllerOpenAPI {

	@Autowired
	private RestauranteService restauranteService;

	@Autowired
	private RestauranteModelAssembler restauranteModelAssembler;
	
	@Autowired
	private RestauranteResumoModelAssembler restauranteResumoModelAssembler;
	
	@Autowired
	private RestauranteInputDisassembler restauranteInputDisassembler;
	
	@Autowired
	private AlgaLinks algaLinks;

	@Override
	@CheckSecurity.Restaurantes.PodeConsultar
	@GetMapping
	public ResponseEntity<CollectionModel<RestauranteResumoModel>> listar() {
		var restaurantesModel = restauranteResumoModelAssembler.toCollectionModel(restauranteService.listar());

		return ResponseEntity.ok(restaurantesModel);
	}

	@Override
	@CheckSecurity.Restaurantes.PodeConsultar
	@GetMapping("/lista-personalizada")
	public ResponseEntity<CollectionModel<RestauranteResumoModel>> listarPersonalizado(String nome, BigDecimal taxaFreteInicial,
			BigDecimal taxaFreteFinal) {

		var restaurantesModel = restauranteResumoModelAssembler
				.toCollectionModel(restauranteService.listarPersonalizado(nome, taxaFreteInicial, taxaFreteFinal));
		
		restaurantesModel.removeLinks()
			.add(algaLinks.linkToRestaurnatesPersonalizado());

		return ResponseEntity.ok(restaurantesModel);
	}

	@Override
	@CheckSecurity.Restaurantes.PodeConsultar
	@GetMapping("/{restauranteId}")
	public ResponseEntity<RestauranteModel> buscar(@PathVariable Long restauranteId) {
		var restauranteModel = restauranteModelAssembler.toModel(restauranteService.buscar(restauranteId));

		return ResponseEntity.ok(restauranteModel);
	}

	@Override
	@CheckSecurity.Restaurantes.PodeGerenciarCadastro
	@PostMapping
	public ResponseEntity<RestauranteModel> adicionar(@RequestBody @Valid RestauranteInput restauranteInput) {
		var restaurante = restauranteInputDisassembler.toDomainObject(restauranteInput);
		restaurante = restauranteService.adicionar(restaurante);
		
		var restauranteModel = restauranteModelAssembler.toModel(restaurante);
		
		return ResponseEntity.created(ApiUtils.uri(restauranteModel.getId())).body(restauranteModel);
	}

	@Override
	@CheckSecurity.Restaurantes.PodeGerenciarCadastro
	@PutMapping("/{restauranteId}")
	public ResponseEntity<RestauranteModel> atualizar(@PathVariable Long restauranteId,
			@RequestBody @Valid RestauranteInput restauranteInput) {
		var restaurante = restauranteInputDisassembler.toDomainObject(restauranteInput);
		restaurante = restauranteService.atualizar(restauranteId, restaurante);

		var restauranteModel = restauranteModelAssembler.toModel(restaurante);
		
		return ResponseEntity.ok(restauranteModel);
	}
	
	@Override
	@CheckSecurity.Restaurantes.PodeGerenciarCadastro
	@PutMapping("/{restauranteId}/ativo")
	public ResponseEntity<Void> ativar(@PathVariable Long restauranteId) {
		restauranteService.ativar(restauranteId);
		return ResponseEntity.noContent().build();
	}
	
	@Override
	@CheckSecurity.Restaurantes.PodeGerenciarCadastro
	@PutMapping("/ativacoes")
	public ResponseEntity<Void> ativar(@RequestBody List<Long> restaurantesId) {
		restauranteService.ativar(restaurantesId);
		return ResponseEntity.noContent().build();
	}
	
	@Override
	@CheckSecurity.Restaurantes.PodeGerenciarCadastro
	@DeleteMapping("/{restauranteId}/ativo")
	public ResponseEntity<Void> inativar(@PathVariable Long restauranteId) {
		restauranteService.inativar(restauranteId);
		return ResponseEntity.noContent().build();
	}
	
	@Override
	@CheckSecurity.Restaurantes.PodeGerenciarCadastro
	@DeleteMapping("/ativacoes")
	public ResponseEntity<Void> inativar(@RequestBody List<Long> restaurantesId) {
		restauranteService.inativar(restaurantesId);
		return ResponseEntity.noContent().build();
	}
	
	@Override
	@CheckSecurity.Restaurantes.PodeGerenciarFuncionamento
	@PutMapping("/{restauranteId}/abertura")
	public ResponseEntity<Void> abrir(@PathVariable Long restauranteId) {
		restauranteService.abrir(restauranteId);
		return ResponseEntity.noContent().build();
	}
	
	@Override
	@CheckSecurity.Restaurantes.PodeGerenciarFuncionamento
	@DeleteMapping("/{restauranteId}/fechamento")
	public ResponseEntity<Void> fechar(@PathVariable Long restauranteId) {
		restauranteService.fechar(restauranteId);
		return ResponseEntity.noContent().build();
	}
}
