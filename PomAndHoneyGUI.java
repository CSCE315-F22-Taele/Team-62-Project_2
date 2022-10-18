import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.StyleContext;
import java.sql.*;
import java.lang.Math;
import java.util.Locale;

public class PomAndHoneyGUI extends JFrame {
	private JPanel mainPanel;
	private JButton btnServerView;
	private JButton btnSummary;
	private JButton btnInventory;
	private JButton btnOrders;
	private JPanel serverPanel;
	private JPanel summaryPanel;
	private JPanel orderPanel;
	private JPanel inventoryPanel;
	private dbConnection db;
	Color customPurple = new Color(65, 30, 122);
	Color customWhite = new Color(255, 255, 255);

	public PomAndHoneyGUI(dbConnection database) {
		db = database;
		Border border = new LineBorder(customPurple, 2);

		btnInventory = new JButton();
		btnInventory.setBorder(border);
		btnInventory.getInsets();

		btnOrders = new JButton();
		btnOrders.setBorder(border);

		btnSummary = new JButton();
		btnSummary.setBorder(border);

		btnServerView = new JButton();
		btnServerView.setBorder(border);


		$$$setupUI$$$();
		this.setContentPane(mainPanel);
		this.setSize(1500, 1000);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// only show buttons when initially launch
		inventoryPanel.setVisible(false);
		summaryPanel.setVisible(false);
		serverPanel.setVisible(true);
		orderPanel.setVisible(false);

		// changed button colors
		int fontSize = 17;
		String customFont = "Gill Sans Nova Light";
		btnInventory.setBackground(customPurple);
		btnInventory.setForeground(customWhite);
		btnInventory.setFont(new Font(customFont, Font.BOLD, fontSize));

		btnServerView.setBackground(customWhite);
		btnServerView.setForeground(customPurple);
		btnServerView.setFont(new Font(customFont, Font.BOLD, fontSize));

		btnOrders.setBackground(customPurple);
		btnOrders.setForeground(customWhite);
		btnOrders.setFont(new Font(customFont, Font.BOLD, fontSize));

		btnSummary.setBackground(customPurple);
		btnSummary.setForeground(customWhite);
		btnSummary.setFont(new Font(customFont, Font.BOLD, fontSize));

		btnServerView.addActionListener(new ActionListener() {
			/**
			 * @param e the event to be processed
			 */
			@Override
			public void actionPerformed(ActionEvent e) {
				inventoryPanel.setVisible(false);
				summaryPanel.setVisible(false);
				serverPanel.setVisible(true);
				orderPanel.setVisible(false);


			}
		});
		btnSummary.addActionListener(new ActionListener() {
			/**
			 * @param e the event to be processed
			 */
			@Override
			public void actionPerformed(ActionEvent e) {
				inventoryPanel.setVisible(false);
				summaryPanel.setVisible(true);
				serverPanel.setVisible(false);
				orderPanel.setVisible(false);


				// changed button colors
				btnInventory.setBackground(customPurple);
				btnInventory.setForeground(customWhite);
				btnServerView.setBackground(customPurple);
				btnServerView.setForeground(customWhite);
				btnOrders.setBackground(customPurple);
				btnOrders.setForeground(customWhite);
				btnSummary.setBackground(customWhite);
				btnSummary.setForeground(customPurple);
			}
		});
		btnInventory.addActionListener(new ActionListener() {
			/**
			 * @param e the event to be processed
			 */
			@Override
			public void actionPerformed(ActionEvent e) {
				inventoryPanel.setVisible(true);
				summaryPanel.setVisible(false);
				serverPanel.setVisible(false);
				orderPanel.setVisible(false);

				// changed button colors
				//InventoryComponents();
				Inventory inventory = new Inventory(db, customPurple, customWhite);
				JPanel verticalPanel = new JPanel(new BorderLayout());
				inventoryPanel.removeAll();
				inventoryPanel.add(inventory.mainInventoryPanel(verticalPanel));
				inventoryPanel.validate();
				inventoryPanel.revalidate();
				btnInventory.setBackground(customWhite);
				btnInventory.setForeground(customPurple);
				btnServerView.setBackground(customPurple);
				btnServerView.setForeground(customWhite);
				btnOrders.setBackground(customPurple);
				btnOrders.setForeground(customWhite);
				btnSummary.setBackground(customPurple);
				btnSummary.setForeground(customWhite);
			}
		});
		btnOrders.addActionListener(new ActionListener() {
			/**
			 * @param e the event to be processed
			 */
			@Override
			public void actionPerformed(ActionEvent e) {
				inventoryPanel.setVisible(false);
				summaryPanel.setVisible(false);
				serverPanel.setVisible(false);
				orderPanel.setVisible(true);

				// changed button colors
				Order order = new Order(db, customPurple, customWhite);
				JPanel verticalPanel = new JPanel(new BorderLayout());
				orderPanel.removeAll();
				orderPanel.add(order.mainOrderPanel(verticalPanel));
				orderPanel.validate();
				orderPanel.revalidate();

				btnInventory.setBackground(customPurple);
				btnInventory.setForeground(customWhite);
				btnServerView.setBackground(customPurple);
				btnServerView.setForeground(customWhite);
				btnOrders.setBackground(customWhite);
				btnOrders.setForeground(customPurple);
				btnSummary.setBackground(customPurple);
				btnSummary.setForeground(customWhite);
			}
		});


	}

