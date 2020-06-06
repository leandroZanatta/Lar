package br.com.lar.server.repository.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "tb_paciente")
@SequenceGenerator(name = "GEN_PACIENTE", sequenceName = "GEN_PACIENTE", allocationSize = 1)
public class Paciente implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "GEN_PACIENTE")
	@Column(name = "id_paciente")
	private Long idPaciente;

	@ManyToOne
	@JoinColumn(name = "cd_cliente")
	private Cliente cliente;

	@ManyToOne
	@JoinColumn(name = "cd_responsavel")
	private Cliente responsavel;

	@ManyToOne
	@JoinColumn(name = "cd_procedencia")
	private Procedencia procedencia;

	@Column(name = "dt_admissao")
	@Temporal(TemporalType.DATE)
	private Date dataAdmissao;

	@Column(name = "cd_status")
	private Long codigoStatus;

	@Column(name = "nr_cartaosus")
	private Long numeroCartaoSUS;

	@Column(name = "tx_ocupacaoinss")
	private String ocupacaoINSS;

	@OneToOne(mappedBy = "paciente", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true, optional = true)
	private ImagemPaciente imagemPaciente;
}