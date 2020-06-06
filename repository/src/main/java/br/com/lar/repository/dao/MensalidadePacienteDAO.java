package br.com.lar.repository.dao;

import static br.com.lar.repository.model.QMensalidadePaciente.mensalidadePaciente;

import java.util.List;

import com.mysema.query.BooleanBuilder;
import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.types.ConstantImpl;
import com.mysema.query.types.Ops;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.NumberExpression;
import com.mysema.query.types.expr.NumberOperation;

import br.com.lar.repository.model.MensalidadePaciente;
import br.com.sysdesc.pesquisa.repository.dao.impl.PesquisableDAOImpl;

public class MensalidadePacienteDAO extends PesquisableDAOImpl<MensalidadePaciente> {

    private static final long serialVersionUID = 1L;

    public MensalidadePacienteDAO() {
        super(mensalidadePaciente, mensalidadePaciente.codigoPaciente);
    }

    public List<MensalidadePaciente> obterMensalidadesDiaVencimento(Long diaMes, boolean isUltimoDiaMes) {

        JPAQuery query = from();

        query.where(getDiaMensalidade(diaMes, isUltimoDiaMes));

        return query.list(mensalidadePaciente);
    }

    private Predicate getDiaMensalidade(Long diaMes, boolean isUltimoDiaMes) {

        if (isUltimoDiaMes) {

            return new BooleanBuilder(mensalidadePaciente.diaVencimento.goe(diaMes));
        }

        return new BooleanBuilder(mensalidadePaciente.diaVencimento.eq(diaMes));
    }

    public List<MensalidadePaciente> buscarMensalidadePorTipoPagamento(Long codigo) {

        return from().where(mensalidadePaciente.formasPagamento.codigoFormaPagamento.eq(codigo)).list(mensalidadePaciente);
    }

    public List<MensalidadePaciente> buscarInconsistenciasBoletoPrimeiroDia() {

        return from().where(
                mensalidadePaciente.envioAutomaticoBoletos.eq(true).and(mensalidadePaciente.diasAntecedencia.gt(mensalidadePaciente.diaVencimento)))
                .list(mensalidadePaciente);
    }

    public List<MensalidadePaciente> buscarInconsistenciasBoletoUltimoDia() {

        return from().where(mensalidadePaciente.envioAutomaticoBoletos.eq(true)
                .and(mensalidadePaciente.diasAntecedencia.gt(mensalidadePaciente.diaVencimento.add(1L)))).list(mensalidadePaciente);
    }

    public List<MensalidadePaciente> buscarInconsistenciasDiaFixo(Long diaFixo) {

        NumberExpression<Long> operation = NumberOperation.create(Long.class, Ops.ADD,
                NumberOperation.create(Long.class, Ops.SUB, ConstantImpl.create(28L), mensalidadePaciente.diaVencimento),
                mensalidadePaciente.diasAntecedencia);

        return from().where(mensalidadePaciente.envioAutomaticoBoletos.eq(true).and(mensalidadePaciente.diasAntecedencia.gt(operation)))
                .list(mensalidadePaciente);
    }

    public List<MensalidadePaciente> buscarInconsistenciasBaseMensalidade(Long diaFixo) {
        return from().where(mensalidadePaciente.envioAutomaticoBoletos.eq(true).and(mensalidadePaciente.diasAntecedencia.gt(diaFixo)))
                .list(mensalidadePaciente);
    }

    public List<MensalidadePaciente> buscarMensalidadesPorBoleto() {

        return from().where(mensalidadePaciente.envioAutomaticoBoletos.eq(true)).list(mensalidadePaciente);
    }

}
