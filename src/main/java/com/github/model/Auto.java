package main.java.com.github.model;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "autos")
public class Auto extends AbstractModel{

	public static Integer FREE = 1;
	public static Integer WORK = 2;
	public static Integer REPAIR = 3;

	@DatabaseField(columnName="state_number", dataType=DataType.STRING)
	private String stateNumber;
	@DatabaseField(columnName="brand", dataType=DataType.STRING)
	private String brand;
	@DatabaseField(columnName="driver_id",dataType=DataType.INTEGER_OBJ)
	private Integer driverId;
	@DatabaseField(columnName="under_repair", dataType=DataType.BOOLEAN_OBJ)
	private Boolean underRepair = false;
	private Integer status = 1;

	public String getStateNumber() {
		return stateNumber;
	}
	public void setStateNumber(String stateNumber) {
		this.stateNumber = stateNumber;
	}
	public Integer getDriverId() {
		return driverId;
	}
	public void setDriverId(Integer driverId) {
		this.driverId = driverId;
	}

	public String getBrand() {
		return brand;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}

	public Boolean getUnderRepair() {
		return underRepair;
	}
	public void setUnderRepair(Boolean underRepair) {
		this.underRepair = underRepair;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	
}
