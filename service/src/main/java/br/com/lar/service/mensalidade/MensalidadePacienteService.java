package br.com.lar.service.mensalidade;

import java.util.List;

import br.com.lar.repository.dao.MensalidadePacienteDAO;
import br.com.lar.repository.model.MensalidadePaciente;
import br.com.sysdesc.pesquisa.service.impl.AbstractPesquisableServiceImpl;
import br.com.sysdesc.util.classes.BigDecimalUtil;
import br.com.sysdesc.util.classes.LongUtil;
import br.com.sysdesc.util.constants.MensagemConstants;
import br.com.sysdesc.util.enumeradores.FormaPagamentoEnum;
import br.com.sysdesc.util.enumeradores.TipoContasReceberEnum;
import br.com.sysdesc.util.exception.SysDescException;
import br.com.sysdesc.util.vo.ConfiguracaoMensalidadeVO;

public class MensalidadePacienteService extends AbstractPesquisableServiceImpl<MensalidadePaciente> {

    private MensalidadePacienteDAO mensalidadePacienteDAO;

    public MensalidadePacienteService() {
        this(new MensalidadePacienteDAO());
    }

    public MensalidadePacienteService(MensalidadePacienteDAO mensalidadePacienteDAO) {
        super(mensalidadePacienteDAO, MensalidadePaciente::getCodigoPaciente);

        this.mensalidadePacienteDAO = mensalidadePacienteDAO;
    }

    public void validar(MensalidadePaciente objetoPersistir, ConfiguracaoMensalidadeVO configuracaoMensalidadeVO) {

        if (objetoPersistir.getPaciente() == null) {

            throw new SysDescException(MensagemConstants.MENSAGEM_SELECIONE_PACIENTE);
        }

        if (objetoPersistir.getFormasPagamento() == null) {

            throw new SysDescException(MensagemConstants.MENSAGEM_SELECIONE_FORMA_PAGAMENTO);
        }

        if (BigDecimalUtil.isNullOrZero(objetoPersistir.getValorMensalidade())) {

            throw new SysDescException(MensagemConstants.MENSAGEM_INSIRA_VALOR_MENSALIDADE);
        }

        FormaPagamentoEnum formaPagamento = FormaPagamentoEnum.forCodigo(objetoPersistir.getFormasPagamento().getCodigoFormaPagamento());

        if (FormaPagamentoEnum.BOLETO.equals(formaPagamento) && objetoPersistir.isEnvioAutomaticoBoletos()) {

            if (configuracaoMensalidadeVO == null) {
                throw new SysDescException(MensagemConstants.MENSAGEM_CONFIGURACAO_BOLETO_INEXISTENTE);
            }

            TipoContasReceberEnum tipoContasReceber = TipoContasReceberEnum
                    .findByCodigo(configuracaoMensalidadeVO.getConfiguracaoContasReceber().getFormaContasReceber());

            validarDiasGeracaoBoleto(tipoContasReceber, configuracaoMensalidadeVO.getConfiguracaoContasReceber().getDiasGeracao(), objetoPersistir);

            if (LongUtil.isNullOrZero(objetoPersistir.getDiasAntecedencia())) {

                throw new SysDescException(MensagemConstants.MENSAGEM_INSIRA_DIAS_ANTECEDENCIA);
            }
        }
        if (LongUtil.isNullOrZero(objetoPersistir.getDiaVencimento())) {

            throw new SysDescException(MensagemConstants.MENSAGEM_INSIRA_DIA_VENCIMENTO);
        }
    }

    private void validarDiasGeracaoBoleto(TipoContasReceberEnum tipoContasReceber, Long diasGeracao, MensalidadePaciente objetoPersistir) {

        switch (tipoContasReceber) {
            case PRIMEIRO_DIA:
                if (LongUtil.maior(objetoPersistir.getDiasAntecedencia(), objetoPersistir.getDiaVencimento())) {

                    throw new SysDescException(MensagemConstants.MENSAGEM_VALIDACAO_DIAS_ANTECEDENCIA, objetoPersistir.getDiaVencimento());
                }
                break;

            case ULTIMO_DIA:
                if (LongUtil.maior(objetoPersistir.getDiasAntecedencia(), objetoPersistir.getDiaVencimento() + 1L)) {

                    throw new SysDescException(MensagemConstants.MENSAGEM_VALIDACAO_DIAS_ANTECEDENCIA, objetoPersistir.getDiaVencimento() + 1L);
                }
                break;

            case DIA_FIXO:
                if (LongUtil.maior(objetoPersistir.getDiasAntecedencia(), 28L - objetoPersistir.getDiaVencimento() + diasGeracao)) {

                    throw new SysDescException(MensagemConstants.MENSAGEM_VALIDACAO_DIAS_ANTECEDENCIA,
                            (28L - objetoPersistir.getDiaVencimento() + diasGeracao));
                }
                break;

            case BASE_MENSALIDADE:
                if (LongUtil.maior(objetoPersistir.getDiasAntecedencia(), diasGeracao)) {

                    throw new SysDescException(MensagemConstants.MENSAGEM_VALIDACAO_DIAS_ANTECEDENCIA, diasGeracao);
                }
                break;

            default:
                throw new SysDescException(MensagemConstants.MENSAGEM_CONFIGURACOES_INVALIDAS);
        }
    }

    public List<MensalidadePaciente> buscarInconsistenciasEnvioBoletos(TipoContasReceberEnum tipoContasReceber, Long diaFixo) {

        switch (tipoContasReceber) {
            case PRIMEIRO_DIA:
                return mensalidadePacienteDAO.buscarInconsistenciasBoletoPrimeiroDia();
            case ULTIMO_DIA:
                return mensalidadePacienteDAO.buscarInconsistenciasBoletoUltimoDia();
            case DIA_FIXO:
                return mensalidadePacienteDAO.buscarInconsistenciasDiaFixo(diaFixo);
            case BASE_MENSALIDADE:
                return mensalidadePacienteDAO.buscarInconsistenciasBaseMensalidade(diaFixo);
            default:
                throw new SysDescException(MensagemConstants.MENSAGEM_CONFIGURACOES_INVALIDAS);
        }
    }

    public void atualizarInconsistencias(TipoContasReceberEnum tipoContasReceber, Long diasGeracao, List<MensalidadePaciente> inconsistencias) {

        switch (tipoContasReceber) {
            case PRIMEIRO_DIA:
                inconsistencias.forEach(mensalidade -> mensalidade.setDiasAntecedencia(mensalidade.getDiaVencimento()));
                break;

            case ULTIMO_DIA:
                inconsistencias.forEach(mensalidade -> mensalidade.setDiasAntecedencia(mensalidade.getDiaVencimento() + 1L));
                break;

            case DIA_FIXO:
                inconsistencias.forEach(mensalidade -> mensalidade.setDiasAntecedencia(28L - mensalidade.getDiaVencimento() + diasGeracao));
                break;

            case BASE_MENSALIDADE:
                inconsistencias.forEach(mensalidade -> mensalidade.setDiasAntecedencia(diasGeracao));
                break;

            default:
                throw new SysDescException(MensagemConstants.MENSAGEM_CONFIGURACOES_INVALIDAS);
        }

        mensalidadePacienteDAO.salvar(inconsistencias);

    }

    public List<MensalidadePaciente> buscarMensalidadesPorBoleto() {

        return mensalidadePacienteDAO.buscarMensalidadesPorBoleto();
    }

}
