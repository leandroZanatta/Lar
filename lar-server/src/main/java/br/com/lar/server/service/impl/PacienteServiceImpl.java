package br.com.lar.server.service.impl;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import br.com.lar.server.dto.EvolucaoPacienteDTO;
import br.com.lar.server.dto.PacienteDTO;
import br.com.lar.server.dto.PageEvolucaoPacienteDTO;
import br.com.lar.server.repository.EvolucaoPacienteRepository;
import br.com.lar.server.repository.PacienteRepository;
import br.com.lar.server.repository.domain.ImagemPaciente;
import br.com.lar.server.repository.projections.EvolucaoDetalheProjection;
import br.com.lar.server.service.PacienteService;
import br.com.sysdesc.util.classes.DateUtil;
import br.com.sysdesc.util.classes.LongUtil;

@Service
public class PacienteServiceImpl implements PacienteService {

	@Autowired
	@Lazy
	private PacienteRepository pacienteRepository;

	@Autowired
	@Lazy
	private EvolucaoPacienteRepository evolucaoPacienteRepository;

	@Override
	public List<PacienteDTO> listar() {

		List<PacienteDTO> retorno = new ArrayList<>();

		pacienteRepository.findByCodigoStatus(1L).forEach(paciente -> {

			PacienteDTO pacienteDTO = new PacienteDTO();
			pacienteDTO.setIdPaciente(paciente.getIdPaciente());
			pacienteDTO.setDataAdmissao(paciente.getDataAdmissao());
			pacienteDTO.setNumeroCartaoSUS(paciente.getNumeroCartaoSUS());
			pacienteDTO.setCodigoStatus(paciente.getCodigoStatus());

			pacienteDTO.setNome(paciente.getCliente().getNome());
			pacienteDTO.setDataNascimento(paciente.getCliente().getDatadenascimento());
			pacienteDTO.setFotoPaciente(getImageFromBase64(paciente.getImagemPaciente()));

			retorno.add(pacienteDTO);
		});

		return retorno;
	}

	@Override
	public PageEvolucaoPacienteDTO buscarEvolucaoPaciente(Long codigoPaciente, Long pagina, Long registros) {
		PageEvolucaoPacienteDTO pageEvolucaoPacienteDTO = new PageEvolucaoPacienteDTO();

		List<EvolucaoPacienteDTO> evolucaoPacienteDTOs = new ArrayList<>();

		if (LongUtil.isNullOrZero(pagina)) {

			pageEvolucaoPacienteDTO.setNumeroRegistros(
					evolucaoPacienteRepository.buscarRegistrosEvolucoesPaciente(codigoPaciente, pagina, registros));
		}

		List<EvolucaoDetalheProjection> evolucaoPacientes = evolucaoPacienteRepository
				.buscarEvolucoesPaciente(codigoPaciente, pagina, registros);

		evolucaoPacientes
				.forEach(evolucaoProjection -> evolucaoPacienteDTOs.add(converterProjectionDTO(evolucaoProjection)));

		pageEvolucaoPacienteDTO.setEvolucaoPacientes(evolucaoPacienteDTOs);

		return pageEvolucaoPacienteDTO;
	}

	private EvolucaoPacienteDTO converterProjectionDTO(EvolucaoDetalheProjection evolucaoProjection) {

		String data = DateUtil.format(DateUtil.FORMATO_DD_MM_YYYY_HH_MM_SS, evolucaoProjection.getDataCadastro());

		EvolucaoPacienteDTO evolucaoPacienteDTO = new EvolucaoPacienteDTO();
		evolucaoPacienteDTO.setDataCadastro(data);
		evolucaoPacienteDTO.setEvolucao(evolucaoProjection.getEvolucao());
		evolucaoPacienteDTO.setFuncionario(evolucaoProjection.getFuncionario());
		evolucaoPacienteDTO.setIdEvolucaoDetalhe(evolucaoProjection.getIdEvolucaoDetalhe());

		return evolucaoPacienteDTO;
	}

	private String getImageFromBase64(ImagemPaciente imagemPaciente) {

		if (imagemPaciente != null) {

			StringBuilder imagem = new StringBuilder("data:image/");

			imagem.append(imagemPaciente.getExtensao()).append(";base64,");

			imagem.append(Base64.getEncoder().encodeToString(imagemPaciente.getImagem()));

			return imagem.toString();
		}

		return null;
	}

}
