package br.com.lar.service.processamento;

import static br.com.sysdesc.util.classes.DateUtil.addDays;
import static br.com.sysdesc.util.classes.DateUtil.getInitialDate;
import static br.com.sysdesc.util.classes.DateUtil.menorOuIgual;

import java.util.Date;

import com.google.gson.Gson;

import br.com.lar.repository.dao.ParametrosDAO;
import br.com.lar.repository.dao.ProcessamentoDiarioDAO;
import br.com.lar.repository.model.Parametros;
import br.com.lar.repository.model.ProcessamentoDiario;
import br.com.lar.service.email.EmailService;
import br.com.lar.service.factory.ProcessamentoMensalidadeFactory;
import br.com.sysdesc.boletos.service.remessa.RemessaService;
import br.com.sysdesc.util.classes.CryptoUtil;
import br.com.sysdesc.util.constants.MensagemConstants;
import br.com.sysdesc.util.enumeradores.ProcessamentoDiarioEnum;
import br.com.sysdesc.util.exception.SysDescException;
import br.com.sysdesc.util.vo.ConfiguracaoMensalidadeVO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ProcessamentoDiarioService {

	private ProcessamentoDiarioDAO processamentoDiarioDAO = new ProcessamentoDiarioDAO();

	private ProcessamentoMensalidadeFactory processamentoMensalidadeFactory = new ProcessamentoMensalidadeFactory();

	private ProcessamentoBoletoMensalidade processamentoBoletoMensalidade = new ProcessamentoBoletoMensalidade();

	private RemessaService remessaService = new RemessaService();

	private ParametrosDAO parametrosDAO = new ParametrosDAO();

	private EmailService emailService = new EmailService();

	public void iniciarProcessamento() {

		Parametros parametros = parametrosDAO.first();

		if (parametros == null) {
			throw new SysDescException(MensagemConstants.MENSAGEM_CONFIGURAR_PARAMETROS);
		}

		try {
			processamentoDiarioDAO.atualizarProcessamentosAnteriores();

			this.executarProcessoDiario(parametros);

			this.executarProcessosAutomaticos();

		} catch (SysDescException e) {

			log.error("Erro ao executar o processamento diário", e);
		}
	}

	private void executarProcessosAutomaticos() {

		remessaService.processarRemessaBancosSuportados();

		emailService.enviarEmails();
	}

	private void executarProcessoDiario(Parametros parametros) {

		ConfiguracaoMensalidadeVO configuracaoMensalidadeVO = new Gson().fromJson(CryptoUtil.fromBlowfish(parametros.getConfigMensalidade()),
				ConfiguracaoMensalidadeVO.class);

		Date dataProcessar = this.buscarDataProcesar();

		while (menorOuIgual(dataProcessar, getInitialDate(new Date()))) {

			this.processarData(dataProcessar, configuracaoMensalidadeVO);

			dataProcessar = addDays(dataProcessar, 1L);
		}
	}

	private void processarData(Date dataProcessar, ConfiguracaoMensalidadeVO configuracaoMensalidadeVO) {

		ProcessamentoDiario processamentoDiario = registrarInicioProcessamento(dataProcessar);

		try {

			processamentoMensalidadeFactory.getProcessamento(configuracaoMensalidadeVO).processar(dataProcessar);

			processamentoBoletoMensalidade.gerarBoleto(dataProcessar, configuracaoMensalidadeVO);

			registrarProcessamento(processamentoDiario, ProcessamentoDiarioEnum.CONCLUIDO);

		} catch (Exception e) {

			log.error("Erro ao executar processamento diário", e);

			registrarProcessamento(processamentoDiario, ProcessamentoDiarioEnum.ERRO);

			throw new SysDescException(MensagemConstants.MENSAGEM_ERRO_PROCESSAMENTO_DIARIO);
		}
	}

	private ProcessamentoDiario registrarInicioProcessamento(Date dataProcessar) {

		ProcessamentoDiario processamentoDiario = new ProcessamentoDiario();
		processamentoDiario.setDataProcessamento(dataProcessar);
		processamentoDiario.setDataInicio(new Date());
		processamentoDiario.setCodigoStatus(ProcessamentoDiarioEnum.INICIANDO.getCodigo());

		processamentoDiarioDAO.salvar(processamentoDiario);

		return processamentoDiario;
	}

	private void registrarProcessamento(ProcessamentoDiario processamentoDiario, ProcessamentoDiarioEnum status) {

		processamentoDiario.setCodigoStatus(status.getCodigo());
		processamentoDiario.setDataFim(new Date());

		processamentoDiarioDAO.salvar(processamentoDiario);
	}

	private Date buscarDataProcesar() {

		Date ultimaDataProcessada = processamentoDiarioDAO.buscarUltimaDataProcesada();

		if (ultimaDataProcessada != null) {
			return addDays(ultimaDataProcessada, 1L);
		}

		return getInitialDate(new Date());
	}

}
