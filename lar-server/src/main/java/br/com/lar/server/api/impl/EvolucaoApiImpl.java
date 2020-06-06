package br.com.lar.server.api.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import br.com.lar.server.api.EvolucaoApi;
import br.com.lar.server.dto.ParametrosEvolucaoDTO;
import br.com.lar.server.service.EvolucaoService;

@CrossOrigin
@RestController
public class EvolucaoApiImpl implements EvolucaoApi {

	@Autowired
	@Lazy
	private EvolucaoService evolucaoService;

	@Override
	public <T> ResponseEntity<T> salvar(ParametrosEvolucaoDTO parametrosEvolucaoDTO) {

		evolucaoService.salvar(parametrosEvolucaoDTO);

		return ResponseEntity.noContent().build();
	}

}
