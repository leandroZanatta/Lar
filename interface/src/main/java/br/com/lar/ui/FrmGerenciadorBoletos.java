package br.com.lar.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import org.apache.commons.io.FileUtils;

import com.toedter.calendar.JDateChooser;

import br.com.lar.repository.dao.EmailDAO;
import br.com.lar.tablemodels.BoletoRemessaTableModel;
import br.com.lar.tablemodels.BoletoRetornoTableModel;
import br.com.lar.tablemodels.RemessaTableModel;
import br.com.lar.tablemodels.RetornoTableModel;
import br.com.sysdesc.arquivos.exceptions.SysdescArquivosException;
import br.com.sysdesc.arquivos.util.ListUtil;
import br.com.sysdesc.boletos.repository.dao.RetornoDAO;
import br.com.sysdesc.boletos.repository.model.Remessa;
import br.com.sysdesc.boletos.repository.model.RemessaBoleto;
import br.com.sysdesc.boletos.repository.model.Retorno;
import br.com.sysdesc.boletos.service.factory.RetornoFactory;
import br.com.sysdesc.boletos.service.remessa.RemessaService;
import br.com.sysdesc.boletos.service.retorno.RetornoService;
import br.com.sysdesc.components.AbstractInternalFrame;
import br.com.sysdesc.components.ButtonColumn;
import br.com.sysdesc.components.JNumericField;
import br.com.sysdesc.components.renders.ComboBoxRenderer;
import br.com.sysdesc.util.classes.ImageUtil;
import br.com.sysdesc.util.enumeradores.TipoBancoEnum;
import br.com.sysdesc.util.enumeradores.TipoRetornoMovimentoTEnum;
import br.com.sysdesc.util.enumeradores.TipoStatusRemessaEnum;
import br.com.sysdesc.util.enumeradores.TipoStatusRetornoEnum;
import br.com.sysdesc.util.exception.SysDescException;
import br.com.sysdesc.util.resources.Configuracoes;
import br.com.sysdesc.util.vo.PesquisaRemessaRetornoVO;
import lombok.extern.slf4j.Slf4j;
import net.miginfocom.swing.MigLayout;

@Slf4j
public class FrmGerenciadorBoletos extends AbstractInternalFrame {

	private static final long serialVersionUID = 1L;

	private JPanel painelContent;
	private JPanel painelRemessaFiltros;
	private JPanel panel_1;
	private JLabel lblNewLabel_4;
	private JDateChooser dtInicial;
	private JLabel lblNewLabel_5;
	private JDateChooser dtFinal;
	private JLabel lblNewLabel;
	private JLabel lblNewLabel_1;
	private JLabel lblNewLabel_2;
	private JComboBox<TipoStatusRemessaEnum> cbSituacao;
	private JComboBox<TipoBancoEnum> cbBanco;
	private JNumericField txNumero;
	private JLabel lblNewLabel_3;
	private JScrollPane scrollPane;
	private JScrollPane scrollPane_1;
	private JPanel panel_2;
	private JButton btnNewButton;
	private JButton btProcessar;
	private JTable table;
	private JTable table_1;
	private RemessaTableModel remessaTableModel = new RemessaTableModel();
	private RetornoTableModel retornoTableModel = new RetornoTableModel();
	private BoletoRemessaTableModel boletoRemessaTableModel = new BoletoRemessaTableModel();
	private BoletoRetornoTableModel boletoRetornoTableModel = new BoletoRetornoTableModel();
	private RemessaService remessaService = new RemessaService();
	private RetornoService retornoService = new RetornoService();
	private JTabbedPane tabbedPane;
	private JPanel panelRemessa;
	private JPanel panelRetorno;
	private JPanel painelRetornoFiltros;
	private JScrollPane scrollPane_2;
	private JScrollPane scrollPane_3;
	private JPanel panel_5;
	private JButton btnImportar;
	private JTable tbRetorno;
	private JTable table_3;
	private JNumericField txNumero_1;
	private JComboBox<TipoBancoEnum> cbBancoRetorno;
	private JLabel lblNewLabel_6;
	private JLabel lblNewLabel_7;
	private JComboBox<TipoStatusRetornoEnum> cbSituacaoRetorno;
	private JPanel panel_3;
	private JLabel lblNewLabel_8;
	private JDateChooser dtInicial_1;
	private JLabel lblNewLabel_9;
	private JDateChooser dtFinal_1;

