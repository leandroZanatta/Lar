package br.com.lar.server.api;

import org.springframework.http.ResponseEntity;

import br.com.lar.server.dto.AutenticacaoDTO;
import br.com.lar.server.dto.UsuarioAutenticadoDTO;

@FunctionalInterface
public interface AutenticacaoApi {

	public ResponseEntity<UsuarioAutenticadoDTO> autenticar(AutenticacaoDTO autenticacaoDTO);
}
