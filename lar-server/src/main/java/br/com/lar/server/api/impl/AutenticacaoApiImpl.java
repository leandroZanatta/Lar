package br.com.lar.server.api.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.lar.server.api.AutenticacaoApi;
import br.com.lar.server.dto.AutenticacaoDTO;
import br.com.lar.server.dto.UsuarioAutenticadoDTO;
import br.com.lar.server.service.AutenticacaoService;

@CrossOrigin
@RestController
public class AutenticacaoApiImpl implements AutenticacaoApi {

	@Autowired
	@Lazy
	private AutenticacaoService autenticacaoService;

	@Override
	@PostMapping(path = "/autenticacao")
	public ResponseEntity<UsuarioAutenticadoDTO> autenticar(@RequestBody AutenticacaoDTO autenticacaoDTO) {

		return ResponseEntity.ok(autenticacaoService.autenticar(autenticacaoDTO));
	}

}
