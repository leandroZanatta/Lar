package br.com.lar.server.repository;

import java.util.Date;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.lar.server.repository.custom.EvolucaoPacienteRepositoryCustom;
import br.com.lar.server.repository.domain.EvolucaoPaciente;

@Repository
public interface EvolucaoPacienteRepository
		extends EvolucaoPacienteRepositoryCustom, JpaRepository<EvolucaoPaciente, Long> {

	public Optional<EvolucaoPaciente> findByCodigoPacienteAndDataEvolucao(Long paciente, Date dataEvolucao);

}
