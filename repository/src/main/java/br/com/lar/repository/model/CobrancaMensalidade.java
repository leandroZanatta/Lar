package br.com.lar.repository.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "tb_cobrancamensalidade")
@SequenceGenerator(name = "GEN_COBRANCAMENSALIDADE", allocationSize = 1, sequenceName = "GEN_COBRANCAMENSALIDADE")
public class CobrancaMensalidade implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "GEN_COBRANCAMENSALIDADE")
	@Column(name = "id_cobrancamensalidade")
	private Long idCobrancaMensalidade;

	@ManyToOne
	@JoinColumn(name = "cd_cliente")
	private Cliente cliente;

	@ManyToOne
	@JoinColumn(name = "cd_contasreceber")
	private ContasReceber contasReceber;

	@Column(name = "cd_referencia")
	private String referencia;

	@Column(name = "vl_mensalidade")
	private BigDecimal valorMensalidade;

	@Column(name = "dt_cadastro")
	@Temporal(TemporalType.DATE)
	private Date dataCadastro;

	@Column(name = "dt_manutencao")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataManutencao;

}