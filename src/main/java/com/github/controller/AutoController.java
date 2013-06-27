package main.java.com.github.controller;

import java.awt.Color;
import java.awt.Component;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableRowSorter;
import main.java.com.github.Program;
import main.java.com.github.model.AbstractModel;
import main.java.com.github.model.Auto;
import main.java.com.github.model.Order;
import main.java.com.github.util.AutoTableModel;
import main.java.com.github.view.AbstractView.Mode;
import main.java.com.github.view.AutoView;
import main.java.com.github.view.ViewContext;

import com.j256.ormlite.dao.Dao;

public class AutoController extends AbstractController<Auto> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8143849514025696195L;
	private Dao<Auto, Integer> autoDAO;
	private HashMap<Integer, Date> orderEndDates;
	private AutoTableModel tableModel;
	private List<Auto> autos;

	public AutoController(String title, String tip) throws SQLException {

		super(title, tip, Auto.class);
		this.view = new AutoView(this);
		this.autoDAO = Program.getAutoDAO();
		this.orderEndDates = getOrderEndDates();
		this.autos = autoDAO.queryForAll();
		setAutoStatuses(autos, orderEndDates);
		
		setIconImage("autos.gif");

		this.tableModel = new AutoTableModel();
		table.setModel(tableModel);
		table.setDefaultRenderer(Object.class, new AutoTableCellRender(
				tableModel));

		TableRowSorter<AutoTableModel> rowSorter = new TableRowSorter<AutoTableModel>(
				tableModel);
		rowSorter.setSortable(0, false);

		rowSorter.toggleSortOrder(tableModel
				.findColumn("status"));
		table.setRowSorter(rowSorter);
		table.getColumnModel().getColumn(0).setMaxWidth(40);
		
	}

	public void refreshData() {

		try {
			autos = autoDAO.queryForAll();
			this.orderEndDates = getOrderEndDates();
			setAutoStatuses(autos, orderEndDates);
			((AutoTableModel) table.getModel()).setRawData(autos);
			table.setDefaultRenderer(Object.class, new AutoTableCellRender(
					tableModel));

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void addButtonClicked() {

		this.view = new AutoView(this);
		try {
			ViewContext initContext = scope.newInstance().emptyContext();
			initContext.put("driverId", null);
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

		this.view = new AutoView(this);
		int rowIndex = table.getSelectedRow();
		Integer id = Integer.valueOf((String) table.getValueAt(rowIndex, 0));

		Auto model;
		try {
			model = autoDAO.queryForId(Integer.valueOf(id));

			ViewContext initContext = model.getViewContext();
			initContext.put("driverId", model.getDriverId());

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
			autoDAO.create((Auto) context.makeModel(scope));
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			refreshData();
		}

	}

	@Override
	public void updateEntry(ViewContext context) {

		try {
			autoDAO.update((Auto) context.makeModel(scope));
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			refreshData();
		}

	}

	@Override
	protected ViewContext addListSelection(ViewContext initContext,
			AbstractModel model) {

		initContext.put("driver_id", ((Auto) model).getDriverId());
		return initContext;

	}

	@Override
	protected ViewContext addEmptySelection(ViewContext initContext) {

		initContext.put("driver_id", null);
		return initContext;
	}

	public HashMap<Integer, Date> getOrderEndDates() {
		HashMap<Integer, Date> orderEndDates = new HashMap<Integer, Date>();
		List<Order> orders;
		try {
			
			orders = Program.getOrderDAO().queryForAll();
			for (Order order : orders) {
				Integer autoId = Program.getDriverDAO().queryForId(order.getDriver()).getAutoId();
				orderEndDates.put(autoId, order.getEndDate());
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return orderEndDates;
	}

	public void setAutoStatuses(List<Auto> autos,
			HashMap<Integer, Date> orderEndDates) {
		for (Auto auto : autos) {

			Date orderEndDate = orderEndDates.get(auto.getId());
			Integer status;

			if (auto.getUnderRepair().equals(true)) {
				status = Auto.REPAIR;
			} else {
				if (orderEndDate == null) {
					status = Auto.FREE;
				} else {
					if (orderEndDate.after(new Date())) {
						status = Auto.WORK;
					} else {
						status = Auto.FREE;
					}
				}
			}
			auto.setStatus(status);
		}
	}

	@Override
	protected void printMapButtonClicked() {}

	@Override
	protected void printOrderButtonClicked() {}
}

class AutoTableCellRender extends DefaultTableCellRenderer {
	/**
	 * 
	 */
	private static final long serialVersionUID = -851027197333293118L;
	private AutoTableModel tableModel;
	private static final Map<Integer, String> STATES = new HashMap<Integer, String>();

	static {
		STATES.put(Auto.WORK, "На заказе");
		STATES.put(Auto.FREE, "Свободен");
		STATES.put(Auto.REPAIR, "В ремонте");
	}

	public AutoTableCellRender(AutoTableModel tableModel) {
		super();
		this.tableModel = tableModel;
		setOpaque(true);
	}

	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {

		int modelRow = table.getRowSorter().convertRowIndexToModel(row);
		Auto auto = tableModel.getRawByRowIndex(modelRow);

		Color firstColor = Color.black;
		Color secondColor = Color.white;

		Integer autoStatus = auto.getStatus();
		if (tableModel.findColumn("status") == column) {
			value = STATES.get(autoStatus);
		}
		
		setText(value != null ? value.toString() : "");

		if (autoStatus.equals(Auto.FREE)) {
			secondColor = Color.green;
		} else if (autoStatus.equals(Auto.REPAIR)) {
			firstColor = Color.red;
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
