package br.com.lar.service.Util;

import br.com.lar.repository.model.Cliente;
import br.com.lar.repository.model.FormasPagamento;
import br.com.lar.repository.model.MensalidadePaciente;
import br.com.lar.repository.model.Paciente;
import br.com.sysdesc.boletos.repository.model.Banco;
import br.com.sysdesc.boletos.repository.model.ConfiguracaoBoleto;
import br.com.sysdesc.util.vo.ConfiguracaoBoletoVO;
import br.com.sysdesc.util.vo.ConfiguracaoContasReceberVO;
import br.com.sysdesc.util.vo.ConfiguracaoDiarioVO;
import br.com.sysdesc.util.vo.ConfiguracaoMensalidadeVO;

public class ResourcesTest {

	public static final ConfiguracaoMensalidadeVO F1D5BOLETOS = configuracaoMensalidade(1l, 5l, true, true);
	public static final ConfiguracaoMensalidadeVO F25D5BOLETOS = configuracaoMensalidade(25l, 5l, true, true);
	public static final ConfiguracaoMensalidadeVO F1D20BOLETOS = configuracaoMensalidade(1l, 20l, true, true);
	public static final ConfiguracaoMensalidadeVO F1D15BOLETOS = configuracaoMensalidade(1l, 15l, true, true);

	private static ConfiguracaoMensalidadeVO configuracaoMensalidade(Long formaContasReceber, Long diasGeracao, boolean gerarboletos,
			boolean enviarEmail) {

		ConfiguracaoMensalidadeVO config = new ConfiguracaoMensalidadeVO();
		config.setConfiguracaoBoletoVO(getConfiguracaoBoleto(gerarboletos, enviarEmail));
		config.setConfiguracaoContasReceber(getConfiguracaoContasReceber(formaContasReceber, diasGeracao));
		config.setConfiguracaoDiario(getConfiguracaoDiario());

		return config;
	}

	public static MensalidadePaciente createMensalidade(Paciente paciente, FormasPagamento formasPagamento, Long vencimento, Long antecedencia,
			boolean envio, boolean agrupar) {
		MensalidadePaciente mensalidade = new MensalidadePaciente();

		mensalidade.setFormasPagamento(formasPagamento);
		mensalidade.setDiaVencimento(vencimento);
		mensalidade.setDiasAntecedencia(antecedencia);
		mensalidade.setPaciente(paciente);
		mensalidade.setEnvioAutomaticoBoletos(envio);
		mensalidade.setAgruparContas(agrupar);

		return mensalidade;
	}

	public static ConfiguracaoBoleto createConfiguracaoBoleto(String agencia, String conta, Long carteira) {
		ConfiguracaoBoleto configuracaoBoleto = new ConfiguracaoBoleto();
		configuracaoBoleto.setNossoNumero(1L);
		configuracaoBoleto.setNumeroAgencia(agencia);
		configuracaoBoleto.setNumeroConta(conta);
		configuracaoBoleto.setCodigoCarteira(carteira);
		configuracaoBoleto.setCgc("064.175.649-67");
		configuracaoBoleto.setNome("Leandro Zanatta");

		return configuracaoBoleto;
	}

	public static FormasPagamento createFormaPagamento(String nome, Banco banco) {

		FormasPagamento formasPagamento = new FormasPagamento();
		formasPagamento.setBanco(banco);
		formasPagamento.setDescricao(nome);
		return formasPagamento;
	}

	public static Banco createBanco(Long numeroBanco, ConfiguracaoBoleto configuracaoBoleto) {
		Banco banco = new Banco();
		banco.setConfiguracaoBoleto(configuracaoBoleto);
		banco.setNumeroBanco(numeroBanco);
		return banco;
	}

	public static Paciente createPaciente(Cliente cliente, Cliente responsavel) {
		Paciente paciente = new Paciente();

		paciente.setCliente(cliente);
		paciente.setResponsavel(responsavel);

		return paciente;
	}

	public static Cliente createCliente(Long id, String nome) {
		Cliente cliente = new Cliente();

		cliente.setIdCliente(id);
		cliente.setNome(nome);

		return cliente;
	}

	public static ConfiguracaoBoletoVO getConfiguracaoBoleto(boolean gerarboletos, boolean enviarEmail) {

		return new ConfiguracaoBoletoVO(gerarboletos, enviarEmail);
	}

	public static ConfiguracaoContasReceberVO getConfiguracaoContasReceber(Long formaContasReceber, Long diasGeracao) {

		return new ConfiguracaoContasReceberVO(formaContasReceber, diasGeracao);
	}

	private static ConfiguracaoDiarioVO getConfiguracaoDiario() {

		return new ConfiguracaoDiarioVO();
	}

}
