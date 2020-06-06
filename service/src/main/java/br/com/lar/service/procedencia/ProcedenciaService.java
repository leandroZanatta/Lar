package br.com.lar.service.procedencia;

import java.util.List;

import br.com.lar.repository.dao.ProcedenciaDAO;
import br.com.lar.repository.model.Procedencia;
import br.com.sysdesc.pesquisa.service.impl.AbstractPesquisableServiceImpl;
import br.com.sysdesc.util.classes.StringUtil;
import br.com.sysdesc.util.constants.MensagemConstants;
import br.com.sysdesc.util.exception.SysDescException;

public class ProcedenciaService extends AbstractPesquisableServiceImpl<Procedencia> {

	private ProcedenciaDAO procedenciaDAO;

	public ProcedenciaService() {
		this(new ProcedenciaDAO());
	}

	public ProcedenciaService(ProcedenciaDAO procedenciaDAO) {
		super(procedenciaDAO, Procedencia::getIdProcedencia);

		this.procedenciaDAO = procedenciaDAO;
	}

	@Override
	public void validar(Procedencia objetoPersistir) {

		if (StringUtil.isNullOrEmpty(objetoPersistir.getDescricao())) {

			throw new SysDescException(MensagemConstants.MENSAGEM_INSIRA_DESCRICAO_VALIDA);
		}

	}

	public List<Procedencia> buscarTodos() {

		return procedenciaDAO.listar();
	}

}
