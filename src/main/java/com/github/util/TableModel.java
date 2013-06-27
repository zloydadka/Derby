package main.java.com.github.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import javax.swing.table.AbstractTableModel;

public class TableModel extends AbstractTableModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7283458930266214545L;
	private List<Entry<String,String>> columnNames;
	private List<HashMap<String,String>> data;
	
	@Override
	public String getColumnName(int column) {
		return columnNames.get(column).getValue();
	}

	public TableModel(String ... names) {
		LinkedHashMap<String,String> map = new LinkedHashMap<String, String>();
		for (int i=0; i< names.length; i+=2) {
			map.put(names[i], names[i+1]);
		
		}
		this.columnNames = new ArrayList<Entry<String,String>>(map.entrySet());
	}

	public int findColumn(String columnName) {
		for (int i=0; i < columnNames.size(); i++ ) {
			if(columnNames.get(i).getKey().equals(columnName)) {
				return i;
			}
		}		
		return -1;
	}
		
	public int getColumnCount() {
		return columnNames.size();
	}

	public int getRowCount() {
		return data == null ? 0 : data.size();
	}

	public void setData(List<HashMap<String,String>> data) {
		this.data = data;
		fireTableDataChanged();
	}
	
	public Object getValueAt(int rowIndex, int columnIndex) {
		return data.get(rowIndex).get(columnNames.get(columnIndex).getKey());
	}
	
	public Object getValueAt(int rowIndex, String columnName) {	
		return getValueAt(rowIndex, findColumn(columnName));
		
	}

}
