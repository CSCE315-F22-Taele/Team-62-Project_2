import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.sql.*;
import java.lang.Math;

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
	String lowerDate = "";
	String upperDate = "";
	private dbConnection db;

	public PomAndHoneyGUI(dbConnection database) {
		db = database;

		$$$setupUI$$$();
		this.setContentPane(mainPanel);
		this.setSize(1500, 1000);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


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
				inventoryPanel.setVisible(false);
				summaryPanel.setVisible(true);
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

	/**
	 * this is where to add our serverView, summary, inventory, orders
	 * TODO: place custom component creation code here
	 */
	private void createUIComponents() {
		InventoryComponents();
		SummaryComponents();
//		OrderComponents(lowerDate, upperDate);
	}

	private void InventoryComponents() {
		Inventory inventory = new Inventory(db);
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
		summaryPanel.add(contents);
	}

	private void ServerComponents() {
		JPanel serverPanel = new JPanel(new BorderLayout());
//		serverView = new serverView(serverPanel, this, db);
	}

	public String retrieveOrders(String lowDate, String highDate){
		//Get the smallest id on the starting date
		double price = 0;
		String prevOrders = "Sales from "+ lowDate + " to " + highDate + ":\n\n";
		int lowID = 0;
		int highID = 0;
		try {
			ResultSet r = db.sendCommand("SELECT productList[1] FROM orders WHERE id = (SELECT MIN (id) FROM orders WHERE date >='"+lowDate+"')");
			if(r.next()){
				lowID = r.getInt("productList");
			}
			else{
				return prevOrders;
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		//Get the largest id on end date
		try {
			ResultSet r = db.sendCommand("SELECT productList[cardinality(productList)] FROM orders WHERE id = (SELECT MAX (id) FROM orders WHERE date <='"+highDate+"')");
			if(r.next()){
				highID = r.getInt("productList");
			}
			else{
				return prevOrders;
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		//Go to each needed product and output its name, price, and sale date.
		for(int i = lowID; i <=highID;i++){
			//Get date
			String productDate  = "";
			String name = "";
			try {
				ResultSet r = db.sendCommand("select date from orders where " + i + "= ANY(productList)");
				if(r.next()){
					productDate = r.getString("date");
				}
			} catch (Exception e) {
				e.printStackTrace();
				System.err.println(e.getClass().getName() + ": " + e.getMessage());
				System.exit(0);
			}
			try {
				ResultSet r = db.sendCommand("select name from products where id = " + i);
				r.next();
				name = r.getString("name");
			} catch (Exception e) {
				e.printStackTrace();
				System.err.println(e.getClass().getName() + ": " + e.getMessage());
				System.exit(0);
			}
			try {
				ResultSet r = db.sendCommand("select price from products where id = " + i);
				r.next();
				price = r.getDouble("price");
			} catch (Exception e) {
				e.printStackTrace();
				System.err.println(e.getClass().getName() + ": " + e.getMessage());
				System.exit(0);
			}
			prevOrders += name + "       " + price + "       " + productDate +"\n";
		}
		return prevOrders;
	}
	private void OrderComponents(String lowDate, String highDate) {
		orderPanel = new JPanel();
		JLabel title = new JLabel("Orders");
		orderPanel.add(title);
		String prevOrders = retrieveOrders(lowDate,highDate);

		// create a new frame
		//System.out.println(ordersToday + " " + salesToday);
		JButton dateUpdate = new JButton("Change dates");
		JTextField text = new JTextField(10);
		JTextArea contents = new JTextArea(prevOrders,20,20);
		contents.setLineWrap(true);
		contents.setWrapStyleWord(true);
		dateUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String update = text.getText();
				String[] input = update.split(" ");
				lowerDate = input[0];
				upperDate = input[1];
				orderPanel.removeAll();
				orderPanel.validate();
				orderPanel.revalidate();
			}
		});
		contents.setEditable(false);
		JScrollPane pane = new JScrollPane(contents);
		pane.setBounds(10, 11, getWidth()+5, getHeight());
		pane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		orderPanel.add(dateUpdate);
		orderPanel.add(text);
		orderPanel.add(pane);
		mainPanel.add(orderPanel);
	}

	public static void main(String[] args) {

		/*
		 IMPORTANT NOTES!!!!!!!!
		 must use to currently run, will prob need to add more stuff later:
		 javac .\PomAndHoneyGUI.java .\dbConnection.java .\dbSetup.java .\Inventory.java .\GUI.java .\Product.java .\ProductDef.java .\serverView.java .\testGeneration.java .\Item.java .\addItems.java -cp ";forms_rt.jar"

		 windows
		 java -cp ".;forms_rt.jar;postgresql-42.2.8.jar" .\PomAndHoneyGUI.java

		 mac/linux
		 java -cp ".;forms_rt.jar:postgresql-42.2.8.jar" .\PomAndHoneyGUI.java
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
		mainPanel.add(summaryPanel, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
		serverPanel = new JPanel();
		serverPanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
		mainPanel.add(serverPanel, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
		orderPanel = new JPanel();
		orderPanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
		mainPanel.add(orderPanel, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
		mainPanel.add(inventoryPanel, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
	}

	/**
	 * @noinspection ALL
	 */
	public JComponent $$$getRootComponent$$$() {
		return mainPanel;
	}


}
