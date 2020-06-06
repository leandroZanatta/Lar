package br.com.lar.server.service.impl;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import br.com.lar.server.dto.ParametrosEvolucaoDTO;
import br.com.lar.server.repository.EvolucaoDetalheRepository;
import br.com.lar.server.repository.EvolucaoPacienteRepository;
import br.com.lar.server.repository.domain.EvolucaoDetalhe;
import br.com.lar.server.repository.domain.EvolucaoPaciente;
import br.com.lar.server.service.EvolucaoService;
import br.com.sysdesc.util.classes.DateUtil;
import br.com.sysdesc.util.classes.LongUtil;
import br.com.sysdesc.util.classes.StringUtil;
import br.com.sysdesc.util.constants.MensagemConstants;
import br.com.sysdesc.util.exception.SysDescException;

@Service
public class EvolucaoServiceImpl implements EvolucaoService {

	@Autowired
	@Lazy
	private EvolucaoPacienteRepository evolucaoPacienteRepository;

	@Autowired
	@Lazy
	private EvolucaoDetalheRepository evolucaoDetalheRepository;

	@Override
	public void salvar(ParametrosEvolucaoDTO parametrosEvolucaoDTO) {

		if (LongUtil.isNullOrZero(parametrosEvolucaoDTO.getPaciente())) {

			throw new SysDescException(MensagemConstants.MENSAGEM_PACIENTE_NAO_ENCONTRADO);
		}

		if (LongUtil.isNullOrZero(parametrosEvolucaoDTO.getUsuario())) {

			throw new SysDescException(MensagemConstants.MENSAGEM_USUARIO_NAO_ENCONTADO);
		}

		Date dataEvolucao = DateUtil.parse(DateUtil.FORMATO_YYYY_MM_DD_HH_MM_SS,
				parametrosEvolucaoDTO.getDataEvolucao());

		if (dataEvolucao == null) {
			throw new SysDescException(MensagemConstants.MENSAGEM_DATA_EVOLUCAO_INVALIDA);
		}

		if (StringUtil.isNullOrEmptyTrim(parametrosEvolucaoDTO.getEvolucao())) {
			throw new SysDescException(MensagemConstants.MENSAGEM_EVOLUCAO_NAO_INFORMADA);
		}

		EvolucaoPaciente evolucaoPaciente = validarEvolucaoDia(parametrosEvolucaoDTO.getPaciente(), dataEvolucao);

		EvolucaoDetalhe evolucaoDetalhe = new EvolucaoDetalhe();
		evolucaoDetalhe.setCodigoUsuario(parametrosEvolucaoDTO.getUsuario());
		evolucaoDetalhe.setCodigoEvolucaoPaciente(evolucaoPaciente.getIdEvolucaoPaciente());
		evolucaoDetalhe.setDataCadastro(dataEvolucao);
		evolucaoDetalhe.setEvolucao(parametrosEvolucaoDTO.getEvolucao());

		evolucaoDetalheRepository.save(evolucaoDetalhe);
	}

	private EvolucaoPaciente validarEvolucaoDia(Long paciente, Date dataEvolucao) {

		Optional<EvolucaoPaciente> optional = evolucaoPacienteRepository.findByCodigoPacienteAndDataEvolucao(paciente,
				dataEvolucao);

		if (!optional.isPresent()) {

			EvolucaoPaciente evolucaoPaciente = new EvolucaoPaciente();

			evolucaoPaciente.setCodigoPaciente(paciente);
			evolucaoPaciente.setDataEvolucao(dataEvolucao);

			evolucaoPacienteRepository.save(evolucaoPaciente);

			return evolucaoPaciente;
		}

		return optional.get();

	}
}
