package main.java.com.github.model;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "executers")
public class Executer extends AbstractModel{

	@DatabaseField(columnName="name", dataType=DataType.STRING)
	private String name;
	@DatabaseField(columnName="address", dataType=DataType.STRING)
	private String address;
	@DatabaseField(columnName="phone", dataType=DataType.STRING)
	private String phone;

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
}
