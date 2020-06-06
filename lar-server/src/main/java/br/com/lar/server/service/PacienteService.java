package br.com.lar.server.service;

import java.util.List;

import br.com.lar.server.dto.PacienteDTO;
import br.com.lar.server.dto.PageEvolucaoPacienteDTO;

public interface PacienteService {

	public List<PacienteDTO> listar();

	public PageEvolucaoPacienteDTO buscarEvolucaoPaciente(Long codigoPaciente, Long pagina, Long registros);
}
