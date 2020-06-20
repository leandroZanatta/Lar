package br.com.lar.ui;

import static br.com.sysdesc.util.enumeradores.TipoBancoEnum.BANCO_SICOOB;

import java.awt.FlowLayout;
import java.text.ParseException;
import java.util.Arrays;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.text.MaskFormatter;

import br.com.sysdesc.boletos.repository.model.Banco;
import br.com.sysdesc.boletos.repository.model.ConfiguracaoBoleto;
import br.com.sysdesc.boletos.service.configuracaoboleto.ConfiguracaoBoletoService;
import br.com.sysdesc.boletos.util.enumeradores.TipoMultaJurosEnum;
import br.com.sysdesc.boletos.util.enumeradores.TipoProtestoEnum;
import br.com.sysdesc.components.AbstractInternalFrame;
import br.com.sysdesc.components.JMoneyField;
import br.com.sysdesc.components.JNumericField;
import br.com.sysdesc.components.JTextFieldId;
import br.com.sysdesc.util.enumeradores.TipoBancoEnum;
import br.com.sysdesc.util.enumeradores.TipoClienteEnum;
import br.com.sysdesc.util.exception.SysDescException;
import net.miginfocom.swing.MigLayout;

public class FrmBancoBoleto extends AbstractInternalFrame {

    private static final long serialVersionUID = 1L;

    private final Banco banco;

    private MaskFormatter mascaraCPF;
    private MaskFormatter mascaraCNPJ;

    private JPanel painelContent;
    private JTextFieldId txCodigo;
    private JLabel lblCodigo;
    private JPanel panelActions;
    private JLabel lbAgencia;
    private JTextField txAgencia;
    private JLabel lbConta;
    private JTextField txContaCorrente;
    private JLabel lbCarteira;
    private JNumericField txCarteira;
    private JPanel pnlTipoPessoa;
    private JLabel lbCgc;
    private JFormattedTextField txCGC;
    private JLabel lbNome;
    private JTextField txNome;
    private JLabel lbNossoNumero;
    private JNumericField txNossoNumero;
    private JButton btSalvar;
    private JButton btCancelar;

    private ButtonGroup buttonGroup;
    private JRadioButton chCPF;
    private JRadioButton chCNPJ;

    private ConfiguracaoBoletoService configuracaoBoletoService = new ConfiguracaoBoletoService();
    private ConfiguracaoBoleto configuracaoBoleto;
    private JLabel lbModalidade;
    private JTextField txModalidade;
    private JLabel lbProtestar;
    private JComboBox<TipoProtestoEnum> cbProtestar;
    private JLabel lbDiasProtesto;
    private JNumericField txDiasProtesto;
    private JPanel panelMulta;
    private JPanel panelJuros;
    private JLabel lbTipoMulta;
    private JLabel lbTipoJuros;
    private JComboBox<TipoMultaJurosEnum> cbTipoJuros;
    private JComboBox<TipoMultaJurosEnum> cbTipoMulta;
    private JLabel lbDiasMulta;
    private JLabel lbDiasJuros;
    private JNumericField txDiasMulta;
    private JNumericField txDiasJuros;
    private JLabel lbValorMulta;
    private JLabel lbValorJuros;
    private JMoneyField txValorJuros;
    private JMoneyField txValorMulta;
    private JLabel lbDiasPagamento;
    private JNumericField txDiasPagamento;
    private JPanel panel_2;

    public FrmBancoBoleto(Long permissaoPrograma, Long codigoUsuario, Banco banco) throws ParseException {
        super(permissaoPrograma, codigoUsuario);

        this.banco = banco;

        initComponents();
    }

