package br.com.lar.server.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.lar.server.repository.domain.Paciente;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Long> {

	public List<Paciente> findByCodigoStatus(Long codigoStatus);

}
