import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import java.sql.*;

public class PomAndHoneyGUI extends JFrame {
	private JPanel mainPanel;
	private JButton btnServerView;
	private JButton btnSummary;
	private JButton btnInventory;
	private JButton btnOrders;
	private JPanel serverPanel;
	private JPanel summaryPanel;
	private JPanel InventoryPanel;
	private JPanel orderPanel;

	public PomAndHoneyGUI() {

		setContentPane(mainPanel);
		setSize(1500, 1000);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);

		btnServerView.addActionListener(new ActionListener() {
			/**
			 * @param e the event to be processed
			 */
			@Override
			public void actionPerformed(ActionEvent e) {

			}
		});
		btnSummary.addActionListener(new ActionListener() {
			/**
			 * @param e the event to be processed
			 */
			@Override
			public void actionPerformed(ActionEvent e) {

			}
		});
		btnInventory.addActionListener(new ActionListener() {
			/**
			 * @param e the event to be processed
			 */
			@Override
			public void actionPerformed(ActionEvent e) {

			}
		});
		btnOrders.addActionListener(new ActionListener() {
			/**
			 * @param e the event to be processed
			 */
			@Override
			public void actionPerformed(ActionEvent e) {

			}
		});


	}


	public static void main(String[] args) {
		// IMPORTANT NOTES!!!!!!!!
		// must use to currently run, will prob need to add more stuff later:
		// javac .\PomAndHoneyGUI.java -cp ";forms_rt.jar"
		// java -cp ".;forms_rt.jar" .\PomAndHoneyGUI.java
		PomAndHoneyGUI mainFrame = new PomAndHoneyGUI();

	}

	{
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
		$$$setupUI$$$();
	}

	/**
	 * Method generated by IntelliJ IDEA GUI Designer
	 * >>> IMPORTANT!! <<<
	 * DO NOT edit this method OR call it in your code!
	 *
	 * @noinspection ALL
	 */
	private void $$$setupUI$$$() {
		mainPanel = new JPanel();
		mainPanel.setLayout(new GridLayoutManager(3, 2, new Insets(0, 0, 0, 0), -1, -1));
		final JPanel panel1 = new JPanel();
		panel1.setLayout(new GridLayoutManager(1, 4, new Insets(0, 0, 0, 0), -1, -1));
		mainPanel.add(panel1, new GridConstraints(0, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
		btnServerView = new JButton();
		btnServerView.setText("Server View");
		panel1.add(btnServerView, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(50, 50), null, 0, false));
		btnSummary = new JButton();
		btnSummary.setText("Summary");
		panel1.add(btnSummary, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(50, 50), null, 0, false));
		btnInventory = new JButton();
		btnInventory.setText("Inventory");
		panel1.add(btnInventory, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(50, 50), null, 0, false));
		btnOrders = new JButton();
		btnOrders.setText("Orders");
		panel1.add(btnOrders, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(50, 50), null, 0, false));
		summaryPanel = new JPanel();
		summaryPanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
		mainPanel.add(summaryPanel, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
		serverPanel = new JPanel();
		serverPanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
		mainPanel.add(serverPanel, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
		InventoryPanel = new JPanel();
		InventoryPanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
		mainPanel.add(InventoryPanel, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
		orderPanel = new JPanel();
		orderPanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
		mainPanel.add(orderPanel, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
	}

	/**
	 * @noinspection ALL
	 */
	public JComponent $$$getRootComponent$$$() {
		return mainPanel;
	}

}