 package com.algaworks.algafood.core.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.domain.repository.PedidoRepository;
import com.algaworks.algafood.domain.repository.RestauranteRepository;

@Component
public class AlgaSecurity {
	
	@Autowired
	private RestauranteRepository restauranteRepository;
	
	@Autowired
	private PedidoRepository pedidoRepository;
	
	protected Authentication getAuthentication() {
		return SecurityContextHolder.getContext().getAuthentication();
	}
	
	public boolean hasAuthority(String authorityName) {
		return getAuthentication().getAuthorities().stream()
				.anyMatch(authority -> authority.getAuthority().endsWith(authorityName));
	}
	
	public boolean temEscopoEscrita() {
		return hasAuthority("SCOPE_WRITE");
	}
	
	public boolean temEscopoLeitura() {
		return hasAuthority("SCOPE_READ");
	}
	
	public Long getUsuarioId() {
		var jwt = (Jwt) getAuthentication().getPrincipal();
		
		return jwt.getClaim("user_id");
	}
	
	public boolean isAutenticado() {
		return getAuthentication().isAuthenticated();
	}
	
	public boolean gerenciaRestaurante(Long restauranteId) {
		if (restauranteId == null) {
			return false;
		}
		
		return restauranteRepository.existsResponsavel(restauranteId, getUsuarioId());
	}
	
	public boolean gerenciaRestauranteDoPedido(String codigoPedido) {
		return pedidoRepository.isPedidoGerenciadoPor(codigoPedido, getUsuarioId());
	}
	
	public boolean usuarioAutenticadoIgual(Long usuarioId) {
		return getUsuarioId() != null && usuarioId != null && getUsuarioId() == usuarioId;
	}
	
	public boolean podeGerenciarPedidos(String codigoPedido) {
		return hasAuthority("SCOPE_WRITE") 
				&& (hasAuthority("GERENCIAR_PEDIDOS") || gerenciaRestauranteDoPedido(codigoPedido));
	}
	
	public boolean podeConsultarRestaurantes() {
		return temEscopoLeitura() && isAutenticado();
	}
	
	public boolean podeGerenciarCadastroRestaurantes() {
		return temEscopoEscrita() && hasAuthority("EDITAR_RESTAURANTES");
	}
	
	public boolean podeGerenciarFuncionamentoRestaurantes(Long restauranteId) {
		return temEscopoEscrita() 
				&& (hasAuthority("EDITAR_RESTAURANTES") || gerenciaRestaurante(restauranteId));
	}
	
	public boolean podeConsultarUsuariosGruposPermissoes() {
		return temEscopoLeitura() && hasAuthority("CONSULTAR_USUARIOS_GRUPOS_PERMISSOES");
	}
	
	public boolean podeEditarUsuariosGruposPermissoes() {
		return temEscopoEscrita() && hasAuthority("EDITAR_USUARIOS_GRUPOS_PERMISSOES");
	}
	
	public boolean podePesquisarPedidos(Long clienteId, Long restauranteId) {
		return temEscopoLeitura() && (hasAuthority("CONSULTAR_PEDIDOS") || usuarioAutenticadoIgual(clienteId) 
				|| gerenciaRestaurante(restauranteId));
	}
	
	public boolean podeEmitirPedidos() {
		return temEscopoEscrita() && isAutenticado();
	}

	public boolean podeBuscarPedidos() {
		return temEscopoLeitura() && isAutenticado();
	}
	
	public boolean podeConsultarFormasPagamento() {
		return temEscopoLeitura() && isAutenticado();
	}
	
	public boolean podeConsultarCidades() {
		return temEscopoLeitura() && isAutenticado();
	}
	
	public boolean podeConsultarEstados() {
		return temEscopoLeitura() && isAutenticado();
	}
	
	public boolean podeEditarCozinhas() {
		return temEscopoEscrita() && hasAuthority("EDITAR_COZINHAS");
	}
	
	public boolean podeConsultarCozinhas() {
		return temEscopoLeitura() && isAutenticado();
	}
	
	public boolean podeConsultarEstatisticas() {
		return temEscopoLeitura() && hasAuthority("GERAR_RELATORIOS");
	}
}
