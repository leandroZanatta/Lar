package br.com.lar.server.service;

import br.com.lar.server.dto.PesquisaPaginadaDTO;

public interface PesquisaService {

	public <T> PesquisaPaginadaDTO<T> pesquisar(Long codigoPesquisa, String campoPesquisa, Long pagina, Long registros);
}
