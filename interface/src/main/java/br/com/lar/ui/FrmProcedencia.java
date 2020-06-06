package br.com.lar.ui;

import static br.com.sysdesc.util.resources.Resources.FRMPROCEDENCIA_LB_CODIGO;
import static br.com.sysdesc.util.resources.Resources.FRMPROCEDENCIA_LB_DESCRICAO;
import static br.com.sysdesc.util.resources.Resources.FRMPROCEDENCIA_TITLE;
import static br.com.sysdesc.util.resources.Resources.translate;

import javax.swing.JLabel;
import javax.swing.JPanel;

import br.com.lar.repository.model.Procedencia;
import br.com.lar.service.procedencia.ProcedenciaService;
import br.com.lar.startup.enumeradores.PesquisaEnum;
import br.com.sysdesc.components.AbstractInternalFrame;
import br.com.sysdesc.components.JTextFieldId;
import br.com.sysdesc.components.JTextFieldMaiusculo;
import br.com.sysdesc.pesquisa.ui.components.PanelActions;
import net.miginfocom.swing.MigLayout;

public class FrmProcedencia extends AbstractInternalFrame {

	private static final long serialVersionUID = 1L;

	private JPanel painelContent;
	private JTextFieldId txCodigo;
	private JLabel lblCodigo;
	private JLabel lblDescricao;
	private JTextFieldMaiusculo txDescricao;
	private PanelActions<Procedencia> panelActions;
	private ProcedenciaService procedenciaService = new ProcedenciaService();

	public FrmProcedencia(Long permissaoPrograma, Long codigoUsuario) {
		super(permissaoPrograma, codigoUsuario);

		initComponents();
	}

	private void initComponents() {

		setSize(450, 180);
		setClosable(Boolean.TRUE);
		setTitle(translate(FRMPROCEDENCIA_TITLE));

		painelContent = new JPanel();
		txCodigo = new JTextFieldId();
		lblCodigo = new JLabel(translate(FRMPROCEDENCIA_LB_CODIGO));
		lblDescricao = new JLabel(translate(FRMPROCEDENCIA_LB_DESCRICAO));
		txDescricao = new JTextFieldMaiusculo();

		painelContent.setLayout(new MigLayout("", "[grow]", "[][][][][grow]"));
		getContentPane().add(painelContent);
		painelContent.add(lblCodigo, "cell 0 0");
		painelContent.add(txCodigo, "cell 0 1,width 50:100:100");

		painelContent.add(lblDescricao, "cell 0 2");
		painelContent.add(txDescricao, "cell 0 3,growx");

		panelActions = new PanelActions<Procedencia>(this, procedenciaService,
				PesquisaEnum.PES_PROCEDENCIA.getCodigoPesquisa()) {

			private static final long serialVersionUID = 1L;

			@Override
			public void carregarObjeto(Procedencia objeto) {
				txCodigo.setValue(objeto.getIdProcedencia());
				txDescricao.setText(objeto.getDescricao());
			}

			@Override
			public boolean preencherObjeto(Procedencia objetoPesquisa) {

				objetoPesquisa.setIdProcedencia(txCodigo.getValue());
				objetoPesquisa.setDescricao(txDescricao.getText());

				return true;
			}

		};

		panelActions.addSaveListener((objeto) -> txCodigo.setValue(objeto.getIdProcedencia()));

		painelContent.add(panelActions, "cell 0 4,growx,aligny bottom");
	}

}
