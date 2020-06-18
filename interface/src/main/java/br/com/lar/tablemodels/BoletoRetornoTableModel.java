package br.com.lar.tablemodels;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;

import br.com.sysdesc.boleto.repository.model.RetornoBoleto;
import br.com.sysdesc.components.AbstractInternalFrameTable;
import br.com.sysdesc.util.classes.NumericFormatter;
import br.com.sysdesc.util.enumeradores.TipoRetornoMovimentoTEnum;
import br.com.sysdesc.util.enumeradores.TipoStatusRetornoBoletoEnum;

public class BoletoRetornoTableModel extends AbstractInternalFrameTable {

	private static final long serialVersionUID = 1L;
	private List<RetornoBoleto> rows = new ArrayList<>();
	private List<String> colunas = new ArrayList<>();

	public BoletoRetornoTableModel() {

		colunas.add("Código");
		colunas.add("Tipo de Movimento");
		colunas.add("Situação");
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {

		RetornoBoleto retornoBoleto = rows.get(rowIndex);

		switch (columnIndex) {

		case 0:
			return NumericFormatter.format(retornoBoleto.getIdRetornoBoleto());

		case 1:
			return TipoRetornoMovimentoTEnum.findByCodigo(retornoBoleto.getTipoRetorno()).getDescricao();

		case 2:
			return TipoStatusRetornoBoletoEnum.findByCodigo(retornoBoleto.getCodigoStatus()).getDescricao();

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

		return Boolean.FALSE;
	}

	public RetornoBoleto getRow(int selectedRow) {
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

	public List<RetornoBoleto> getRows() {
		return rows;
	}

	public void setRows(List<RetornoBoleto> rows) {
		this.rows = Lists.newArrayList(rows);
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