	public FrmGerenciadorBoletos(Long permissaoPrograma, Long codigoUsuario) {
		super(permissaoPrograma, codigoUsuario);

		initComponents();
	}

	private void initComponents() {

		setSize(800, 450);
		setClosable(Boolean.TRUE);
		setTitle("GERENCIADOR DE BOLETOS");

		painelContent = new JPanel();
		getContentPane().add(painelContent);
		painelContent.setLayout(new BorderLayout(0, 0));

		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		painelContent.add(tabbedPane);

		panelRemessa = new JPanel();
		tabbedPane.addTab("Remessa", null, panelRemessa, null);
		panelRemessa.setLayout(new MigLayout("", "[grow]", "[][150px:n][][]"));

		painelRemessaFiltros = new JPanel();
		painelRemessaFiltros.setBorder(
				new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Filtros", TitledBorder.CENTER, TitledBorder.TOP, null,
						new Color(0, 0, 0)));
		panelRemessa.add(painelRemessaFiltros, "cell 0 0,grow");
		painelRemessaFiltros.setLayout(new MigLayout("", "[][grow][120.00][275.00]", "[][]"));

		lblNewLabel = new JLabel("Número:");
		painelRemessaFiltros.add(lblNewLabel, "cell 0 0,aligny bottom");

		lblNewLabel_1 = new JLabel("Banco:");
		painelRemessaFiltros.add(lblNewLabel_1, "cell 1 0,aligny bottom");

		lblNewLabel_2 = new JLabel("Situação:");
		painelRemessaFiltros.add(lblNewLabel_2, "cell 2 0,aligny bottom");

		lblNewLabel_3 = new JLabel("Período:");
		painelRemessaFiltros.add(lblNewLabel_3, "cell 3 0");

		panel_1 = new JPanel();
		panel_1.setLayout(null);
		painelRemessaFiltros.add(panel_1, "cell 3 1,grow");

		lblNewLabel_4 = new JLabel("De:");
		lblNewLabel_4.setBounds(10, 3, 23, 14);
		panel_1.add(lblNewLabel_4);

		dtInicial = new JDateChooser("dd/MM/yyyy", "##/##/#####", '_');
		dtInicial.setBounds(38, 0, 100, 20);
		panel_1.add(dtInicial);

		lblNewLabel_5 = new JLabel("Até:");
		lblNewLabel_5.setBounds(145, 3, 25, 14);
		panel_1.add(lblNewLabel_5);

		dtFinal = new JDateChooser("dd/MM/yyyy", "##/##/#####", '_');
		dtFinal.setBounds(170, 0, 100, 20);
		panel_1.add(dtFinal);

		txNumero = new JNumericField();
		painelRemessaFiltros.add(txNumero, "cell 0 1,growx");
		txNumero.setColumns(10);

		cbBanco = new JComboBox<>();
		painelRemessaFiltros.add(cbBanco, "cell 1 1,growx");
		Arrays.asList(TipoBancoEnum.values()).forEach(cbBanco::addItem);
		cbBanco.setSelectedIndex(-1);

		cbSituacao = new JComboBox<>();
		painelRemessaFiltros.add(cbSituacao, "cell 2 1,growx");
		Arrays.asList(TipoStatusRemessaEnum.values()).forEach(cbSituacao::addItem);
		cbSituacao.setSelectedItem(TipoStatusRemessaEnum.GERADO);
		scrollPane = new JScrollPane();
		panelRemessa.add(scrollPane, "cell 0 1,grow");

		table = new JTable(remessaTableModel);
		scrollPane.setViewportView(table);

		ComboBoxRenderer comboboxRender = new ComboBoxRenderer(table, 4);
		Arrays.asList(TipoStatusRemessaEnum.values()).forEach(comboboxRender::addItem);
		ButtonColumn buttonColumn = new ButtonColumn(table, 5, "", ImageUtil.resize("save_file.png", 18, 18));
		buttonColumn.addButtonListener(e -> salvarArquivo());
		scrollPane_1 = new JScrollPane();
		panelRemessa.add(scrollPane_1, "cell 0 2,grow");

		table_1 = new JTable(boletoRemessaTableModel);
		scrollPane_1.setViewportView(table_1);

