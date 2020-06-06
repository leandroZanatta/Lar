package br.com.lar.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.lar.server.repository.domain.EvolucaoDetalhe;

@Repository
public interface EvolucaoDetalheRepository extends JpaRepository<EvolucaoDetalhe, Long> {

}