    private void initComponents() throws ParseException {

        setSize(451, 401);
        setClosable(Boolean.TRUE);
        setTitle("CONFIGURAÇÃO DE BOLETOS");

        mascaraCPF = new MaskFormatter("###.###.###-##");
        mascaraCNPJ = new MaskFormatter("##.###.###/####-##");

        painelContent = new JPanel();
        panelActions = new JPanel();
        pnlTipoPessoa = new JPanel();

        lblCodigo = new JLabel("Código:");
        lbCgc = new JLabel("CPF/CNPJ:");
        lbNome = new JLabel("Nome:");
        lbAgencia = new JLabel("Ag:");
        lbConta = new JLabel("CC:");
        lbCarteira = new JLabel("Carteira:");
        lbModalidade = new JLabel("Modalidade:");
        lbDiasPagamento = new JLabel("Dias Pagamento:");
        lbNossoNumero = new JLabel("Nosso Número:");
        lbProtestar = new JLabel("Protestar:");
        lbDiasProtesto = new JLabel("Dias Protesto:");

        lbTipoMulta = new JLabel("Tipo:");
        lbDiasMulta = new JLabel("Dias:");
        lbValorMulta = new JLabel("Valor:");

        lbTipoJuros = new JLabel("Tipo:");
        lbDiasJuros = new JLabel("Dias:");
        lbValorJuros = new JLabel("Valor:");

        buttonGroup = new ButtonGroup();
        chCPF = new JRadioButton("CPF");
        chCNPJ = new JRadioButton("CNPJ");
        txCodigo = new JTextFieldId();
        txCGC = new JFormattedTextField();
        txNome = new JTextField();
        txAgencia = new JTextField();
        txContaCorrente = new JTextField();
        txCarteira = new JNumericField();

        btSalvar = new JButton("Salvar");
        btCancelar = new JButton("Cancelar");

        btCancelar.addActionListener((e) -> dispose());
        btSalvar.addActionListener((e) -> salvarConfiguracao());

        chCPF.addActionListener(e -> selecionouPessoaFisica());
        chCNPJ.addActionListener(e -> selecionouPessoaJuridica());

        buttonGroup.add(chCPF);
        buttonGroup.add(chCNPJ);

        painelContent.setLayout(new MigLayout("", "[grow][grow][][grow]", "[][][][][][][grow][grow][grow][grow]"));
        panelActions.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

        pnlTipoPessoa.add(chCPF);
        pnlTipoPessoa.add(chCNPJ);

        painelContent.add(lblCodigo, "cell 0 0");
        painelContent.add(lbCgc, "cell 0 1");
        painelContent.add(lbNome, "cell 0 2");
        painelContent.add(lbAgencia, "cell 0 3");
        painelContent.add(lbConta, "cell 2 3");
        painelContent.add(lbCarteira, "cell 0 4");

        painelContent.add(txCodigo, "cell 1 0,width 50:100:100");
        painelContent.add(txCGC, "cell 1 1 2 1,growx");
        painelContent.add(txNome, "cell 1 2 3 1,growx");
        painelContent.add(txAgencia, "cell 1 3,growx");
        painelContent.add(txContaCorrente, "cell 3 3,growx");

        painelContent.add(txCarteira, "cell 1 4,growx");
        painelContent.add(lbModalidade, "cell 2 4");

        txModalidade = new JTextField();
        painelContent.add(txModalidade, "cell 3 4,growx");

        painelContent.add(lbDiasPagamento, "cell 0 5");

        txDiasPagamento = new JNumericField();
        painelContent.add(txDiasPagamento, "cell 1 5,growx");

        painelContent.add(lbNossoNumero, "cell 2 5");
        txNossoNumero = new JNumericField();
        painelContent.add(txNossoNumero, "cell 3 5,growx");

        panel_2 = new JPanel();
        panel_2.setBorder(
                new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Protesto", TitledBorder.CENTER, TitledBorder.TOP, null, null));
        painelContent.add(panel_2, "cell 0 6 4 1,grow");
        panel_2.setLayout(new MigLayout("", "[][grow][][70px:n:70px]", "[]"));

        panel_2.add(lbProtestar, "cell 0 0");

        cbProtestar = new JComboBox<>();
        Arrays.asList(TipoProtestoEnum.values()).forEach(cbProtestar::addItem);
        panel_2.add(cbProtestar, "cell 1 0,growx");

        panel_2.add(lbDiasProtesto, "cell 2 0");

        txDiasProtesto = new JNumericField();
        panel_2.add(txDiasProtesto, "cell 3 0,growx");

        panelMulta = new JPanel();
        panelMulta.setBorder(
                new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Multa", TitledBorder.CENTER, TitledBorder.TOP, null, null));
        painelContent.add(panelMulta, "cell 0 7 4 1,grow");
        panelMulta.setLayout(new MigLayout("", "[][grow][][70px:n:70px][][grow]", "[]"));

        panelMulta.add(lbTipoMulta, "cell 0 0");

        cbTipoMulta = new JComboBox<>();
        Arrays.asList(TipoMultaJurosEnum.values()).forEach(cbTipoMulta::addItem);
        panelMulta.add(cbTipoMulta, "cell 1 0,growx");

        panelMulta.add(lbDiasMulta, "cell 2 0,aligny baseline");

        txDiasMulta = new JNumericField();
        panelMulta.add(txDiasMulta, "cell 3 0,growx");

        panelMulta.add(lbValorMulta, "cell 4 0,aligny baseline");

        txValorMulta = new JMoneyField();
        panelMulta.add(txValorMulta, "cell 5 0,growx");

        panelJuros = new JPanel();
        panelJuros.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Juros/Mora", TitledBorder.CENTER, TitledBorder.TOP,
                null, null));
        painelContent.add(panelJuros, "cell 0 8 4 1,grow");
        panelJuros.setLayout(new MigLayout("", "[][grow][][70px:n:70px][][grow]", "[]"));

        panelJuros.add(lbTipoJuros, "cell 0 0");

        cbTipoJuros = new JComboBox<>();
        Arrays.asList(TipoMultaJurosEnum.values()).forEach(cbTipoJuros::addItem);
        panelJuros.add(cbTipoJuros, "cell 1 0,growx");

        panelJuros.add(lbDiasJuros, "cell 2 0,aligny baseline");

        txDiasJuros = new JNumericField();
        panelJuros.add(txDiasJuros, "cell 3 0,growx");

        panelJuros.add(lbValorJuros, "cell 4 0");

        txValorJuros = new JMoneyField();
        panelJuros.add(txValorJuros, "cell 5 0,growx");
        painelContent.add(panelActions, "cell 0 9 4 1,growx,aligny bottom");
        painelContent.add(pnlTipoPessoa, "cell 3 0 1 2,alignx right,growy");
        panelActions.add(btSalvar);
        panelActions.add(btCancelar);

        getContentPane().add(painelContent);

        carregarConfiguracoes();
    }

