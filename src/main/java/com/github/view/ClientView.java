package main.java.com.github.view;

import java.awt.Component;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTextField;

import com.google.common.base.Objects;

import main.java.com.github.controller.ClientController;
import main.java.com.github.model.Client;
import main.java.com.github.model.KeyValueComboBoxModel;

public class ClientView extends AbstractView<Client>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1724354499558171862L;

	public ClientView(ClientController controller) {
		
		super(controller, Client.class);
	}
	
	@Override
	protected Map<String, String> engRusFieldMap() {
		
		HashMap<String, String> hashMap = new HashMap<String, String>();
		hashMap.put("id", "ID");
		hashMap.put("name", "Название");
		hashMap.put("person", "Контактное лицо");
		hashMap.put("phone", "Телефон");
		hashMap.put("address", "Адрес");
		hashMap.put("saldo", "Сальдо");
	
		return hashMap;
	}

	@Override
	protected String getTitleByMode() {
		
		return mode.equals(Mode.NEW) ? "Добавление клиента" : "Редактирование клиента";	
	}

	@Override
	protected HashMap<Integer, String> getComboBoxOptions(String fieldName) {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings({ "unchecked", "serial" })
	@Override
	protected HashMap<JLabel, JComponent> fillComponents(ViewContext context) {
		HashMap<JLabel, JComponent> labelCompMap = new HashMap<JLabel, JComponent>();
		for (Entry<String, JComponent> entry : labelDataMap.entrySet()) {
			String key = entry.getKey();
			Map<String, String> labelTransl = engRusFieldMap();
			JLabel label = new JLabel(labelTransl.get(key));
			final JComponent component = entry.getValue();
			Object value = context.get(key);
	
				if (component instanceof JCheckBox) {
					((JCheckBox) component).setSelected(((Boolean) Objects
							.firstNonNull(value, false)).booleanValue());
				} else if (component instanceof JTextField) {
					((JTextField) component).setText(Objects.firstNonNull(
							value, "").toString());
				} else if (component instanceof JComboBox) {
	
					Integer selectedId = (Integer) value;
					// System.out.println("AbstractView.setContext()" +
					// selectedId);
					@SuppressWarnings("rawtypes")
					JComboBox combo = (JComboBox) component;
					HashMap<Integer, String> options = getComboBoxOptions(key);
	
					KeyValueComboBoxModel<Integer> boxModel = new KeyValueComboBoxModel<Integer>();
					boxModel.putAll(options);
					boxModel.setSelectedItem(selectedId,
							options.get(selectedId));
					combo.setRenderer(new DefaultListCellRenderer() {

						@Override
						public Component getListCellRendererComponent(
								JList<?> list, Object value, int index,
								boolean isSelected, boolean cellHasFocus) {
							if (value instanceof Map.Entry) {
								Map.Entry<Integer, String> entry = (Map.Entry<Integer, String>) value;
								return super.getListCellRendererComponent(list,
										entry.getValue(), index, isSelected,
										cellHasFocus);
							}
							return super.getListCellRendererComponent(list,
									value, index, isSelected, cellHasFocus);
						}
	
					});
					combo.setModel(boxModel);
					// System.out.println(boxModel.getSelectedItem());
					combo.setSelectedItem(boxModel.getSelectedItem());
					
			}
			labelCompMap.put(label, component);
		}
		
		return labelCompMap;
	}
}
