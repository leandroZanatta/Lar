package br.com.lar.server.service.impl;

import static br.com.sysdesc.util.resources.Resources.FRMLOGIN_MSG_LOGIN;
import static br.com.sysdesc.util.resources.Resources.FRMLOGIN_MSG_SENHA;
import static br.com.sysdesc.util.resources.Resources.FRMLOGIN_MSG_USUARIO;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import br.com.lar.server.dto.AutenticacaoDTO;
import br.com.lar.server.dto.PermisaoProgramaDTO;
import br.com.lar.server.dto.UsuarioAutenticadoDTO;
import br.com.lar.server.repository.UsuarioRepository;
import br.com.lar.server.repository.domain.PermissaoPrograma;
import br.com.lar.server.repository.domain.Usuario;
import br.com.lar.server.service.AutenticacaoService;
import br.com.sysdesc.util.classes.CryptoUtil;
import br.com.sysdesc.util.classes.StringUtil;
import br.com.sysdesc.util.exception.SysDescException;

@Service
public class AutenticacaoServiceImpl implements AutenticacaoService {

	@Autowired
	@Lazy
	private UsuarioRepository usuarioRepository;

	@Override
	public UsuarioAutenticadoDTO autenticar(AutenticacaoDTO autenticacaoDTO) {

		if (StringUtil.isNullOrEmpty(autenticacaoDTO.getUsuario())) {

			throw new SysDescException(FRMLOGIN_MSG_USUARIO);
		}

		if (StringUtil.isNullOrEmpty(autenticacaoDTO.getSenha())) {

			throw new SysDescException(FRMLOGIN_MSG_SENHA);

		}

		Optional<Usuario> usuario = usuarioRepository.findByNomeUsuarioAndSenha(autenticacaoDTO.getUsuario(),
				CryptoUtil.toMD5(autenticacaoDTO.getSenha()));

		if (!usuario.isPresent()) {

			throw new SysDescException(FRMLOGIN_MSG_LOGIN);
		}

		UsuarioAutenticadoDTO usuarioAutenticadoDTO = new UsuarioAutenticadoDTO();
		usuarioAutenticadoDTO.setIdUsuario(usuario.get().getIdUsuario());
		usuarioAutenticadoDTO.setNome(usuario.get().getNomeUsuario());
		usuarioAutenticadoDTO.setPermissaoProgramas(montarPermissoesUsuario(usuario.get()));

		return usuarioAutenticadoDTO;
	}

	private List<PermisaoProgramaDTO> montarPermissoesUsuario(Usuario usuario) {
		Set<PermisaoProgramaDTO> permissaoProgramas = new HashSet<>();

		usuario.getPermissaoProgramas().forEach(permissao -> montarPermissao(permissao, permissaoProgramas));

		usuario.getPerfilUsuarios().forEach(perfilUsuarios -> perfilUsuarios.getPerfil().getPermissaoProgramas()
				.forEach(permissao -> montarPermissao(permissao, permissaoProgramas)));

		return new ArrayList<>(permissaoProgramas);
	}

	private void montarPermissao(PermissaoPrograma permissao, Set<PermisaoProgramaDTO> permissaoProgramas) {

		if (permissao.getPrograma().getCodigoAplicativo() == null
				|| permissao.getPrograma().getCodigoAplicativo().equals(2L)) {

			permissaoProgramas.add(montarPermissaoDTO(permissao));
		}
	}

	private PermisaoProgramaDTO montarPermissaoDTO(PermissaoPrograma permissao) {

		return new PermisaoProgramaDTO(permissao.getCodigoPrograma(), permissao.getFlagLeitura(),
				permissao.getFlagCadastro(), permissao.getFlagExclusao());
	}

}
