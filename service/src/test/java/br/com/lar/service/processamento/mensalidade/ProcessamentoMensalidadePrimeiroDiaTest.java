package br.com.lar.service.processamento.mensalidade;

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

@RunWith(MockitoJUnitRunner.class)
public class ProcessamentoMensalidadePrimeiroDiaTest {

	@Spy
	@InjectMocks
	private ProcessamentoMensalidadePrimeiroDia processamentoMensalidadePrimeiroDia = new ProcessamentoMensalidadePrimeiroDia();

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

		verify(mensalidadePacienteDAO, times(12)).listar();
		verify(processamentoMensalidadePaciente, times(0)).gerarMensalidade(
				any(Date.class), any(MensalidadePaciente.class));
	}

	@Test()
	public void testarProcessamentoDiarioComPacientes() {

		Long dia = 31L;

		MensalidadePaciente mensalidadePaciente = createMensalidade(createPaciente(createCliente(1L, "João"), null),
				createFormaPagamento("dinheiro", null), dia, 10L, false, false);

		when(mensalidadePacienteDAO.listar()).thenReturn(Arrays.asList(mensalidadePaciente));

		Date dataInicial = parse(FORMATO_DD_MM_YYY, "01/01/2019");
		Date dataFinal = parse(FORMATO_DD_MM_YYY, "31/12/2019");

		while (DateUtil.menorOuIgual(dataInicial, dataFinal)) {

			processamentoMensalidadePrimeiroDia.processar(dataInicial);

			dataInicial = addDays(dataInicial, 1L);
		}

		System.out.println("Testando dia:" + dia);

		ArgumentCaptor<Date> dataGeracao = ArgumentCaptor.forClass(Date.class);

		verify(mensalidadePacienteDAO, times(12)).listar();

		verify(processamentoMensalidadePaciente, times(12)).gerarMensalidade(
				dataGeracao.capture(), any(MensalidadePaciente.class));
		assertEquals(12, dataGeracao.getAllValues().size());

		List<String> datas = dataGeracao.getAllValues().stream().map(data -> format(FORMATO_DD_MM_YYY, data))
				.collect(Collectors.toList());

		System.out.println("Mensalidades: " + datas);

		for (Date data : dataGeracao.getAllValues()) {

			Integer diaMesAtual = dia.intValue() > getLastDayOfMonth(data) ? getLastDayOfMonth(data) : dia.intValue();

			assertEquals(diaMesAtual, getDayOfMonth(data));

		}
	}

}
