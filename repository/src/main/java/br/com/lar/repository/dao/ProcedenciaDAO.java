package br.com.lar.repository.dao;

import static br.com.lar.repository.model.QProcedencia.procedencia;

import br.com.lar.repository.model.Procedencia;
import br.com.sysdesc.pesquisa.repository.dao.impl.PesquisableDAOImpl;

public class ProcedenciaDAO extends PesquisableDAOImpl<Procedencia> {

	private static final long serialVersionUID = 1L;

	public ProcedenciaDAO() {
		super(procedencia, procedencia.idProcedencia);
	}

}
