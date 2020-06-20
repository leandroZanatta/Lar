package br.com.lar.ui;

import static br.com.lar.startup.enumeradores.PesquisaEnum.PES_BANCOS;

import java.awt.event.ActionEvent;
import java.text.ParseException;
import java.util.Arrays;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;

import br.com.lar.ui.buttonactions.ButtonActionBoleto;
import br.com.sysdesc.boletos.repository.model.Banco;
import br.com.sysdesc.boletos.service.banco.BancoService;
import br.com.sysdesc.components.AbstractInternalFrame;
import br.com.sysdesc.components.JTextFieldId;
import br.com.sysdesc.pesquisa.ui.components.PanelActions;
import br.com.sysdesc.util.classes.ContadorUtil;
import br.com.sysdesc.util.enumeradores.TipoBancoEnum;
import br.com.sysdesc.util.enumeradores.TipoStatusEnum;
import net.miginfocom.swing.MigLayout;

public class FrmBanco extends AbstractInternalFrame {

	private static final long serialVersionUID = 1L;

	private JPanel painelContent;
	private JTextFieldId txCodigo;
	private JLabel lblCodigo;
	private PanelActions<Banco> panelActions;
	private BancoService bancoService = new BancoService();
	private JLabel lblBanco;
	private JComboBox<TipoBancoEnum> cbBanco;
	private JPanel panel;
	private JLabel lbAgencia;
	private JTextField txAgencia;
	private JLabel lbConta;
	private JTextField txContaCorrente;
	private JLabel lbSituacao;
	private JComboBox<TipoStatusEnum> cbSituacao;
	private ButtonActionBoleto configurarBoleto;

	public FrmBanco(Long permissaoPrograma, Long codigoUsuario) {
		super(permissaoPrograma, codigoUsuario);

		initComponents();
	}

	private void initComponents() {

		setSize(500, 263);
		setClosable(Boolean.TRUE);
		setTitle("CADASTRO DE BANCOS");

		painelContent = new JPanel();
		panel = new JPanel();

		lblCodigo = new JLabel("Código:");
		lblBanco = new JLabel("Banco:");
		lbAgencia = new JLabel("Agência:");
		lbConta = new JLabel("Conta Corrente:");
		lbSituacao = new JLabel("Situação:");

		txCodigo = new JTextFieldId();
		cbBanco = new JComboBox<>();
		txAgencia = new JTextField();
		txContaCorrente = new JTextField();
		cbSituacao = new JComboBox<>();

		configurarBoleto = new ButtonActionBoleto();

		painelContent.setLayout(new MigLayout("", "[grow]", "[][][][][][][][grow]"));
		panel.setLayout(new MigLayout("", "[][grow][][grow]", "[27.00]"));

		panel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));

		getContentPane().add(painelContent);

		Arrays.asList(TipoBancoEnum.values()).forEach(cbBanco::addItem);
		Arrays.asList(TipoStatusEnum.values()).forEach(cbSituacao::addItem);

		panel.add(lbAgencia, "cell 0 0,alignx trailing,aligny baseline");
		panel.add(txAgencia, "cell 1 0,growx");
		panel.add(lbConta, "cell 2 0,alignx trailing,aligny baseline");
		panel.add(txContaCorrente, "cell 3 0,growx");

		painelContent.add(lblCodigo, "cell 0 0");
		painelContent.add(txCodigo, "cell 0 1,width 50:100:100");
		painelContent.add(panel, "cell 0 4,grow");
		painelContent.add(lblBanco, "cell 0 2");
		painelContent.add(cbBanco, "cell 0 3,growx");
		painelContent.add(lbSituacao, "cell 0 5");
		painelContent.add(cbSituacao, "cell 0 6,growx");

		Action actionEdicarConfigBoleto = new AbstractAction() {

			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {

				try {

					FrmBancoBoleto frmBancoBoleto = new FrmBancoBoleto(getCodigoPrograma(), getCodigoUsuario(),
							panelActions.getObjetoPesquisa());

					FrmApplication.getInstance().posicionarFrame(frmBancoBoleto, "boleto.png");
				} catch (ParseException pe) {

					JOptionPane.showMessageDialog(null, "Não foi possivel abrir as configurações de boleto");
				}
			}

		};

		panelActions = new PanelActions<Banco>(this, bancoService, PES_BANCOS.getCodigoPesquisa(), configurarBoleto) {

			private static final long serialVersionUID = 1L;

			@Override
			protected void posicionarBotoes() {

				ContadorUtil contadorUtil = new ContadorUtil();

				posicionarBotao(contadorUtil, btPrimeiro, Boolean.TRUE);
				posicionarBotao(contadorUtil, btRetroceder, Boolean.TRUE);

				posicionarBotao(contadorUtil, configurarBoleto, Boolean.TRUE);
				posicionarBotao(contadorUtil, btSalvar, Boolean.TRUE);
				posicionarBotao(contadorUtil, btEditar, Boolean.TRUE);
				posicionarBotao(contadorUtil, btNovo, Boolean.TRUE);
				posicionarBotao(contadorUtil, btBuscar, Boolean.TRUE);
				posicionarBotao(contadorUtil, btCancelar, Boolean.TRUE);

				posicionarBotao(contadorUtil, btAvancar, Boolean.TRUE);
				posicionarBotao(contadorUtil, btUltimo, Boolean.TRUE);

			}

			@Override
			protected void registrarEventosBotoesPagina() {
				registrarEvento(configurarBoleto, actionEdicarConfigBoleto);
			}

			@Override
			public void carregarObjeto(Banco objeto) {
				txCodigo.setValue(objeto.getIdBanco());
				txAgencia.setText(objeto.getNumeroAgencia());
				txContaCorrente.setText(objeto.getNumeroConta());
				cbBanco.setSelectedItem(TipoBancoEnum.forCodigo(objeto.getNumeroBanco()));
				cbSituacao.setSelectedItem(TipoStatusEnum.findByCodigo(objeto.getSituacao()));
			}

			@Override
			public boolean preencherObjeto(Banco objetoPesquisa) {

				objetoPesquisa.setIdBanco(txCodigo.getValue());
				objetoPesquisa.setNumeroAgencia(txAgencia.getText());
				objetoPesquisa.setNumeroConta(txContaCorrente.getText());
				objetoPesquisa.setNumeroBanco(null);
				objetoPesquisa.setSituacao(null);

				if (cbBanco.getSelectedIndex() >= 0) {
					objetoPesquisa.setNumeroBanco(((TipoBancoEnum) cbBanco.getSelectedItem()).getCodigo());
				}

				if (cbSituacao.getSelectedIndex() >= 0) {
					objetoPesquisa.setSituacao(((TipoStatusEnum) cbSituacao.getSelectedItem()).getCodigo());
				}

				return true;
			}

		};

		panelActions.addSaveListener(objeto -> txCodigo.setValue(objeto.getIdBanco()));

		painelContent.add(panelActions, "cell 0 7,growx,aligny bottom");
	}

}
