package main.java.com.github.controller;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import main.java.com.github.Program;
import main.java.com.github.model.AbstractModel;
import main.java.com.github.model.Object2;
import main.java.com.github.model.Order;
import main.java.com.github.view.AbstractView;

import main.java.com.github.view.ViewContext;

public abstract class AbstractController<M extends AbstractModel> extends
		JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 9198256893031041222L;
	protected String title;
	protected String tip;
	protected Icon iconImage;

	protected JTable table;
	protected Class<M> scope;
	protected AbstractView<M> view;

	public static Map<String, String> dataMap = new HashMap<String, String>();

	public String getTitle() {
		return title;
	}

	public String getTip() {
		return tip;
	}

	public Icon getIconImage() {
		return iconImage;
	}

	public AbstractController(String title, String tip, Class<M> scope) {

		super(new BorderLayout());
		this.title = title;
		this.tip = tip;
		add(makeTablePanel(), BorderLayout.CENTER);
		this.scope = scope;

	}

	private Component makeTablePanel() {

		table = new JTable();
		table.setAutoCreateRowSorter(true);
		initializeTableListeners();
		JScrollPane scrollPane = new JScrollPane(table);	
		scrollPane.addMouseListener(new TableMouseListener());
		return scrollPane;
		
	}

	private void initializeTableListeners() {

		KeyStroke enter = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0);
		table.getInputMap(JTable.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(enter,
				"enter");
		table.getActionMap().put("enter", new EnterAction());
		
		KeyStroke delete = KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0);	
		table.getInputMap(JTable.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(
				delete, "delete");
		table.getActionMap().put("delete", new DeleteAction());
		
		table.addMouseListener(new TableMouseListener());
		
	}

	protected abstract ViewContext addListSelection(ViewContext initContext,
			AbstractModel model);

	protected abstract ViewContext addEmptySelection(ViewContext initContext);

	protected abstract void addButtonClicked();

	protected abstract void editButtonClicked();

	void deleteButtonClicked() {

		int dialogButton = JOptionPane.YES_NO_OPTION;
		int dialogResult = JOptionPane.showConfirmDialog(null,
				"Удалить запись?", "Подтверждение", dialogButton);
		
		if (dialogResult == JOptionPane.YES_OPTION) {
			try {
				int[] rowIndexes = table.getSelectedRows();
				for (int index : rowIndexes) {
					Program.getDaoByModel(scope).deleteById(
							new Integer(table.getValueAt(index, 0).toString()));
				}
				refreshData();
			} catch (SQLException e) {e.printStackTrace();}
		}
		
	}

	protected void refreshButtonClicked() {
		
		refreshData();
		
	}

	public abstract void createEntry(ViewContext context);

	public abstract void updateEntry(ViewContext context);

	protected void setIconImage(String url) {
		
		this.iconImage = new ImageIcon(ClassLoader.getSystemResource("images/"
				+ url));
		
	}

	public abstract void refreshData();

	protected abstract void printMapButtonClicked();

	protected abstract void printOrderButtonClicked();

	public void openURL(String url) {
		
		try {
			if (Desktop.isDesktopSupported())
				Desktop.getDesktop().browse((new URL(url)).toURI());
		} catch (Exception ex) {ex.printStackTrace();}
		
	}

	private class EnterAction extends AbstractAction {

		/**
		 * 
		 */
		private static final long serialVersionUID = 3762901813626075332L;

		@Override
		public void actionPerformed(ActionEvent e) {
			editButtonClicked();
		}
		
	}

	private class DeleteAction extends AbstractAction {

		/**
		 * 
		 */
		private static final long serialVersionUID = -2753281926483922982L;

		@Override
		public void actionPerformed(ActionEvent e) {
			deleteButtonClicked();
		}
		
	}

	private class TableMouseListener extends MouseAdapter {

		public void mouseClicked(MouseEvent e) {
			
			if (e.getButton() == MouseEvent.BUTTON1) {
				if (e.getClickCount() == 2)
					editButtonClicked();
				else {
					Point point = new Point(e.getX(), e.getY()); 
					int row = table.rowAtPoint(point);
					int col = table.columnAtPoint(point);
					String url = (String) table.getModel().getValueAt(row, col);
					if (table.getModel().getColumnClass(col) == URL.class) openURL(url);
				}
			}

		}

		public void mousePressed(MouseEvent e) {
			
			if (e.isPopupTrigger()) {
				int rowNumber = table.rowAtPoint(e.getPoint());
				if (e.getComponent() instanceof JTable)
					((JTable) e.getComponent()).getSelectionModel()
							.setSelectionInterval(rowNumber, rowNumber);
				doPop(e);
			}
			
		}

		public void mouseReleased(MouseEvent e) {
			
			if (e.isPopupTrigger())
				doPop(e);
			
		}

		private void doPop(MouseEvent e) {
			
			ContextMenu menu = new ContextMenu();
			menu.show(e.getComponent(), e.getX(), e.getY());
			
		}
	}

	private class ContextMenu extends JPopupMenu {

		/**
		 * 
		 */
		private static final long serialVersionUID = 3569301837988652545L;

		private JMenuItem addItem;
		private JMenuItem editItem;
		private JMenuItem deleteItem;
		private JMenuItem refreshItem;
		private JMenuItem printOrderItem;
		private JMenuItem printMapItem;

		
		
		public ContextMenu() {

			addItem = new JMenuItem("Добавить");
			addItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					addButtonClicked();
				}
			});
			editItem = new JMenuItem("Редактировать");
			editItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					editButtonClicked();
				}
			});
			deleteItem = new JMenuItem("Удалить");
			deleteItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					deleteButtonClicked();
				}
			});
			refreshItem = new JMenuItem("Обновить");
			refreshItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					refreshButtonClicked();
				}
			});
			printOrderItem = new JMenuItem("Распечатать заказ");
			printOrderItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					printOrderButtonClicked();
				}
			});
			printMapItem = new JMenuItem("Распечатать карту");
			printMapItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					printMapButtonClicked();
				}
			});

			add(addItem);
			add(editItem);
			add(deleteItem);
			add(refreshItem);
			add(printOrderItem);
			add(printMapItem);

			editItem.addChangeListener(new ChangeListener() {

				@Override
				public void stateChanged(ChangeEvent e) {
					Boolean enabled = table.getSelectedRow() >= 0;
					editItem.setEnabled(enabled);
				}
			});
			deleteItem.addChangeListener(new ChangeListener() {

				@Override
				public void stateChanged(ChangeEvent e) {
					Boolean enabled = table.getSelectedRow() >= 0;
					deleteItem.setEnabled(enabled);
				}
			});
			printOrderItem.addChangeListener(new ChangeListener() {

				@Override
				public void stateChanged(ChangeEvent e) {
					Boolean enabled = (table.getSelectedRow() >= 0)
							&& (Order.class.isAssignableFrom(scope) || Object2.class
									.isAssignableFrom(scope));
					printOrderItem.setEnabled(enabled);
				}
			});
			printMapItem.addChangeListener(new ChangeListener() {

				@Override
				public void stateChanged(ChangeEvent e) {
					Boolean enabled = (table.getSelectedRow() >= 0)
							&& (Order.class.isAssignableFrom(scope) || Object2.class
									.isAssignableFrom(scope));
					printMapItem.setEnabled(enabled);
				}
			});
		}
	}
}
