package br.com.lar.service.processamento.mensalidade;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import br.com.lar.repository.dao.MensalidadePacienteDAO;
import br.com.lar.repository.model.MensalidadePaciente;
import br.com.lar.service.interfaces.ProcessamentoMensalidade;
import br.com.sysdesc.util.classes.DateUtil;
import br.com.sysdesc.util.vo.ConfiguracaoMensalidadeVO;

public class ProcessamentoMensalidadeDiaFixo implements ProcessamentoMensalidade {

    private MensalidadePacienteDAO mensalidadePacienteDAO = new MensalidadePacienteDAO();

    private ProcessamentoMensalidadePaciente processamentoMensalidadePaciente = new ProcessamentoMensalidadePaciente();

    private ConfiguracaoMensalidadeVO configuracaoMensalidadeVO;

    public ProcessamentoMensalidadeDiaFixo(ConfiguracaoMensalidadeVO configuracaoMensalidadeVO) {
        this.configuracaoMensalidadeVO = configuracaoMensalidadeVO;
    }

    public void processar(Date dataProcessar) {

        if (DateUtil.isDayOrLastDayOfMonth(dataProcessar, configuracaoMensalidadeVO.getConfiguracaoContasReceber().getDiasGeracao())) {

            List<MensalidadePaciente> mensalidadePacientes = mensalidadePacienteDAO.listar();

            Map<Date, List<MensalidadePaciente>> mapaMensalidades = mensalidadePacientes.stream()
                    .collect(Collectors.groupingBy(mensalidade -> dataMensalidade(dataProcessar, mensalidade)));

            if (!mapaMensalidades.isEmpty()) {

                for (Entry<Date, List<MensalidadePaciente>> entry : mapaMensalidades.entrySet()) {

                    entry.getValue().forEach(
                            mensalidade -> processamentoMensalidadePaciente.gerarMensalidade(configuracaoMensalidadeVO, entry.getKey(), mensalidade));
                }
            }
        }
    }

    private Date dataMensalidade(Date dataProcessar, MensalidadePaciente mensalidade) {

        Long ultimoDiaMes = DateUtil.getLastDayOfMonth(dataProcessar).longValue();
        Long diaVencimento = ultimoDiaMes < mensalidade.getDiaVencimento() ? ultimoDiaMes : mensalidade.getDiaVencimento();

        return DateUtil.setDay(dataProcessar, diaVencimento);

    }
}
