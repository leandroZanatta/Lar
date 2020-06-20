package br.com.lar.ui.dialogs;

import java.awt.BorderLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.commons.io.FilenameUtils;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamResolution;

import br.com.lar.repository.model.ImagemPaciente;
import br.com.lar.repository.model.Paciente;
import br.com.lar.service.paciente.PacienteService;
import br.com.sysdesc.util.classes.ImageUtil;
import br.com.sysdesc.util.vo.DimensionsVO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FrmCadastroImagemPaciente extends JDialog {

	private static final long serialVersionUID = 1L;
	private JTextField txArquivo;
	private JPanel panelImagem;
	private JButton btGravar;
	private Paciente paciente;
	private JButton btOk;
	private PacienteService pacienteService;
	private JButton btCamera;
	private DimensionsVO dimensionsVO = new DimensionsVO(100, 100);

	public FrmCadastroImagemPaciente(Paciente paciente, PacienteService pacienteService) {
		this.paciente = paciente;
		this.pacienteService = pacienteService;

		setModal(true);
		setTitle("Cadastro de Imagens");
		setSize(450, 225);
		setLocationRelativeTo(null);
		getContentPane().setLayout(null);

		btCamera = new JButton("Câmera");
		btGravar = new JButton("Gravar");
		JButton btCancelar = new JButton("Cancelar");
		btOk = new JButton("Ok");
		JButton btProcurar = new JButton("...");
		txArquivo = new JTextField();
		panelImagem = new JPanel();

		int posicaoX = 327 + (100 - dimensionsVO.getWidth());
		panelImagem.setBounds(posicaoX, 7, dimensionsVO.getWidth(), dimensionsVO.getHeight());

		btCamera.addActionListener(e -> abrirWebCam());
		btCamera.setBounds(7, 29, 89, 23);
		getContentPane().add(btCamera);

		panelImagem.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		getContentPane().add(panelImagem);
		panelImagem.setLayout(new BorderLayout(0, 0));

		btGravar.addActionListener(e -> gravarWebcam());
		btGravar.setEnabled(false);
		btGravar.setBounds(7, 63, 89, 23);
		getContentPane().add(btGravar);

		btCancelar.addActionListener(e -> dispose());
		btCancelar.setBounds(352, 157, 75, 23);
		getContentPane().add(btCancelar);

		btOk.addActionListener(e -> salvarImagemPaciente());
		btOk.setEnabled(false);
		btOk.setBounds(267, 157, 75, 23);
		getContentPane().add(btOk);

		btProcurar.addActionListener(e -> buscarArquivo());
		btProcurar.setBounds(382, 123, 45, 23);
		getContentPane().add(btProcurar);

		txArquivo.setEditable(false);
		txArquivo.setBounds(7, 123, 368, 23);
		getContentPane().add(txArquivo);

	}

	private void salvarImagemPaciente() {

		try {
			FrmCortarImagem cortarImagem = new FrmCortarImagem(ImageIO.read(new File(txArquivo.getText())));

			cortarImagem.setVisible(true);

			if (cortarImagem.isOk()) {

				BufferedImage bufferedImage = cortarImagem.getImagemCortada();

				byte[] imagem = ImageUtil.resizeToBytes(bufferedImage, FilenameUtils.getExtension(txArquivo.getText()));

				ImagemPaciente imagemPaciente = new ImagemPaciente();
				imagemPaciente.setAltura(100L);
				imagemPaciente.setLargura(100L);
				imagemPaciente.setExtensao(FilenameUtils.getExtension(txArquivo.getText()));
				imagemPaciente.setPaciente(this.paciente);
				imagemPaciente.setImagem(imagem);

				this.paciente.setImagemPaciente(imagemPaciente);

				pacienteService.salvar(this.paciente);

				JOptionPane.showMessageDialog(this, "Imagem do paciente cadastrada com sucesso");

				dispose();

			}
		} catch (IOException e) {

			log.error("Erro ao gravar imagem", e);

			JOptionPane.showMessageDialog(this, "Ocorreu um erro ao salvar a imagem");
		}

	}

	private void buscarArquivo() {

		JFileChooser fileChooser = new JFileChooser();

		FileNameExtensionFilter filter = new FileNameExtensionFilter("Imagens (*.png, *.jpeg, *.jpg)", "png", "jpeg",
				"jpg");
		fileChooser.setAcceptAllFileFilterUsed(false);
		fileChooser.addChoosableFileFilter(filter);

		int i = fileChooser.showOpenDialog(null);

		if (i != 1) {

			File arquivo = fileChooser.getSelectedFile();
			txArquivo.setText(arquivo.getPath());

			montarPainelImagem(arquivo);

		}

	}

	private void gravarWebcam() {

		try {

			Webcam webcam = Webcam.getDefault();

			File tempFile = File.createTempFile("image", ".png");

			ImageIO.write(webcam.getImage(), "PNG", tempFile);

			webcam.close();

			txArquivo.setText(tempFile.getAbsolutePath());

			montarPainelImagem(tempFile);

		} catch (IOException e) {

			log.error("Ocorreu um erro ao salvar a imagem da câmera", e);

			JOptionPane.showMessageDialog(this, "Ocorreu um erro ao salvar a imagem da câmera");
		}
	}

	private void montarPainelImagem(File tempFile) {

		panelImagem.removeAll();

		ImageIcon imageIcon = ImageUtil.resize(tempFile, dimensionsVO.getWidth(), dimensionsVO.getHeight());

		JLabel imagem = new JLabel();

		imagem.setIcon(imageIcon);
		panelImagem.add(imagem, BorderLayout.CENTER);
		panelImagem.revalidate();

		btGravar.setEnabled(false);
		btCamera.setEnabled(true);
		btOk.setEnabled(true);
	}

	private void abrirWebCam() {

		Webcam webcam = Webcam.getDefault();

		if (webcam != null) {
			webcam.setViewSize(WebcamResolution.VGA.getSize());

			WebcamPanel panel = new WebcamPanel(webcam);
			panel.setFPSDisplayed(true);
			panel.setDisplayDebugInfo(true);
			panel.setImageSizeDisplayed(true);
			panel.setMirrored(true);

			panelImagem.removeAll();
			panelImagem.add(panel, BorderLayout.CENTER);
			panelImagem.revalidate();
			btGravar.setEnabled(true);
			btCamera.setEnabled(false);

			btOk.setEnabled(false);
		} else {

			JOptionPane.showMessageDialog(this, "Nenuma câmera foi encontrada");
		}
	}
}
