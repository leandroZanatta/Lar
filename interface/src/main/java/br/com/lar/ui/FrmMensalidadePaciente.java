package br.com.lar.ui;

import java.util.Date;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import com.toedter.calendar.JDateChooser;

import br.com.lar.repository.model.FormasPagamento;
import br.com.lar.repository.model.MensalidadePaciente;
import br.com.lar.repository.model.Paciente;
import br.com.lar.service.formaspagamento.FormasPagamentoService;
import br.com.lar.service.mensalidade.MensalidadePacienteService;
import br.com.lar.service.paciente.PacienteService;
import br.com.lar.service.parametros.ParametrosService;
import br.com.lar.startup.enumeradores.PesquisaEnum;
import br.com.sysdesc.components.AbstractInternalFrame;
import br.com.sysdesc.components.JMoneyField;
import br.com.sysdesc.components.JNumericField;
import br.com.sysdesc.pesquisa.ui.components.CampoPesquisa;
import br.com.sysdesc.util.classes.ImageUtil;
import br.com.sysdesc.util.enumeradores.FormaPagamentoEnum;
import br.com.sysdesc.util.exception.SysDescException;
import br.com.sysdesc.util.vo.ConfiguracaoMensalidadeVO;
import net.miginfocom.swing.MigLayout;

public class FrmMensalidadePaciente extends AbstractInternalFrame {

    private static final long serialVersionUID = 1L;

    private MensalidadePacienteService mensalidadePacienteService = new MensalidadePacienteService();
    private PacienteService pacienteService = new PacienteService();
    private FormasPagamentoService formasPagamentoService = new FormasPagamentoService();
    private ParametrosService parametrosService = new ParametrosService();

    private JPanel painelContent;
    private JLabel lbPaciente;
    private CampoPesquisa<Paciente> pesquisaPaciente;
    private JLabel lbPagamento;
    private CampoPesquisa<FormasPagamento> pesquisaPagamento;
    private JLabel lbValorMensalidade;
    private JMoneyField txValorMensalidade;
    private JLabel lbDiaVencimento;
    private JLabel lbDiasAntecedencia;
    private JNumericField txDiaVencimento;
    private JNumericField txDiasAntecedencia;
    private JLabel lbDataCadastro;
    private JLabel lbDataManutencao;
    private JLabel lbDataExclusao;
    private JDateChooser txDataCadastro;
    private JDateChooser txDataManutencao;
    private JDateChooser txDataExclusao;
    private JPanel panel;
    private JButton btSalvar;
    private JButton btCancelar;

    private MensalidadePaciente mensalidadePaciente;
    private JPanel panel_1;
    private JCheckBox chAgruparContas;
    private JCheckBox chEnvioAutomatico;

    private ConfiguracaoMensalidadeVO configuracaoMensalidadeVO;

    public FrmMensalidadePaciente(Long permissaoPrograma, Long codigoUsuario) {
        super(permissaoPrograma, codigoUsuario);

        initComponents();
    }

