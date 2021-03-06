package com.algaworks.algafood.api.disassembler;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.model.input.UsuarioComSenhaInput;
import com.algaworks.algafood.api.model.input.UsuarioInput;
import com.algaworks.algafood.domain.model.Usuario;

@Component
public class UsuarioInputDisassembler {

	@Autowired
	private ModelMapper mapper;
	
	public Usuario toDomainObject(UsuarioInput usuarioInput) {
		return mapper.map(usuarioInput, Usuario.class);
	}
	
	public Usuario toDomainObject(UsuarioComSenhaInput usuarioComSenhaInput) {
		return mapper.map(usuarioComSenhaInput, Usuario.class);
	}
}
