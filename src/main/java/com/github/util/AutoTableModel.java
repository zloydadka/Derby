package main.java.com.github.util;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.google.common.base.Objects;
import main.java.com.github.Program;
import main.java.com.github.model.Auto;
import main.java.com.github.model.Driver;
import main.java.com.github.util.TableModel;

public class AutoTableModel extends TableModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6235632253470572646L;
	private List<Auto> rawData;

	public AutoTableModel() {
		
		super("id", "ID", "state_number", "Госномер", "brand", "Марка",
				"driver_id", "Водитель", "status", "Состояние");

	}

	public void setRawData(List<Auto> autos) {
		
		this.rawData = autos;
		List<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
		try {
			for (Auto auto : autos) {
				
				Driver driver = Program.getDriverDAO().queryForId(auto.getDriverId());
				HashMap<String, String> hashMap = new HashMap<String, String>();
				
				hashMap.put("id", auto.getId().toString());
				if (driver != null) {
					hashMap.put("driver_id",
							Objects.firstNonNull(driver.getName(), ""));
				} else
					hashMap.put("driver_id", "");
				hashMap.put("state_number",
						Objects.firstNonNull(auto.getStateNumber(), ""));
				hashMap.put("status",
						Objects.firstNonNull(auto.getStatus(), "").toString());
				hashMap.put("brand", Objects.firstNonNull(auto.getBrand(), ""));
				data.add(hashMap);
			}
		} catch (SQLException e) {e.printStackTrace();}
		
		setData(data);
	}

	
	public Auto getRawByRowIndex(int rowIndex) {
		Integer id = Integer.valueOf((String) getValueAt(rowIndex, "id"));
		return getRawByID(id);
		
	}
	
	public Auto getRawByID(int id) {
		
		for (Auto auto : rawData) {
			if (auto.getId().equals(id)) {
				return auto;
			}
		}
		return null;
		
	}
	
	public List<Auto> getRawData() {
		return rawData;
	}
	
}
