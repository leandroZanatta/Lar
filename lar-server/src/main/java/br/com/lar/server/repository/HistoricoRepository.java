package br.com.lar.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.lar.server.repository.custom.HistoricoRepositoryCustom;
import br.com.lar.server.repository.domain.HistoricoEvolucao;

@Repository
public interface HistoricoRepository extends HistoricoRepositoryCustom, JpaRepository<HistoricoEvolucao, Long> {

}
