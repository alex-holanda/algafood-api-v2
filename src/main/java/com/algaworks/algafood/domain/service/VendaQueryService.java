package com.algaworks.algafood.domain.service;

import java.util.List;

import com.algaworks.algafood.domain.dto.VendaDiaria;
import com.algaworks.algafood.domain.filter.VendaDiariaFilter;

public interface VendaQueryService {

	List<VendaDiaria> consultarVendasDiarias(VendaDiariaFilter filtro, String timeOffset);
}
