package br.com.lar.server.dto;

import lombok.Data;

@Data
public class ParametrosEvolucaoDTO {

	private Long usuario;

	private Long paciente;

	private String dataEvolucao;

	private String evolucao;

}
