package br.com.lar.repository.model;

import java.io.Serializable;
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

import br.com.sysdesc.pesquisa.repository.model.Usuario;
import lombok.Data;

@Data
@Entity
@Table(name = "tb_evolucaodetalhe")
@SequenceGenerator(name = "GEN_EVOLUCAODETALHE", allocationSize = 1, sequenceName = "GEN_EVOLUCAODETALHE")
public class EvolucaoDetalhe implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "GEN_EVOLUCAODETALHE")
	@Column(name = "id_evolucaodetalhe")
	private Long idEvolucaoDetalhe;

	@ManyToOne
	@JoinColumn(name = "cd_evolucaopaciente")
	private EvolucaoPaciente evolucaoPaciente;

	@ManyToOne
	@JoinColumn(name = "cd_usuario")
	private Usuario usuario;

	@Column(name = "tx_evolucao")
	private String evolucao;

	@Column(name = "dt_cadastro")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataCadastro;

}