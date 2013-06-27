package main.java.com.github.controller;

import java.awt.Color;
import java.awt.Component;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableRowSorter;
import com.j256.ormlite.dao.Dao;
import main.java.com.github.Program;
import main.java.com.github.model.AbstractModel;
import main.java.com.github.model.Driver;
import main.java.com.github.model.Order;
import main.java.com.github.util.DriverTableModel;
import main.java.com.github.view.DriverView;
import main.java.com.github.view.ViewContext;
import main.java.com.github.view.AbstractView.Mode;

public class DriverController extends AbstractController<Driver> {

	private static final long serialVersionUID = 1934129755614651083L;
	private Dao<Driver, Integer> driverDAO;
	private List<Driver> drivers;
	private DriverTableModel tableModel;
	final Integer RESTHOURS = 8;

	public DriverController(String title, String tip) throws SQLException {

		super(title, tip, Driver.class);
		this.view = new DriverView(this);
		this.driverDAO = Program.getDriverDAO();
		this.drivers = driverDAO.queryForAll();
		setDriverStatuses(drivers);
		setIconImage("drivers.gif");

		this.tableModel = new DriverTableModel();
		table.setModel(tableModel);
		table.setDefaultRenderer(Object.class, new DriverTableCellRender(
				tableModel));

		TableRowSorter<DriverTableModel> rowSorter = new TableRowSorter<DriverTableModel>(
				tableModel);
		rowSorter.setSortable(0, false);
		rowSorter.toggleSortOrder(tableModel.findColumn("status"));
		table.setRowSorter(rowSorter);
		table.getColumnModel().getColumn(0).setMaxWidth(40);

	}

	public void refreshData() {

		Dao<Driver, Integer> driverDAO = Program.getDriverDAO();
		try {

			drivers = driverDAO.queryForAll();
			setDriverStatuses(drivers);
			((DriverTableModel) table.getModel()).setRawData(drivers);
			table.setDefaultRenderer(Object.class, new DriverTableCellRender(
					tableModel));

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void addButtonClicked() {

		this.view = new DriverView(this);
		try {
			ViewContext initContext = scope.newInstance().emptyContext();
			initContext.put("autoId", null);
			view.setMode(Mode.NEW);
			view.setContext(initContext);
			view.setSize(600, 400);
			view.setVisible(true);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
		}
	}

	@Override
	public void editButtonClicked() {

		this.view = new DriverView(this);
		int rowIndex = table.getSelectedRow();
		Integer id = Integer.valueOf((String) table.getValueAt(rowIndex, 0));

		Driver model;
		try {
			model = driverDAO.queryForId(Integer.valueOf(id));

			ViewContext initContext = model.getViewContext();
			initContext.put("autoId", model.getAutoId());

			view.setMode(Mode.EDIT);
			view.setContext(initContext);
			view.setSize(600, 400);
			view.setVisible(true);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void createEntry(ViewContext context) {

		try {
			this.driverDAO.create((Driver) context.makeModel(scope));
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			refreshData();
		}

	}

	@Override
	public void updateEntry(ViewContext context) {

		try {
			this.driverDAO.update((Driver) context.makeModel(scope));
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			refreshData();
		}

	}

	@Override
	protected ViewContext addListSelection(ViewContext initContext,
			AbstractModel model) {

		initContext.put("auto_id", ((Driver) model).getAutoId());
		return initContext;
	}

	@Override
	protected ViewContext addEmptySelection(ViewContext initContext) {

		initContext.put("auto_id", null);
		return initContext;
	}

	public HashMap<Integer, ArrayList<Date>> getOrderStartDates() {
		HashMap<Integer, ArrayList<Date>> orderStartDates = new HashMap<Integer, ArrayList<Date>>();
		List<Order> orders;
		try {

			orders = Program.getOrderDAO().queryForAll();
			for (Order order : orders) {
				Integer driverId = order.getDriver();
				if ((orderStartDates.containsKey(driverId)) == false) {
					orderStartDates.put(driverId, new ArrayList<Date>());
				}
				orderStartDates.get(driverId).add(order.getStartDate());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return orderStartDates;
	}

	public HashMap<Integer, ArrayList<Date>> getOrderEndDates() {
		HashMap<Integer, ArrayList<Date>> orderEndDates = new HashMap<Integer, ArrayList<Date>>();
		List<Order> orders;
		try {

			orders = Program.getOrderDAO().queryForAll();
			for (Order order : orders) {
				Integer driverId = order.getDriver();
				if ((orderEndDates.containsKey(driverId)) == false) {
					orderEndDates.put(driverId, new ArrayList<Date>());
				}
				orderEndDates.get(driverId).add(order.getEndDate());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return orderEndDates;
	}

	public void setDriverStatuses(List<Driver> drivers) {

		for (Driver driver : drivers) {

			Integer status = Driver.FREE;
			if (driver.getOnVacation().equals(true)) {
				status = Driver.VACATION;
			} else {
				List<Order> orders;
				try {

					orders = Program.getOrderDAO().queryForAll();
					for (Order order : orders) {
						Date startDate = order.getStartDate();
						Date endDate = order.getEndDate();
						if (order.getDriver() == driver.getId()) {
							if (endDate.after(new Date())) {
								status = Driver.WORK;
							} else {
								if (onRest(startDate, endDate, RESTHOURS))
									status = Driver.REST;
							}
							
						}
					}

				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			
			driver.setStatus(status);
		}
	}

	@Override
	protected void printMapButtonClicked() {
	}

	@Override
	protected void printOrderButtonClicked() {
	}

	@SuppressWarnings("deprecation")
	private Boolean onRest(Date startDate, Date endDate, int numRestHours) {
		long currTime = (new Date()).getTime();
		if (currTime - endDate.getTime() > 3600000 * numRestHours) {
			return false;
		} else {
			if ((startDate.getHours() >= 23 || startDate.getHours() <= 8)
					&& endDate.before(new Date())) {
				return true;
			}
		}

		return null;

	}
}

class DriverTableCellRender extends DefaultTableCellRenderer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private DriverTableModel tableModel;
	private static final Map<Integer, String> STATES = new HashMap<Integer, String>();

	public DriverTableCellRender(DriverTableModel tableModel) {
		super();
		this.tableModel = tableModel;
		setOpaque(true);
	}

	static {
		STATES.put(Driver.WORK, "На заказе");
		STATES.put(Driver.FREE, "Свободен");
		STATES.put(Driver.VACATION, "В отпуске");
		STATES.put(Driver.REST, "С ночи");
	}

	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {

		int modelRow = table.getRowSorter().convertRowIndexToModel(row);
		Driver driver = tableModel.getRawByRowIndex(modelRow);

		Color firstColor = Color.black;
		Color secondColor = Color.white;

		Integer driverStatus = driver.getStatus();
		if (tableModel.findColumn("status") == column) {
			value = STATES.get(driverStatus);
		}
		setText(value != null ? value.toString() : "");

		if (driverStatus.equals(Driver.FREE)) {
			secondColor = Color.green;
		} else if (driverStatus.equals(Driver.VACATION)) {
			firstColor = Color.red;
		} else if (driverStatus.equals(Driver.REST)) {
			secondColor = Color.yellow;
		}

		if (isSelected) {
			setBackground(Color.cyan);
			setForeground(firstColor);
		} else {

			setBackground(secondColor);
			setForeground(firstColor);
		}

		return this;
	}
}
