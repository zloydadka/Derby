package main.java.com.github.util;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.common.base.Objects;

import main.java.com.github.Program;
import main.java.com.github.model.Auto;
import main.java.com.github.model.Client;
import main.java.com.github.model.Driver;
import main.java.com.github.model.Executer;
import main.java.com.github.model.Object2;
import main.java.com.github.model.Order;

public class OrderTableModel extends TableModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2272437467453928992L;

	List<Order> rawData;

	public OrderTableModel() {
		super("id", "ID", "start_date", "Время начала", "end_date",
				"Время конца", "executer_id", "Исполнитель", "driver_id",
				"Водитель", "auto_id", "Автомобиль", "brand", "Марка",
				"client_id", "Клиент", "object_id", "Объект", "load",
				"Вид груза", "bill_passed", "Накладная",
				"waybill_passed", "Путевой лист");
	}

	@SuppressWarnings("unused")
	public void setRawData(List<Order> orders) {
		this.rawData = orders;
		List<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
		try {

			for (Order order : orders) {
				Driver driver = Program.getDriverDAO().queryForId(
						order.getDriver());
				Executer executer = Program.getExecuterDAO().queryForId(
						order.getExecuter());
				Auto auto = Program.getAutoDAO().queryForId(driver.getAutoId());
				Client client = Program.getClientDAO().queryForId(
						order.getClient());
				Object2 object = Program.getObjectDAO().queryForId(
						order.getObject());

				HashMap<String, String> hashMap = new HashMap<String, String>();
				hashMap.put("id", order.getId().toString());
				SimpleDateFormat format = new SimpleDateFormat(
						"dd.MM.yyyy  HH:mm:SS");
				hashMap.put(
						"start_date",
						Objects.firstNonNull(
								format.format(order.getStartDate()), "")
								.toString());
				hashMap.put(
						"end_date",
						Objects.firstNonNull(format.format(order.getEndDate()),
								"").toString());
				if (executer != null) {
					hashMap.put("executer_id",
							Objects.firstNonNull(executer.getName(), ""));
				} else
					hashMap.put("executer_id", "");
				if (driver != null) {
					hashMap.put("driver_id",
							Objects.firstNonNull(driver.getName(), ""));
				} else
					hashMap.put("driver_id", "");
				if (auto != null) {
					hashMap.put("auto_id",
							Objects.firstNonNull(auto.getStateNumber(), ""));
					hashMap.put("brand",
							Objects.firstNonNull(auto.getBrand(), ""));
				} else {
					hashMap.put("auto_id", "");
					hashMap.put("brand", "");
				}
				if (client != null) {
					hashMap.put("client_id",
							Objects.firstNonNull(client.getName(), ""));
				} else
					hashMap.put("client_id", "");
				if (object != null) {
					hashMap.put("object_id",
							Objects.firstNonNull(object.getName(), ""));
				} else
					hashMap.put("object_id", "");
				hashMap.put("load", Objects.firstNonNull(order.getLoad(), ""));
				hashMap.put("bill_passed", Objects.firstNonNull(order
						.getBillPassed().toString(), ""));
				hashMap.put("waybill_passed", Objects.firstNonNull(order
						.getWaybillPassed().toString(), ""));

				data.add(hashMap);
			}
			setData(data);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public Order getRawByRowIndex(int rowIndex) {

		Integer id = Integer.valueOf((String) getValueAt(rowIndex, "id"));
		return getRawByID(id);

	}

	public Order getRawByID(int id) {
		for (Order Order : rawData) {
			if (Order.getId().equals(id)) {
				return Order;
			}
		}

		return null;

	}

	public List<Order> getRawData() {

		return rawData;

	}
	
}
