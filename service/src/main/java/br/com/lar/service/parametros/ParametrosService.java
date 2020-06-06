package br.com.lar.service.parametros;

import com.google.gson.Gson;

import br.com.lar.repository.dao.ParametrosDAO;
import br.com.lar.repository.model.Parametros;
import br.com.sysdesc.pesquisa.service.impl.AbstractPesquisableServiceImpl;
import br.com.sysdesc.util.classes.CryptoUtil;
import br.com.sysdesc.util.classes.StringUtil;
import br.com.sysdesc.util.vo.ConfiguracaoMensalidadeVO;

public class ParametrosService extends AbstractPesquisableServiceImpl<Parametros> {

    private ParametrosDAO parametrosDAO;

    public ParametrosService() {
        this(new ParametrosDAO());
    }

    public ParametrosService(ParametrosDAO parametrosDAO) {
        super(parametrosDAO, Parametros::getIdParametro);

        this.parametrosDAO = parametrosDAO;
    }

    public Parametros obterPorId() {

        return parametrosDAO.obterPorId(1L);
    }

    public ConfiguracaoMensalidadeVO getConfiguracaoMensalidade(String configMensalidade) {

        if (!StringUtil.isNullOrEmpty(configMensalidade)) {
            return new Gson().fromJson(CryptoUtil.fromBlowfish(configMensalidade), ConfiguracaoMensalidadeVO.class);
        }

        return new ConfiguracaoMensalidadeVO();
    }

    public ConfiguracaoMensalidadeVO getConfiguracaoMensalidade() {

        Parametros parametros = obterPorId();

        if (parametros == null) {
            return null;
        }

        if (!StringUtil.isNullOrEmpty(parametros.getConfigMensalidade())) {
            return new Gson().fromJson(CryptoUtil.fromBlowfish(parametros.getConfigMensalidade()), ConfiguracaoMensalidadeVO.class);
        }

        return null;
    }

}
