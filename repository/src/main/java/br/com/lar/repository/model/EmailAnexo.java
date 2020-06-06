package br.com.lar.repository.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "tb_emailanexo")
@SequenceGenerator(name = "GEN_EMAILANEXO", allocationSize = 1, sequenceName = "GEN_EMAILANEXO")
public class EmailAnexo implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "GEN_EMAILANEXO")
    @Column(name = "id_emailanexo")
    private Long idEmailAnexo;

    @Column(name = "cd_email", insertable = false, updatable = false)
    private Long codigoEmail;

    @ManyToOne
    @JoinColumn(name = "cd_email")
    private Email email;

    @Column(name = "tx_nomearquivo")
    private String nomeArquivo;

    @Column(name = "bl_anexo")
    private byte[] anexo;

}