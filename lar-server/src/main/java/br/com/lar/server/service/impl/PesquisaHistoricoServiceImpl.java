package br.com.lar.server.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import br.com.lar.server.repository.HistoricoRepository;
import br.com.lar.server.service.PesquisaHistoricoService;

@Service
public class PesquisaHistoricoServiceImpl implements PesquisaHistoricoService {

	@Autowired
	@Lazy
	private HistoricoRepository historicoRepository;

	@Override
	public <T> List<T> pesquisar(String campoPesquisa, Long pagina, Long registros) {

		return historicoRepository.pesquisar(campoPesquisa, pagina, registros);
	}

	@Override
	public Long contarRegistroPesquisa(String campoPesquisa) {

		return historicoRepository.contarRegistroPesquisa(campoPesquisa);
	}

}
