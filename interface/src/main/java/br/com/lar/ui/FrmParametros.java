package br.com.lar.ui;

import java.awt.Color;
import java.awt.FlowLayout;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import com.google.gson.Gson;

import br.com.lar.repository.model.MensalidadePaciente;
import br.com.lar.repository.model.Parametros;
import br.com.lar.repository.model.PlanoContas;
import br.com.lar.service.email.ServidorEmail;
import br.com.lar.service.mensalidade.MensalidadePacienteService;
import br.com.lar.service.parametros.ParametrosService;
import br.com.lar.service.planocontas.PlanoContasService;
import br.com.lar.startup.enumeradores.PesquisaEnum;
import br.com.sysdesc.components.AbstractInternalFrame;
import br.com.sysdesc.components.JNumericField;
import br.com.sysdesc.pesquisa.ui.components.CampoPesquisa;
import br.com.sysdesc.util.classes.CryptoUtil;
import br.com.sysdesc.util.classes.ImageUtil;
import br.com.sysdesc.util.classes.ListUtil;
import br.com.sysdesc.util.classes.LongUtil;
import br.com.sysdesc.util.classes.StringUtil;
import br.com.sysdesc.util.constants.MensagemConstants;
import br.com.sysdesc.util.enumeradores.ConfiguracaoEmailEnum;
import br.com.sysdesc.util.enumeradores.TipoContasReceberEnum;
import br.com.sysdesc.util.enumeradores.TipoSaldoEnum;
import br.com.sysdesc.util.exception.SysDescException;
import br.com.sysdesc.util.resources.Resources;
import br.com.sysdesc.util.vo.ConfiguracaoBoletoVO;
import br.com.sysdesc.util.vo.ConfiguracaoContasReceberVO;
import br.com.sysdesc.util.vo.ConfiguracaoDiarioVO;
import br.com.sysdesc.util.vo.ConfiguracaoEmailVO;
import br.com.sysdesc.util.vo.ConfiguracaoMensalidadeVO;
import net.miginfocom.swing.MigLayout;

public class FrmParametros extends AbstractInternalFrame {

    private static final long serialVersionUID = 1L;

    private JPanel panelActions;
    private JButton btSalvar;
    private JButton btCancelar;
    private JTabbedPane tabbedPane;
    private JPanel painelMensalidades;
    private JPanel panelContasReceber;
    private JLabel lbFormaGeracao;
    private JComboBox<TipoContasReceberEnum> cbFormaGeracao;
    private JLabel lbDias;
    private JNumericField txNumeroDias;
    private JPanel panelDiario;
    private JLabel lbContaDebito;
    private CampoPesquisa<PlanoContas> pesquisaPlanoDebito;
    private JLabel lbContaCredito;
    private CampoPesquisa<PlanoContas> pesquisaPlanoCredito;

    private Parametros parametros;

    private PlanoContasService planoContasService = new PlanoContasService();
    private ParametrosService parametrosService = new ParametrosService();
    private MensalidadePacienteService mensalidadePacienteService = new MensalidadePacienteService();
    private JCheckBox chGerarBoleto;
    private JPanel panelBoletos;
    private JCheckBox chEnviarEmail;
    private JPanel panelEmail;
    private JLabel lbConfiguracao;
    private JComboBox<ConfiguracaoEmailEnum> cbConfiguracao;
    private JLabel lbUsuario;
    private JTextField txUsuario;
    private JLabel lbSenha;
    private JPasswordField txSenha;
    private JButton btTestar;

    public FrmParametros(Long permissaoPrograma, Long codigoUsuario) {
        super(permissaoPrograma, codigoUsuario);

        initComponents();
    }

