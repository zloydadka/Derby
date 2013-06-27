package main.java.com.github.util;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.common.base.Objects;

import main.java.com.github.Program;
import main.java.com.github.model.Client;
import main.java.com.github.model.Object2;

public class ObjectTableModel extends TableModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7552844577014889214L;

	List<Object2> rawData;

	public ObjectTableModel() {
		super("id", "ID", "name", "Название", "client_id", "Клиент", "address",
				"Адрес", "addressLink", "Ссылка", "person", "Контактное лицо",
				"phone", "Телефон", "comment", "Комментарий");
	}

	public void setRawData(List<Object2> objects) {
		this.rawData = objects;
		List<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
		try {
			for (Object2 object : objects) {

				Client client = Program.getClientDAO().queryForId(
						object.getClientId());
				HashMap<String, String> hashMap = new HashMap<String, String>();

				hashMap.put("id", object.getId().toString());
				if (client != null) {
					hashMap.put("client_id",
							Objects.firstNonNull(client.getName(), ""));
				} else
					hashMap.put("driver_id", "");
				hashMap.put("name", object.getName());
				hashMap.put("person", object.getPerson());
				hashMap.put("phone", object.getPhone());
				hashMap.put("address", object.getAddress());
				hashMap.put("addressLink", Program.makeAddressLink(
						"http://maps.yandex.ru/?text=", object.getAddress()));
				hashMap.put("comment", object.getComment());
				data.add(hashMap);
			}
			setData(data);

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public Object2 getRawByRowIndex(int rowIndex) {

		Integer id = Integer.valueOf((String) getValueAt(rowIndex, "id"));
		return getRawByID(id);

	}

	public Object2 getRawByID(int id) {
		for (Object2 Object2 : rawData) {
			if (Object2.getId().equals(id)) {
				return Object2;
			}
		}

		return null;

	}

	public List<Object2> getRawData() {

		return rawData;

	}

	public Class<?> getColumnClass(int column) {
		if (this.getColumnName(column) == "Ссылка") {
			return URL.class;
		} else
			return Object.class;
	}

}
