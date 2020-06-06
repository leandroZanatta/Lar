package br.com.lar.server.repository.domain;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "tb_procedencia")
@SequenceGenerator(name = "GEN_PROCEDENCIA", allocationSize = 1, sequenceName = "GEN_PROCEDENCIA")
public class Procedencia implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "GEN_PROCEDENCIA")
	@Column(name = "id_procedencia")
	private Long idProcedencia;

	@Column(name = "tx_descricao")
	private String descricao;

	@OneToMany(mappedBy = "procedencia")
	private List<Paciente> pacientes;

	@Override
	public String toString() {
		return this.descricao;
	}

}