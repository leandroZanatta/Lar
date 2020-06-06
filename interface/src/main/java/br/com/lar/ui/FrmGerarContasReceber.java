package br.com.lar.ui;

import java.awt.BorderLayout;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.toedter.calendar.JDateChooser;

import br.com.lar.repository.model.Cliente;
import br.com.lar.repository.model.ContasReceber;
import br.com.lar.repository.model.FormasPagamento;
import br.com.lar.service.cliente.ClienteService;
import br.com.lar.service.contasreceber.ContasReceberService;
import br.com.lar.service.formaspagamento.FormasPagamentoService;
import br.com.lar.startup.enumeradores.PesquisaEnum;
import br.com.sysdesc.components.AbstractInternalFrame;
import br.com.sysdesc.components.JMoneyField;
import br.com.sysdesc.components.JTextFieldId;
import br.com.sysdesc.pesquisa.ui.components.CampoPesquisa;
import br.com.sysdesc.pesquisa.ui.components.PanelActions;
import br.com.sysdesc.util.enumeradores.TipoProgramaContasReceberEnum;
import br.com.sysdesc.util.enumeradores.TipoStatusEnum;
import net.miginfocom.swing.MigLayout;

public class FrmGerarContasReceber extends AbstractInternalFrame {

    private static final long serialVersionUID = 1L;
    private ContasReceberService contasReceberService = new ContasReceberService();
    private FormasPagamentoService formasPagamentoService = new FormasPagamentoService();
    private ClienteService clienteService = new ClienteService();
    private JPanel container;
    private JLabel lblNewLabel;
    private JTextFieldId txCodigo;
    private JLabel lblNewLabel_1;
    private CampoPesquisa<Cliente> pesquisaCliente;
    private PanelActions<ContasReceber> panelActions;
    private JLabel lblNewLabel_4;
    private CampoPesquisa<FormasPagamento> pesquisaPagamento;
    private JLabel lblNewLabel_5;
    private JComboBox<TipoProgramaContasReceberEnum> cbPrograma;
    private JLabel lblNewLabel_2;
    private JMoneyField txValorParcela;
    private JLabel lblNewLabel_3;
    private JLabel lblNewLabel_6;
    private JMoneyField txAcrescimo;
    private JMoneyField txDesconto;
    private JLabel lblNewLabel_7;
    private JDateChooser dtVencimento;

