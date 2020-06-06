package br.com.lar.repository.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;

@Data
@Entity
@Table(name = "tb_evolucaopaciente")
@SequenceGenerator(name = "GEN_EVOLUCAOPACIENTE", allocationSize = 1, sequenceName = "GEN_EVOLUCAOPACIENTE")
public class EvolucaoPaciente implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "GEN_EVOLUCAOPACIENTE")
	@Column(name = "id_evolucaopaciente")
	private Long idEvolucaoPaciente;

	@ManyToOne
	@JoinColumn(name = "cd_paciente")
	private Paciente paciente;

	@Column(name = "dt_evolucao")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataEvolucao;

	@OneToMany(mappedBy = "evolucaoPaciente")
	private List<EvolucaoDetalhe> evolucaoDetalhes;

}