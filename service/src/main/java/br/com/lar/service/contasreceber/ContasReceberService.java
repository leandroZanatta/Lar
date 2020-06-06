package br.com.lar.service.contasreceber;

import java.util.Date;
import java.util.List;

import br.com.lar.repository.dao.ContasReceberDAO;
import br.com.lar.repository.model.Cliente;
import br.com.lar.repository.model.ContasReceber;
import br.com.lar.repository.model.FormasPagamento;
import br.com.sysdesc.pesquisa.service.impl.AbstractPesquisableServiceImpl;
import br.com.sysdesc.util.enumeradores.TipoStatusEnum;
import br.com.sysdesc.util.vo.PesquisaContasReceberVO;
import br.com.sysdesc.util.vo.ValoresContasReceberVO;

public class ContasReceberService extends AbstractPesquisableServiceImpl<ContasReceber> {

    private ContasReceberDAO contasReceberDAO;

    public ContasReceberService() {
        this(new ContasReceberDAO());
    }

    public ContasReceberService(ContasReceberDAO contasReceberDAO) {
        super(contasReceberDAO, ContasReceber::getIdContasReceber);

        this.contasReceberDAO = contasReceberDAO;
    }

    public ContasReceber criarContasReceber(Cliente cliente, FormasPagamento formasPagamento, Date dataVencimento,
            ValoresContasReceberVO valoresContasReceberVO, String programa) {

        Date dataCadastro = new Date();

        ContasReceber contasReceber = new ContasReceber();
        contasReceber.setCliente(cliente);
        contasReceber.setFormasPagamento(formasPagamento);
        contasReceber.setBaixado(false);
        contasReceber.setDataVencimento(dataVencimento);
        contasReceber.setCodigoStatus(TipoStatusEnum.ATIVO.getCodigo());
        contasReceber.setDataCadastro(dataCadastro);
        contasReceber.setDataManutencao(dataCadastro);
        contasReceber.setValorAcrescimo(valoresContasReceberVO.getAcrescimo());
        contasReceber.setValorDesconto(valoresContasReceberVO.getDesconto());
        contasReceber.setValorJuros(valoresContasReceberVO.getValorJuros());
        contasReceber.setValorPago(valoresContasReceberVO.getValorPago());
        contasReceber.setValorParcela(valoresContasReceberVO.getValor());
        contasReceber.setPrograma(programa);

        contasReceberDAO.salvar(contasReceber);

        return contasReceber;
    }

    public List<ContasReceber> filtrarContasReceber(PesquisaContasReceberVO pesquisaContasReceberVO) {

        return contasReceberDAO.filtrarContasReceber(pesquisaContasReceberVO);
    }

    public List<ContasReceber> buscarContasReceberPacienteTipoBoleto(Long codigoCliente, boolean agruparContas) {

        return contasReceberDAO.buscarContasReceberPacienteTipoBoleto(codigoCliente, agruparContas);
    }

}
