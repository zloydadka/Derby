package main.java.com.github.model;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "objects")
public class Object2 extends AbstractModel {
	
	@DatabaseField(dataType=DataType.STRING)
	private String name;
	@DatabaseField(columnName = "client_id", dataType=DataType.INTEGER_OBJ)
	private Integer clientId;
	@DatabaseField(dataType=DataType.STRING)
	private String address;
	@DatabaseField(dataType=DataType.STRING)
	private String person;
	@DatabaseField(dataType=DataType.STRING)
	private String phone;
	@DatabaseField(dataType=DataType.STRING)
	private String comment;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getClientId() {
		return clientId;
	}

	public void setClientId(Integer clientId) {
		this.clientId = clientId;
	}

	public String getPerson() {
		return person;
	}

	public void setPerson(String person) {
		this.person = person;
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

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
	
}
