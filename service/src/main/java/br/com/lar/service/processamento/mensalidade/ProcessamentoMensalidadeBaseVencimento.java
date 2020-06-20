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

public class ProcessamentoMensalidadeBaseVencimento implements ProcessamentoMensalidade {

	private MensalidadePacienteDAO mensalidadePacienteDAO = new MensalidadePacienteDAO();

	private ProcessamentoMensalidadePaciente processamentoMensalidadePaciente = new ProcessamentoMensalidadePaciente();

	private ConfiguracaoMensalidadeVO configuracaoMensalidadeVO;

	public ProcessamentoMensalidadeBaseVencimento(ConfiguracaoMensalidadeVO configuracaoMensalidadeVO) {
		this.configuracaoMensalidadeVO = configuracaoMensalidadeVO;
	}

	public void processar(Date dataProcessar) {

		Date diaMensalidade = DateUtil.addDays(dataProcessar,
				configuracaoMensalidadeVO.getConfiguracaoContasReceber().getDiasGeracao());

		Integer diadoMes = DateUtil.getDayOfMonth(diaMensalidade);
		Integer ultimoDiadoMes = DateUtil.getLastDayOfMonth(diaMensalidade);
		boolean isUltimoDiaMes = diadoMes.equals(ultimoDiadoMes);

		List<MensalidadePaciente> mensalidadePacientes = mensalidadePacienteDAO.listar().stream()
				.filter(mensalidade -> verificarDiaMensalidade(mensalidade, diadoMes.longValue(), isUltimoDiaMes))
				.collect(Collectors.toList());

		Map<Date, List<MensalidadePaciente>> mapaMensalidades = mensalidadePacientes.stream()
				.collect(Collectors.groupingBy(mensalidade -> dataMensalidade(diaMensalidade, mensalidade)));

		if (!mapaMensalidades.isEmpty()) {

			for (Entry<Date, List<MensalidadePaciente>> entry : mapaMensalidades.entrySet()) {

				entry.getValue().forEach(mensalidade -> processamentoMensalidadePaciente
						.gerarMensalidade(entry.getKey(), mensalidade));
			}
		}
	}

	private boolean verificarDiaMensalidade(MensalidadePaciente mensalidade, Long diadoMes, boolean isUltimoDiaMes) {

		if (isUltimoDiaMes) {

			return mensalidade.getDiaVencimento() >= diadoMes;
		}

		return mensalidade.getDiaVencimento().equals(diadoMes);
	}

	private Date dataMensalidade(Date dataProcessar, MensalidadePaciente mensalidade) {

		Long ultimoDiaMes = DateUtil.getLastDayOfMonth(dataProcessar).longValue();
		Long diaVencimento = ultimoDiaMes < mensalidade.getDiaVencimento() ? ultimoDiaMes
				: mensalidade.getDiaVencimento();

		return DateUtil.setDay(dataProcessar, diaVencimento);
	}
}
