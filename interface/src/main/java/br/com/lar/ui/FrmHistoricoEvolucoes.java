package br.com.lar.ui;

import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import br.com.lar.repository.model.HistoricoEvolucao;
import br.com.lar.service.historicoevolucoes.HistoricoEvolucoesService;
import br.com.lar.startup.enumeradores.PesquisaEnum;
import br.com.sysdesc.components.AbstractInternalFrame;
import br.com.sysdesc.components.JTextFieldId;
import br.com.sysdesc.components.JTextFieldMaiusculo;
import br.com.sysdesc.pesquisa.ui.components.PanelActions;

public class FrmHistoricoEvolucoes extends AbstractInternalFrame {

	private static final long serialVersionUID = 1L;

	public FrmHistoricoEvolucoes(Long permissaoPrograma, Long codigoUsuario) {

		super(permissaoPrograma, codigoUsuario);

		initComponents();
	}

	private void initComponents() {

		setSize(476, 281);
		setClosable(Boolean.TRUE);
		setTitle("HISTÓRICO DE EVOLUÇÕES");

		JPanel painelContent = new JPanel();
		JScrollPane scrollPane = new JScrollPane();

		JLabel lblCodigo = new JLabel("Código:");
		JLabel lblDescricao = new JLabel("Descrição:");
		JLabel lbHistorico = new JLabel("Histórico:");

		JTextFieldId txCodigo = new JTextFieldId();
		JTextFieldMaiusculo txDescricao = new JTextFieldMaiusculo();
		JEditorPane txHistorico = new JEditorPane();

		painelContent.add(scrollPane);
		scrollPane.setViewportView(txHistorico);

		lblCodigo.setBounds(7, 7, 100, 14);
		lblDescricao.setBounds(7, 49, 443, 14);
		lbHistorico.setBounds(7, 91, 420, 14);

		txCodigo.setBounds(7, 25, 100, 20);
		txDescricao.setBounds(7, 67, 443, 20);
		scrollPane.setBounds(7, 109, 443, 78);

		getContentPane().add(painelContent);
		painelContent.setLayout(null);
		painelContent.add(lblCodigo);
		painelContent.add(txCodigo);

		painelContent.add(lblDescricao);
		painelContent.add(txDescricao);
		painelContent.add(lbHistorico);

		PanelActions<HistoricoEvolucao> panelActions = new PanelActions<HistoricoEvolucao>(this,
				new HistoricoEvolucoesService(), PesquisaEnum.PES_HISTORICO.getCodigoPesquisa()) {

			private static final long serialVersionUID = 1L;

			@Override
			public void carregarObjeto(HistoricoEvolucao objeto) {
				txCodigo.setValue(objeto.getIdHistorico());
				txDescricao.setText(objeto.getDescricao());
				txHistorico.setText(objeto.getHistorico());
			}

			@Override
			public boolean preencherObjeto(HistoricoEvolucao objetoPesquisa) {

				objetoPesquisa.setIdHistorico(txCodigo.getValue());
				objetoPesquisa.setDescricao(txDescricao.getText());
				objetoPesquisa.setHistorico(txHistorico.getText());

				return true;
			}

		};
		panelActions.setBounds(7, 199, 443, 45);

		panelActions.addSaveListener(objeto -> txCodigo.setValue(objeto.getIdHistorico()));

		painelContent.add(panelActions);
	}

}
