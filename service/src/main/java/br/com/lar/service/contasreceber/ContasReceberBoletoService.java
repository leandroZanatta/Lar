package br.com.lar.service.contasreceber;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import br.com.lar.repository.dao.ContasReceberBoletoDAO;
import br.com.lar.repository.dao.ContasReceberDAO;
import br.com.lar.repository.model.ContasReceber;
import br.com.lar.repository.model.ContasReceberBoleto;
import br.com.lar.repository.model.ContasReceberPagamento;
import br.com.sysdesc.boletos.repository.model.Boleto;
import br.com.sysdesc.boletos.service.retorno.models.RetornoDetalheModel;

public class ContasReceberBoletoService {

	private ContasReceberBoletoDAO contasReceberBoletoDAO = new ContasReceberBoletoDAO();
	private ContasReceberDAO contasReceberDAO = new ContasReceberDAO();

	public void salvar(List<ContasReceberBoleto> contasReceberBoletos) {

		contasReceberBoletoDAO.salvar(contasReceberBoletos);
	}

	public void receberContaBoleto(Boleto boleto, RetornoDetalheModel boletoDetalhe) {

		List<ContasReceberBoleto> contasReceberBoleto = contasReceberBoletoDAO.buscarContasReceberBoleto(boleto.getIdBoleto());

		this.registrarContas(boletoDetalhe, contasReceberBoleto);

	}

	private void registrarContas(RetornoDetalheModel boletoDetalhe, List<ContasReceberBoleto> contasReceberBoleto) {

		contasReceberDAO.salvar(contasReceberBoleto.stream()
				.map(contaReceberBoleto -> mapearPagamentoContasReceber(boletoDetalhe, contaReceberBoleto)).collect(Collectors.toList()));
	}

	private ContasReceber mapearPagamentoContasReceber(RetornoDetalheModel boletoDetalhe, ContasReceberBoleto contaReceberBoleto) {

		ContasReceber contasReceber = contaReceberBoleto.getContasReceber();

		BigDecimal valorRestante = getValorRestante(contasReceber);

		contasReceber.setBaixado(Boolean.TRUE);
		contasReceber.setValorPago(contasReceber.getValorPago().add(valorRestante));

		ContasReceberPagamento contasReceberPagamento = new ContasReceberPagamento();
		contasReceberPagamento.setContasReceber(contasReceber);
		contasReceberPagamento.setDataCadastro(new Date());
		contasReceberPagamento.setDataManutencao(new Date());
		contasReceberPagamento.setDataPagamento(boletoDetalhe.getDataCredito());
		contasReceberPagamento.setValorAcrescimo(boletoDetalhe.getAcrescimos());
		contasReceberPagamento.setValorDesconto(boletoDetalhe.getDescontos());
		contasReceberPagamento.setValorPago(boletoDetalhe.getValorPago());

		contasReceber.getContasReceberPagamentos().add(contasReceberPagamento);

		return contasReceber;
	}

	private BigDecimal getValorRestante(ContasReceber contasReceber) {

		return contasReceber.getValorParcela().add(contasReceber.getValorAcrescimo()).add(contasReceber.getValorJuros())
				.subtract(contasReceber.getValorDesconto())
				.subtract(contasReceber.getValorPago());
	}

}
