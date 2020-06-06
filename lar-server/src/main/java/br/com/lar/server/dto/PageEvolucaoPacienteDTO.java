package br.com.lar.server.dto;

import java.util.List;

import lombok.Data;

@Data
public class PageEvolucaoPacienteDTO {

	private Long numeroRegistros;

	private List<EvolucaoPacienteDTO> evolucaoPacientes;
}
