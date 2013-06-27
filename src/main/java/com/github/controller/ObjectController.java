package main.java.com.github.controller;

import java.awt.Color;
import java.awt.Component;
import java.sql.SQLException;
import java.util.List;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableRowSorter;
import com.j256.ormlite.dao.Dao;
import main.java.com.github.Program;
import main.java.com.github.model.AbstractModel;
import main.java.com.github.model.Object2;
import main.java.com.github.util.ObjectTableModel;
import main.java.com.github.view.ObjectView;
import main.java.com.github.view.ViewContext;
import main.java.com.github.view.AbstractView.Mode;

public class ObjectController extends AbstractController<Object2> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3987255015433702056L;
	private Dao<Object2, Integer> objectDAO;
	private ObjectTableModel tableModel;

	public ObjectController(String title, String tip) {

		super(title, tip, Object2.class);
		this.view = new ObjectView(this);
		this.objectDAO = Program.getObjectDAO();
		setIconImage("objects.gif");

		this.tableModel = new ObjectTableModel();
		table.setModel(tableModel);
		table.setDefaultRenderer(Object.class, new ObjectTableCellRender(
				tableModel));
		TableRowSorter<ObjectTableModel> rowSorter = new TableRowSorter<ObjectTableModel>(
				tableModel);
		rowSorter.setSortable(0, false);

		rowSorter.toggleSortOrder(tableModel.findColumn("name"));
		table.setRowSorter(rowSorter);
		table.getColumnModel().getColumn(0).setMaxWidth(40);

	}

	public void refreshData() {

		Dao<Object2, Integer> objectDAO = Program.getObjectDAO();
		try {

			List<Object2> objects = objectDAO.queryForAll();
			((ObjectTableModel) table.getModel()).setRawData(objects);

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void addButtonClicked() {

		this.view = new ObjectView(this);
		try {
			ViewContext initContext = scope.newInstance().emptyContext();
			initContext.put("clientId", null);
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

		this.view = new ObjectView(this);
		int rowIndex = table.getSelectedRow();
		Integer id = Integer.valueOf((String) table.getValueAt(rowIndex, 0));

		Object2 model;
		try {
			model = objectDAO.queryForId(Integer.valueOf(id));

			ViewContext initContext = model.getViewContext();
			initContext.put("clientId", model.getClientId());

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
			this.objectDAO.create((Object2) context.makeModel(scope));
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			refreshData();
		}

	}

	@Override
	public void updateEntry(ViewContext context) {

		try {
			this.objectDAO.update((Object2) context.makeModel(scope));
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			refreshData();
		}

	}

	@Override
	protected ViewContext addListSelection(ViewContext initContext,
			AbstractModel model) {

		initContext.put("client_id", ((Object2) model).getClientId());
		return initContext;
	}

	@Override
	protected ViewContext addEmptySelection(ViewContext initContext) {

		initContext.put("client_id", null);
		return initContext;
	}

	@Override
	protected void printMapButtonClicked() {}

	@Override
	protected void printOrderButtonClicked() {}

}

class ObjectTableCellRender extends DefaultTableCellRenderer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ObjectTableModel tableModel;

	public ObjectTableCellRender(ObjectTableModel tableModel) {
		super();
		this.tableModel = tableModel;
		setOpaque(true);
	}

	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {

		Color firstColor = Color.black;
		Color secondColor = Color.white;

		if (tableModel.findColumn("addressLink") == column) {
			value = "<html><body><p><a href="
					+ "\"www.maps.yandex.ru\">Яндекс.Карты</a></p></body></html>";
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