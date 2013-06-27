package main.java.com.github.model;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "drivers")
public class Driver extends AbstractModel {
	
	public static Integer FREE = 1;
	public static Integer REST = 2;
	public static Integer WORK = 3;
	public static Integer VACATION = 4;
	
	@DatabaseField(dataType=DataType.STRING)
	private String name;
	@DatabaseField(dataType=DataType.STRING)
	private String phone;
	@DatabaseField(columnName="auto_id",dataType=DataType.INTEGER_OBJ)
	private Integer autoId;
	@DatabaseField(columnName="on_vacation", dataType=DataType.BOOLEAN_OBJ)
	private Boolean onVacation = false;
	private Integer status = 1;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getAutoId() {
		return autoId;
	}

	public void setAutoId(Integer autoId) {
		this.autoId = autoId;
	}

	public Boolean getOnVacation() {
		return onVacation;
	}

	public void setOnVacation(Boolean onVacation) {
		this.onVacation = onVacation;
	}
	
}
