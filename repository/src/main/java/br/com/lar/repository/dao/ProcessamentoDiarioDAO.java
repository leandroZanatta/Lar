package br.com.lar.repository.dao;

import static br.com.lar.repository.model.QProcessamentoDiario.processamentoDiario;

import java.util.Date;

import com.mysema.query.jpa.impl.JPAUpdateClause;

import br.com.lar.repository.model.ProcessamentoDiario;
import br.com.sysdesc.util.dao.AbstractGenericDAO;
import br.com.sysdesc.util.enumeradores.ProcessamentoDiarioEnum;

public class ProcessamentoDiarioDAO extends AbstractGenericDAO<ProcessamentoDiario> {

	private static final long serialVersionUID = 1L;

	public ProcessamentoDiarioDAO() {
		super(processamentoDiario, processamentoDiario.idProcessamentoDiario);
	}

	public void atualizarProcessamentosAnteriores() {

		getEntityManager().getTransaction().begin();

		JPAUpdateClause update = new JPAUpdateClause(getEntityManager(), processamentoDiario);

		update.set(processamentoDiario.codigoStatus, ProcessamentoDiarioEnum.ERRO.getCodigo());
		update.set(processamentoDiario.dataFim, new Date());

		update.where(processamentoDiario.codigoStatus.eq(ProcessamentoDiarioEnum.INICIANDO.getCodigo()));

		update.execute();

		getEntityManager().getTransaction().commit();
	}

	public Date buscarUltimaDataProcesada() {

		return sqlFrom().where(processamentoDiario.codigoStatus.eq(ProcessamentoDiarioEnum.CONCLUIDO.getCodigo()))
				.singleResult(processamentoDiario.dataProcessamento.max());
	}

}
