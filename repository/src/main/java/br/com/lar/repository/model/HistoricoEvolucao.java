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
@Table(name = "tb_historicoevolucao")
@SequenceGenerator(name = "GEN_HISTORICOEVOLUCAO", allocationSize = 1, sequenceName = "GEN_HISTORICOEVOLUCAO")
public class HistoricoEvolucao implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "GEN_HISTORICOEVOLUCAO")
	@Column(name = "id_historicoevolucao")
	private Long idHistorico;

	@Column(name = "tx_descricao")
	private String descricao;

	@Column(name = "tx_historico")
	private String historico;

	@Column(name = "dt_cadastro")
	@Temporal(TemporalType.DATE)
	private Date dataCadastro;

}