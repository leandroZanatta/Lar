package br.com.lar.service.boleto;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.apache.commons.io.FileUtils;

import br.com.lar.repository.dao.EmailDAO;
import br.com.lar.service.contasreceber.ContasReceberBoletoService;
import br.com.sysdesc.arquivos.exceptions.SysdescArquivosException;
import br.com.sysdesc.boletos.service.builders.RetornoBuilder;
import br.com.sysdesc.boletos.service.factory.RetornoFactory;

public class RetornoBoletoService {

	private RetornoFactory retornoFactory = new RetornoFactory();

	private EmailDAO emailDAO = new EmailDAO();

	private ContasReceberBoletoService contasReceberBoletoService = new ContasReceberBoletoService();

	public void processarRetorno(File arquivoRetorno) throws SysdescArquivosException, IOException {

		List<String> arquivos = FileUtils.readLines(arquivoRetorno, StandardCharsets.UTF_8);

		RetornoBuilder retornoBuilder = retornoFactory.getRetornoBuilder(arquivos);

		retornoBuilder.addRetornoRemessaAutorizadoListener(boleto -> emailDAO.marcarBoletoParaEnvio(boleto.getIdBoleto()));

		retornoBuilder.addRetornoRemessaPagoListener((boleto, boletoDetalhe) -> contasReceberBoletoService.receberContaBoleto(boleto, boletoDetalhe));

		retornoBuilder.build();

	}
}
