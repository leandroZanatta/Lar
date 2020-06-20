package br.com.lar.service.email;

import static br.com.sysdesc.util.classes.MoneyFormatter.format;
import static br.com.sysdesc.util.enumeradores.TipoProgramaContasReceberEnum.findByCodigo;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.io.FileUtils;
import org.overviewproject.mime_types.GetBytesException;
import org.overviewproject.mime_types.MimeTypeDetector;

import br.com.lar.repository.dao.EmailDAO;
import br.com.lar.repository.model.ContasReceber;
import br.com.lar.repository.model.Email;
import br.com.lar.repository.model.EmailAnexo;
import br.com.lar.repository.model.EmailBoleto;
import br.com.lar.repository.model.Paciente;
import br.com.sysdesc.boleto.repository.model.Boleto;
import br.com.sysdesc.util.classes.DateUtil;
import br.com.sysdesc.util.classes.ListUtil;
import br.com.sysdesc.util.classes.StringUtil;
import br.com.sysdesc.util.enumeradores.TipoEnvioEmailEnum;
import br.com.sysdesc.util.html.Table;
import br.com.sysdesc.util.html.TableData;
import br.com.sysdesc.util.html.TableHeader;
import br.com.sysdesc.util.html.TableRow;
import br.com.sysdesc.util.html.Tag;
import br.com.sysdesc.util.resources.Configuracoes;
import br.com.sysdesc.util.vo.AttachamentVO;
import br.com.sysdesc.util.vo.CIDVO;
import br.com.sysdesc.util.vo.ChaveValorParametroHTML;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EmailService {

	private static final String STYLE = "style";
	private EmailDAO emailDAO = new EmailDAO();

	public void gerarEmailBoleto(Paciente paciente, List<ContasReceber> value, Boleto boleto) {

		String destinatario = this.getDestinatarioPaciente(paciente);

		if (!StringUtil.isNullOrEmpty(destinatario)) {

			try {

				Email email = new Email();
				email.setDestinatario(destinatario);
				email.setCodigoStatus(TipoEnvioEmailEnum.PENDENTE.getCodigo());
				email.setAssunto("Boleto Mensalidade");
				email.setMensagem(this.gerarMensagemBoleto(value, paciente, boleto.getDataVencimento()));

				EmailAnexo anexo = new EmailAnexo();
				anexo.setEmail(email);
				anexo.setAnexo(boleto.getArquivo());
				anexo.setNomeArquivo(boleto.getCodigoBarras() + ".pdf");
				email.setEmailAnexos(Arrays.asList(anexo));

				EmailBoleto emailBoleto = new EmailBoleto();
				emailBoleto.setBoleto(boleto);
				emailBoleto.setEmail(email);
				email.setEmailBoleto(emailBoleto);

				emailDAO.salvar(email);

			} catch (IOException e) {

				log.error("Erro ao abri o template de email", e);
			}
		}
	}

	public void enviarEmails() {

		List<Email> emails = emailDAO.buscarEmailsEnviar();

		if (!ListUtil.isNullOrEmpty(emails)) {

			emails.forEach(this::enviarEmail);
		}
	}

	private void enviarEmail(Email email) {

		List<AttachamentVO> attachamentVOs = new ArrayList<>();

		email.getEmailAnexos().forEach(anexo -> {

			String mimeType = getMimeType(anexo.getAnexo(), anexo.getNomeArquivo());

			AttachamentVO attachamentVO = new AttachamentVO();
			attachamentVO.setFile(anexo.getAnexo());
			attachamentVO.setMime(mimeType);
			attachamentVO.setName(anexo.getNomeArquivo());

			attachamentVOs.add(attachamentVO);
		});

		ServidorEmail.getInstance().sendHtmlMessage(email.getDestinatario(), email.getAssunto(), email.getMensagem(), attachamentVOs,
				new CIDVO(new File(Configuracoes.CABECALHO_MENSALIDADE), "image"));

		email.setCodigoStatus(TipoEnvioEmailEnum.ENVIADO.getCodigo());

		emailDAO.salvar(email);
	}

	private String getMimeType(byte[] anexo, String nome) {

		try (ByteArrayInputStream bs = new ByteArrayInputStream(anexo)) {

			return new MimeTypeDetector().detectMimeType(nome, bs);

		} catch (IOException | GetBytesException e) {

			log.error("Erro ao extrair mime type do anexo", e);
		}

		return null;
	}

	private String gerarMensagemBoleto(List<ContasReceber> contasReceber, Paciente paciente, Date dataVencimento) throws IOException {

		String template = FileUtils.readFileToString(new File(Configuracoes.TEMPLATE_MENSALIDADE), StandardCharsets.UTF_8);

		return template.replace("{0}", this.getNomeCliente(paciente)).replace("{1}", DateUtil.format(DateUtil.FORMATO_DD_MM_YYY, dataVencimento))
				.replace("{2}", this.montarDetalhe(contasReceber));
	}

	private String montarDetalhe(List<ContasReceber> contasReceber) {

		BigDecimal valorTotal = contasReceber.stream().map(ContasReceber::getValorParcela).reduce(BigDecimal.ZERO, BigDecimal::add);

		Map<String, String> cels = new HashMap<>();
		Map<String, String> lasts = new HashMap<>();

		cels.put(STYLE, "border: 1px solid #ddd; padding: 8px;");
		lasts.put(STYLE, "border: 1px solid #ddd; padding: 8px;width: 120px;");

		Table table = new Table();

		TableRow tableHeader = new TableRow();
		TableRow detail = new TableRow();

		table.addParameter(STYLE, "font-family: Trebuchet MS, Arial, Helvetica, sans-serif; border-collapse: collapse; width: 100%;");
		tableHeader.addParameter(STYLE, "padding-top: 12px; padding-bottom: 12px; text-align: left; background-color: #4CAF50; color: white;");
		detail.addParameter(STYLE, "padding-top: 12px; padding-bottom: 12px; text-align: left; background-color: #CCC;");

		criarHeader(tableHeader, new ChaveValorParametroHTML("Descrição", cels), new ChaveValorParametroHTML("Valor", lasts));
		criarHeader(detail, new ChaveValorParametroHTML("Total", cels), new ChaveValorParametroHTML(format(valorTotal.doubleValue()), lasts));

		table.setHeader(tableHeader);

		contasReceber.forEach(conta -> {

			TableRow tableData = new TableRow();

			criarDetalhe(tableData, new ChaveValorParametroHTML(findByCodigo(conta.getPrograma()).getDescricao(), cels),
					new ChaveValorParametroHTML(format(conta.getValorParcela().doubleValue()), lasts));

			table.addrow(tableData);
		});

		table.addrow(detail);

		return table.build();
	}

	private void criarHeader(TableRow tableRow, ChaveValorParametroHTML... valores) {

		Arrays.asList(valores).forEach(valor -> {

			TableHeader tableHeader = new TableHeader();
			tableHeader.addData(valor.getDescricao());
			this.adicionarParametro(valor, tableHeader);

			tableRow.appendContent(tableHeader);
		});

	}

	private void adicionarParametro(ChaveValorParametroHTML valor, Tag tag) {

		for (Entry<String, String> parametro : valor.getParametro().entrySet()) {

			tag.addParameter(parametro.getKey(), parametro.getValue());
		}
	}

	private void criarDetalhe(TableRow tableRow, ChaveValorParametroHTML... valores) {

		Arrays.asList(valores).forEach(valor -> {

			TableData tableData = new TableData();
			tableData.addData(valor.getDescricao());
			this.adicionarParametro(valor, tableData);
			tableRow.appendContent(tableData);
		});

	}

	private String getNomeCliente(Paciente paciente) {

		if (paciente.getResponsavel() != null) {
			return paciente.getResponsavel().getNome();
		}

		return paciente.getCliente().getNome();
	}

	private String getDestinatarioPaciente(Paciente paciente) {

		if (paciente.getResponsavel() != null && !StringUtil.isNullOrEmpty(paciente.getResponsavel().getEmail())) {

			return paciente.getResponsavel().getEmail();
		}

		return paciente.getCliente().getEmail();
	}

}
