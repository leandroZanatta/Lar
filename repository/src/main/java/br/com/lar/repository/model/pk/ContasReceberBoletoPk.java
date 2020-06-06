package br.com.lar.repository.model.pk;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContasReceberBoletoPk implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name = "cd_boleto")
	private Long codigoBoleto;

	@Column(name = "cd_contasreceber")
	private Long codigoContasReceber;
}
