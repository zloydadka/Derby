package main.java.com.github.controller;

import java.awt.Color;
import java.awt.Component;
import java.sql.SQLException;
import java.util.List;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableRowSorter;
import main.java.com.github.Program;
import main.java.com.github.model.AbstractModel;
import main.java.com.github.model.Client;
import main.java.com.github.util.ClientTableModel;
import main.java.com.github.util.TableModel;
import main.java.com.github.view.ClientView;
import main.java.com.github.view.ViewContext;
import main.java.com.github.view.AbstractView.Mode;

import com.j256.ormlite.dao.Dao;

public class ClientController extends AbstractController<Client> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8143849514025696195L;
	private Dao<Client, Integer> clientDao;

	public ClientController(String title, String tip) {

		super(title, tip, Client.class);
		this.view = new ClientView(this);
		this.clientDao = Program.getClientDAO();
		setIconImage("clients.gif");
		
		ClientTableModel tableModel = new ClientTableModel();
		table.setModel(tableModel);
		table.setDefaultRenderer(Object.class, new ClientTableCellRender(tableModel));
		
		TableRowSorter<ClientTableModel> rowSorter = new TableRowSorter<ClientTableModel>(
				tableModel);
		rowSorter.setSortable(0, false);
		
		rowSorter.toggleSortOrder(((TableModel) table.getModel())
				.findColumn("name"));
		table.setRowSorter(rowSorter);
		table.getColumnModel().getColumn(0).setMaxWidth(40);
	}

	public void refreshData() {

		Dao<Client, Integer> clientDAO = Program.getClientDAO();
		try {
			
			List<Client> clients = clientDAO.queryForAll();
			((ClientTableModel) table.getModel()).setRawData(clients);

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
	
	
	@Override
	public void createEntry(ViewContext context) {

		try {
			this.clientDao.create((Client) context.makeModel(scope));
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			refreshData();
		}

	}
	
	@Override
	public void addButtonClicked() {
		
		this.view = new ClientView(this);
		try {
			ViewContext initContext = scope.newInstance().emptyContext();
			initContext = addEmptySelection(initContext);

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
		
		this.view = new ClientView(this);
		int rowIndex = table.getSelectedRow();

		Integer id = Integer.valueOf((String) table.getValueAt(rowIndex, 0));

		Dao<? extends AbstractModel, Integer> dao = Program
				.getDaoByModel(scope);

		AbstractModel model;
		try {

			model = dao.queryForId(Integer.valueOf(id));
			ViewContext initContext = model.getViewContext();
			initContext = addListSelection(initContext, model);

			view.setMode(Mode.EDIT);
			view.setContext(initContext);
			view.setSize(600, 400);
			view.setVisible(true);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void updateEntry(ViewContext context) {

		try {
			this.clientDao.update((Client) context.makeModel(scope));
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			refreshData();
		}

	}

	@Override
	protected ViewContext addListSelection(ViewContext initContext,
			AbstractModel model) {
		return initContext;
	}

	@Override
	protected ViewContext addEmptySelection(ViewContext initContext) {
		return initContext;
	}

	@Override
	protected void printMapButtonClicked() {}

	@Override
	protected void printOrderButtonClicked() {}
}

class ClientTableCellRender extends DefaultTableCellRenderer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8198245932583626488L;

	public ClientTableCellRender(ClientTableModel tableModel) {
		super();
		setOpaque(true);
	}

	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {

		Color firstColor = Color.black;
		Color secondColor = Color.white;
		
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
