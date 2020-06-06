package br.com.lar.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.File;
import java.util.Arrays;
import java.util.stream.Collectors;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.commons.io.FileUtils;

import com.toedter.calendar.JDateChooser;

import br.com.lar.tablemodels.BoletoRemessaTableModel;
import br.com.lar.tablemodels.RemessaTableModel;
import br.com.syscesc.boleto.service.remessa.RemessaService;
import br.com.sysdesc.boleto.repository.model.Remessa;
import br.com.sysdesc.boleto.repository.model.RemessaBoleto;
import br.com.sysdesc.components.AbstractInternalFrame;
import br.com.sysdesc.components.ButtonColumn;
import br.com.sysdesc.components.JNumericField;
import br.com.sysdesc.components.renders.ComboBoxRenderer;
import br.com.sysdesc.util.classes.ImageUtil;
import br.com.sysdesc.util.enumeradores.TipoBancoEnum;
import br.com.sysdesc.util.enumeradores.TipoStatusRemessaEnum;
import br.com.sysdesc.util.resources.Configuracoes;
import br.com.sysdesc.util.vo.PesquisaRemessaVO;
import lombok.extern.slf4j.Slf4j;
import net.miginfocom.swing.MigLayout;

@Slf4j
public class FrmGerenciadorBoletos extends AbstractInternalFrame {

    private static final long serialVersionUID = 1L;

    private JPanel painelContent;
    private JPanel panel;
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
    private BoletoRemessaTableModel boletoRemessaTableModel = new BoletoRemessaTableModel();
    private RemessaService remessaService = new RemessaService();
    private JTabbedPane tabbedPane;
    private JPanel panelRemessa;
    private JPanel panelRetorno;
    private JPanel panel_4;
    private JScrollPane scrollPane_2;
    private JScrollPane scrollPane_3;
    private JPanel panel_5;
    private JButton btnImportar;
    private JTable table_2;
    private JTable table_3;

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
        panelRemessa.setLayout(new MigLayout("", "[grow]", "[][grow][][150px:n][][]"));

        panel = new JPanel();
        panel.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Filtros", TitledBorder.CENTER, TitledBorder.TOP, null,
                new Color(0, 0, 0)));
        panelRemessa.add(panel, "cell 0 2,grow");
        panel.setLayout(new MigLayout("", "[][grow][120.00][275.00]", "[][]"));

        lblNewLabel = new JLabel("Número:");
        panel.add(lblNewLabel, "cell 0 0,aligny bottom");

        lblNewLabel_1 = new JLabel("Banco:");
        panel.add(lblNewLabel_1, "cell 1 0,aligny bottom");

        lblNewLabel_2 = new JLabel("Situação:");
        panel.add(lblNewLabel_2, "cell 2 0,aligny bottom");

        lblNewLabel_3 = new JLabel("Período:");
        panel.add(lblNewLabel_3, "cell 3 0");

        panel_1 = new JPanel();
        panel_1.setLayout(null);
        panel.add(panel_1, "cell 3 1,grow");

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
        panel.add(txNumero, "cell 0 1,growx");
        txNumero.setColumns(10);

        cbBanco = new JComboBox<>();
        panel.add(cbBanco, "cell 1 1,growx");
        Arrays.asList(TipoBancoEnum.values()).forEach(cbBanco::addItem);
        cbBanco.setSelectedIndex(-1);

        cbSituacao = new JComboBox<>();
        panel.add(cbSituacao, "cell 2 1,growx");
        Arrays.asList(TipoStatusRemessaEnum.values()).forEach(cbSituacao::addItem);
        cbSituacao.setSelectedItem(TipoStatusRemessaEnum.GERADO);
        scrollPane = new JScrollPane();
        panelRemessa.add(scrollPane, "cell 0 3,grow");

        table = new JTable(remessaTableModel);
        scrollPane.setViewportView(table);

        ComboBoxRenderer comboboxRender = new ComboBoxRenderer(table, 4);
        Arrays.asList(TipoStatusRemessaEnum.values()).forEach(comboboxRender::addItem);
        ButtonColumn buttonColumn = new ButtonColumn(table, 5, "", ImageUtil.resize("save_file.png", 18, 18));
        buttonColumn.addButtonListener(e -> salvarArquivo());
        scrollPane_1 = new JScrollPane();
        panelRemessa.add(scrollPane_1, "cell 0 4,grow");

        table_1 = new JTable(boletoRemessaTableModel);
        scrollPane_1.setViewportView(table_1);

        panel_2 = new JPanel();
        panelRemessa.add(panel_2, "cell 0 5,grow");

        btnNewButton = new JButton("Novo");
        panel_2.add(btnNewButton);

        btProcessar = new JButton("Processar");
        panel_2.add(btProcessar);

        panelRetorno = new JPanel();
        tabbedPane.addTab("Retorno", null, panelRetorno, null);
        panelRetorno.setLayout(new MigLayout("", "[55px,grow]", "[61px][150px:n][][]"));

        panel_4 = new JPanel();
        panel_4.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Filtros", TitledBorder.CENTER, TitledBorder.TOP, null,

                new Color(0, 0, 0)));
        panelRetorno.add(panel_4, "cell 0 0,growx,aligny top");
        panel_4.setLayout(new MigLayout("", "[]", "[]"));

        scrollPane_2 = new JScrollPane();
        panelRetorno.add(scrollPane_2, "cell 0 1,grow");

        table_2 = new JTable();
        scrollPane_2.setViewportView(table_2);

        scrollPane_3 = new JScrollPane();
        panelRetorno.add(scrollPane_3, "cell 0 2,grow");

        table_3 = new JTable();
        scrollPane_3.setViewportView(table_3);

        panel_5 = new JPanel();
        panelRetorno.add(panel_5, "cell 0 3,grow");

        btnImportar = new JButton("Importar");
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

        rowSelMod.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {

                if (!e.getValueIsAdjusting()) {

                    carregarBoletos();
                }
            }

        });

        remessaTableModel.addActionListener(e -> this.carregarRemessas());

        table.getColumnModel().getColumn(5).setMaxWidth(50);

        table_1.getColumnModel().getColumn(0).setMaxWidth(80);
        table_1.getColumnModel().getColumn(2).setMaxWidth(120);
        table_1.getColumnModel().getColumn(3).setMaxWidth(75);
        table_1.getColumnModel().getColumn(4).setMaxWidth(180);
        table_1.getColumnModel().getColumn(2).setPreferredWidth(120);
        table_1.getColumnModel().getColumn(3).setPreferredWidth(75);
        table_1.getColumnModel().getColumn(4).setPreferredWidth(180);
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

    private void carregarRemessas() {

        PesquisaRemessaVO pesquisa = new PesquisaRemessaVO();
        pesquisa.setDataInicial(dtInicial.getDate());
        pesquisa.setDataFinal(dtFinal.getDate());
        pesquisa.setNumeroRemessa(txNumero.getValue());

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

}
