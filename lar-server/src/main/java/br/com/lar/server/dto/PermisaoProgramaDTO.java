package br.com.lar.server.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PermisaoProgramaDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long idPrograma;

	private Boolean flagLeitura;

	private Boolean flagCadastro;

	private Boolean flagExclusao;

}