	/**
	 * this is where to add our serverView, summary, inventory, orders
	 * TODO: place custom component creation code here
	 */
	private void createUIComponents() {
		InventoryComponents();
		SummaryComponents();
		OrderComponents();
		ServerComponents();
	}

	private void InventoryComponents() {
		Inventory inventory = new Inventory(db, customPurple, customWhite);
		JPanel verticalPanel = new JPanel(new BorderLayout());

		inventoryPanel = new JPanel();
		inventoryPanel.add(inventory.mainInventoryPanel(verticalPanel));
	}

	private void SummaryComponents() {
		summaryPanel = new JPanel();
		JLabel title = new JLabel("Summary");
		summaryPanel.add(title);
		int id = 0;
		String date = "";
		int ordersToday = 0;
		double salesToday = 0;
		int ordersWeek = 0;
		double salesWeek = 0;
		try {
			ResultSet r = db.sendCommand("SELECT MAX(id) FROM orders");
			r.next();
			id = r.getInt("max");
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}
		try {
			ResultSet r = db.sendCommand("SELECT date FROM orders WHERE id = " + id);
			r.next();
			date = r.getString("date");
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}
		try {
			ResultSet r = db.sendCommand("SELECT total FROM orders WHERE date = '" + date + "'");
			while (r.next()) {
				ordersToday++;
				salesToday += r.getDouble("total");
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}

		// create a new frame
		//System.out.println(ordersToday + " " + salesToday);
		String today = "                 TODAY                  \n Revenue: " + salesToday + "       Orders: " + ordersToday + "\n";
		for (int i = 0; i < 7; i++) {
			if (date != "") {
				try {
					ResultSet r = db.sendCommand("SELECT total FROM orders WHERE date = '" + date + "'");
					while (r.next()) {
						ordersWeek++;
						salesWeek += r.getDouble("total");
					}
				} catch (Exception e) {
					break;
				}
			}
			try {
				ResultSet r = db.sendCommand("SELECT date FROM orders WHERE date = (date '" + date + "' - integer '1')");
				r.next();
				date = r.getString("date");
			} catch (Exception e) {
				date = "";
				continue;
			}
		}
		String week = "                 Week                  \n Revenue: " + salesWeek + "       Orders: " + ordersWeek + "       Avg. Order: " + (double) salesWeek / ordersWeek;
		JTextArea contents = new JTextArea(today + week);
		contents.setEditable(false);

		Border border = new LineBorder(customPurple, 2);
		contents.setBorder(border);

		summaryPanel.add(contents);
	}

	private void ServerComponents() {
		JPanel server_panel = new JPanel(new BorderLayout());
		serverView serverView = new serverView(server_panel, db);
		serverPanel = new JPanel();

		server_panel.setBackground(customWhite);
		serverPanel.setBackground(customWhite);

		serverPanel.add(server_panel);
	}


	private void OrderComponents() {
		Order order = new Order(db, customPurple, customWhite);
		JPanel verticalPanel = new JPanel(new BorderLayout());
		orderPanel = new JPanel();
		orderPanel.add(order.mainOrderPanel(verticalPanel));

	}

	public static void main(String[] args) {

		/*
		 IMPORTANT NOTES!!!!!!!!
		 must use to currently run, will prob need to add more stuff later:
		 window:
		 javac .\PomAndHoneyGUI.java .\dbConnection.java .\dbSetup.java .\Inventory.java .\GUI.java .\Product.java .\ProductDef.java .\serverView.java .\testGeneration.java .\Item.java .\addItems.java .\Order.java -cp ";forms_rt.jar"

		 Mac
		 javac ./PomAndHoneyGUI.java ./dbConnection.java ./dbSetup.java ./Inventory.java ./GUI.java ./Product.java ./ProductDef.java ./serverView.java ./testGeneration.java ./Item.java ./addItems.java -cp ":forms_rt.jar"

		 windows
		 java -cp ".;forms_rt.jar;postgresql-42.2.8.jar" .\PomAndHoneyGUI.java

		 mac/linux
		 java -cp ".:forms_rt.jar:postgresql-42.2.8.jar" ./PomAndHoneyGUI.java
		 */

		dbConnection db = new dbConnection();
		db.connect();
		PomAndHoneyGUI mainFrame = new PomAndHoneyGUI(db);
		mainFrame.setVisible(true);

	}

	/**
	 * Method generated by IntelliJ IDEA GUI Designer
	 * >>> IMPORTANT!! <<<
	 * DO NOT edit this method OR call it in your code!
	 *
	 * @noinspection ALL
	 */
	private void $$$setupUI$$$() {
		createUIComponents();
		mainPanel = new JPanel();
		mainPanel.setLayout(new GridLayoutManager(3, 2, new Insets(0, 0, 0, 0), -1, -1));
		mainPanel.setBackground(new Color(-1));
		mainPanel.setForeground(new Color(-1));
		final JPanel panel1 = new JPanel();
		panel1.setLayout(new GridLayoutManager(1, 4, new Insets(0, 0, 0, 0), -1, -1));
		panel1.setBackground(new Color(-1));
		panel1.setForeground(new Color(-1));
		mainPanel.add(panel1, new GridConstraints(0, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
		btnServerView.setBackground(new Color(-12509574));
		btnServerView.setForeground(new Color(-1));
		btnServerView.setText("Server View");
		panel1.add(btnServerView, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(100, 50), null, null, 0, false));
		btnSummary.setBackground(new Color(-12509574));
		btnSummary.setForeground(new Color(-1));
		btnSummary.setText("Summary");
		panel1.add(btnSummary, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(100, 50), null, null, 0, false));
		btnInventory.setBackground(new Color(-12509574));
		btnInventory.setForeground(new Color(-1));
		btnInventory.setText("Inventory");
		panel1.add(btnInventory, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(100, 50), null, null, 0, false));
		btnOrders.setBackground(new Color(-12509574));
		Font btnOrdersFont = this.$$$getFont$$$("Gill Sans Nova Light", -1, -1, btnOrders.getFont());
		if (btnOrdersFont != null) btnOrders.setFont(btnOrdersFont);
		btnOrders.setForeground(new Color(-1));
		btnOrders.setText("Orders");
		panel1.add(btnOrders, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(100, 50), null, null, 0, false));
		serverPanel.setBackground(new Color(-1));
		mainPanel.add(serverPanel, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(1500, 750), new Dimension(1500, 750), 0, true));
		orderPanel.setBackground(new Color(-1));
		mainPanel.add(orderPanel, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(1500, 750), null, 0, true));
		inventoryPanel.setBackground(new Color(-1));
		inventoryPanel.setForeground(new Color(-1));
		mainPanel.add(inventoryPanel, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(1500, 750), null, 0, true));
		summaryPanel.setBackground(new Color(-1));
		mainPanel.add(summaryPanel, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(1500, 750), null, 0, true));
	}

	/**
	 * @noinspection ALL
	 */
	private Font $$$getFont$$$(String fontName, int style, int size, Font currentFont) {
		if (currentFont == null) return null;
		String resultName;
		if (fontName == null) {
			resultName = currentFont.getName();
		} else {
			Font testFont = new Font(fontName, Font.PLAIN, 10);
			if (testFont.canDisplay('a') && testFont.canDisplay('1')) {
				resultName = fontName;
			} else {
				resultName = currentFont.getName();
			}
		}
		Font font = new Font(resultName, style >= 0 ? style : currentFont.getStyle(), size >= 0 ? size : currentFont.getSize());
		boolean isMac = System.getProperty("os.name", "").toLowerCase(Locale.ENGLISH).startsWith("mac");
		Font fontWithFallback = isMac ? new Font(font.getFamily(), font.getStyle(), font.getSize()) : new StyleContext().getFont(font.getFamily(), font.getStyle(), font.getSize());
		return fontWithFallback instanceof FontUIResource ? fontWithFallback : new FontUIResource(fontWithFallback);
	}

	/**
	 * @noinspection ALL
	 */
	public JComponent $$$getRootComponent$$$() {
		return mainPanel;
	}


}
