package br.com.lar.repository.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;

@Data
@Entity
@Table(name = "tb_parametros")
public class Parametros implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id_parametro")
	private Long idParametro;

	@Column(name = "tx_mensalidade")
	private String configMensalidade;

	@Column(name = "tx_email")
	private String configEmail;

	@Column(name = "dt_cadastro", updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date cadastro;

	@Column(name = "dt_manutencao")
	@Temporal(TemporalType.TIMESTAMP)
	private Date manutencao;
}