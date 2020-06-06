package br.com.lar.server.service;

import java.util.List;

public interface PesquisaPadraoService {

	public <T> List<T> pesquisar(String campoPesquisa, Long pagina, Long registros);

	public Long contarRegistroPesquisa(String campoPesquisa);
}
