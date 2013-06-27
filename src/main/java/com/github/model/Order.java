package main.java.com.github.model;


import java.util.Date;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "orders")
public class Order extends AbstractModel{

	@DatabaseField(columnName="start_date", dataType=DataType.DATE)
	private Date startDate;
	@DatabaseField(columnName="end_date", dataType=DataType.DATE)
	private Date endDate;
	@DatabaseField(columnName="executer_id", dataType=DataType.INTEGER_OBJ)
	private Integer executerId;
	@DatabaseField(columnName="driver_id", dataType=DataType.INTEGER_OBJ)
	private Integer driverId;
	@DatabaseField(columnName="client_id", dataType=DataType.INTEGER_OBJ)
	private Integer clientId;
	@DatabaseField(columnName="object_id", dataType=DataType.INTEGER_OBJ)
	private Integer objectId;
	@DatabaseField(columnName="load", dataType=DataType.STRING)
	private String load;
	@DatabaseField(columnName="bill_passed", dataType=DataType.BOOLEAN_OBJ)
	private Boolean billPassed;
	@DatabaseField(columnName="waybill_passed", dataType=DataType.BOOLEAN_OBJ)
	private Boolean waybillPassed;

	public Integer getExecuter() {
		return executerId;
	}
	public void setExecuter(Integer executerId) {
		this.executerId = executerId;
	}

	public Integer getDriver() {
		return driverId;
	}
	public void setDriver(Integer driverId) {
		this.driverId = driverId;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public Integer getClient() {
		return clientId;
	}
	public void setClient(Integer clientId) {
		this.clientId = clientId;
	}
	public Integer getObject() {
		return objectId;
	}
	public void setObject(Integer objectId) {
		this.objectId = objectId;
	}
	public String getLoad() {
		return load;
	}
	public void setLoad(String load) {
		this.load = load;
	}
	public Boolean getBillPassed() {
		return billPassed;
	}
	public void setBillPassed(Boolean billPassed) {
		this.billPassed = billPassed;
	}
	public Boolean getWaybillPassed() {
		return waybillPassed;
	}
	public void setWaybillPassed(Boolean waybillPassed) {
		this.waybillPassed = waybillPassed;
	}	
	
}
