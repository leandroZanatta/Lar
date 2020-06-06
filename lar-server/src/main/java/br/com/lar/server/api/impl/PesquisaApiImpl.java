package br.com.lar.server.api.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import br.com.lar.server.api.PesquisaApi;
import br.com.lar.server.dto.PesquisaPaginadaDTO;
import br.com.lar.server.service.PesquisaService;

@CrossOrigin
@RestController
public class PesquisaApiImpl implements PesquisaApi {

	@Autowired
	@Lazy
	private PesquisaService pesquisaService;

	@Override
	public <T> ResponseEntity<PesquisaPaginadaDTO<T>> pesquisar(Long codigoPesquisa, String campoPesquisa, Long pagina,
			Long registros) {

		return ResponseEntity.ok(pesquisaService.pesquisar(codigoPesquisa, campoPesquisa, pagina, registros));

	}

}
