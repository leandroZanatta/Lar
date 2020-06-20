package br.com.lar.tablemodels;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;

import br.com.sysdesc.boleto.repository.model.Boleto;
import br.com.sysdesc.components.AbstractInternalFrameTable;
import br.com.sysdesc.util.classes.DateUtil;
import br.com.sysdesc.util.classes.MoneyFormatter;
import br.com.sysdesc.util.classes.NumericFormatter;
import br.com.sysdesc.util.enumeradores.TipoStatusBoletoEnum;

public class BoletoRemessaTableModel extends AbstractInternalFrameTable {

	private static final long serialVersionUID = 1L;
	private List<Boleto> rows = new ArrayList<>();
	private List<String> colunas = new ArrayList<>();

	public BoletoRemessaTableModel() {

		colunas.add("Código");
		colunas.add("Cliente");
		colunas.add("Valor");
		colunas.add("Vencimento");
		colunas.add("Situação");
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {

		Boleto boleto = rows.get(rowIndex);

		switch (columnIndex) {

		case 0:
			return NumericFormatter.format(boleto.getIdBoleto());

		case 1:
			return boleto.getBoletoDadosCliente().getNome();

		case 2:
			return MoneyFormatter.format(boleto.getValorBoleto());

		case 3:
			return DateUtil.format(DateUtil.FORMATO_DD_MM_YYY, boleto.getDataVencimento());

		case 4:
			return TipoStatusBoletoEnum.findByCodigo(boleto.getCodigoStatus()).toString();

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

	public Boleto getRow(int selectedRow) {
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

	public List<Boleto> getRows() {
		return rows;
	}

	public void setRows(List<Boleto> rows) {
		this.rows = Lists.newArrayList(rows);
		fireTableDataChanged();
	}

	@Override
	public void clear() {
		this.rows = new ArrayList<>();

		fireTableDataChanged();
	}

}