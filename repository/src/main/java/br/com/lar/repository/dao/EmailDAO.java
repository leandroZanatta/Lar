package br.com.lar.repository.dao;

import static br.com.lar.repository.model.QEmail.email;
import static br.com.lar.repository.model.QEmailBoleto.emailBoleto;

import java.util.List;

import com.mysema.query.jpa.impl.JPAUpdateClause;

import br.com.lar.repository.model.Email;
import br.com.sysdesc.pesquisa.repository.dao.impl.PesquisableDAOImpl;
import br.com.sysdesc.util.enumeradores.TipoEnvioEmailEnum;

public class EmailDAO extends PesquisableDAOImpl<Email> {

    private static final long serialVersionUID = 1L;

    public EmailDAO() {
        super(email, email.idEmail);
    }

    public List<Email> buscarEmailsEnviar() {

        return from().where(email.codigoStatus.eq(TipoEnvioEmailEnum.AGUARDANDO.getCodigo())).list(email);
    }

    public void marcarBoletosParaEnvio(List<Long> codigoBoletos) {

        getEntityManager().getTransaction().begin();

        new JPAUpdateClause(getEntityManager(), email).set(email.codigoStatus, TipoEnvioEmailEnum.AGUARDANDO.getCodigo())
                .where(email.codigoStatus.eq(TipoEnvioEmailEnum.PENDENTE.getCodigo())
                        .and(subQuery().from(emailBoleto)
                                .where(email.idEmail.eq(emailBoleto.id.codigoEmail).and(emailBoleto.id.codigoBoleto.in(codigoBoletos))).exists()))
                .execute();

        getEntityManager().getTransaction().commit();
    }

}
