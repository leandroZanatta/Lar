package br.com.lar.service.paciente;

import br.com.lar.repository.dao.PacienteDAO;
import br.com.lar.repository.model.Paciente;
import br.com.sysdesc.pesquisa.service.impl.AbstractPesquisableServiceImpl;
import br.com.sysdesc.util.classes.LongUtil;
import br.com.sysdesc.util.classes.StringUtil;
import br.com.sysdesc.util.constants.MensagemConstants;
import br.com.sysdesc.util.exception.SysDescException;

public class PacienteService extends AbstractPesquisableServiceImpl<Paciente> {

	private PacienteDAO pacienteDAO;

	public PacienteService() {
		this(new PacienteDAO());
	}

	public PacienteService(PacienteDAO pacienteDAO) {
		super(pacienteDAO, Paciente::getIdPaciente);
		this.pacienteDAO = pacienteDAO;
	}

	@Override
	public void validar(Paciente objetoPersistir) {

		if (objetoPersistir.getCliente() == null) {

			throw new SysDescException(MensagemConstants.MENSAGEM_SELECIONE_CLIENTE);
		}

		if (objetoPersistir.getDataAdmissao() == null) {

			throw new SysDescException(MensagemConstants.MENSAGEM_INSIRA_DATA_ADMISSAO);
		}

		if (objetoPersistir.getProcedencia() == null) {

			throw new SysDescException(MensagemConstants.MENSAGEM_SELECIONE_PROCEDENCIA);

		}

		if (LongUtil.isNullOrZero(objetoPersistir.getNumeroCartaoSUS())) {

			throw new SysDescException(MensagemConstants.MENSAGEM_INSIRA_CARTAO_SUS);
		}

		if (StringUtil.isNullOrEmptyTrim(objetoPersistir.getOcupacaoINSS())) {

			throw new SysDescException(MensagemConstants.MENSAGEM_INSIRA_OCUPACAO_INSS);
		}

		Paciente paciente = pacienteDAO.buscarPacienteCadastrado(objetoPersistir.getCliente().getIdCliente(),
				objetoPersistir.getIdPaciente());

		if (paciente != null) {

			throw new SysDescException(MensagemConstants.MENSAGEM_PACIENTE_CADASTRADO);
		}
	}
}
