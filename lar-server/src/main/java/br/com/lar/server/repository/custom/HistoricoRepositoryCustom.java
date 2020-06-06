package br.com.lar.server.repository.custom;

import java.util.List;

public interface HistoricoRepositoryCustom {

	public <T> List<T> pesquisar(String campoPesquisa, Long pagina, Long registros);

	public Long contarRegistroPesquisa(String campoPesquisa);

}
