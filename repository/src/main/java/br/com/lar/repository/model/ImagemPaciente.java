package br.com.lar.repository.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "tb_imagempaciente")
@SequenceGenerator(name = "GEN_IMAGEMPACIENTE", allocationSize = 1, sequenceName = "GEN_IMAGEMPACIENTE")
public class ImagemPaciente implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "GEN_IMAGEMPACIENTE")
	@Column(name = "id_imagempaciente")
	private Long idImagemPaciente;

	@OneToOne
	@JoinColumn(name = "cd_paciente")
	private Paciente paciente;

	@Column(name = "tx_width")
	private Long largura;

	@Column(name = "tx_height")
	private Long altura;

	@Column(name = "tx_extensao")
	private String extensao;

	@Column(name = "tx_imagem")
	private byte[] imagem;

}