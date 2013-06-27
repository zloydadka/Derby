package main.java.com.github.util;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.google.common.base.Objects;
import main.java.com.github.Program;
import main.java.com.github.model.Auto;
import main.java.com.github.model.Driver;

public class DriverTableModel extends TableModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7108416068213648553L;
	List<Driver> rawData;
	
	public DriverTableModel() {
		super("id", "ID", "name",
				"ФИО", "phone", "Телефон","auto_id", "Автомобиль", "status", "Состояние");
	}
	
	public void setRawData(List<Driver> drivers) {
		this.rawData = drivers;
		List<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
		try {
			for (Driver driver : drivers) {
				
				Auto auto = Program.getAutoDAO().queryForId(driver.getAutoId());	
				HashMap<String, String> hashMap = new HashMap<String, String>();
				
				hashMap.put("id", driver.getId().toString());
				if (auto != null) {
					hashMap.put("auto_id", Objects.firstNonNull(auto.getStateNumber(),""));
				} else hashMap.put("auto_id","");
				hashMap.put("name", Objects.firstNonNull(driver.getName(),""));
				hashMap.put("phone", Objects.firstNonNull(driver.getPhone(),""));
				hashMap.put("status", Objects.firstNonNull(driver.getStatus(),"").toString());
				data.add(hashMap);
			}
			setData(data);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
	}
	public Driver getRawByRowIndex(int rowIndex) {
		Integer id = Integer.valueOf((String) getValueAt(rowIndex, "id"));
		return getRawByID(id);
		
	}
	
	public Driver getRawByID(int id) {
		for (Driver driver : rawData) {
			if (driver.getId().equals(id)) {
				return driver;
			}
		}
		return null;
		
	}
	
	public List<Driver> getRawData() {
		return rawData;
	}
	
}
