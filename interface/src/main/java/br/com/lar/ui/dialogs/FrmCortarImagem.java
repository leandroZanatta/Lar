package br.com.lar.ui.dialogs;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

import br.com.sysdesc.util.classes.ImageUtil;
import br.com.sysdesc.util.vo.DimensionsVO;

public class FrmCortarImagem extends JDialog {

	private static final long serialVersionUID = 1L;

	private final JPanel contentPanel = new JPanel();
	private JPanel panelAcoes;
	private JButton btSalvar;
	private JButton btCancelar;
	private BufferedImage imagem;
	private double fatorMultiplicacao = 1;
	private JPanel panelImagem;
	private JPanel panelCortar;
	private JLabel lbImagem;
	private boolean ok = false;
	private DimensionsVO dimensionsVO;
	private BufferedImage imagemCortada;

	public FrmCortarImagem(BufferedImage imagem) {
		this.imagem = imagem;

		btSalvar = new JButton("Salvar");
		btCancelar = new JButton("Cancelar");
		panelImagem = new JPanel();
		panelCortar = new JPanel();
		lbImagem = new JLabel();

		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		panelAcoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));

		this.fatorMultiplicacao = calculateDimensionsImage(imagem.getWidth(), imagem.getHeight());

		Double newWidth = imagem.getWidth() / this.fatorMultiplicacao;
		Double newHeight = imagem.getHeight() / this.fatorMultiplicacao;

		dimensionsVO = ImageUtil.calculateDimension(newWidth.intValue(), newHeight.intValue());

		lbImagem.setIcon(ImageUtil.resize(imagem, newWidth.intValue(), newHeight.intValue()));

		panelAcoes.setBounds(10, newHeight.intValue() + 15, newWidth.intValue(), 33);
		panelImagem.setBounds(10, 10, newWidth.intValue(), newHeight.intValue());
		panelCortar.setBounds(0, 0, dimensionsVO.getWidth(), dimensionsVO.getHeight());
		lbImagem.setBounds(0, 0, newWidth.intValue(), newHeight.intValue());
		panelCortar.setBorder(new EtchedBorder());
		panelImagem.setLayout(null);

		panelImagem.addMouseMotionListener(new MouseMotionAdapter() {

			@Override
			public void mouseDragged(MouseEvent e) {

				int width = e.getX() - (dimensionsVO.getWidth() / 2);
				int height = e.getY() - (dimensionsVO.getHeight() / 2);

				if (width < 0) {
					width = 0;
				}
				if (newHeight < 0) {
					height = 0;
				}

				if (width + dimensionsVO.getWidth() > newWidth.intValue()) {
					width = newWidth.intValue() - dimensionsVO.getWidth();
				}

				if (newHeight + dimensionsVO.getHeight() > newHeight.intValue()) {
					height = newHeight.intValue() - dimensionsVO.getHeight();
				}

				panelCortar.setBounds(width, height, dimensionsVO.getWidth(), dimensionsVO.getHeight());
			}
		});

		btCancelar.addActionListener(e -> dispose());
		btSalvar.addActionListener(e -> cortarImagem());

		panelCortar.setOpaque(false);

		panelImagem.add(panelCortar);
		panelImagem.add(lbImagem);

		contentPanel.add(panelImagem);
		contentPanel.add(panelAcoes);
		panelAcoes.add(btSalvar);
		panelAcoes.add(btCancelar);

		setTitle("CORTAR IMAGEM");
		setSize(newWidth.intValue() + 35, newHeight.intValue() + 90);
		setModal(true);
		setLocationRelativeTo(null);

	}

	private double calculateDimensionsImage(int larguraImagem, int alturaImagem) {

		if (larguraImagem > 800 || alturaImagem > 600) {

			if (larguraImagem > 800) {

				return larguraImagem / 800.0;
			}

			if (alturaImagem > 600) {

				return alturaImagem / 600.0;
			}
		}

		return 1;
	}

	private void cortarImagem() {

		Double x = panelCortar.getX() * this.fatorMultiplicacao;
		Double y = panelCortar.getY() * this.fatorMultiplicacao;
		Double newWidth = dimensionsVO.getWidth() * this.fatorMultiplicacao;
		Double newHeight = dimensionsVO.getHeight() * this.fatorMultiplicacao;

		imagemCortada = ImageUtil.cortar(this.imagem, x.intValue(), y.intValue(), newWidth.intValue(),
				newHeight.intValue());

		ok = true;

		dispose();
	}

	public boolean isOk() {
		return ok;
	}

	public BufferedImage getImagemCortada() {
		return imagemCortada;
	}

}
