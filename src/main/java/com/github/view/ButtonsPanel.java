package main.java.com.github.view;

import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JPanel;

public class ButtonsPanel extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5844469848394862735L;
	private JButton newButton;
	private JButton editButton;
	private JButton deleteButton;
	private JButton refreshButton;

	
	public ButtonsPanel() {
		
		super(new FlowLayout(FlowLayout.LEFT));
		newButton = new JButton("Добавить");
		editButton = new JButton("Редактировать");
		editButton.setEnabled(false);
		deleteButton = new JButton("Удалить");
		deleteButton.setEnabled(false);
		refreshButton = new JButton("Обновить");
		refreshButton.setEnabled(true);
		add(newButton);
		add(editButton);
		add(deleteButton);
		add(refreshButton);
		
	}

	public JButton getNewButton() {
		return newButton;
	}


	public JButton getEditButton() {
		return editButton;
	}

	public JButton getDeleteButton() {
		return deleteButton;
	}
	
	public JButton getRefreshButton() {
		return refreshButton;
	}
}
