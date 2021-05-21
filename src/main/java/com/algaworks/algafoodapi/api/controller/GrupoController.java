package com.algaworks.algafoodapi.api.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.algaworks.algafoodapi.api.assembler.GrupoModelAssembler;
import com.algaworks.algafoodapi.api.disassembler.GrupoInputDisassembler;
import com.algaworks.algafoodapi.api.model.GrupoModel;
import com.algaworks.algafoodapi.api.model.input.GrupoInput;
import com.algaworks.algafoodapi.api.util.ApiUtils;
import com.algaworks.algafoodapi.domain.service.GrupoService;

@RestController
@RequestMapping(path = "/grupos", produces = MediaType.APPLICATION_JSON_VALUE)
public class GrupoController {
	
	@Autowired
	private GrupoModelAssembler grupoModelAssembler;
	
	@Autowired
	private GrupoInputDisassembler grupoInputDisassembler;
	
	@Autowired
	private GrupoService grupoService;

	@GetMapping
	public ResponseEntity<List<GrupoModel>> listar() {
		var gruposModel = grupoModelAssembler.toCollectionModel(grupoService.listar());
		return ResponseEntity.ok(gruposModel);
	}
	
	@GetMapping("/{grupoId}")
	public ResponseEntity<GrupoModel> buscar(@PathVariable Long grupoId) {
		var grupoModel = grupoModelAssembler.toModel(grupoService.buscar(grupoId));
		return ResponseEntity.ok(grupoModel);
	}
	
	@PostMapping
	public ResponseEntity<GrupoModel> adicionar(@RequestBody @Valid GrupoInput grupoInput) {
		var grupo = grupoService.adicionar(grupoInputDisassembler.toDomainObject(grupoInput));
		var grupoModel = grupoModelAssembler.toModel(grupo);
		return ResponseEntity.created(ApiUtils.uri(grupoModel.getId())).body(grupoModel);
	}
	
	@PutMapping("/{grupoId}")
	public ResponseEntity<GrupoModel> atualizar(@PathVariable Long grupoId, @RequestBody @Valid GrupoInput grupoInput) {
		var grupo = grupoInputDisassembler.toDomainObject(grupoInput);
		var grupoModel = grupoModelAssembler.toModel(grupoService.atualizar(grupoId, grupo));
		return ResponseEntity.ok(grupoModel);
	}
	
	@DeleteMapping("/{grupoId}")
	public ResponseEntity<Void> remover(@PathVariable Long grupoId) {
		grupoService.remover(grupoId);
		return ResponseEntity.noContent().build();
	}
}
