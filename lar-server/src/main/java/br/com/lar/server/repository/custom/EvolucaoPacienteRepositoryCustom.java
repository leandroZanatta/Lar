package br.com.lar.server.repository.custom;

import java.util.List;

import br.com.lar.server.repository.projections.EvolucaoDetalheProjection;

public interface EvolucaoPacienteRepositoryCustom {

	public List<EvolucaoDetalheProjection> buscarEvolucoesPaciente(Long codigoPaciente, Long pagina, Long registros);

	public Long buscarRegistrosEvolucoesPaciente(Long codigoPaciente, Long pagina, Long registros);
}