    public FrmGerarContasReceber(Long permissaoPrograma, Long codigoUsuario) {
        super(permissaoPrograma, codigoUsuario);

        container = new JPanel();
        getContentPane().add(container, BorderLayout.CENTER);
        container
                .setLayout(new MigLayout("", "[grow][100px:100px:100px,grow][100px:100px:100px,grow][100px:100px:100px,grow][100px:100px:100px,grow]",
                        "[][][][][][][22px][][33px,grow]"));

        lblNewLabel = new JLabel("Código:");
        container.add(lblNewLabel, "cell 0 0,growx,aligny center");

        txCodigo = new JTextFieldId();
        container.add(txCodigo, "cell 0 1,alignx left,aligny center");
        txCodigo.setColumns(10);

        lblNewLabel_1 = new JLabel("Cliente:");
        container.add(lblNewLabel_1, "cell 0 2,alignx left,aligny center");

        pesquisaCliente = new CampoPesquisa<Cliente>(clienteService, PesquisaEnum.PES_CLIENTES.getCodigoPesquisa(), getCodigoUsuario()) {

            private static final long serialVersionUID = 1L;

            @Override
            public String formatarValorCampo(Cliente objeto) {
                return String.format("%d - %s", objeto.getIdCliente(), objeto.getNome());
            }
        };

        container.add(pesquisaCliente, "cell 0 3 5 1,grow");

        lblNewLabel_4 = new JLabel("Pagamento:");
        container.add(lblNewLabel_4, "cell 0 4,alignx left,aligny center");

        pesquisaPagamento = new CampoPesquisa<FormasPagamento>(formasPagamentoService, PesquisaEnum.PES_FORMAS_PAGAMENTO.getCodigoPesquisa(),
                getCodigoUsuario(), formasPagamentoService.pesquisarApenasAPrazo()) {

            private static final long serialVersionUID = 1L;

            @Override
            public String formatarValorCampo(FormasPagamento objeto) {
                return String.format("%d - %s", objeto.getIdFormaPagamento(), objeto.getDescricao());
            }
        };

        container.add(pesquisaPagamento, "cell 0 5 5 1,grow");

        lblNewLabel_5 = new JLabel("Programa:");
        container.add(lblNewLabel_5, "cell 0 6,growx,aligny center");

        lblNewLabel_7 = new JLabel("Vencimento:");
        container.add(lblNewLabel_7, "cell 1 6");

        lblNewLabel_2 = new JLabel("Valor Parcela:");
        container.add(lblNewLabel_2, "cell 2 6");

        lblNewLabel_3 = new JLabel("Acréscimo:");
        container.add(lblNewLabel_3, "cell 3 6");

        lblNewLabel_6 = new JLabel("Desconto:");
        container.add(lblNewLabel_6, "cell 4 6,alignx center");

        cbPrograma = new JComboBox<>();
        cbPrograma.setSelectedIndex(-1);
        container.add(cbPrograma, "cell 0 7,growx,aligny top");
        Arrays.asList(TipoProgramaContasReceberEnum.values()).forEach(cbPrograma::addItem);

        dtVencimento = new JDateChooser("dd/MM/yyyy", "##/##/#####", '_');
        container.add(dtVencimento, "cell 1 7,growx");

        txValorParcela = new JMoneyField();
        container.add(txValorParcela, "cell 2 7,growx");

        txAcrescimo = new JMoneyField();
        container.add(txAcrescimo, "cell 3 7,growx");

        txDesconto = new JMoneyField();
        container.add(txDesconto, "cell 4 7,growx");

        panelActions = new PanelActions<ContasReceber>(this, contasReceberService, PesquisaEnum.PES_CONTAS_RECEBER.getCodigoPesquisa()) {

            private static final long serialVersionUID = 1L;

            @Override
            protected void carregarObjeto(ContasReceber objetoPesquisa) {
                txCodigo.setValue(objetoPesquisa.getIdContasReceber());
                pesquisaCliente.setValue(objetoPesquisa.getCliente());
                pesquisaPagamento.setValue(objetoPesquisa.getFormasPagamento());
                txValorParcela.setValue(objetoPesquisa.getValorParcela());
                txAcrescimo.setValue(objetoPesquisa.getValorAcrescimo());
                txDesconto.setValue(objetoPesquisa.getValorDesconto());
                dtVencimento.setDate(objetoPesquisa.getDataVencimento());
            }

            @Override
            public boolean preencherObjeto(ContasReceber objetoPesquisa) {
                objetoPesquisa.setIdContasReceber(txCodigo.getValue());
                objetoPesquisa.setCliente(pesquisaCliente.getObjetoPesquisado());
                objetoPesquisa.setFormasPagamento(pesquisaPagamento.getObjetoPesquisado());
                objetoPesquisa.setDataVencimento(dtVencimento.getDate());
                objetoPesquisa.setValorParcela(txValorParcela.getValue());
                objetoPesquisa.setValorAcrescimo(txAcrescimo.getValue());
                objetoPesquisa.setValorDesconto(txDesconto.getValue());
                objetoPesquisa.setDataManutencao(new Date());
                objetoPesquisa.setPrograma(null);

                if (cbPrograma.getSelectedIndex() >= 0) {
                    objetoPesquisa.setPrograma(((TipoProgramaContasReceberEnum) cbPrograma.getSelectedItem()).getCodigo());
                }

                return true;
            }
        };
        container.add(panelActions, "cell 0 8 5 1,growx,aligny bottom");
        panelActions.addSaveListener(objeto -> txCodigo.setValue(objeto.getIdContasReceber()));
        panelActions.addNewListener(conta -> {
            conta.setValorJuros(BigDecimal.ZERO);
            conta.setBaixado(false);
            conta.setValorPago(BigDecimal.ZERO);
            conta.setDataCadastro(new Date());
            conta.setCodigoStatus(TipoStatusEnum.ATIVO.getCodigo());
        });

        initComponents();

    }

    private void initComponents() {

        setSize(600, 275);
        setClosable(Boolean.TRUE);
        setTitle("CADASTRO DE CONTAS Á RECEBER");
    }
}
