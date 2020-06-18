package br.com.lar.tablemodels;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.JPanel;
import javax.swing.event.EventListenerList;

import com.google.common.collect.Lists;

import br.com.syscesc.boleto.service.boleto.BoletoService;
import br.com.syscesc.boleto.service.remessa.RemessaService;
import br.com.sysdesc.boleto.repository.model.Boleto;
import br.com.sysdesc.boleto.repository.model.Remessa;
import br.com.sysdesc.boleto.repository.model.RemessaBoleto;
import br.com.sysdesc.components.AbstractInternalFrameTable;
import br.com.sysdesc.util.classes.DateUtil;
import br.com.sysdesc.util.classes.NumericFormatter;
import br.com.sysdesc.util.enumeradores.TipoBancoEnum;
import br.com.sysdesc.util.enumeradores.TipoStatusBoletoEnum;
import br.com.sysdesc.util.enumeradores.TipoStatusRemessaEnum;

public class RemessaTableModel extends AbstractInternalFrameTable {

	private static final long serialVersionUID = 1L;
	private List<Remessa> rows = new ArrayList<>();
	private List<String> colunas = new ArrayList<>();
	protected EventListenerList actionListener = new EventListenerList();

	public RemessaTableModel() {

		colunas.add("Banco");
		colunas.add("Remessa");
		colunas.add("Data");
		colunas.add("Boletos");
		colunas.add("Situação");
		colunas.add("Arquivo");
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {

		Remessa remessa = rows.get(rowIndex);

		switch (columnIndex) {

		case 0:
			return TipoBancoEnum.forCodigo(remessa.getNumeroBanco()).getDescricao();

		case 1:
			return NumericFormatter.format(remessa.getNumeroRemessa());

		case 2:
			return DateUtil.format(DateUtil.FORMATO_DD_MM_YYYY_HH_MM_SS, remessa.getDataCadastro());

		case 3:
			return NumericFormatter.format(remessa.getRemessaBoletos().size());

		case 4:
			return TipoStatusRemessaEnum.findByCodigo(remessa.getCodigoStatus());

		default:
			return null;
		}
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {

		Remessa remessa = rows.get(rowIndex);

		if (columnIndex == 4) {

			TipoStatusRemessaEnum tipoStatusRemessa = (TipoStatusRemessaEnum) aValue;

			if (!remessa.getCodigoStatus().equals(tipoStatusRemessa.getCodigo())) {
				remessa.setCodigoStatus(tipoStatusRemessa.getCodigo());

				new RemessaService().salvar(remessa);

				List<Boleto> boletos = remessa.getRemessaBoletos().stream().map(RemessaBoleto::getBoleto).collect(Collectors.toList());

				TipoStatusBoletoEnum tipoStatusBoletoEnum = TipoStatusRemessaEnum.GERADO.equals(tipoStatusRemessa)
						? TipoStatusBoletoEnum.REMESSA_GERADA
						: TipoStatusBoletoEnum.REMESSA_ENVIADA;

				boletos.forEach(boleto -> boleto.setCodigoStatus(tipoStatusBoletoEnum.getCodigo()));

				new BoletoService().salvar(boletos);

				fireActionListener();
			}
		}
	}

	@Override
	public int getColumnCount() {
		return colunas.size();
	}

	@Override
	public String getColumnName(int column) {
		return colunas.get(column);
	}

	@Override
	public int getRowCount() {
		return rows.size();
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {

		if (columnIndex == 4) {
			return TipoStatusRemessaEnum.class;
		}

		if (columnIndex == 5) {
			return JPanel.class;
		}

		return String.class;
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		if (column == 5 || column == 4) {

			return Boolean.TRUE;
		}

		return Boolean.FALSE;
	}

	public Remessa getRow(int selectedRow) {
		return rows.get(selectedRow);
	}

	public void remove(int selectedRow) {
		rows.remove(selectedRow);
		fireTableDataChanged();
	}

	public void removeAll() {
		rows = new ArrayList<>();
		fireTableDataChanged();
	}

	public List<Remessa> getRows() {
		return rows;
	}

	public void setRows(List<Remessa> rows) {
		this.rows = Lists.newArrayList(rows);
		fireTableDataChanged();
	}

	public void addRow(Remessa remessa) {

		this.rows.add(remessa);

		fireTableDataChanged();
	}

	@Override
	public void clear() {
		this.rows = new ArrayList<>();

		fireTableDataChanged();
	}

	@Override
	public void setEnabled(Boolean enabled) {

	}

	private void fireActionListener() {

		Object[] listeners = actionListener.getListenerList();

		for (int i = 0; i < listeners.length; i = i + 2) {

			if (listeners[i] == ActionListener.class) {

				((ActionListener) listeners[i + 1]).actionPerformed(null);
			}
		}
	}

	public void addActionListener(ActionListener listener) {

		actionListener.add(ActionListener.class, listener);
	}

}