package br.com.lar.service.processamento.mensalidade;

import static br.com.lar.service.Util.ResourcesTest.F1D20BOLETOS;
import static br.com.lar.service.Util.ResourcesTest.createCliente;
import static br.com.lar.service.Util.ResourcesTest.createFormaPagamento;
import static br.com.lar.service.Util.ResourcesTest.createMensalidade;
import static br.com.lar.service.Util.ResourcesTest.createPaciente;
import static br.com.sysdesc.util.classes.DateUtil.FORMATO_DD_MM_YYY;
import static br.com.sysdesc.util.classes.DateUtil.addDays;
import static br.com.sysdesc.util.classes.DateUtil.format;
import static br.com.sysdesc.util.classes.DateUtil.getDayOfMonth;
import static br.com.sysdesc.util.classes.DateUtil.getLastDayOfMonth;
import static br.com.sysdesc.util.classes.DateUtil.parse;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import br.com.lar.repository.dao.MensalidadePacienteDAO;
import br.com.lar.repository.model.MensalidadePaciente;
import br.com.sysdesc.util.classes.DateUtil;
import br.com.sysdesc.util.vo.ConfiguracaoMensalidadeVO;

@RunWith(MockitoJUnitRunner.class)
public class ProcessamentoMensalidadeBaseVencimentoTest {

	@Spy
	@InjectMocks
	private ProcessamentoMensalidadeBaseVencimento processamentoMensalidadePrimeiroDia = new ProcessamentoMensalidadeBaseVencimento(
			F1D20BOLETOS);

	@Mock
	private MensalidadePacienteDAO mensalidadePacienteDAO;

	@Mock
	private ProcessamentoMensalidadePaciente processamentoMensalidadePaciente;

	@Test()
	public void testarProcessamentoDiarioSemPacientes() {

		when(mensalidadePacienteDAO.listar()).thenReturn(new ArrayList<>());

		Date dataInicial = parse(FORMATO_DD_MM_YYY, "01/01/2019");
		Date dataFinal = parse(FORMATO_DD_MM_YYY, "31/12/2019");

		while (DateUtil.menorOuIgual(dataInicial, dataFinal)) {

			processamentoMensalidadePrimeiroDia.processar(dataInicial);

			dataInicial = addDays(dataInicial, 1L);
		}

		verify(processamentoMensalidadePaciente, times(0)).gerarMensalidade(any(ConfiguracaoMensalidadeVO.class),
				any(Date.class), any(MensalidadePaciente.class));
	}

	@Test()
	public void testarProcessamentoDiarioComPacientes() {

		Long dia = F1D20BOLETOS.getConfiguracaoContasReceber().getDiasGeracao();

		MensalidadePaciente mensalidadePaciente = createMensalidade(createPaciente(createCliente(1L, "João"), null),
				createFormaPagamento("dinheiro", null), 31L, 10L, false, false);

		when(mensalidadePacienteDAO.listar()).thenReturn(Arrays.asList(mensalidadePaciente));

		Date dataInicial = parse(FORMATO_DD_MM_YYY, "01/01/2019");
		Date dataFinal = parse(FORMATO_DD_MM_YYY, "31/12/2019");

		while (DateUtil.menorOuIgual(dataInicial, dataFinal)) {

			processamentoMensalidadePrimeiroDia.processar(dataInicial);

			dataInicial = addDays(dataInicial, 1L);
		}

		System.out.println("Testando dia:" + dia);

		ArgumentCaptor<Date> dataGeracao = ArgumentCaptor.forClass(Date.class);

		verify(processamentoMensalidadePaciente, times(12)).gerarMensalidade(any(ConfiguracaoMensalidadeVO.class),
				dataGeracao.capture(), any(MensalidadePaciente.class));
		assertEquals(dataGeracao.getAllValues().size(), 12);

		List<String> datas = dataGeracao.getAllValues().stream().map(data -> format(FORMATO_DD_MM_YYY, data))
				.collect(Collectors.toList());

		System.out.println("Mensalidades: " + datas);

		for (Date data : dataGeracao.getAllValues()) {

			assertEquals(getLastDayOfMonth(data), getDayOfMonth(data));
		}
	}

	@Test()
	public void testarProcessamentoDiarioComPacientesDiasGeracao() {

		MensalidadePaciente mensalidadePaciente = createMensalidade(createPaciente(createCliente(1L, "João"), null),
				createFormaPagamento("dinheiro", null), 20L, 10L, false, false);

		when(mensalidadePacienteDAO.listar()).thenReturn(Arrays.asList(mensalidadePaciente));

		List<Date> datas = Arrays.asList(parseDate("31/12/2019"), parseDate("31/01/2020"), parseDate("29/02/2020"),
				parseDate("31/03/2020"), parseDate("30/04/2020"), parseDate("31/05/2020"), parseDate("30/06/2020"),
				parseDate("31/07/2020"), parseDate("31/08/2020"), parseDate("30/09/2020"), parseDate("31/10/2020"),
				parseDate("30/11/2020"));

		datas.forEach(data -> processamentoMensalidadePrimeiroDia.processar(data));

		verify(processamentoMensalidadePaciente, times(12)).gerarMensalidade(any(ConfiguracaoMensalidadeVO.class),
				any(Date.class), any(MensalidadePaciente.class));
	}

	private Date parseDate(String date) {

		return parse(FORMATO_DD_MM_YYY, date);
	}
}
