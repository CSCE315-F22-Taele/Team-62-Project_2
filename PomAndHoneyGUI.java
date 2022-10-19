import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.StyleContext;
import java.util.Locale;

/**
* The PomAndHoneyGUI is the main class that initializes, controls, and maintains the GUI
*/
public class PomAndHoneyGUI extends JFrame {
	private JPanel mainPanel;
	private JButton btnServerView;
	private JButton btnSummary;
	private JButton btnInventory;
	private JButton btnOrders;
	private JPanel serverPanel;
    private summaryView sumView;
	private JPanel summaryPanel;
	private JPanel orderPanel;
	private JPanel inventoryPanel;
	private dbConnection db;
	Color customPurple = new Color(65, 30, 122);
	Color customWhite = new Color(255, 255, 255);

    /**
    * Initialize the entire GUI
    */
	public PomAndHoneyGUI(dbConnection database) {
		db = database;
		Border border = new LineBorder(customPurple, 2);

		btnInventory = new JButton();
		btnInventory.setBorder(border);
		btnInventory.getInsets();
		btnInventory.setOpaque(true);

		btnOrders = new JButton();
		btnOrders.setBorder(border);
		btnOrders.setOpaque(true);

		btnSummary = new JButton();
		btnSummary.setBorder(border);
		btnSummary.setOpaque(true);

		btnServerView = new JButton();
		btnServerView.setBorder(border);
		btnServerView.setOpaque(true);


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

                // changed button colors
				btnInventory.setBackground(customPurple);
				btnInventory.setForeground(customWhite);
				btnServerView.setBackground(customWhite);
				btnServerView.setForeground(customPurple);
				btnOrders.setBackground(customPurple);
				btnOrders.setForeground(customWhite);
				btnSummary.setBackground(customPurple);
				btnSummary.setForeground(customWhite);

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

                sumView.update();

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
	 * adds the inventory panel, summary panel, order panel and server view panel to the skeleton frame
	 */
	private void createUIComponents() {
		InventoryComponents();
		SummaryComponents();
		OrderComponents();
		ServerComponents();
	}

	/**
	 * creates the inventory panel using the inventory class
	 * the inventory can view inventory, added new items, change price, and update quantity
	 */
	private void InventoryComponents() {
		Inventory inventory = new Inventory(db, customPurple, customWhite);
		JPanel verticalPanel = new JPanel(new BorderLayout());

		inventoryPanel = new JPanel();
		inventoryPanel.add(inventory.mainInventoryPanel(verticalPanel));
	}

	/**
	 * summary panel using the summaryView class
	 * displays the revenue and order today and of the week
	 * displays commonly paired items
	 * displays items that restocks
	 * displays and generate excess sales
	 */
	private void SummaryComponents() {
		sumView = new summaryView(db);
        summaryPanel = sumView.mainPanel;
	}

	/**
	 * contains the server with all items to be ordered. uses the serverView class
	 * contains all the product such as Gyro, bowls, falafels, etc.
	 * contains the price of each item
	 * once orders are added, it can be added to order panels
	 */
	private void ServerComponents() {
		JPanel server_panel = new JPanel(new BorderLayout());
		serverView serverView = new serverView(server_panel, db);
		serverPanel = new JPanel();

		server_panel.setBackground(customWhite);
		serverPanel.setBackground(customWhite);

		serverPanel.add(server_panel);
	}


	/**
	 * displays the order added from the serverView
	 * can also change the date of the order placed
	 */
	private void OrderComponents() {
		Order order = new Order(db, customPurple, customWhite);
		JPanel verticalPanel = new JPanel(new BorderLayout());
		orderPanel = new JPanel();
		orderPanel.add(order.mainOrderPanel(verticalPanel));
	}

    /**
    * Delete a component from a parent and ensure a refresh.
    */
    public static void deleteComponent(JComponent parent, JComponent child){
        parent.remove(child);
        parent.revalidate();
        parent.repaint();
    }

    /**
    * Refresh a UI component. Should be used after adding or removing components from a panel or frame.
    */
    public static void refresh(JComponent c){
        c.revalidate();
        c.repaint();
    }

    /**
    * Initializes the database and UI
    */
	public static void main(String[] args) {

		/*
		 IMPORTANT NOTES!!!!!!!!
		 must use to currently run, will prob need to add more stuff later:
		 window:
		 javac .\PomAndHoneyGUI.java .\dbConnection.java .\dbSetup.java .\Inventory.java .\Product.java .\ProductDef.java .\serverView.java .\testGeneration.java .\Item.java .\addItems.java .\Order.java -cp ";forms_rt.jar"

		 Mac
		 javac ./PomAndHoneyGUI.java ./dbConnection.java ./dbSetup.java ./Inventory.java ./Product.java ./ProductDef.java ./serverView.java ./testGeneration.java ./Item.java ./addItems.java -cp ./Order.java ":forms_rt.jar"

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
