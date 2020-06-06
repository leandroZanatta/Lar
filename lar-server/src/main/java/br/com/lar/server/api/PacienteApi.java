package br.com.lar.server.api;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import br.com.lar.server.dto.PacienteDTO;
import br.com.lar.server.dto.PageEvolucaoPacienteDTO;

public interface PacienteApi {

	@GetMapping("/pacientes")
	public ResponseEntity<List<PacienteDTO>> listar();

	@GetMapping("/paciente/evolucoes")
	public ResponseEntity<PageEvolucaoPacienteDTO> buscarEvolucaoPaciente(
			@RequestParam("codigoPaciente") Long codigoPaciente, @RequestParam("pagina") Long pagina,
			@RequestParam("registros") Long registros);
}
