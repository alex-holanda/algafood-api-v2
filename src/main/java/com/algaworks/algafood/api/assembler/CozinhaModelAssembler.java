package com.algaworks.algafood.api.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.controller.CozinhaController;
import com.algaworks.algafood.api.model.CozinhaModel;
import com.algaworks.algafood.domain.model.Cozinha;

@Component
public class CozinhaModelAssembler extends RepresentationModelAssemblerSupport<Cozinha, CozinhaModel> {
	
	@Autowired
	private ModelMapper mapper;
	
	public CozinhaModelAssembler() {
		super(CozinhaController.class, CozinhaModel.class);
	}
	
	public CozinhaModel toModel(Cozinha cozinha) {
		var cozinhaModel = createModelWithId(cozinha.getId(), cozinha);
		
		mapper.map(cozinha, cozinhaModel);
		
		cozinhaModel.add(linkTo(CozinhaController.class).withRel("cozinhas"));
		
		return cozinhaModel;
	}
	
}
