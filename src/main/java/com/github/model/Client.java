package main.java.com.github.model;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "clients")
public class Client extends AbstractModel {

	@DatabaseField(dataType=DataType.STRING)
	private String name;
	@DatabaseField(dataType=DataType.STRING)
	private String person;
	@DatabaseField(dataType=DataType.STRING)
	private String phone;
	@DatabaseField(dataType=DataType.STRING)
	private String address;
	@DatabaseField(dataType=DataType.STRING)
	private String saldo;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPerson() {
		return person;
	}
	public void setPerson(String person) {
		this.person = person;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getSaldo() {
		return saldo;
	}
	public void setSaldo(String saldo) {
		this.saldo = saldo;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
		
}
