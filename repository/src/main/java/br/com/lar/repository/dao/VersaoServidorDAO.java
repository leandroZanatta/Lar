package br.com.lar.repository.dao;

import static br.com.lar.repository.model.QVersaoServidor.versaoServidor;

import br.com.lar.repository.model.VersaoServidor;
import br.com.sysdesc.util.dao.AbstractGenericDAO;

public class VersaoServidorDAO extends AbstractGenericDAO<VersaoServidor> {

	private static final long serialVersionUID = 1L;

	public VersaoServidorDAO() {
		super(versaoServidor, versaoServidor.idVersaoServidor);
	}

}
