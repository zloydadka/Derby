package main.java.com.github.view;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import javax.swing.AbstractAction;
import javax.swing.GroupLayout;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import main.java.com.github.controller.AbstractController;
import main.java.com.github.model.AbstractModel;
import main.java.com.github.util.ObservingTextField;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;

public abstract class AbstractView<M extends AbstractModel> extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3345327856268830426L;

	private static final Map<DataType, Class<? extends JComponent>> fieldTypes = new HashMap<DataType, Class<? extends JComponent>>();
	protected Map<String, JComponent> labelDataMap = new HashMap<String, JComponent>();
	protected ViewContext context = new ViewContext();
	protected Field[] fields;
	private JPanel panel;
	private BottomButtonsPanel bottomPanel;
	protected Class<M> scope;
	protected Mode mode = Mode.NEW;
	protected AbstractController<M> controller;

	public enum Mode {
		NEW, EDIT
	}

	static {

		fieldTypes.put(DataType.STRING, JTextField.class);
		fieldTypes.put(DataType.BOOLEAN_OBJ, JCheckBox.class);
		fieldTypes.put(DataType.INTEGER_OBJ, JComboBox.class);
		fieldTypes.put(DataType.DATE, JTextField.class);

	}

	public AbstractView(AbstractController<M> controller, Class<M> scope) {

		super();
		this.controller = controller;
		this.scope = scope;

		fields = scope.getDeclaredFields();
		panel = new JPanel(new BorderLayout());
		bottomPanel = new BottomButtonsPanel(getMode());

		add(makeInsertPanel(fields), BorderLayout.CENTER);
		add(bottomPanel, BorderLayout.SOUTH);
		initializeButtonListeners();
		initializeKeyListeners();
		this.add(panel);

		KeyStroke enter = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0);
		panel.getInputMap(JPanel.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(enter,
				"enter");
		panel.getActionMap().put("enter", new SubmitAction());
	}

	private JPanel makeInsertPanel(Field[] fields) {

		panel = new JPanel();
		GroupLayout layout = new GroupLayout(panel);
		layout.setAutoCreateContainerGaps(true);
		layout.setAutoCreateGaps(true);
		panel.setLayout(layout);
		GroupLayout.SequentialGroup hGroup = layout.createSequentialGroup();
		GroupLayout.SequentialGroup vGroup = layout.createSequentialGroup();
		GroupLayout.ParallelGroup hLabelGroup = layout.createParallelGroup();
		GroupLayout.ParallelGroup hFieldGroup = layout.createParallelGroup();

		try {
			for (Field field : fields) {
				DatabaseField annotation = field
						.getAnnotation(DatabaseField.class);
				if (annotation != null) {
					DataType dataType = annotation.dataType();
					Map<String, String> labelTransl = engRusFieldMap();

					JLabel label = new JLabel(labelTransl.get(field.getName()));
					Class<? extends JComponent> class1 = fieldTypes
							.get(dataType);
					JComponent instance = class1.newInstance();
					labelDataMap.put(field.getName(), instance);
					hLabelGroup.addComponent(label);
					hFieldGroup.addComponent(instance);
					vGroup.addGroup(layout
							.createParallelGroup(GroupLayout.Alignment.BASELINE)
							.addComponent(label).addComponent(instance));

				}

			}
			hGroup.addGroup(hLabelGroup);
			hGroup.addGroup(hFieldGroup);
			layout.setHorizontalGroup(hGroup);
			layout.setVerticalGroup(vGroup);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return panel;
	}

	public void setContext(ViewContext viewContext) {

		this.context = viewContext;
		GroupLayout layout = new GroupLayout(panel);
		layout.setAutoCreateContainerGaps(true);
		layout.setAutoCreateGaps(true);
		panel.setLayout(layout);
		GroupLayout.SequentialGroup hGroup = layout.createSequentialGroup();
		GroupLayout.SequentialGroup vGroup = layout.createSequentialGroup();
		GroupLayout.ParallelGroup hLabelGroup = layout.createParallelGroup();
		GroupLayout.ParallelGroup hFieldGroup = layout.createParallelGroup();

		HashMap<JLabel, JComponent> labelCompMap = fillComponents(context);
		for (Entry<JLabel, JComponent> entry : labelCompMap.entrySet()) {
			JLabel label = entry.getKey();
			JComponent component = entry.getValue();
			hLabelGroup.addComponent(label);
			hFieldGroup.addComponent(component);
			vGroup.addGroup(layout
					.createParallelGroup(GroupLayout.Alignment.BASELINE)
					.addComponent(label).addComponent(component));

		}

		hGroup.addGroup(hLabelGroup);
		hGroup.addGroup(hFieldGroup);
		layout.setHorizontalGroup(hGroup);
		layout.setVerticalGroup(vGroup);
	}

	protected abstract HashMap<Integer, String> getComboBoxOptions(
			String fieldName);

	private void initializeButtonListeners() {

		this.bottomPanel.getInsertButton().addActionListener(
				new ActionListener() {

					public void actionPerformed(ActionEvent arg0) {
						submit();
					}
				});

		this.bottomPanel.getCancelButton().addActionListener(
				new ActionListener() {

					public void actionPerformed(ActionEvent arg0) {
						cancelButtonClicked();
					}
				});
	}

	private void initializeKeyListeners() {

		KeyStroke enter = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0);
		KeyStroke escape = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);

		panel.getInputMap(JPanel.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(enter,
				"enter");
		panel.getInputMap(JPanel.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(
				escape, "escape");

		panel.getActionMap().put("enter", new SubmitAction());
		panel.getActionMap().put("escape", new EscapeAction());
	}

	@SuppressWarnings("unchecked")
	private void submit() {

		for (Entry<String, JComponent> item : labelDataMap.entrySet()) {
			JComponent component = item.getValue();
			Object value = null;
			if (component instanceof ObservingTextField) {
				value = ((JTextField) component).getText();
			} else if (component instanceof JCheckBox) {
				value = ((JCheckBox) component).isSelected();
			} else if (component instanceof JTextField) {
				value = ((JTextField) component).getText();
			} else if (component instanceof JComboBox<?>) {
				value = ((Map.Entry<Integer, String>) ((JComboBox<String>) component)
						.getSelectedItem()).getKey();
			}
			context.put(item.getKey(), value);

		}
		if (mode.equals(Mode.NEW)) {
			controller.createEntry(context);
		} else {
			controller.updateEntry(context);
		}
		super.dispose();
		this.dispose();

	}

	protected abstract Map<String, String> engRusFieldMap();

	private void cancelButtonClicked() {
		this.dispose();
	}

	public Mode getMode() {
		return mode;
	}

	public void setMode(Mode mode) {

		this.mode = mode;
		setTitle(this.getTitleByMode());
		bottomPanel.setMode(mode);

	}

	protected abstract String getTitleByMode();

	protected abstract HashMap<JLabel, JComponent> fillComponents(
			ViewContext context);

	private class SubmitAction extends AbstractAction {

		/**
		 * 
		 */
		private static final long serialVersionUID = -7297147015508718102L;

		@Override
		public void actionPerformed(ActionEvent e) {
			submit();
		}
	}

	private class EscapeAction extends AbstractAction {

		/**
		 * 
		 */
		private static final long serialVersionUID = 5351796041568306978L;

		@Override
		public void actionPerformed(ActionEvent e) {
			cancelButtonClicked();
		}
	}
}
