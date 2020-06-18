package br.com.lar.tablemodels;

import java.util.ArrayList;
import java.util.List;

import javax.swing.event.EventListenerList;

import com.google.common.collect.Lists;

import br.com.sysdesc.boleto.repository.model.Retorno;
import br.com.sysdesc.components.AbstractInternalFrameTable;
import br.com.sysdesc.util.classes.DateUtil;
import br.com.sysdesc.util.classes.NumericFormatter;
import br.com.sysdesc.util.enumeradores.TipoBancoEnum;
import br.com.sysdesc.util.enumeradores.TipoStatusRetornoEnum;

public class RetornoTableModel extends AbstractInternalFrameTable {

	private static final long serialVersionUID = 1L;
	private List<Retorno> rows = new ArrayList<>();
	private List<String> colunas = new ArrayList<>();
	protected EventListenerList actionListener = new EventListenerList();

	public RetornoTableModel() {

		colunas.add("Banco");
		colunas.add("Retorno");
		colunas.add("Data");
		colunas.add("Boletos");
		colunas.add("Situação");
		colunas.add("Arquivo");
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {

		Retorno retorno = rows.get(rowIndex);

		switch (columnIndex) {

		case 0:
			return TipoBancoEnum.forCodigo(retorno.getNumeroBanco()).getDescricao();

		case 1:
			return NumericFormatter.format(retorno.getNumeroRetono());

		case 2:
			return DateUtil.format(DateUtil.FORMATO_DD_MM_YYYY_HH_MM_SS, retorno.getDataCadastro());

		case 3:
			return NumericFormatter.format(retorno.getRetornoBoletos().size());

		case 4:
			return TipoStatusRetornoEnum.findByCodigo(retorno.getCodigoStatus()).getDescricao();

		default:
			return null;
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

		return String.class;
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		if (column == 5 || column == 4) {

			return Boolean.TRUE;
		}

		return Boolean.FALSE;
	}

	public Retorno getRow(int selectedRow) {
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

	public List<Retorno> getRows() {
		return rows;
	}

	public void setRows(List<Retorno> rows) {
		this.rows = Lists.newArrayList(rows);
		fireTableDataChanged();
	}

	public void addRow(Retorno retorno) {

		this.rows.add(retorno);

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

}