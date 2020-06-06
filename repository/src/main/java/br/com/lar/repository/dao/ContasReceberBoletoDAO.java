package br.com.lar.repository.dao;

import static br.com.lar.repository.model.QContasReceberBoleto.contasReceberBoleto;

import br.com.lar.repository.model.ContasReceberBoleto;
import br.com.sysdesc.util.dao.AbstractGenericDAO;

public class ContasReceberBoletoDAO extends AbstractGenericDAO<ContasReceberBoleto> {

	private static final long serialVersionUID = 1L;

	public ContasReceberBoletoDAO() {
		super(contasReceberBoleto, contasReceberBoleto.codigoBoleto);
	}

}
