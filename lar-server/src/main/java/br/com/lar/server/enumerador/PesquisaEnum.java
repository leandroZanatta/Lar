package br.com.lar.server.enumerador;

import java.util.HashMap;
import java.util.Map;

import br.com.lar.server.service.PesquisaHistoricoService;
import br.com.lar.server.service.PesquisaPadraoService;

public enum PesquisaEnum {

	PESQUISA_HISTORICO(1L, PesquisaHistoricoService.class);

	private final Long codigoPesquisa;

	private final Class<? extends PesquisaPadraoService> interfacePesquisa;

	private static Map<Long, PesquisaEnum> mapa = new HashMap<>();

	static {

		for (PesquisaEnum pesquisa : PesquisaEnum.values()) {
			mapa.put(pesquisa.getCodigoPesquisa(), pesquisa);
		}
	}

	private PesquisaEnum(final Long codigoPesquisa, Class<? extends PesquisaPadraoService> interfacePesquisa) {
		this.codigoPesquisa = codigoPesquisa;
		this.interfacePesquisa = interfacePesquisa;
	}

	public Long getCodigoPesquisa() {
		return codigoPesquisa;
	}

	public Class<? extends PesquisaPadraoService> getInterfacePesquisa() {
		return interfacePesquisa;
	}

	public static PesquisaEnum findByCodigo(Long codigoPesquisa) {
		return mapa.get(codigoPesquisa);
	}
}
