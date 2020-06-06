package br.com.lar.server.dto;

import java.util.Date;

import lombok.Data;

@Data
public class PacienteDTO {

	private Long idPaciente;

	private String nome;

	private Date dataAdmissao;

	private Date dataNascimento;

	private String fotoPaciente;

	private Long numeroCartaoSUS;

	private Long codigoStatus;

}
