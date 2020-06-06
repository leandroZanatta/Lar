package br.com.lar.server.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import br.com.lar.server.dto.PesquisaPaginadaDTO;
import br.com.lar.server.enumerador.PesquisaEnum;
import br.com.lar.server.service.PesquisaPadraoService;
import br.com.lar.server.service.PesquisaService;
import br.com.sysdesc.util.classes.LongUtil;

@Service
public class PesquisaServiceImpl implements PesquisaService {

	@Autowired
	@Lazy
	private ApplicationContext applicationContext;

	@Override
	public <T> PesquisaPaginadaDTO<T> pesquisar(Long codigoPesquisa, String campoPesquisa, Long pagina,
			Long registros) {

		PesquisaPadraoService pesquisaPadraoService = applicationContext
				.getBean(PesquisaEnum.findByCodigo(codigoPesquisa).getInterfacePesquisa());

		PesquisaPaginadaDTO<T> pesquisaPaginadaDTO = new PesquisaPaginadaDTO<>();

		if (LongUtil.isNullOrZero(pagina)) {
			pesquisaPaginadaDTO.setNumeroRegistros(pesquisaPadraoService.contarRegistroPesquisa(campoPesquisa));
		}

		pesquisaPaginadaDTO.setRows(pesquisaPadraoService.pesquisar(campoPesquisa, pagina, registros));

		return pesquisaPaginadaDTO;
	}

}
