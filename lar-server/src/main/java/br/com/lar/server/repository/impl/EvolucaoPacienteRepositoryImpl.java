package br.com.lar.server.repository.impl;

import static br.com.lar.server.repository.domain.QCliente.cliente;
import static br.com.lar.server.repository.domain.QEvolucaoDetalhe.evolucaoDetalhe;
import static br.com.lar.server.repository.domain.QEvolucaoPaciente.evolucaoPaciente;
import static br.com.lar.server.repository.domain.QUsuario.usuario;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.sql.JPASQLQuery;
import com.querydsl.sql.SQLTemplates;

import br.com.lar.server.repository.custom.EvolucaoPacienteRepositoryCustom;
import br.com.lar.server.repository.projections.EvolucaoDetalheProjection;

public class EvolucaoPacienteRepositoryImpl implements EvolucaoPacienteRepositoryCustom {

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private SQLTemplates sqlTemplates;

	@Override
	public List<EvolucaoDetalheProjection> buscarEvolucoesPaciente(Long codigoPaciente, Long pagina, Long registros) {

		JPASQLQuery<EvolucaoDetalheProjection> query = getQueryEvolucaoPaciente(codigoPaciente);

		query.leftJoin(usuario).on(evolucaoDetalhe.codigoUsuario.eq(usuario.idUsuario));

		query.leftJoin(cliente).on(usuario.codigoCliente.eq(cliente.idCliente));

		query.orderBy(evolucaoDetalhe.dataCadastro.desc());

		query.offset(pagina * registros).limit(registros);

		query.select(Projections.fields(EvolucaoDetalheProjection.class, evolucaoDetalhe.idEvolucaoDetalhe,
				evolucaoDetalhe.dataCadastro, evolucaoDetalhe.evolucao, cliente.nome.as("funcionario")));

		return query.fetch();
	}

	@Override
	public Long buscarRegistrosEvolucoesPaciente(Long codigoPaciente, Long pagina, Long registros) {

		return getQueryEvolucaoPaciente(codigoPaciente).fetchCount();
	}

	private JPASQLQuery<EvolucaoDetalheProjection> getQueryEvolucaoPaciente(Long codigoPaciente) {

		JPASQLQuery<EvolucaoDetalheProjection> query = new JPASQLQuery<>(entityManager, sqlTemplates);

		query.from(evolucaoPaciente).innerJoin(evolucaoDetalhe)
				.on(evolucaoPaciente.idEvolucaoPaciente.eq(evolucaoDetalhe.codigoEvolucaoPaciente));

		query.where(evolucaoPaciente.codigoPaciente.eq(codigoPaciente));

		return query;
	}
}
