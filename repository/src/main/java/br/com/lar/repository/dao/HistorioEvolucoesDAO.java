package br.com.lar.repository.dao;

import static br.com.lar.repository.model.QHistoricoEvolucao.historicoEvolucao;

import br.com.lar.repository.model.HistoricoEvolucao;
import br.com.sysdesc.pesquisa.repository.dao.impl.PesquisableDAOImpl;

public class HistorioEvolucoesDAO extends PesquisableDAOImpl<HistoricoEvolucao> {

	private static final long serialVersionUID = 1L;

	public HistorioEvolucoesDAO() {
		super(historicoEvolucao, historicoEvolucao.idHistorico);
	}

}
