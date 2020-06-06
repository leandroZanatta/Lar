package br.com.lar.server.repository.domain;

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
@Table(name = "tb_versaoservidor")
@SequenceGenerator(name = "GEN_VERSAOSERVIDOR", sequenceName = "GEN_VERSAOSERVIDOR", allocationSize = 1)
public class VersaoServidor implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id_versaoservidor")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "GEN_VERSAOSERVIDOR")
	private Long idVersaoServidor;

	@Column(name = "nr_versao")
	private String numeroVersao;

	@Column(name = "dt_atualizacao")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataAtualizacao;
}