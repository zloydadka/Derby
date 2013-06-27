package main.java.com.github.controller;

import java.awt.Color;
import java.awt.Component;
import java.util.Date;
import java.sql.SQLException;
import java.util.List;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableRowSorter;
import main.java.com.github.Program;
import main.java.com.github.model.AbstractModel;
import main.java.com.github.model.Object2;
import main.java.com.github.model.Order;
import main.java.com.github.util.OrderTableModel;
import main.java.com.github.view.OrderView;
import main.java.com.github.view.ViewContext;
import main.java.com.github.view.AbstractView.Mode;

import com.j256.ormlite.dao.Dao;

public class OrderController extends AbstractController<Order> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8143849514025696196L;

	private Dao<Order, Integer> orderDAO;
	private OrderTableModel tableModel;

	public OrderController(String title, String tip) {

		super(title, tip, Order.class);
		this.view = new OrderView(this);
		this.orderDAO = Program.getOrderDAO();
		setIconImage("orders.gif");

		this.tableModel = new OrderTableModel();
		table.setModel(tableModel);
		table.setDefaultRenderer(Object.class, new OrderTableCellRender(
				tableModel));

		TableRowSorter<OrderTableModel> rowSorter = new TableRowSorter<OrderTableModel>(
				tableModel);
		rowSorter.setSortable(0, false);
		rowSorter.toggleSortOrder(tableModel.findColumn("start_date"));
		table.setRowSorter(rowSorter);
		table.getColumnModel().getColumn(0).setMaxWidth(40);
	}

	public void refreshData() {

		Dao<Order, Integer> orderDAO = Program.getOrderDAO();
		try {

			List<Order> orders = orderDAO.queryForAll();
			((OrderTableModel) table.getModel()).setRawData(orders);

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void addButtonClicked() {

		this.view = new OrderView(this);
		try {
			
			ViewContext initContext = scope.newInstance().emptyContext();
			initContext.put("autoId", null);
			initContext.put("driverId", null);
			initContext.put("executerId", null);
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

		this.view = new OrderView(this);
		int rowIndex = table.getSelectedRow();
		Integer id = Integer.valueOf((String) table.getValueAt(rowIndex, 0));

		Order model;
		try {
			
			model = orderDAO.queryForId(Integer.valueOf(id));
			ViewContext initContext = model.getViewContext();
			initContext.put("driverId", model.getDriver());
			initContext.put("executerId", model.getExecuter());
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
			this.orderDAO.create((Order) context.makeModel(scope));
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			refreshData();
		}

	}

	@Override
	public void updateEntry(ViewContext context) {

		try {
			this.orderDAO.update((Order) context.makeModel(scope));
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			refreshData();
		}

	}

	@Override
	protected ViewContext addListSelection(ViewContext initContext,
			AbstractModel model) {

		initContext.put("driver_id", ((Order) model).getDriver());
		initContext.put("executer_id", ((Order) model).getExecuter());
		return initContext;
	}

	@Override
	protected ViewContext addEmptySelection(ViewContext initContext) {

		initContext.put("driver_id", null);
		initContext.put("executer_id", null);
		return initContext;
	}

	@Override
	protected void printMapButtonClicked() {
		
		int rowIndex = table.getSelectedRow();
		Integer id = Integer.valueOf((String) table.getValueAt(rowIndex, 0));
		Order order;
		Object2 object;
		try {
			
			order = orderDAO.queryForId(Integer.valueOf(id));
			object = Program.getObjectDAO().queryForId(order.getObject());
			openURL(Program.makeAddressLink("http://maps.yandex.ru/print/?text=", object.getAddress()));
			
		} catch (Exception ex) {ex.printStackTrace();};
		
	}

	@Override
	protected void printOrderButtonClicked() {
		
	}
	
}

class OrderTableCellRender extends DefaultTableCellRenderer {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2322481703183886262L;
	private OrderTableModel tableModel;

	public OrderTableCellRender(OrderTableModel tableModel) {
		super();
		this.tableModel = tableModel;
		setOpaque(true);
	}

	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {

		int modelRow = table.getRowSorter().convertRowIndexToModel(row);
		Order order = tableModel.getRawByRowIndex(modelRow);

		Color firstColor = Color.black;
		Color secondColor = Color.white;

		if (order.getEndDate().before(new Date())
				&& (order.getBillPassed().equals(false) || order
						.getWaybillPassed().equals(false))) {
			firstColor = Color.red;
		}

		if (tableModel.findColumn("bill_passed") == column) {
			value = order.getBillPassed() ? "Да" : "Нет";
		}

		if (tableModel.findColumn("waybill_passed") == column) {
			value = order.getWaybillPassed() ? "Да" : "Нет";
		}

		setText(value != null ? value.toString() : "");

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