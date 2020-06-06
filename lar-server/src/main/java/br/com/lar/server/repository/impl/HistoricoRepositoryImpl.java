package br.com.lar.server.repository.impl;

import static br.com.lar.server.repository.domain.QHistoricoEvolucao.historicoEvolucao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.sql.JPASQLQuery;
import com.querydsl.sql.SQLTemplates;

import br.com.lar.server.repository.custom.HistoricoRepositoryCustom;
import br.com.lar.server.repository.domain.HistoricoEvolucao;
import br.com.lar.server.repository.projections.HistoricoEvolucaoProjection;
import br.com.sysdesc.util.classes.StringUtil;

public class HistoricoRepositoryImpl implements HistoricoRepositoryCustom {

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private SQLTemplates sqlTemplates;

	@Override
	public <T> List<T> pesquisar(String campoPesquisa, Long pagina, Long registros) {

		JPASQLQuery<T> query = getQuery(campoPesquisa);

		query.select(Projections.fields(HistoricoEvolucaoProjection.class, historicoEvolucao.idHistorico,
				historicoEvolucao.descricao, historicoEvolucao.historico));

		query.orderBy(historicoEvolucao.idHistorico.asc());

		query.offset(pagina * registros).limit(registros);

		return query.fetch();
	}

	@Override
	public Long contarRegistroPesquisa(String campoPesquisa) {

		JPASQLQuery<HistoricoEvolucao> query = getQuery(campoPesquisa);

		return query.fetchCount();
	}

	private <T> JPASQLQuery<T> getQuery(String campoPesquisa) {

		JPASQLQuery<T> query = new JPASQLQuery<>(entityManager, sqlTemplates);

		query.from(historicoEvolucao);

		BooleanBuilder booleanBuilder = getWhere(campoPesquisa);

		if (booleanBuilder.hasValue()) {
			query.where(booleanBuilder);
		}

		return query;
	}

	private BooleanBuilder getWhere(String campoPesquisa) {

		BooleanBuilder booleanBuilder = new BooleanBuilder();

		if (!StringUtil.isNullOrEmpty(campoPesquisa)) {

			if (isId(campoPesquisa)) {
				return getCampoId(campoPesquisa);
			}

			BooleanBuilder clausula = new BooleanBuilder();

			if (StringUtil.isNumeric(campoPesquisa)) {

				clausula.or(historicoEvolucao.idHistorico.eq(Long.valueOf(StringUtil.formatarNumero(campoPesquisa))));
			}

			clausula.or(historicoEvolucao.descricao.likeIgnoreCase("%" + campoPesquisa + "%"));

			booleanBuilder.and(clausula);
		}

		return booleanBuilder;
	}

	private BooleanBuilder getCampoId(String campoPesquisa) {

		Long id = getId(campoPesquisa);

		return new BooleanBuilder(historicoEvolucao.idHistorico.eq(id));
	}

	private Long getId(String campoPesquisa) {

		if (StringUtil.isNumeric(campoPesquisa)) {
			return Long.valueOf(campoPesquisa);
		}

		return Long.valueOf(StringUtil.formatarNumero(campoPesquisa.split(" - ")[0]));

	}

	private boolean isId(String campoPesquisa) {

		String[] campos = campoPesquisa.split(" - ");

		return StringUtil.isNumeric(campoPesquisa) || (campos.length == 2 && StringUtil.isNumeric(campos[0]));
	}
}
