package br.com.lar.repository.dao;

import static br.com.lar.repository.model.QPlanoContas.planoContas;

import com.mysema.query.BooleanBuilder;

import br.com.lar.repository.model.PlanoContas;
import br.com.sysdesc.pesquisa.repository.dao.impl.PesquisableDAOImpl;
import br.com.sysdesc.util.classes.LongUtil;
import br.com.sysdesc.util.enumeradores.TipoSaldoEnum;

public class PlanoContasDAO extends PesquisableDAOImpl<PlanoContas> {

	private static final long serialVersionUID = 1L;

	public PlanoContasDAO() {
		super(planoContas, planoContas.idPlanoContas);
	}

	public Long getNextIdentifier(Long idPlanoContas) {

		BooleanBuilder booleanBuilder = new BooleanBuilder();

		if (LongUtil.isNullOrZero(idPlanoContas)) {
			booleanBuilder.and(planoContas.contaPrincipal.isNull());
		} else {
			booleanBuilder.and(planoContas.codigoContaPrincipal.eq(idPlanoContas));
		}

		return from().where(booleanBuilder).count();
	}

	public BooleanBuilder getContasSinteticas() {

		return new BooleanBuilder(planoContas.contaAnalitica.eq(Boolean.FALSE));
	}

	public BooleanBuilder getPreFilter(TipoSaldoEnum tipoSaldo, boolean analitica) {

		return new BooleanBuilder(
				planoContas.saldo.eq(tipoSaldo.getCodigo()).and(planoContas.contaAnalitica.eq(analitica)));
	}

}
