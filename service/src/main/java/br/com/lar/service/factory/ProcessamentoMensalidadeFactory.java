package br.com.lar.service.factory;

import br.com.lar.service.interfaces.ProcessamentoMensalidade;
import br.com.lar.service.processamento.mensalidade.ProcessamentoMensalidadeBaseVencimento;
import br.com.lar.service.processamento.mensalidade.ProcessamentoMensalidadeDiaFixo;
import br.com.lar.service.processamento.mensalidade.ProcessamentoMensalidadePrimeiroDia;
import br.com.lar.service.processamento.mensalidade.ProcessamentoMensalidadeUltimoDia;
import br.com.sysdesc.util.constants.MensagemConstants;
import br.com.sysdesc.util.enumeradores.TipoContasReceberEnum;
import br.com.sysdesc.util.exception.SysDescException;
import br.com.sysdesc.util.vo.ConfiguracaoContasReceberVO;
import br.com.sysdesc.util.vo.ConfiguracaoMensalidadeVO;

public class ProcessamentoMensalidadeFactory {

	public ProcessamentoMensalidade getProcessamento(ConfiguracaoMensalidadeVO configuracaoMensalidadeVO) {

		ConfiguracaoContasReceberVO configuracao = configuracaoMensalidadeVO.getConfiguracaoContasReceber();

		switch (TipoContasReceberEnum.findByCodigo(configuracao.getFormaContasReceber())) {
		case DIA_FIXO:
			return new ProcessamentoMensalidadeDiaFixo(configuracaoMensalidadeVO);

		case PRIMEIRO_DIA:
			return new ProcessamentoMensalidadePrimeiroDia();

		case ULTIMO_DIA:
			return new ProcessamentoMensalidadeUltimoDia();
		case BASE_MENSALIDADE:
			return new ProcessamentoMensalidadeBaseVencimento(configuracaoMensalidadeVO);

		default:
			throw new SysDescException(MensagemConstants.MENSAGEM_CONFIGURACOES_INVALIDAS);
		}

	}

}