    private void initComponents() {

        setSize(469, 317);
        setClosable(Boolean.TRUE);
        setTitle("MENSALIDADE DOS PACIENTES");

        painelContent = new JPanel();
        lbPaciente = new JLabel("Paciente:");

        painelContent.setLayout(new MigLayout("", "[grow][grow][grow]", "[][22.00][][22.00][][][][][][grow]"));

        getContentPane().add(painelContent);

        pesquisaPaciente = new CampoPesquisa<Paciente>(pacienteService, PesquisaEnum.PES_PACIENTE.getCodigoPesquisa(), getCodigoUsuario()) {

            private static final long serialVersionUID = 1L;

            @Override
            public String formatarValorCampo(Paciente objeto) {

                return String.format("%d - %s", objeto.getIdPaciente(), objeto.getCliente().getNome());
            }

        };

        pesquisaPaciente.addChangeListener((value) -> this.carregarMensalidade(value));

        painelContent.add(pesquisaPaciente, "cell 0 1 3 1,grow");
        painelContent.add(lbPaciente, "cell 0 0");

        lbPagamento = new JLabel("Pagamento:");
        painelContent.add(lbPagamento, "cell 0 2,aligny top");

        pesquisaPagamento = new CampoPesquisa<FormasPagamento>(formasPagamentoService, PesquisaEnum.PES_FORMAS_PAGAMENTO.getCodigoPesquisa(),
                getCodigoUsuario()) {

            private static final long serialVersionUID = 1L;

            @Override
            public String formatarValorCampo(FormasPagamento objeto) {

                return String.format("%d - %s", objeto.getIdFormaPagamento(), objeto.getDescricao());
            }

        };

        pesquisaPagamento.addChangeListener((value) -> this.habilitarCamposBoleto(value));

        painelContent.add(pesquisaPagamento, "cell 0 3 3 1,grow");

        lbValorMensalidade = new JLabel("Valor Mensalidade:");
        painelContent.add(lbValorMensalidade, "cell 0 4");

        lbDiaVencimento = new JLabel("Dia de Vencimento:");
        painelContent.add(lbDiaVencimento, "cell 1 4");

        txValorMensalidade = new JMoneyField();
        painelContent.add(txValorMensalidade, "cell 0 5,growx");

        txDiaVencimento = new JNumericField();
        painelContent.add(txDiaVencimento, "cell 1 5,growx");

        lbDataCadastro = new JLabel("Data de Cadastro:");
        painelContent.add(lbDataCadastro, "cell 0 6");

        lbDataManutencao = new JLabel("Data de Manutenção:");
        painelContent.add(lbDataManutencao, "cell 1 6");

        lbDataExclusao = new JLabel("Data de Exclusão:");
        painelContent.add(lbDataExclusao, "cell 2 6");

        txDataCadastro = new JDateChooser("dd/MM/yyyy", "##/##/#####", '_');
        txDataCadastro.setEnabled(false);
        painelContent.add(txDataCadastro, "cell 0 7,growx");

        txDataManutencao = new JDateChooser("dd/MM/yyyy", "##/##/#####", '_');
        txDataManutencao.setEnabled(false);
        painelContent.add(txDataManutencao, "cell 1 7,growx");

        txDataExclusao = new JDateChooser("dd/MM/yyyy", "##/##/#####", '_');
        painelContent.add(txDataExclusao, "cell 2 7,growx");

        panel_1 = new JPanel();
        panel_1.setBorder(
                new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Boletos", TitledBorder.CENTER, TitledBorder.TOP, null, null));
        painelContent.add(panel_1, "cell 0 8 3 1,grow");
        panel_1.setLayout(new MigLayout("", "[][][grow][]", "[]"));

        chEnvioAutomatico = new JCheckBox("Envio Automático");
        chEnvioAutomatico.setEnabled(false);
        panel_1.add(chEnvioAutomatico, "cell 0 0");

        lbDiasAntecedencia = new JLabel("Dias Antecedência:");
        lbDiasAntecedencia.setEnabled(false);
        panel_1.add(lbDiasAntecedencia, "cell 1 0");

        txDiasAntecedencia = new JNumericField();
        txDiasAntecedencia.setEnabled(false);
        txDiasAntecedencia.setColumns(5);
        panel_1.add(txDiasAntecedencia, "cell 2 0,growx");

        chAgruparContas = new JCheckBox("Agrupar contas");
        chAgruparContas.setEnabled(false);
        panel_1.add(chAgruparContas, "cell 3 0");

        panel = new JPanel();
        painelContent.add(panel, "cell 0 9 3 1,growx,aligny bottom");

        btSalvar = new JButton("Salvar");
        btSalvar.addActionListener(e -> salvarMensalidade());
        panel.add(btSalvar);

        btCancelar = new JButton("Cancelar");
        btCancelar.addActionListener(e -> dispose());
        panel.add(btCancelar);

        btCancelar.setIcon(ImageUtil.resize("cancel.png", 16, 16));
        btSalvar.setIcon(ImageUtil.resize("ok.png", 16, 16));

        this.configuracaoMensalidadeVO = parametrosService.getConfiguracaoMensalidade();
    }

    private void habilitarCamposBoleto(FormasPagamento newValue) {

        panel_1.setEnabled(configuracaoMensalidadeVO != null);
        panel_1.setToolTipText(configuracaoMensalidadeVO != null ? null : "Por favor configure as opções de Mensalidade no cadastro de parâmetros");

        if (configuracaoMensalidadeVO == null) {

            chAgruparContas.setSelected(false);
            chEnvioAutomatico.setSelected(false);
            txDiasAntecedencia.setText("");

        }

        boolean habilitado = configuracaoMensalidadeVO != null && newValue != null
                && FormaPagamentoEnum.BOLETO.equals(FormaPagamentoEnum.forCodigo(newValue.getCodigoFormaPagamento()));

        chAgruparContas.setEnabled(habilitado);
        chEnvioAutomatico.setEnabled(habilitado);
        txDiasAntecedencia.setEnabled(habilitado);
        lbDiasAntecedencia.setEnabled(habilitado);
    }

    private void salvarMensalidade() {

        if (pesquisaPaciente.getObjetoPesquisado() == null) {
            JOptionPane.showMessageDialog(this, "Selecione um paciente");
        }

        Paciente paciente = pesquisaPaciente.getObjetoPesquisado();

        mensalidadePaciente.setPaciente(paciente);
        mensalidadePaciente.setCodigoPaciente(paciente.getIdPaciente());
        mensalidadePaciente.setFormasPagamento(pesquisaPagamento.getObjetoPesquisado());
        mensalidadePaciente.setDataCadastro(txDataCadastro.getDate());
        mensalidadePaciente.setDataManutencao(txDataManutencao.getDate());
        mensalidadePaciente.setDataExclusao(txDataExclusao.getDate());
        mensalidadePaciente.setDiasAntecedencia(txDiasAntecedencia.getValue());
        mensalidadePaciente.setEnvioAutomaticoBoletos(chEnvioAutomatico.isSelected());
        mensalidadePaciente.setAgruparContas(chAgruparContas.isSelected());
        mensalidadePaciente.setDiaVencimento(txDiaVencimento.getValue());
        mensalidadePaciente.setValorMensalidade(txValorMensalidade.getValue());

        try {
            mensalidadePacienteService.validar(mensalidadePaciente, configuracaoMensalidadeVO);

            pesquisaPaciente.getObjetoPesquisado().setMensalidadePaciente(mensalidadePaciente);

            mensalidadePacienteService.salvar(mensalidadePaciente);

            JOptionPane.showMessageDialog(this, "MENSALIDADE DO PACIENTE SALVA COM SUCESSO.");

            dispose();

        } catch (SysDescException e) {

            mensalidadePacienteService.invalidarObjeto();

            JOptionPane.showMessageDialog(this, e.getMensagem());
        }
    }

    private void carregarMensalidade(Paciente newValue) {

        this.mensalidadePaciente = getMensalidade(newValue);

        pesquisaPagamento.setValue(mensalidadePaciente.getFormasPagamento());

        txDataCadastro.setDate(mensalidadePaciente.getDataCadastro() != null ? mensalidadePaciente.getDataCadastro() : new Date());
        txDataManutencao.setDate(new Date());
        txDataExclusao.setDate(mensalidadePaciente.getDataExclusao());
        txDiasAntecedencia.setValue(mensalidadePaciente.getDiasAntecedencia());
        txDiaVencimento.setValue(mensalidadePaciente.getDiaVencimento());
        txValorMensalidade.setValue(mensalidadePaciente.getValorMensalidade());
        chAgruparContas.setSelected(mensalidadePaciente.isAgruparContas());
        chEnvioAutomatico.setSelected(mensalidadePaciente.isEnvioAutomaticoBoletos());
    }

    private MensalidadePaciente getMensalidade(Paciente newValue) {

        if (newValue.getMensalidadePaciente() != null) {
            return newValue.getMensalidadePaciente();
        }

        return new MensalidadePaciente();
    }

}
