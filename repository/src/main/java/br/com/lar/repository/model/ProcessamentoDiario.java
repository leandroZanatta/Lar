package br.com.lar.repository.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;

@Data
@Entity
@Table(name = "tb_processamentodiario")
@SequenceGenerator(name = "GEN_PROCESSAMENTODIARIO", allocationSize = 1, sequenceName = "GEN_PROCESSAMENTODIARIO")
public class ProcessamentoDiario implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "GEN_PROCESSAMENTODIARIO")
	@Column(name = "id_processamentodiario")
	private Long idProcessamentoDiario;

	@Column(name = "dt_procesamento")
	@Temporal(TemporalType.DATE)
	private Date dataProcessamento;

	@Column(name = "cd_status")
	private Long codigoStatus;

	@Column(name = "dt_inicio", updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataInicio;

	@Column(name = "dt_fim")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataFim;

}