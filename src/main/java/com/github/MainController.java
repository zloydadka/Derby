package main.java.com.github;

import java.sql.SQLException;

import javax.swing.Icon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import main.java.com.github.controller.AbstractController;
import main.java.com.github.controller.AutoController;
import main.java.com.github.controller.ClientController;
import main.java.com.github.controller.DriverController;
import main.java.com.github.controller.ExecuterController;
import main.java.com.github.controller.ObjectController;
import main.java.com.github.controller.OrderController;

public class MainController extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8102705951192354270L;
	public JTabbedPane mainPanel;

	public MainController() {

		super("Логистическая база");
		mainPanel = new JTabbedPane();
		add(mainPanel);
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}

	private void addTab(AbstractController<?> controller) {

		mainPanel.addTab(controller.getTitle(), null, controller,
				controller.getTip());

		JLabel lbl = new JLabel(controller.getTitle());
		Icon icon = controller.getIconImage();
		lbl.setIcon(icon);
		lbl.setIconTextGap(15);
		lbl.setHorizontalTextPosition(JLabel.CENTER);
		lbl.setVerticalTextPosition(JLabel.BOTTOM);

		mainPanel.setTabComponentAt(mainPanel.getTabCount() - 1, lbl);
		mainPanel.setMnemonicAt(mainPanel.getTabCount() - 1,
				48 + mainPanel.getTabCount());

	}

	public void init() throws SQLException {

		AbstractController<?> driverController = new DriverController(
				"Водители", "");
		AbstractController<?> autoController = new AutoController("Техника", "");
		AbstractController<?> executerController = new ExecuterController(
				"Исполнители", "");
		AbstractController<?> clientController = new ClientController("Клиенты",
				"");
		AbstractController<?> objectController = new ObjectController("Объекты",
				"");
		AbstractController<?> orderController = new OrderController("Заказы",
				"");
		
		addTab(driverController);
		addTab(autoController);
		addTab(executerController);
		addTab(clientController);
		addTab(objectController);
		addTab(orderController);
		
		driverController.refreshData();

		mainPanel.addChangeListener(new ChangeListener() {

			public void stateChanged(ChangeEvent e) {

				AbstractController<?> selectedComponent = (AbstractController<?>) mainPanel
						.getSelectedComponent();
				selectedComponent.refreshData();
			}
		});

		setVisible(true);

	}
}
