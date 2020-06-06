package br.com.lar.service.historicoevolucoes;

import java.util.Date;

import br.com.lar.repository.dao.HistorioEvolucoesDAO;
import br.com.lar.repository.model.HistoricoEvolucao;
import br.com.sysdesc.pesquisa.service.impl.AbstractPesquisableServiceImpl;
import br.com.sysdesc.util.classes.LongUtil;
import br.com.sysdesc.util.classes.StringUtil;
import br.com.sysdesc.util.constants.MensagemConstants;
import br.com.sysdesc.util.exception.SysDescException;

public class HistoricoEvolucoesService extends AbstractPesquisableServiceImpl<HistoricoEvolucao> {

	public HistoricoEvolucoesService() {
		super(new HistorioEvolucoesDAO(), HistoricoEvolucao::getIdHistorico);
	}

	@Override
	public void validar(HistoricoEvolucao objetoPersistir) {

		if (StringUtil.isNullOrEmptyTrim(objetoPersistir.getDescricao())) {

			throw new SysDescException(MensagemConstants.MENSAGEM_INSIRA_DESCRICAO_VALIDA);
		}

		if (StringUtil.isNullOrEmptyTrim(objetoPersistir.getHistorico())) {

			throw new SysDescException(MensagemConstants.MENSAGEM_INSIRA_HISTORICO_VALIDO);
		}
	}

	@Override
	public void salvar(HistoricoEvolucao objetoPersistir) {

		if (LongUtil.isNullOrZero(objetoPersistir.getIdHistorico())) {
			objetoPersistir.setDataCadastro(new Date());
		}

		super.salvar(objetoPersistir);
	}
}
