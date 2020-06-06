package br.com.lar.ui;

import static br.com.sysdesc.util.resources.Resources.FRMPACIENTE_LB_ADMISSAO;
import static br.com.sysdesc.util.resources.Resources.FRMPACIENTE_LB_CLIENTE;
import static br.com.sysdesc.util.resources.Resources.FRMPACIENTE_LB_CODIGO;
import static br.com.sysdesc.util.resources.Resources.FRMPACIENTE_LB_PROCEDENCIA;
import static br.com.sysdesc.util.resources.Resources.FRMPACIENTE_TITLE;
import static br.com.sysdesc.util.resources.Resources.translate;

import java.awt.event.ActionEvent;
import java.util.Arrays;
import java.util.Date;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.toedter.calendar.JDateChooser;

import br.com.lar.repository.model.Cliente;
import br.com.lar.repository.model.Paciente;
import br.com.lar.repository.model.Procedencia;
import br.com.lar.service.cliente.ClienteService;
import br.com.lar.service.paciente.PacienteService;
import br.com.lar.service.procedencia.ProcedenciaService;
import br.com.lar.startup.enumeradores.PesquisaEnum;
import br.com.lar.ui.buttonactions.ButtonActionAdicionarFoto;
import br.com.lar.ui.dialogs.FrmCadastroImagemPaciente;
import br.com.sysdesc.components.AbstractInternalFrame;
import br.com.sysdesc.components.JNumericField;
import br.com.sysdesc.components.JTextFieldId;
import br.com.sysdesc.components.JTextFieldMaiusculo;
import br.com.sysdesc.components.listeners.adapter.ButtonActionListenerAdapter;
import br.com.sysdesc.pesquisa.ui.components.CampoPesquisa;
import br.com.sysdesc.pesquisa.ui.components.PanelActions;
import br.com.sysdesc.util.classes.ContadorUtil;
import br.com.sysdesc.util.classes.ImageUtil;
import br.com.sysdesc.util.enumeradores.TipoStatusEnum;
import br.com.sysdesc.util.enumeradores.TipoStatusPacienteEnum;
import br.com.sysdesc.util.vo.DimensionsVO;
import net.miginfocom.swing.MigLayout;

public class FrmPaciente extends AbstractInternalFrame {

	private static final long serialVersionUID = 1L;

	private JLabel lblCodigo;
	private JLabel lblCliente;
	private JLabel lblAdmissao;
	private JLabel lblProcedencia;
	private JLabel lbImagem;
	private JLabel lblSituao;

	private JTextFieldId txCodigo;
	private CampoPesquisa<Cliente> pesquisaCliente;
	private CampoPesquisa<Cliente> pesquisaResponsavel;
	private JDateChooser dataAdmissao;
	private JComboBox<Procedencia> cbProcedencia;
	private JComboBox<TipoStatusPacienteEnum> cbSituacao;

	private JPanel painelContent;
	private PanelActions<Paciente> panelActions;

	private PacienteService pacienteService = new PacienteService();
	private ClienteService clienteService = new ClienteService();
	private ProcedenciaService procedenciaService = new ProcedenciaService();
	private ButtonActionAdicionarFoto adicionarFoto;
	private DimensionsVO dimensionsVO = new DimensionsVO(100, 100);
	private JLabel lbResponsavel;

	private JNumericField txCartaoSus;
	private JLabel lbCartaoSus;
	private JLabel lbOcupacao;
	private JTextFieldMaiusculo txOcupacao;

	public FrmPaciente(Long permissaoPrograma, Long codigoUsuario) {
		super(permissaoPrograma, codigoUsuario);

		initComponents();
	}

