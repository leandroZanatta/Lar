package br.com.lar.repository.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import br.com.lar.repository.model.pk.ContasReceberBoletoPk;
import lombok.Data;

@Data
@Entity
@Table(name = "tb_contasreceberboleto")
public class ContasReceberBoleto implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private ContasReceberBoletoPk id;

	@Column(name = "cd_boleto", insertable = false, updatable = false)
	private Long codigoBoleto;

	@Column(name = "cd_contasreceber", insertable = false, updatable = false)
	private Long codigoContasReceber;

}