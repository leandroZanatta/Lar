package br.com.lar.repository.model;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import br.com.lar.repository.model.pk.EmailBoletoPk;
import br.com.sysdesc.boletos.repository.model.Boleto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "tb_emailboleto")
public class EmailBoleto implements Serializable {
    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private EmailBoletoPk id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cd_email", referencedColumnName = "id_email")
    @MapsId("codigoEmail")
    private Email email;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cd_boleto", referencedColumnName = "id_boleto")
    @MapsId("codigoBoleto")
    private Boleto boleto;
}