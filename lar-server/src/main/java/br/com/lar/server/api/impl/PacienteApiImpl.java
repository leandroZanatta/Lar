package br.com.lar.server.api.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import br.com.lar.server.api.PacienteApi;
import br.com.lar.server.dto.PacienteDTO;
import br.com.lar.server.dto.PageEvolucaoPacienteDTO;
import br.com.lar.server.service.PacienteService;

@CrossOrigin
@RestController
public class PacienteApiImpl implements PacienteApi {

	@Autowired
	@Lazy
	private PacienteService pacienteService;

	@Override
	public ResponseEntity<List<PacienteDTO>> listar() {

		return ResponseEntity.ok(pacienteService.listar());
	}

	@Override
	public ResponseEntity<PageEvolucaoPacienteDTO> buscarEvolucaoPaciente(Long codigoPaciente, Long pagina,
			Long registros) {

		return ResponseEntity.ok(pacienteService.buscarEvolucaoPaciente(codigoPaciente, pagina, registros));
	}

}