    private void initComponents() {

        EtchedBorder etchedBorder = new EtchedBorder(EtchedBorder.LOWERED, null, null);

        tabbedPane = new JTabbedPane(JTabbedPane.TOP);

        painelMensalidades = new JPanel();

        panelDiario = new JPanel();
        lbFormaGeracao = new JLabel("Forma de geração:");
        lbDias = new JLabel("Dia:");
        cbFormaGeracao = new JComboBox<>();
        txNumeroDias = new JNumericField();

        panelContasReceber = new JPanel();
        lbContaDebito = new JLabel("Contas a Receber:");
        lbContaCredito = new JLabel("Conta de receita:");
        pesquisaPlanoCredito = new CampoPesquisa<PlanoContas>(planoContasService, PesquisaEnum.PES_PLANOCONTAS.getCodigoPesquisa(),
                getCodigoUsuario(), planoContasService.getPreFilter(TipoSaldoEnum.CREDOR, false)) {

            private static final long serialVersionUID = 1L;

            @Override
            public String formatarValorCampo(PlanoContas objeto) {

                return String.format("%d - %s %s", objeto.getIdPlanoContas(), objeto.getIdentificador(), objeto.getDescricao());
            }
        };
        pesquisaPlanoDebito = new CampoPesquisa<PlanoContas>(planoContasService, PesquisaEnum.PES_PLANOCONTAS.getCodigoPesquisa(), getCodigoUsuario(),
                planoContasService.getPreFilter(TipoSaldoEnum.ATIVO, false)) {

            private static final long serialVersionUID = 1L;

            @Override
            public String formatarValorCampo(PlanoContas objeto) {

                return String.format("%d - %s %s", objeto.getIdPlanoContas(), objeto.getIdentificador(), objeto.getDescricao());
            }
        };

        cbFormaGeracao.addItemListener(e -> changeTipoContasReceber((TipoContasReceberEnum) e.getItem()));

        panelEmail = new JPanel();
        tabbedPane.addTab("Email", null, panelEmail, null);
        panelEmail.setLayout(new MigLayout("", "[grow][grow]", "[][][][][]"));

        lbConfiguracao = new JLabel("Configuração:");
        panelEmail.add(lbConfiguracao, "cell 0 0,alignx left");

        cbConfiguracao = new JComboBox<>();
        panelEmail.add(cbConfiguracao, "cell 0 1 2 1,growx");

        lbUsuario = new JLabel("Usuário:");
        panelEmail.add(lbUsuario, "cell 0 2");

        lbSenha = new JLabel("Senha:");
        panelEmail.add(lbSenha, "cell 1 2");

        txUsuario = new JTextField();
        panelEmail.add(txUsuario, "cell 0 3,growx");
        txUsuario.setColumns(10);

        txSenha = new JPasswordField();
        panelEmail.add(txSenha, "cell 1 3,growx");
        txSenha.setColumns(10);

        btTestar = new JButton("Testar Envio");
        btTestar.addActionListener(e -> testarEnvioEmail());
        panelEmail.add(btTestar, "cell 0 4,aligny bottom");

        tabbedPane.addTab("Mensalidades", null, painelMensalidades, null);

        getContentPane().setLayout(new MigLayout("", "[280.00px,grow]", "[grow][]"));
        getContentPane().add(tabbedPane, "cell 0 0,grow");

        painelMensalidades.setLayout(new MigLayout("", "[grow]", "[][][]"));
        panelContasReceber.setLayout(new MigLayout("", "[258.00px,grow][]", "[14px][]"));
        panelDiario.setLayout(new MigLayout("", "[grow]", "[][20.00][][20.00,grow]"));

        panelDiario.setBorder(new TitledBorder(etchedBorder, "Diário", TitledBorder.CENTER, TitledBorder.TOP, null, null));
        panelContasReceber
                .setBorder(new TitledBorder(etchedBorder, "Contas a Receber", TitledBorder.CENTER, TitledBorder.TOP, null, new Color(0, 0, 0)));

        Arrays.asList(TipoContasReceberEnum.values()).forEach(cbFormaGeracao::addItem);
        txNumeroDias.setColumns(2);

        painelMensalidades.add(panelContasReceber, "cell 0 0,grow");

        panelBoletos = new JPanel();
        panelBoletos.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Boletos", TitledBorder.CENTER, TitledBorder.TOP,
                null, new Color(0, 0, 0)));
        painelMensalidades.add(panelBoletos, "cell 0 1,growx");
        FlowLayout fl_panelBoletos = (FlowLayout) panelBoletos.getLayout();
        fl_panelBoletos.setAlignment(FlowLayout.LEFT);

        chGerarBoleto = new JCheckBox("Gerar Boletos Automaticamente");

        panelBoletos.add(chGerarBoleto);

        chEnviarEmail = new JCheckBox("Enviar Email");
        panelBoletos.add(chEnviarEmail);
        painelMensalidades.add(panelDiario, "cell 0 2,grow");

        panelContasReceber.add(lbFormaGeracao, "cell 0 0,alignx left,aligny top");
        panelContasReceber.add(lbDias, "cell 1 0,aligny baseline");
        panelContasReceber.add(cbFormaGeracao, "cell 0 1,growx");
        panelContasReceber.add(txNumeroDias, "cell 1 1,growx");
        panelDiario.add(lbContaDebito, "cell 0 0");
        panelDiario.add(pesquisaPlanoDebito, "cell 0 1,grow");
        panelDiario.add(lbContaCredito, "cell 0 2");
        panelDiario.add(pesquisaPlanoCredito, "cell 0 3,grow");
        Arrays.asList(ConfiguracaoEmailEnum.values()).forEach(cbConfiguracao::addItem);

        setTitle("CADASTRO DE PARÂMETROS");
        setSize(500, 400);
        setClosable(Boolean.TRUE);

        panelActions = new JPanel();
        btSalvar = new JButton("Salvar");
        btSalvar.addActionListener(e -> salvarConfiguracoes());
        btCancelar = new JButton("Cancelar");

        panelActions.add(btSalvar);
        panelActions.add(btCancelar);
        getContentPane().add(panelActions, "cell 0 1,growx,aligny bottom");

        chGerarBoleto.addItemListener(e -> {

            boolean selected = ((JCheckBox) e.getItem()).isSelected();

            if (!selected) {
                chEnviarEmail.setSelected(false);
            }

            chEnviarEmail.setEnabled(selected);

        });

        btCancelar.addActionListener(e -> dispose());
        btCancelar.setIcon(ImageUtil.resize("cancel.png", 16, 16));
        btSalvar.setIcon(ImageUtil.resize("ok.png", 16, 16));

        carregarParametros();
    }

    private boolean testarEnvioEmail() {

        if (cbConfiguracao.getSelectedIndex() < 0) {

            JOptionPane.showMessageDialog(this, "SELECIONE UMA CONFIGURAÇÃO DE EMAIL");

            return false;
        }

        if (StringUtil.isNullOrEmpty(txUsuario.getText())) {

            JOptionPane.showMessageDialog(this, Resources.translate(MensagemConstants.MENSAGEM_INSIRA_USUARIO));

            return false;
        }

        if (StringUtil.isNullOrEmpty(StringUtil.arrayToString(txSenha.getPassword()))) {

            JOptionPane.showMessageDialog(this, Resources.translate(MensagemConstants.MENSAGEM_INSIRA_SENHA));

            return false;
        }

        String assunto = "Teste de configuração de email";
        String mensagem = "Se voçê está vendo esta mensagem a configuração de email do aplicativo L.A.R foi efetuada com sucesso";

        boolean enviouMensagem = ServidorEmail.getTestInstance(getCongfiguracaoEmailVO()).sendMessage(txUsuario.getText(), assunto, mensagem);

        if (enviouMensagem) {

            JOptionPane.showMessageDialog(this, "Email de teste enviado com sucesso");
        } else {

            JOptionPane.showMessageDialog(this, "Ocorreu um erro ao enviar a mensagem de teste\nConsulte o log para mais informações");
        }

        return enviouMensagem;

    }

    private void salvarConfiguracoes() {

        try {

            validarConfiguracoes();

            ConfiguracaoMensalidadeVO configuracaoMensalidadeVO = new ConfiguracaoMensalidadeVO();

            TipoContasReceberEnum tipoContasReceberEnum = (TipoContasReceberEnum) cbFormaGeracao.getSelectedItem();
            Long numeroDias = tipoContasReceberEnum.isUsaDias() ? txNumeroDias.getValue() : null;

            ConfiguracaoContasReceberVO configuracaoContasReceberVO = new ConfiguracaoContasReceberVO();
            configuracaoContasReceberVO.setFormaContasReceber(tipoContasReceberEnum.getCodigo());
            configuracaoContasReceberVO.setDiasGeracao(numeroDias);

            ConfiguracaoDiarioVO configuracaoDiarioVO = new ConfiguracaoDiarioVO();
            configuracaoDiarioVO.setContaCliente(pesquisaPlanoDebito.getObjetoPesquisado().getIdPlanoContas());
            configuracaoDiarioVO.setContaReceita(pesquisaPlanoCredito.getObjetoPesquisado().getIdPlanoContas());

            ConfiguracaoBoletoVO configuracaoBoletoVO = new ConfiguracaoBoletoVO();
            configuracaoBoletoVO.setEnviarEmail(chEnviarEmail.isSelected());
            configuracaoBoletoVO.setGerarboletos(chGerarBoleto.isSelected());

            configuracaoMensalidadeVO.setConfiguracaoContasReceber(configuracaoContasReceberVO);
            configuracaoMensalidadeVO.setConfiguracaoDiario(configuracaoDiarioVO);
            configuracaoMensalidadeVO.setConfiguracaoBoletoVO(configuracaoBoletoVO);

            String configuracaoEmail = null;

            if (cbConfiguracao.getSelectedIndex() >= 0) {

                ConfiguracaoEmailVO configuracaoEmailVO = getCongfiguracaoEmailVO();

                configuracaoEmail = CryptoUtil.toBlowfish(new Gson().toJson(configuracaoEmailVO));
            }

            parametros.setManutencao(new Date());
            parametros.setConfigMensalidade(CryptoUtil.toBlowfish(new Gson().toJson(configuracaoMensalidadeVO)));
            parametros.setConfigEmail(configuracaoEmail);
            parametrosService.salvar(parametros);

            JOptionPane.showMessageDialog(this, Resources.translate(MensagemConstants.MENSAGEM_CONFIGURACOES_SALVAS));

            dispose();

        } catch (SysDescException e) {

            JOptionPane.showMessageDialog(this, e.getMensagem());
        }

    }

    private ConfiguracaoEmailVO getCongfiguracaoEmailVO() {

        ConfiguracaoEmailVO configuracaoEmailVO = new ConfiguracaoEmailVO();
        configuracaoEmailVO.setCodigoConfiguracao(((ConfiguracaoEmailEnum) cbConfiguracao.getSelectedItem()).getCodigo());
        configuracaoEmailVO.setUsuario(txUsuario.getText());
        configuracaoEmailVO.setSenha(StringUtil.arrayToString(txSenha.getPassword()));

        return configuracaoEmailVO;
    }

    private void validarConfiguracoes() {

        if (cbConfiguracao.getSelectedIndex() >= 0) {

            if (StringUtil.isNullOrEmpty(txUsuario.getText())) {
                throw new SysDescException(MensagemConstants.MENSAGEM_INSIRA_USUARIO);
            }

            if (StringUtil.isNullOrEmpty(StringUtil.arrayToString(txSenha.getPassword()))) {
                throw new SysDescException(MensagemConstants.MENSAGEM_INSIRA_SENHA);
            }

        }

        if (cbFormaGeracao.getSelectedIndex() < 0) {

            throw new SysDescException(MensagemConstants.MENSAGEM_SELECIONE_FORMA_GERACAO);
        }

        if (((TipoContasReceberEnum) cbFormaGeracao.getSelectedItem()).isUsaDias() && LongUtil.isNullOrZero(txNumeroDias.getValue())) {

            throw new SysDescException(MensagemConstants.MENSAGEM_INSIRA_NUMERO_DIAS);
        }

        if (pesquisaPlanoCredito.getObjetoPesquisado() == null) {

            throw new SysDescException(MensagemConstants.MENSAGEM_SELECIONE_CONTA_CLIENTES);
        }

        if (pesquisaPlanoDebito.getObjetoPesquisado() == null) {

            throw new SysDescException(MensagemConstants.MENSAGEM_SELECIONE_CONTA_RECEITA);
        }

        List<MensalidadePaciente> inconsistencias = mensalidadePacienteService
                .buscarInconsistenciasEnvioBoletos((TipoContasReceberEnum) cbFormaGeracao.getSelectedItem(), txNumeroDias.getValue());

        if (!ListUtil.isNullOrEmpty(inconsistencias)) {

            if (JOptionPane.showConfirmDialog(this,
                    "Existem inconsistências nas mensalidades. Para continuar é necessário corrigir as inconsistências.\n Deseja continuar?",
                    "Validação", JOptionPane.YES_NO_OPTION) == JOptionPane.NO_OPTION) {

                throw new SysDescException(MensagemConstants.MENSAGEM_PROCESSO_INTEROMPIDO_USUARIO);
            }

            mensalidadePacienteService.atualizarInconsistencias((TipoContasReceberEnum) cbFormaGeracao.getSelectedItem(), txNumeroDias.getValue(),
                    inconsistencias);
        }
    }

    private void carregarParametros() {

        parametros = getParametros();

        ConfiguracaoMensalidadeVO configuracaoMensalidadeVO = parametrosService.getConfiguracaoMensalidade(parametros.getConfigMensalidade());

        ConfiguracaoEmailVO configuracaoEmailVO = getConfiguracaoEmail(parametros.getConfigEmail());

        ConfiguracaoContasReceberVO configuracaoContasReceberVO = configuracaoMensalidadeVO.getConfiguracaoContasReceber();

        ConfiguracaoDiarioVO configuracaoDiarioVO = configuracaoMensalidadeVO.getConfiguracaoDiario();
        ConfiguracaoBoletoVO configuracaoBoletoVO = configuracaoMensalidadeVO.getConfiguracaoBoletoVO();

        cbFormaGeracao.setSelectedItem(TipoContasReceberEnum.findByCodigo(configuracaoContasReceberVO.getFormaContasReceber()));

        txNumeroDias.setValue(configuracaoContasReceberVO.getDiasGeracao());

        chEnviarEmail.setSelected(configuracaoBoletoVO.isEnviarEmail());
        chGerarBoleto.setSelected(configuracaoBoletoVO.isGerarboletos());
        pesquisaPlanoCredito.setValueById(configuracaoDiarioVO.getContaReceita());
        pesquisaPlanoDebito.setValueById(configuracaoDiarioVO.getContaCliente());

        cbConfiguracao.setSelectedIndex(-1);

        if (configuracaoEmailVO.getCodigoConfiguracao() != null) {

            cbConfiguracao.setSelectedItem(ConfiguracaoEmailEnum.forCodigo(configuracaoEmailVO.getCodigoConfiguracao()));
        }

        txUsuario.setText(configuracaoEmailVO.getUsuario());
        txSenha.setText(configuracaoEmailVO.getSenha());
    }

    private Parametros getParametros() {

        Parametros parametros = parametrosService.obterPorId();

        if (parametros == null) {

            parametros = new Parametros();
            parametros.setIdParametro(1L);
            parametros.setCadastro(new Date());
        }

        return parametros;
    }

    private ConfiguracaoEmailVO getConfiguracaoEmail(String configEmail) {

        if (!StringUtil.isNullOrEmpty(configEmail)) {

            return new Gson().fromJson(CryptoUtil.fromBlowfish(configEmail), ConfiguracaoEmailVO.class);
        }

        return new ConfiguracaoEmailVO();
    }

    private void changeTipoContasReceber(TipoContasReceberEnum tipoContasReceberEnum) {

        txNumeroDias.setText("");
        txNumeroDias.setEditable(tipoContasReceberEnum.isUsaDias());
    }

}
