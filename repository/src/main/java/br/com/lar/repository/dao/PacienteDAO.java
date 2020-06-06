package br.com.lar.repository.dao;

import static br.com.lar.repository.model.QPaciente.paciente;

import com.mysema.query.BooleanBuilder;

import br.com.lar.repository.model.Paciente;
import br.com.sysdesc.pesquisa.repository.dao.impl.PesquisableDAOImpl;
import br.com.sysdesc.util.classes.LongUtil;

public class PacienteDAO extends PesquisableDAOImpl<Paciente> {

	private static final long serialVersionUID = 1L;

	public PacienteDAO() {
		super(paciente, paciente.idPaciente);
	}

	public Paciente buscarPacienteCadastrado(Long codigoCliente, Long idPaciente) {

		BooleanBuilder booleanBuilder = new BooleanBuilder(paciente.cliente.idCliente.eq(codigoCliente));

		if (!LongUtil.isNullOrZero(idPaciente)) {

			booleanBuilder.and(paciente.idPaciente.ne(idPaciente));
		}

		return from().where(booleanBuilder).singleResult(paciente);
	}

}
