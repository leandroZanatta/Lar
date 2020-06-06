package br.com.lar.server.dto;

import lombok.Data;

@Data
public class EvolucaoPacienteDTO {

	private Long idEvolucaoDetalhe;

	private String funcionario;

	private String evolucao;

	private String dataCadastro;

}
