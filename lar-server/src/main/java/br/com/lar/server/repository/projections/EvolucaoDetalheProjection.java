package br.com.lar.server.repository.projections;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

@Data
public class EvolucaoDetalheProjection implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long idEvolucaoDetalhe;

	private String funcionario;

	private String evolucao;

	private Date dataCadastro;

}