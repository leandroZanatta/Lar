package br.com.lar.server.repository.projections;

import java.io.Serializable;

import lombok.Data;

@Data
public class HistoricoEvolucaoProjection implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long idHistorico;

	private String descricao;

	private String historico;

}