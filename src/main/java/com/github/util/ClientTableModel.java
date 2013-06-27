package main.java.com.github.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import main.java.com.github.model.Client;


public class ClientTableModel extends TableModel {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7552844577014889214L;
	List<Client> rawData;
	
	public ClientTableModel() {
		super("id", "ID", "name", "Название", "person",
				"Контактное лицо", "phone", "Телефон", "address", "Адрес",
				"saldo", "Сальдо");
	}
	
	public void setRawData(List<Client> clients) {
		this.rawData = clients;
		List<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
		for (Client client : clients) {
			
			HashMap<String, String> hashMap = new HashMap<String, String>();
			hashMap.put("id", client.getId().toString());
			hashMap.put("name", client.getName());
			hashMap.put("person", client.getPerson());
			hashMap.put("phone", client.getPhone());
			hashMap.put("address", client.getAddress());
			hashMap.put("saldo", client.getSaldo());
			data.add(hashMap);
		}
		setData(data);
			
	}
	
	public Client getRawByRowIndex(int rowIndex) {
		
		Integer id = Integer.valueOf((String) getValueAt(rowIndex, "id"));
		return getRawByID(id);
		
	}
	
	public Client getRawByID(int id) {
		for (Client Client : rawData) {
			if (Client.getId().equals(id)) {
				return Client;
			}
		}
		
		return null;
		
	}
	
	public List<Client> getRawData() {
		
		return rawData;
		
	}
}