	private void initComponents() {

		setSize(600, 305);
		setClosable(Boolean.TRUE);
		setTitle(translate(FRMPACIENTE_TITLE));

		painelContent = new JPanel();
		lblCodigo = new JLabel(translate(FRMPACIENTE_LB_CODIGO));
		lblCliente = new JLabel(translate(FRMPACIENTE_LB_CLIENTE));
		lblProcedencia = new JLabel(translate(FRMPACIENTE_LB_PROCEDENCIA));
		lblAdmissao = new JLabel(translate(FRMPACIENTE_LB_ADMISSAO));
		lbResponsavel = new JLabel("Responsável");
		lbCartaoSus = new JLabel("Número Cartão SUS:");
		lbImagem = new JLabel("Carregar Imagem");
		lbOcupacao = new JLabel("Ocupação INSS:");
		lblSituao = new JLabel("Situação:");
		txOcupacao = new JTextFieldMaiusculo();
		cbSituacao = new JComboBox<>();
		txCartaoSus = new JNumericField();
		cbProcedencia = new JComboBox<>();
		dataAdmissao = new JDateChooser("dd/MM/yyyy", "##/##/#####", '_');
		txCodigo = new JTextFieldId();

		pesquisaCliente = new CampoPesquisa<Cliente>(clienteService, PesquisaEnum.PES_CLIENTES.getCodigoPesquisa(),
				getCodigoUsuario()) {

			private static final long serialVersionUID = 1L;

			@Override
			public String formatarValorCampo(Cliente objeto) {
				return String.format("%d - %s", objeto.getIdCliente(), objeto.getNome());
			}
		};

		pesquisaResponsavel = new CampoPesquisa<Cliente>(clienteService, PesquisaEnum.PES_CLIENTES.getCodigoPesquisa(),
				getCodigoUsuario()) {

			private static final long serialVersionUID = 1L;

			@Override
			public String formatarValorCampo(Cliente objeto) {
				return String.format("%d - %s", objeto.getIdCliente(), objeto.getNome());
			}
		};

		adicionarFoto = new ButtonActionAdicionarFoto();

		painelContent.setLayout(new MigLayout("", "[grow][grow][77.00,grow][100.00]",
				"[::16px,grow][][::16px][20px:n:20px][::16px][][][][][][grow]"));

		procedenciaService.buscarTodos().forEach(cbProcedencia::addItem);
		Arrays.asList(TipoStatusPacienteEnum.values()).forEach(cbSituacao::addItem);

		painelContent.add(lblCodigo, "cell 0 0,aligny bottom");

		painelContent.add(lbImagem, "cell 3 0 1 4");
		painelContent.add(txCodigo, "cell 0 1,width 50:100:100");
		painelContent.add(lblCliente, "cell 0 2,aligny bottom");
		painelContent.add(pesquisaCliente, "cell 0 3 3 1,grow");
		painelContent.add(lblAdmissao, "cell 0 4,aligny bottom");
		painelContent.add(lbResponsavel, "cell 0 6");
		painelContent.add(pesquisaResponsavel, "cell 0 7 4 1,grow");
		painelContent.add(lbOcupacao, "cell 0 8");
		painelContent.add(lblSituao, "cell 3 8");
		painelContent.add(txOcupacao, "cell 0 9 3 1,growx");
		painelContent.add(cbSituacao, "cell 3 9,growx");
		painelContent.add(lbCartaoSus, "cell 1 4");
		painelContent.add(lblProcedencia, "cell 2 4 2 1,aligny bottom");
		painelContent.add(dataAdmissao, "cell 0 5,growx");

		painelContent.add(txCartaoSus, "cell 1 5,growx");
		painelContent.add(cbProcedencia, "cell 2 5 2 1,growx");

		getContentPane().add(painelContent);

		Action actionAdicionarFoto = new AbstractAction() {

			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				new FrmCadastroImagemPaciente(panelActions.getObjetoPesquisa(), pacienteService).setVisible(true);

				carregarImagemPaciente(panelActions.getObjetoPesquisa());
			}

		};

		panelActions = new PanelActions<Paciente>(this, pacienteService, PesquisaEnum.PES_PACIENTE.getCodigoPesquisa(),
				adicionarFoto) {

			private static final long serialVersionUID = 1L;

			@Override
			protected void posicionarBotoes() {

				ContadorUtil contadorUtil = new ContadorUtil();

				posicionarBotao(contadorUtil, btPrimeiro, Boolean.TRUE);
				posicionarBotao(contadorUtil, btRetroceder, Boolean.TRUE);

				posicionarBotao(contadorUtil, adicionarFoto, Boolean.TRUE);
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
				registrarEvento(adicionarFoto, actionAdicionarFoto);
			}

			@Override
			public void carregarObjeto(Paciente objeto) {

				txCodigo.setValue(objeto.getIdPaciente());
				pesquisaCliente.setValue(objeto.getCliente());
				pesquisaResponsavel.setValue(objeto.getResponsavel());
				txCartaoSus.setValue(objeto.getNumeroCartaoSUS());
				txOcupacao.setText(objeto.getOcupacaoINSS());

				dataAdmissao.setDate(objeto.getDataAdmissao());
				cbProcedencia.setSelectedItem(objeto.getProcedencia());
				cbSituacao.setSelectedItem(TipoStatusPacienteEnum.findByCodigo(objeto.getCodigoStatus()));

				carregarImagemPaciente(objeto);
			}

			@Override
			public boolean preencherObjeto(Paciente objetoPesquisa) {

				objetoPesquisa.setIdPaciente(txCodigo.getValue());
				objetoPesquisa.setCliente(pesquisaCliente.getObjetoPesquisado());
				objetoPesquisa.setResponsavel(pesquisaResponsavel.getObjetoPesquisado());
				objetoPesquisa.setDataAdmissao(dataAdmissao.getDate());
				objetoPesquisa.setNumeroCartaoSUS(txCartaoSus.getValue());
				objetoPesquisa.setOcupacaoINSS(txOcupacao.getText());

				objetoPesquisa.setProcedencia(null);
				objetoPesquisa.setCodigoStatus(null);

				if (cbProcedencia.getSelectedIndex() >= 0) {

					objetoPesquisa.setProcedencia((Procedencia) cbProcedencia.getSelectedItem());
				}

				if (cbSituacao.getSelectedIndex() >= 0) {

					TipoStatusPacienteEnum tipoStatusEnum = (TipoStatusPacienteEnum) cbSituacao.getSelectedItem();

					objetoPesquisa.setCodigoStatus(tipoStatusEnum.getCodigo());
				}

				return true;
			}

		};

		panelActions.addSaveListener(objeto -> txCodigo.setValue(objeto.getIdPaciente()));
		panelActions.addButtonListener(new ButtonActionListenerAdapter() {

			@Override
			public void newEvent() {
				dataAdmissao.setDate(new Date());
				cbSituacao.setSelectedItem(TipoStatusEnum.ATIVO);
			}
		});

		painelContent.add(panelActions, "cell 0 10 4 1,growx,aligny bottom");
	}

	private void carregarImagemPaciente(Paciente paciente) {

		lbImagem.setText("Carregar Imagem");
		lbImagem.setIcon(null);

		if (paciente.getImagemPaciente() != null) {

			byte[] imagem = paciente.getImagemPaciente().getImagem();

			lbImagem.setText("");
			lbImagem.setIcon(ImageUtil.resize(imagem, dimensionsVO.getWidth(), dimensionsVO.getHeight()));
		}
	}

}
