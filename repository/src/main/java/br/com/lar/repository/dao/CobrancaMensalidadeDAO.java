package br.com.lar.repository.dao;

import static br.com.lar.repository.model.QCobrancaMensalidade.cobrancaMensalidade;

import br.com.lar.repository.model.CobrancaMensalidade;
import br.com.sysdesc.util.dao.AbstractGenericDAO;

public class CobrancaMensalidadeDAO extends AbstractGenericDAO<CobrancaMensalidade> {

	private static final long serialVersionUID = 1L;

	public CobrancaMensalidadeDAO() {
		super(cobrancaMensalidade, cobrancaMensalidade.idCobrancaMensalidade);
	}

	public CobrancaMensalidade buscarCobrancaPeriodo(Long idCliente, String dataMensalidade) {

		return from().where(cobrancaMensalidade.cliente.idCliente.eq(idCliente)
				.and(cobrancaMensalidade.referencia.eq(dataMensalidade))).singleResult(cobrancaMensalidade);
	}

	public CobrancaMensalidade buscarCobrancaContasReceber(Long idContasReceber) {

		return from().where(cobrancaMensalidade.contasReceber.idContasReceber.eq(idContasReceber))
				.singleResult(cobrancaMensalidade);
	}

}
