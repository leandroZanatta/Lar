package br.com.lar.server.service;

import br.com.lar.server.dto.AutenticacaoDTO;
import br.com.lar.server.dto.UsuarioAutenticadoDTO;

@FunctionalInterface
public interface AutenticacaoService {

	public UsuarioAutenticadoDTO autenticar(AutenticacaoDTO autenticacaoDTO);
}
