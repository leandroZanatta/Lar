package br.com.lar.service.processamento.mensalidade;

import static br.com.sysdesc.util.classes.DateUtil.FORMATO_MM_YYYYY;
import static br.com.sysdesc.util.classes.DateUtil.format;
import static br.com.sysdesc.util.classes.DateUtil.getDaysOfDates;
import static br.com.sysdesc.util.classes.DateUtil.getLastDayOfMonth;
import static br.com.sysdesc.util.classes.DateUtil.menorOuIgual;
import static br.com.sysdesc.util.classes.DateUtil.obterDiaUtil;
import static br.com.sysdesc.util.classes.DateUtil.setDay;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Date;

import br.com.lar.repository.dao.CobrancaMensalidadeDAO;
import br.com.lar.repository.model.Cliente;
import br.com.lar.repository.model.CobrancaMensalidade;
import br.com.lar.repository.model.ContasReceber;
import br.com.lar.repository.model.MensalidadePaciente;
import br.com.lar.repository.model.Paciente;
import br.com.lar.service.contasreceber.ContasReceberService;
import br.com.sysdesc.util.classes.DateUtil;
import br.com.sysdesc.util.vo.ValoresContasReceberVO;

public class ProcessamentoMensalidadePaciente {

	public static final String PROGRAMA_MENSALIDADE = "MENS";

	private ContasReceberService contasReceberService = new ContasReceberService();

	private CobrancaMensalidadeDAO cobrancaMensalidadeDAO = new CobrancaMensalidadeDAO();

	public void gerarMensalidade(Date dataVencimento, MensalidadePaciente mensalidade) {

		if (!isMensalidadePaga(mensalidade, dataVencimento)) {

			cobrarMensalidade(dataVencimento, mensalidade);
		}
	}

	private boolean isMensalidadePaga(MensalidadePaciente mensalidade, Date dataVencimento) {

		return DateUtil.format(DateUtil.FORMATO_MM_YYYYY, dataVencimento)
				.equals(DateUtil.format(DateUtil.FORMATO_MM_YYYYY, mensalidade.getPaciente().getDataAdmissao()));
	}

	private void cobrarMensalidade(Date dataVencimento, MensalidadePaciente mensalidade) {

		Date dataReferencia = DateUtil.addMonth(dataVencimento, -1L);

		Cliente cliente = obterClienteCobranca(mensalidade);

		String dataMensalidade = format(FORMATO_MM_YYYYY, dataReferencia);

		CobrancaMensalidade cobrancaMensalidade = cobrancaMensalidadeDAO.buscarCobrancaPeriodo(cliente.getIdCliente(), dataMensalidade);

		if (cobrancaMensalidade == null) {

			BigDecimal valorMensalidade = calcularValorMensalidade(dataReferencia, mensalidade);

			Date diaUtil = obterDiaUtil(dataVencimento);

			ValoresContasReceberVO valores = new ValoresContasReceberVO(valorMensalidade);

			ContasReceber contasReceber = contasReceberService.criarContasReceber(cliente, mensalidade.getFormasPagamento(), diaUtil, valores,
					PROGRAMA_MENSALIDADE);

			Date dataCadastro = new Date();

			cobrancaMensalidade = new CobrancaMensalidade();
			cobrancaMensalidade.setCliente(cliente);
			cobrancaMensalidade.setContasReceber(contasReceber);
			cobrancaMensalidade.setReferencia(dataMensalidade);
			cobrancaMensalidade.setValorMensalidade(valorMensalidade);
			cobrancaMensalidade.setDataCadastro(dataCadastro);
			cobrancaMensalidade.setDataManutencao(dataCadastro);

			cobrancaMensalidadeDAO.salvar(cobrancaMensalidade);

		}
	}

	private Cliente obterClienteCobranca(MensalidadePaciente mensalidade) {

		Paciente paciente = mensalidade.getPaciente();

		return paciente.getResponsavel() == null ? paciente.getCliente() : paciente.getResponsavel();
	}

	private BigDecimal calcularValorMensalidade(Date dataReferencia, MensalidadePaciente mensalidade) {

		Date dataAdmissao = mensalidade.getPaciente().getDataAdmissao();

		if (menorOuIgual(DateUtil.setDay(dataReferencia, 1L), dataAdmissao)) {

			return this.calcularValorMensalidadeProporcionalDiaVencimento(dataAdmissao, mensalidade.getValorMensalidade());
		}

		return mensalidade.getValorMensalidade();
	}

	private BigDecimal calcularValorMensalidadeProporcionalDiaVencimento(Date dataAdmissao, BigDecimal valorMensalidade) {

		Integer ultimoDiaMes = getLastDayOfMonth(dataAdmissao);
		Date datUltimoDiaMes = setDay(dataAdmissao, ultimoDiaMes.longValue());

		BigDecimal diasMes = BigDecimal.valueOf(ultimoDiaMes);
		BigDecimal diasAteFinalMes = BigDecimal.valueOf(getDaysOfDates(dataAdmissao, datUltimoDiaMes) + 1L);

		return valorMensalidade.divide(diasMes, 12, RoundingMode.HALF_EVEN).multiply(diasAteFinalMes, MathContext.DECIMAL64).setScale(2,
				RoundingMode.HALF_EVEN);

	}

}
