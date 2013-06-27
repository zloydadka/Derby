package main.java.com.github.view;

import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JPanel;

import main.java.com.github.view.AbstractView.Mode;

public class BottomButtonsPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5844469848394862735L;
	private JButton insertButton;
	private JButton cancelButton;

	public BottomButtonsPanel(Mode mode) {

		super(new FlowLayout(FlowLayout.LEFT));
		insertButton = new JButton("Добавить");
		cancelButton = new JButton("Отмена");
		add(insertButton);
		add(cancelButton);
	}

	public JButton getInsertButton() {
		return insertButton;
	}

	public JButton getCancelButton() {
		return cancelButton;
	}

	public void setMode(Mode mode) {
		
		insertButton.setText(mode.equals(Mode.NEW) ? "Добавить" : "Изменить");
		
	}
}
