package br.com.lar.thread;

import static br.com.sysdesc.util.constants.MensagemConstants.MENSAGEM_LOG_ARQUIVO_DA_VERSAO;
import static br.com.sysdesc.util.constants.MensagemConstants.MENSAGEM_LOG_GERANDO_ARQUIVO;
import static br.com.sysdesc.util.constants.MensagemConstants.MENSAGEM_LOG_VERSAO_NAO_PODE_SER_GERADO;
import static br.com.sysdesc.util.constants.MensagemConstants.MENSAGEM_THREAD_VERSAO_INTEROMPIDA;
import static br.com.sysdesc.util.resources.Configuracoes.FOLDER_VERSOES;
import static br.com.sysdesc.util.resources.Configuracoes.VERSAO_SERVIDOR;

import java.awt.Desktop;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import com.google.gson.Gson;

import br.com.lar.repository.dao.VersaoServidorDAO;
import br.com.lar.repository.model.VersaoServidor;
import br.com.sysdesc.util.classes.ExtratorZip;
import br.com.sysdesc.util.classes.IPUtil;
import br.com.sysdesc.util.classes.ImageUtil;
import br.com.sysdesc.util.classes.ProcessUtil;
import br.com.sysdesc.util.resources.Resources;
import br.com.sysdesc.util.vo.IPVO;
import br.com.sysdesc.util.vo.VersaoServidorVO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ServidorThread extends Thread {

	private Process process;
	private JPanel contentStatusServer;

	private JLabel lbStatusServer;
	private JLabel lbSituacao;

	private String versaoBase;

	private static final String URLVERSAO = "https://raw.githubusercontent.com/leandroZanatta/Lar/develop/versoes/versaoservidor.json";

	private VersaoServidorDAO versaoServidorDAO = new VersaoServidorDAO();

	public ServidorThread(JPanel contentStatusServer) {
		this.contentStatusServer = contentStatusServer;

		lbStatusServer = new JLabel();

		lbSituacao = new JLabel();

		contentStatusServer.add(lbStatusServer);
		contentStatusServer.add(lbSituacao);
	}

	@Override
	public void run() {

		this.verificarVersaoBase();

		this.verificarVersaoRemota(this.recuperarVersaoInternet());

		lbStatusServer.setText(String.format("Servidor: %s", this.versaoBase));
		lbSituacao.setIcon(ImageUtil.resize("statusAguardando.png", 25, 25));

		this.inicializarServidor();
	}

	private void inicializarServidor() {

		try {

			File logFile = File.createTempFile("log", ".txt");

			try (FileInputStream fileInputStream = new FileInputStream(logFile);
					BufferedReader input = new BufferedReader(new InputStreamReader(fileInputStream))) {

				process = ProcessUtil.createProcess("java -jar lar-server.jar", logFile, null);

				boolean servidorIniciado = verificarInicializacaoModulo(input);

				Arrays.asList(lbSituacao.getMouseListeners()).forEach(lbSituacao::removeMouseListener);

				lbSituacao.setIcon(ImageUtil.resize(servidorIniciado ? "statusOk.png" : "statusErro.png", 25, 25));

				if (servidorIniciado) {

					lbSituacao.addMouseListener(new MouseAdapter() {
						@Override
						public void mouseClicked(MouseEvent e) {
							abrirWebView();
						}
					});
				}

				log.info("Servidor iniciado: {}", servidorIniciado ? "SIM" : "NÃO");

			}
		} catch (Exception e) {

			log.error("Erro inicializando servidor", e);

		}

	}

	protected void abrirWebView() {

		String ip = "127.0.0.1";

		Optional<IPVO> optional = IPUtil.getIps().stream().findFirst();

		if (optional.isPresent()) {
			ip = optional.get().getIp();
		}

		if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {

			try {

				Desktop.getDesktop().browse(new URI(String.format("http://%s:2222", ip)));

			} catch (IOException | URISyntaxException e) {
				log.debug("Não foi possivel abrir o Browser");
			}
		}
	}

	private boolean verificarInicializacaoModulo(BufferedReader input) throws IOException, InterruptedException {

		Long timeExecution = 0L;

		String line;

		while (timeExecution < 60) {

			while ((line = input.readLine()) != null) {
				System.out.println(line);
				if (line.contains("Started Application")) {
					return true;
				}
			}

			Thread.sleep(1000);

			timeExecution++;
		}

		return false;
	}

	private VersaoServidorVO recuperarVersaoInternet() {

		URL arquivoUrl;

		try {

			arquivoUrl = new URL(URLVERSAO);

			log.info("buscando versão da internet url {}", URLVERSAO);

			URLConnection urlConnection = arquivoUrl.openConnection();
			urlConnection.setUseCaches(Boolean.FALSE);

			try (BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()))) {

				String inputLine;

				StringBuilder stringBuilder = new StringBuilder();

				while ((inputLine = in.readLine()) != null) {

					stringBuilder.append(inputLine);
				}

				return new Gson().fromJson(stringBuilder.toString(), VersaoServidorVO.class);

			}

		} catch (IOException e) {

			try {
				Thread.sleep(18000);

				return recuperarVersaoInternet();

			} catch (InterruptedException e1) {

				Thread.currentThread().interrupt();

				log.error(Resources.translate(MENSAGEM_THREAD_VERSAO_INTEROMPIDA), e1);

				return null;
			}
		}

	}

	private void verificarVersaoRemota(VersaoServidorVO versaoVO) {
		log.info("Verificando versões do servidor");

		if (versaoVO != null) {

			try {

				VersaoServidorVO versaoLocal = getArquivoVersaoLocal();

				log.info(String.format("Versão base: %s ~ Versão Remota: %s", versaoBase, versaoVO.getVersao()));

				if (!versaoBase.equals(versaoVO.getVersao())) {

					efetuarDownloadVersao(versaoVO.getArquivo());

					File arquivo = criarArquivoVersao(versaoVO.getArquivo());

					new ExtratorZip().extrairVersao(arquivo);

					Files.delete(Paths.get(arquivo.toURI()));

					versaoLocal.setVersao(versaoVO.getVersao());
					versaoLocal.setArquivo(versaoVO.getArquivo());

					VersaoServidor versaoServidor = new VersaoServidor();
					versaoServidor.setDataAtualizacao(new Date());
					versaoServidor.setNumeroVersao(versaoVO.getVersao());

					versaoServidorDAO.salvar(versaoServidor);

					FileUtils.writeStringToFile(new File(VERSAO_SERVIDOR), new Gson().toJson(versaoLocal),
							StandardCharsets.UTF_8);

					this.versaoBase = versaoVO.getVersao();
				}

			} catch (Exception e) {

				try {
					log.warn("Não foi possivel conectar ao servidor esperando 180 segundos para tentar novamente");

					Thread.sleep(18000);

					verificarVersaoRemota(versaoVO);

				} catch (InterruptedException e1) {

					log.error(Resources.translate(MENSAGEM_THREAD_VERSAO_INTEROMPIDA), e);

					Thread.currentThread().interrupt();
				}
			}
		}
	}

	private VersaoServidorVO getArquivoVersaoLocal() {
		try {

			return new Gson().fromJson(FileUtils.readFileToString(new File(VERSAO_SERVIDOR), Charset.defaultCharset()),
					VersaoServidorVO.class);
		} catch (Exception e) {

			return new VersaoServidorVO();
		}

	}

	private void efetuarDownloadVersao(String urlVersao) throws IOException {
		log.info("Efetuando download da versão {}", urlVersao);

		JProgressBar progres = new JProgressBar();

		progres.setStringPainted(true);

		contentStatusServer.removeAll();

		contentStatusServer.add(progres);

		URL arquivoUrl = new URL(urlVersao);

		URLConnection urlConnection = arquivoUrl.openConnection();

		urlConnection.setUseCaches(Boolean.FALSE);

		Long tamanhoArquivo = urlConnection.getContentLengthLong();

		log.info("Tamanho do Arquivo {} bytes", tamanhoArquivo);

		File arquivoVersao = criarArquivoVersao(urlVersao);

		try (BufferedInputStream in = new BufferedInputStream(urlConnection.getInputStream());
				FileOutputStream fileOutputStream = new FileOutputStream(arquivoVersao)) {

			byte[] dataBuffer = new byte[1024];

			int bytesRead;

			Long bufferTotal = 0L;

			while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {

				bufferTotal += bytesRead;

				fileOutputStream.write(dataBuffer, 0, bytesRead);

				Double percentual = bufferTotal.doubleValue() / tamanhoArquivo.doubleValue() * 100;

				progres.setValue(percentual.intValue());

			}
		}

		contentStatusServer.removeAll();

		contentStatusServer.add(lbSituacao);
		contentStatusServer.add(lbStatusServer);
	}

	private File criarArquivoVersao(String arquivo) throws IOException {

		File folderVersao = new File(FOLDER_VERSOES);

		if (!folderVersao.exists()) {
			log.info(Resources.translate(MENSAGEM_LOG_GERANDO_ARQUIVO) + folderVersao.getName());

			folderVersao.mkdir();
		}

		File arquivoVersao = new File(folderVersao, FilenameUtils.getName(new URL(arquivo).getPath()));

		if (!arquivoVersao.exists()) {
			log.info(Resources.translate(MENSAGEM_LOG_ARQUIVO_DA_VERSAO) + arquivoVersao.getName());

			if (!arquivoVersao.createNewFile()) {
				log.error(Resources.translate(MENSAGEM_LOG_VERSAO_NAO_PODE_SER_GERADO) + arquivoVersao.getName());
			}
		}

		return arquivoVersao;
	}

	private void verificarVersaoBase() {

		log.info("Verificando versão da Base");

		VersaoServidor versao = versaoServidorDAO.last();

		if (versao != null) {

			this.versaoBase = versao.getNumeroVersao();

			return;
		}

		this.versaoBase = "0.0.0";
	}

	public void pararProcesso() {

		if (process != null) {

			process.destroyForcibly();
		}

	}

}
