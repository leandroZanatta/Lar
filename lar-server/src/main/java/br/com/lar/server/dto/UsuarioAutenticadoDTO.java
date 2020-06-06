package br.com.lar.server.dto;

import java.util.List;

import lombok.Data;

@Data
public class UsuarioAutenticadoDTO {

	private Long idUsuario;

	private String nome;

	private List<PermisaoProgramaDTO> permissaoProgramas;

}
