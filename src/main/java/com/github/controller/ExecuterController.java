package main.java.com.github.controller;

import java.awt.Color;
import java.awt.Component;
import java.sql.SQLException;
import java.util.List;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import main.java.com.github.Program;
import main.java.com.github.model.AbstractModel;
import main.java.com.github.model.Executer;
import main.java.com.github.util.ExecuterTableModel;
import main.java.com.github.view.ExecuterView;
import main.java.com.github.view.ViewContext;
import main.java.com.github.view.AbstractView.Mode;

import com.j256.ormlite.dao.Dao;

public class ExecuterController extends AbstractController<Executer> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8143849514025696195L;
	private Dao<Executer, Integer> execDao;
	
	public ExecuterController(String title, String tip) {
		
		super(title, tip, Executer.class);
		this.view = new ExecuterView(this);
		this.execDao = Program.getExecuterDAO();
		setIconImage("executers.gif");
		
		ExecuterTableModel tableModel = new ExecuterTableModel();
		table.setModel(tableModel);
		table.setDefaultRenderer(Object.class, new ExecuterTableCellRender(tableModel));
		table.getColumnModel().getColumn(0).setMaxWidth(40);
	}

	public void refreshData() {
		
		Dao<Executer, Integer> executerDAO = Program.getExecuterDAO();
		try {
			
			List<Executer> execs = executerDAO.queryForAll();
			((ExecuterTableModel) table.getModel()).setRawData(execs);

		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	@Override
	public void addButtonClicked() {
		
		this.view = new ExecuterView(this);
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
		
		this.view = new ExecuterView(this);
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
	public void createEntry(ViewContext context) {		
		
		try {
			this.execDao.create((Executer) context.makeModel(scope));
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {refreshData();}	
		
	}

	@Override
	public void updateEntry(ViewContext context) {
		
		try {
			this.execDao.update((Executer) context.makeModel(scope));
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{refreshData();}
		
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

class ExecuterTableCellRender extends DefaultTableCellRenderer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8198245932583626488L;

	public ExecuterTableCellRender(ExecuterTableModel tableModel) {
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
