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

public class ProcessamentoMensalidadePrimeiroDia implements ProcessamentoMensalidade {

	private MensalidadePacienteDAO mensalidadePacienteDAO = new MensalidadePacienteDAO();

	private ProcessamentoMensalidadePaciente processamentoMensalidadePaciente = new ProcessamentoMensalidadePaciente();

	public void processar(Date dataProcessar) {

		if (DateUtil.isFirstDayOfMonth(dataProcessar)) {

			List<MensalidadePaciente> mensalidadePacientes = mensalidadePacienteDAO.listar();

			Map<Date, List<MensalidadePaciente>> mensalidades = mensalidadePacientes.stream()
					.collect(Collectors.groupingBy(mensalidade -> dataMensalidade(dataProcessar, mensalidade)));

			if (!mensalidades.isEmpty()) {

				for (Entry<Date, List<MensalidadePaciente>> entry : mensalidades.entrySet()) {

					entry.getValue().forEach(mensalidade -> processamentoMensalidadePaciente
							.gerarMensalidade(entry.getKey(), mensalidade));
				}
			}

		}

	}

	private Date dataMensalidade(Date dataProcessar, MensalidadePaciente mensalidade) {

		Long ultimoDiaMes = DateUtil.getLastDayOfMonth(dataProcessar).longValue();
		Long diaVencimento = ultimoDiaMes < mensalidade.getDiaVencimento() ? ultimoDiaMes
				: mensalidade.getDiaVencimento();

		return DateUtil.setDay(dataProcessar, diaVencimento);
	}
}