    private void salvarConfiguracao() {
        configuracaoBoleto.setFlagTipoCliente(getTipoPessoa());
        configuracaoBoleto.setCgc(txCGC.getText());
        configuracaoBoleto.setNome(txNome.getText());
        configuracaoBoleto.setCodigoCarteira(txCarteira.getValue());
        configuracaoBoleto.setNumeroAgencia(txAgencia.getText());
        configuracaoBoleto.setNumeroConta(txContaCorrente.getText());
        configuracaoBoleto.setNossoNumero(txNossoNumero.getValue());

        configuracaoBoleto.setModalidade(txModalidade.getText());
        configuracaoBoleto.setCodigoJurosMora(null);
        configuracaoBoleto.setCodigoMulta(null);
        configuracaoBoleto.setCodigoProtesto(null);
        configuracaoBoleto.setDiasJurosMora(txDiasJuros.getValue());
        configuracaoBoleto.setDiasMaximoPagamento(txDiasPagamento.getValue());
        configuracaoBoleto.setDiasMulta(txDiasMulta.getValue());
        configuracaoBoleto.setDiasProtesto(txDiasProtesto.getValue());
        configuracaoBoleto.setValorJurosMora(txValorJuros.getValue());
        configuracaoBoleto.setValorMulta(txValorMulta.getValue());

        if (cbProtestar.getSelectedIndex() >= 0) {
            configuracaoBoleto.setCodigoProtesto(((TipoProtestoEnum) cbProtestar.getSelectedItem()).getCodigo());
        }

        if (cbTipoJuros.getSelectedIndex() >= 0) {
            configuracaoBoleto.setCodigoJurosMora(((TipoMultaJurosEnum) cbTipoJuros.getSelectedItem()).getCodigo());
        }

        if (cbTipoMulta.getSelectedIndex() >= 0) {
            configuracaoBoleto.setCodigoMulta(((TipoMultaJurosEnum) cbTipoMulta.getSelectedItem()).getCodigo());
        }

        try {

            configuracaoBoletoService.validar(configuracaoBoleto);

            configuracaoBoletoService.salvar(configuracaoBoleto);

            JOptionPane.showMessageDialog(this, "Configurações do boleto salvas com sucesso");

            dispose();

        } catch (SysDescException se) {

            JOptionPane.showMessageDialog(this, se.getMensagem());
        }
    }