		panel_2 = new JPanel();
		panelRemessa.add(panel_2, "cell 0 3,grow");

		btnNewButton = new JButton("Novo");
		panel_2.add(btnNewButton);

		btProcessar = new JButton("Processar");
		panel_2.add(btProcessar);

		panelRetorno = new JPanel();
		tabbedPane.addTab("Retorno", null, panelRetorno, null);
		panelRetorno.setLayout(new MigLayout("", "[55px,grow]", "[61px][150px:n][][]"));

		painelRetornoFiltros = new JPanel();
		painelRetornoFiltros.setBorder(
				new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Filtros", TitledBorder.CENTER, TitledBorder.TOP, null,

						new Color(0, 0, 0)));
		panelRetorno.add(painelRetornoFiltros, "cell 0 0,growx,aligny top");
		painelRetornoFiltros.setLayout(new MigLayout("", "[][grow][120.00][275.00]", "[][]"));

		lblNewLabel_6 = new JLabel("Número:");
		painelRetornoFiltros.add(lblNewLabel_6, "cell 0 0");

		lblNewLabel_7 = new JLabel("Banco:");
		painelRetornoFiltros.add(lblNewLabel_7, "cell 1 0");

		JLabel lblNewLabel_2_1 = new JLabel("Situação:");
		painelRetornoFiltros.add(lblNewLabel_2_1, "cell 2 0");

		JLabel lblNewLabel_3_1 = new JLabel("Período:");
		painelRetornoFiltros.add(lblNewLabel_3_1, "cell 3 0");

		txNumero_1 = new JNumericField();
		txNumero_1.setColumns(10);
		painelRetornoFiltros.add(txNumero_1, "cell 0 1,growx");

		cbBancoRetorno = new JComboBox<>();
		cbBancoRetorno.setSelectedIndex(-1);
		painelRetornoFiltros.add(cbBancoRetorno, "cell 1 1,growx");
		Arrays.asList(TipoBancoEnum.values()).forEach(cbBancoRetorno::addItem);
		cbBancoRetorno.setSelectedIndex(-1);
		cbSituacaoRetorno = new JComboBox<>();
		painelRetornoFiltros.add(cbSituacaoRetorno, "cell 2 1,growx");
		Arrays.asList(TipoStatusRetornoEnum.values()).forEach(cbSituacaoRetorno::addItem);
		cbSituacaoRetorno.setSelectedItem(TipoStatusRetornoEnum.PROCESSADO);
		panel_3 = new JPanel();
		panel_3.setLayout(null);
		painelRetornoFiltros.add(panel_3, "cell 3 1,grow");

		lblNewLabel_8 = new JLabel("De:");
		lblNewLabel_8.setBounds(10, 3, 23, 14);
		panel_3.add(lblNewLabel_8);

		dtInicial_1 = new JDateChooser("dd/MM/yyyy", "##/##/#####", '_');
		dtInicial_1.setBounds(38, 0, 100, 20);
		panel_3.add(dtInicial_1);

		lblNewLabel_9 = new JLabel("Até:");
		lblNewLabel_9.setBounds(145, 3, 25, 14);
		panel_3.add(lblNewLabel_9);

		dtFinal_1 = new JDateChooser("dd/MM/yyyy", "##/##/#####", '_');
		dtFinal_1.setBounds(170, 0, 100, 20);
		panel_3.add(dtFinal_1);

		JLabel lblNewLabel_5_1 = new JLabel("Até:");
		lblNewLabel_5_1.setBounds(83, 6, 25, 14);
		panel_3.add(lblNewLabel_5_1);

		scrollPane_2 = new JScrollPane();
		panelRetorno.add(scrollPane_2, "cell 0 1,grow");

		tbRetorno = new JTable(retornoTableModel);
		scrollPane_2.setViewportView(tbRetorno);

		scrollPane_3 = new JScrollPane();
		panelRetorno.add(scrollPane_3, "cell 0 2,grow");

		table_3 = new JTable(boletoRetornoTableModel);
		scrollPane_3.setViewportView(table_3);

		panel_5 = new JPanel();
		panelRetorno.add(panel_5, "cell 0 3,grow");

		btnImportar = new JButton("Importar");
		btnImportar.addActionListener(e -> importarRetorno());

		panel_5.add(btnImportar);
		btProcessar.addActionListener(e -> remessaService.processarRemessaBancosSuportados());

		FocusAdapter focusAdapter = new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				carregarRemessas();
			}
		};
		cbBanco.addActionListener(e -> carregarRemessas());
		cbSituacao.addActionListener(e -> carregarRemessas());
		txNumero.addFocusListener(focusAdapter);
		dtInicial.addPropertyChangeListener(e -> carregarRemessas());
		dtFinal.addPropertyChangeListener(e -> carregarRemessas());

		ListSelectionModel rowSelMod = table.getSelectionModel();

		rowSelMod.addListSelectionListener(e -> {

			if (!e.getValueIsAdjusting()) {

				carregarBoletos();
			}
		});

		remessaTableModel.addActionListener(e -> this.carregarRemessas());

		table.getColumnModel().getColumn(5).setMaxWidth(50);
		tbRetorno.getColumnModel().getColumn(5).setMaxWidth(50);

		table_1.getColumnModel().getColumn(0).setMaxWidth(80);
		table_1.getColumnModel().getColumn(2).setMaxWidth(120);
		table_1.getColumnModel().getColumn(3).setMaxWidth(75);
		table_1.getColumnModel().getColumn(4).setMaxWidth(180);
		table_1.getColumnModel().getColumn(2).setPreferredWidth(120);
		table_1.getColumnModel().getColumn(3).setPreferredWidth(75);
		table_1.getColumnModel().getColumn(4).setPreferredWidth(180);

		ButtonColumn buttonColumnRetorno = new ButtonColumn(tbRetorno, 5, "", ImageUtil.resize("save_file.png", 18, 18));
		buttonColumnRetorno.addButtonListener(e -> salvarArquivoRetorno());

		FocusAdapter focusAdapterRetorno = new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				carregarRetornos();
			}
		};

		cbBancoRetorno.addActionListener(e -> carregarRetornos());
		cbSituacaoRetorno.addActionListener(e -> carregarRetornos());
		txNumero_1.addFocusListener(focusAdapterRetorno);
		dtInicial_1.addPropertyChangeListener(e -> carregarRetornos());
		dtFinal_1.addPropertyChangeListener(e -> carregarRetornos());

		tabbedPane.addChangeListener(e -> tabbedChange());

		ListSelectionModel rowSelModRetorno = tbRetorno.getSelectionModel();

		rowSelModRetorno.addListSelectionListener(e -> {

			if (!e.getValueIsAdjusting()) {

				carregarBoletosRetorno();
			}
		});

		table_3.getColumnModel().getColumn(0).setMaxWidth(80);
		table_3.getColumnModel().getColumn(1).setPreferredWidth(200);
		table_3.getColumnModel().getColumn(2).setMaxWidth(150);
		table_3.getColumnModel().getColumn(2).setMinWidth(150);
	}

	private void tabbedChange() {

		if (tabbedPane.getSelectedIndex() == 0) {
			carregarRemessas();
		} else {
			carregarRetornos();
		}
	}

	private void salvarArquivo() {

		if (table.getSelectedRow() >= 0) {

			try {

				Remessa remessa = remessaTableModel.getRow(table.getSelectedRow());

				File arquivoRemessa = new File(Configuracoes.FOLDER_TRANSFER + File.separator + remessa.getNumeroBanco() + File.separator
						+ String.format("remesa-%d.rem", remessa.getNumeroRemessa()));

				FileUtils.writeByteArrayToFile(arquivoRemessa, remessa.getArquivo());

				Runtime.getRuntime().exec("explorer.exe /select," + arquivoRemessa.getAbsoluteFile());

			} catch (Exception e) {

				log.error("Falha ao gerar arquivo de remessa", e);

				JOptionPane.showMessageDialog(this, "Falha ao gerar arquivo de remessa");
			}
		}
	}

	private void salvarArquivoRetorno() {

		if (tbRetorno.getSelectedRow() >= 0) {

			try {

				Retorno retorno = retornoTableModel.getRow(tbRetorno.getSelectedRow());

				File arquivoRetorno = new File(Configuracoes.FOLDER_TRANSFER + File.separator + retorno.getNumeroBanco() + File.separator
						+ String.format("retorno-%d.ret", retorno.getNumeroRetono()));

				FileUtils.writeByteArrayToFile(arquivoRetorno, retorno.getArquivo());

				Runtime.getRuntime().exec("explorer.exe /select," + arquivoRetorno.getAbsoluteFile());

			} catch (Exception e) {

				log.error("Falha ao gerar arquivo de retorno", e);

				JOptionPane.showMessageDialog(this, "Falha ao gerar arquivo de retorno");
			}
		}
	}

	private void carregarRetornos() {

		PesquisaRemessaRetornoVO pesquisa = new PesquisaRemessaRetornoVO();
		pesquisa.setDataInicial(dtInicial_1.getDate());
		pesquisa.setDataFinal(dtFinal_1.getDate());
		pesquisa.setNumeroDocumento(txNumero_1.getValue());

		if (cbBancoRetorno.getSelectedIndex() >= 0) {
			pesquisa.setCodigoBanco(((TipoBancoEnum) cbBancoRetorno.getSelectedItem()).getCodigo());
		}

		if (cbSituacao.getSelectedIndex() >= 0) {
			pesquisa.setCodigoStatus(((TipoStatusRetornoEnum) cbSituacaoRetorno.getSelectedItem()).getCodigo());
		}

		retornoTableModel.setRows(retornoService.pesquisarRetornos(pesquisa));

		boletoRetornoTableModel.clear();
	}

	private void carregarRemessas() {

		PesquisaRemessaRetornoVO pesquisa = new PesquisaRemessaRetornoVO();
		pesquisa.setDataInicial(dtInicial.getDate());
		pesquisa.setDataFinal(dtFinal.getDate());
		pesquisa.setNumeroDocumento(txNumero.getValue());

		if (cbBanco.getSelectedIndex() >= 0) {
			pesquisa.setCodigoBanco(((TipoBancoEnum) cbBanco.getSelectedItem()).getCodigo());
		}

		if (cbSituacao.getSelectedIndex() >= 0) {
			pesquisa.setCodigoStatus(((TipoStatusRemessaEnum) cbSituacao.getSelectedItem()).getCodigo());
		}

		remessaTableModel.setRows(remessaService.pesquisarRemessas(pesquisa));

		boletoRemessaTableModel.clear();
	}

	private void carregarBoletos() {

		if (table.getSelectedRow() >= 0) {

			boletoRemessaTableModel.setRows(remessaTableModel.getRow(table.getSelectedRow()).getRemessaBoletos().stream()
					.map(RemessaBoleto::getBoleto).collect(Collectors.toList()));
		}
	}

	private void carregarBoletosRetorno() {

		if (tbRetorno.getSelectedRow() >= 0) {

			boletoRetornoTableModel.setRows(retornoTableModel.getRow(tbRetorno.getSelectedRow()).getRetornoBoletos());
		}
	}

	private void importarRetorno() {

		JFileChooser fileChooser = new JFileChooser();

		int i = fileChooser.showOpenDialog(null);

		if (i != 1) {

			try {
				File arquivo = fileChooser.getSelectedFile();

				List<String> arquivos = FileUtils.readLines(arquivo, StandardCharsets.UTF_8);

				Retorno retorno = new RetornoFactory().getRetornoBuilder(arquivos).build();

				new RetornoDAO().salvar(retorno);

				List<Long> codigoBoletosConfirmado = retorno.getRetornoBoletos().stream()
						.filter(retornoBoleto -> retornoBoleto.getTipoRetorno().equals(TipoRetornoMovimentoTEnum.ENTRADA_CONFIRMADA.getCodigo())
								&& retornoBoleto.getBoleto() != null)
						.mapToLong(retornoBoleto -> retornoBoleto.getBoleto().getIdBoleto()).boxed().collect(Collectors.toList());

				if (!ListUtil.isNullOrEmpty(codigoBoletosConfirmado)) {

					new EmailDAO().marcarBoletosParaEnvio(codigoBoletosConfirmado);
				}

				JOptionPane.showMessageDialog(this, "Importação do Retorno realizada com sucesso");

				carregarRetornos();

			} catch (SysdescArquivosException | SysDescException e) {

				JOptionPane.showMessageDialog(this, e.getMessage());
			} catch (IOException e) {

				log.error("Ocorreu um erro ao efetuar a leitura do arquivo de retorno", e);

				JOptionPane.showMessageDialog(this, "Ocorreu um erro ao efetuar a leitura do arquivo de retorno");
			}
		}
	}
}
