package br.com.lar.repository.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "tb_email")
@SequenceGenerator(name = "GEN_EMAIL", allocationSize = 1, sequenceName = "GEN_EMAIL")
public class Email implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "GEN_EMAIL")
    @Column(name = "id_email")
    private Long idEmail;

    @Column(name = "tx_destinatario")
    private String destinatario;

    @Column(name = "tx_assunto")
    private String assunto;

    @Column(name = "tx_mensagem")
    private String mensagem;

    @Column(name = "cd_status")
    private Long codigoStatus;

    @OneToOne(mappedBy = "email", fetch = FetchType.EAGER, cascade = CascadeType.ALL, optional = true)
    private EmailBoleto emailBoleto;

    @OneToMany(mappedBy = "email", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<EmailAnexo> emailAnexos;

}