    private void carregarConfiguracoes() {

        TipoBancoEnum tipoBanco = TipoBancoEnum.forCodigo(banco.getNumeroBanco());
        lbConta.setText(TipoBancoEnum.BANCO_SICOOB.equals(tipoBanco) ? "Cedente:" : "CC:");

        configuracaoBoleto = getConfiguracaoBanco(tipoBanco);

        configurarTipoPessoa(configuracaoBoleto.getFlagTipoCliente());
        txCodigo.setValue(configuracaoBoleto.getIdConfiguracaoBoleto());
        txCGC.setText(configuracaoBoleto.getCgc());
        txNome.setText(configuracaoBoleto.getNome());
        txCarteira.setValue(configuracaoBoleto.getCodigoCarteira());
        txAgencia.setText(configuracaoBoleto.getNumeroAgencia());
        txContaCorrente.setText(configuracaoBoleto.getNumeroConta());
        txNossoNumero.setValue(configuracaoBoleto.getNossoNumero());

        txModalidade.setText(configuracaoBoleto.getModalidade());

        cbProtestar.setSelectedItem(TipoProtestoEnum.findByCodigo(configuracaoBoleto.getCodigoProtesto()));
        cbTipoJuros.setSelectedItem(TipoMultaJurosEnum.findByCodigo(configuracaoBoleto.getCodigoJurosMora()));
        cbTipoMulta.setSelectedItem(TipoMultaJurosEnum.findByCodigo(configuracaoBoleto.getCodigoMulta()));

        configuracaoBoleto.getDiasJurosMora();
        txDiasPagamento.setValue(configuracaoBoleto.getDiasMaximoPagamento());
        txDiasMulta.setValue(configuracaoBoleto.getDiasMulta());
        txDiasJuros.setValue(configuracaoBoleto.getDiasJurosMora());
        txDiasProtesto.setValue(configuracaoBoleto.getDiasProtesto());
        txValorJuros.setValue(configuracaoBoleto.getValorJurosMora());
        txValorMulta.setValue(configuracaoBoleto.getValorMulta());

    }

    private void configurarTipoPessoa(String tipoCliente) {

        if (tipoCliente.equals(TipoClienteEnum.PESSOA_FISICA.getCodigo())) {
            chCPF.setSelected(Boolean.TRUE);
            selecionouPessoaFisica();
        } else {
            chCNPJ.setSelected(Boolean.TRUE);
            selecionouPessoaJuridica();
        }
    }

    private String getTipoPessoa() {

        if (chCPF.isSelected()) {
            return TipoClienteEnum.PESSOA_FISICA.getCodigo();
        }

        return TipoClienteEnum.PESSOA_JURIDICA.getCodigo();
    }

    private ConfiguracaoBoleto getConfiguracaoBanco(TipoBancoEnum tipoBanco) {

        ConfiguracaoBoleto configuracaoBoleto = configuracaoBoletoService.buscarConfiguracao(banco.getIdBanco());

        if (configuracaoBoleto == null) {

            configuracaoBoleto = new ConfiguracaoBoleto();
            configuracaoBoleto.setBanco(this.banco);
            configuracaoBoleto.setFlagTipoCliente(TipoClienteEnum.PESSOA_FISICA.getCodigo());
            configuracaoBoleto.setNumeroAgencia(banco.getNumeroAgencia());
            configuracaoBoleto.setNossoNumero(1L);
            configuracaoBoleto.setNumeroConta(BANCO_SICOOB.equals(tipoBanco) ? "" : banco.getNumeroConta());
            configuracaoBoleto.setDiasJurosMora(0L);
            configuracaoBoleto.setDiasMaximoPagamento(365L);
            configuracaoBoleto.setDiasMulta(0L);
            configuracaoBoleto.setDiasProtesto(30L);
        }

        return configuracaoBoleto;
    }

    private void selecionouPessoaFisica() {
        mascaraCNPJ.uninstall();
        mascaraCPF.install(txCGC);
    }

    private void selecionouPessoaJuridica() {
        mascaraCPF.uninstall();
        mascaraCNPJ.install(txCGC);

    }
}
