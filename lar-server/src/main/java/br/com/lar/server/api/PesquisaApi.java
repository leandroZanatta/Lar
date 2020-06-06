package br.com.lar.server.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import br.com.lar.server.dto.PesquisaPaginadaDTO;

public interface PesquisaApi {

	@GetMapping("/pesquisa")
	public <T> ResponseEntity<PesquisaPaginadaDTO<T>> pesquisar(@RequestParam("codigoPesquisa") Long codigoPesquisa,
			@RequestParam("campoPesquisa") String campoPesquisa, @RequestParam("pagina") Long pagina,
			@RequestParam("registros") Long registros);
}
