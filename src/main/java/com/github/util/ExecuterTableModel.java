package main.java.com.github.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import main.java.com.github.model.Executer;


public class ExecuterTableModel extends TableModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7097746255103471200L;
	
	List<Executer> rawData;
	
	public ExecuterTableModel() {
		super("id", "ID", "name", "Наименование", "address",
				"Адрес", "phone", "Телефон");
	}
	
	public void setRawData(List<Executer> execs) {
		this.rawData = execs;
		List<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
		for (Executer exec : execs) {
			HashMap<String, String> hashMap = new HashMap<String, String>();
			hashMap.put("id", exec.getId().toString());
			hashMap.put("name", exec.getName());
			hashMap.put("address", exec.getAddress());
			hashMap.put("phone", exec.getPhone());
			data.add(hashMap);
		}
		setData(data);
			
	}
	
	public Executer getRawByRowIndex(int rowIndex) {
		
		Integer id = Integer.valueOf((String) getValueAt(rowIndex, "id"));
		return getRawByID(id);
		
	}
	
	public Executer getRawByID(int id) {
		for (Executer Executer : rawData) {
			if (Executer.getId().equals(id)) {
				return Executer;
			}
		}		
		return null;		
	}
	
	public List<Executer> getRawData() {		
		return rawData;		
	}
}
