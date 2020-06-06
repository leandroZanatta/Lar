package br.com.lar.repository.model.pk;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class EmailBoletoPk implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "cd_email")
    private Long codigoEmail;

    @Column(name = "cd_boleto")
    private Long codigoBoleto;

}
