package br.com.lar.repository.dao;

import static br.com.lar.repository.model.QParametros.parametros;

import br.com.lar.repository.model.Parametros;
import br.com.sysdesc.pesquisa.repository.dao.impl.PesquisableDAOImpl;

public class ParametrosDAO extends PesquisableDAOImpl<Parametros> {

	private static final long serialVersionUID = 1L;

	public ParametrosDAO() {
		super(parametros, parametros.idParametro);
	}

}
