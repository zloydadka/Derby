package main.java.com.github;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Properties;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import main.java.com.github.model.AbstractModel;
import main.java.com.github.model.Auto;
import main.java.com.github.model.Client;
import main.java.com.github.model.Driver;
import main.java.com.github.model.Executer;
import main.java.com.github.model.Object2;
import main.java.com.github.model.Order;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class Program {

	private static final URL SETTINGS_FILE = ClassLoader
			.getSystemResource("database.properties");
	private static Dao<Driver, Integer> driverDAO;
	private static Dao<Auto, Integer> autoDAO;
	private static Dao<Executer, Integer> executerDAO;
	private static Dao<Client, Integer> clientDAO;
	private static Dao<Object2, Integer> objectDAO;
	private static Dao<Order, Integer> orderDAO;

	private static MainController controller;

	static {

		Properties properties = new Properties();
		JdbcConnectionSource connectionSource;
		try {
			properties.load(new FileInputStream(new File(SETTINGS_FILE
					.getFile()).getAbsoluteFile()));
			connectionSource = new JdbcConnectionSource(
					properties.getProperty("url"),
					properties.getProperty("user"),
					properties.getProperty("password"));

			driverDAO = DaoManager.createDao(connectionSource, Driver.class);
			autoDAO = DaoManager.createDao(connectionSource, Auto.class);
			executerDAO = DaoManager
					.createDao(connectionSource, Executer.class);
			clientDAO = DaoManager.createDao(connectionSource, Client.class);
			objectDAO = DaoManager.createDao(connectionSource, Object2.class);
			orderDAO = DaoManager.createDao(connectionSource, Order.class);

			TableUtils.createTableIfNotExists(connectionSource, Driver.class);
			TableUtils.createTableIfNotExists(connectionSource, Auto.class);
			TableUtils.createTableIfNotExists(connectionSource, Executer.class);
			TableUtils.createTableIfNotExists(connectionSource, Client.class);
			TableUtils.createTableIfNotExists(connectionSource, Object2.class);
			TableUtils.createTableIfNotExists(connectionSource, Order.class);

		} catch (SQLException e) {

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static Dao<Driver, Integer> getDriverDAO() {
		return driverDAO;
	}

	public static Dao<Auto, Integer> getAutoDAO() {
		return autoDAO;
	}

	public static Dao<Executer, Integer> getExecuterDAO() {
		return executerDAO;
	}

	public static Dao<Client, Integer> getClientDAO() {
		return clientDAO;
	}

	public static Dao<Object2, Integer> getObjectDAO() {
		return objectDAO;
	}

	public static Dao<Order, Integer> getOrderDAO() {
		return orderDAO;
	}

	public static Dao<? extends AbstractModel, Integer> getDaoByModel(
			Class<? extends AbstractModel> scope) {

		if (Driver.class.isAssignableFrom(scope)) {
			return driverDAO;
		} else if (Auto.class.isAssignableFrom(scope)) {
			return autoDAO;
		} else if (Executer.class.isAssignableFrom(scope)) {
			return executerDAO;
		} else if (Client.class.isAssignableFrom(scope)) {
			return clientDAO;
		} else if (Object2.class.isAssignableFrom(scope)) {
			return objectDAO;
		} else if (Order.class.isAssignableFrom(scope)) {
			return orderDAO;
		} else {
			return null;
		}

	}

	public static String makeAddressLink(String urlBeginning, String address) {

		String newAddress = urlBeginning + address.replace(' ', '+');
		return newAddress;

	}

	public static void main(String[] args) throws SQLException,
			ClassNotFoundException, InstantiationException,
			IllegalAccessException, UnsupportedLookAndFeelException {
		
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		controller = new MainController();
		controller.init();

	}

}
