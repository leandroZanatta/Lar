package br.com.lar.repository.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;

@Data
@Entity
@Table(name = "tb_mensalidadepaciente")
public class MensalidadePaciente implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "cd_paciente")
	private Long codigoPaciente;

	@OneToOne
	@JoinColumn(name = "cd_paciente", insertable = false, updatable = false)
	private Paciente paciente;

	@ManyToOne
	@JoinColumn(name = "cd_formapagamento")
	private FormasPagamento formasPagamento;

	@Column(name = "vl_mensalidade")
	private BigDecimal valorMensalidade;

	@Column(name = "nr_diavencimento")
	private Long diaVencimento;

	@Column(name = "fl_envioautomaticoboletos")
	private boolean envioAutomaticoBoletos;

	@Column(name = "fl_agruparcontas")
	private boolean agruparContas;

	@Column(name = "nr_diasantecedencia")
	private Long diasAntecedencia;

	@Column(name = "dt_cadastro")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataCadastro = new Date();

	@Column(name = "dt_manutencao")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataManutencao;

	@Column(name = "dt_exclusao")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataExclusao;
}