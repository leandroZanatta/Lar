package br.com.lar.server.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import br.com.lar.server.dto.ParametrosEvolucaoDTO;

public interface EvolucaoApi {

	@PostMapping("/evolucao/salvar")
	public <T> ResponseEntity<T> salvar(@RequestBody ParametrosEvolucaoDTO parametrosEvolucaoDTO);
}
