import java.sql.*;
import java.awt.event.*;
import java.awt.BorderLayout;
import javax.imageio.plugins.jpeg.JPEGHuffmanTable;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.GridLayout;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

/**
* Initializes, maintains, and updates all UI for the orders tab.
*/
public class Order {

	JPanel orderPanel;
	JPanel BigOrderPanel;
	JPanel contentPanel;
	JPanel mainPanel;
	JPanel mainPanel2;
	JPanel verticalView;
	JFrame mainFrame;
	JButton orderUpdate = new JButton("Change dates");
    String lowerDate = "";
    String upperDate = "";

	dbConnection db;

	/**
     * This is the constructor for the Order class
     *
     * @author JP Pham
     * @param database  Connection to the database
     */
	public Order(dbConnection database) {
		db = database;
		mainFrame = new JFrame("Order GUI");
		BigOrderPanel = new JPanel();
		contentPanel = new JPanel();
	}

	/**
     * This is the constructor for the order class
     *
     * @author JP Pham
     * @param database  Connection to the database
	 * @param btnBackgroundColor  Background color
	 * @param btnForeColor  Foreground color
     */
	public Order(dbConnection database, Color btnBackgroundColor, Color btnForeColor) {
		db = database;
		mainFrame = new JFrame("order GUI");
		BigOrderPanel = new JPanel();

		contentPanel = new JPanel();
		Color customPurple = new Color(65, 30, 122);
		Border border = new LineBorder(customPurple, 2);
		contentPanel.setBorder(border);

		String currentDate = "";
		try {
			ResultSet r = db.sendCommand("SELECT CAST( (SELECT CURRENT_TIMESTAMP) AS Date )");
			r.next();
			currentDate = r.getString("current_timestamp");
			upperDate = currentDate;
			lowerDate = upperDate;
		} catch (Exception e) {
			Logger.log(e);
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}


		BigOrderPanel.setBackground(Color.white);
		contentPanel.setBackground(Color.white);

		// setting button colors
		orderUpdate.setBackground(btnBackgroundColor);
		orderUpdate.setForeground(btnForeColor);
	}

	/**
     * This is the gets the values needed for the order tab
     *
     * @author Connor Callan
     * @param verticalPanel  Main Panel
	 * @return panel with values for the orders tab
     */
	public JPanel mainOrderPanel(JPanel verticalPanel) {

		mainPanel = verticalPanel;

		orders(lowerDate,upperDate);

		// reordering the panels
		int mainWidth = 2000;
		int mainHeight = 1000;
		int mainX = 450;
		int mainY = 0;
		BigOrderPanel.setBounds(mainX, mainY, mainWidth, 50);
		contentPanel.setBounds(mainX, 150, mainWidth, mainHeight);
		JTextField text = new JTextField(10);
		orderUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
                //Stores input as the lower bound date and the higher date
				String update = text.getText();
				String[] input = update.split(" ");
				lowerDate = input[0];
				upperDate = input[1];
                //Call retrieveOrder function to get orders sales within the two dates
                String prevOrders = retrieveOrders(lowerDate,upperDate);
				contentPanel.removeAll();
				JTextArea content = new JTextArea(prevOrders);


				content.setEditable(false);
				contentPanel.add(content);
				contentPanel.validate();
				contentPanel.revalidate();
			}
		});

		// vertical layout of inventory section
		mainPanel.add(text, BorderLayout.PAGE_START);
		mainPanel.add(orderUpdate, BorderLayout.CENTER);
		mainPanel.add(BigOrderPanel, BorderLayout.LINE_END);
		mainPanel.add(contentPanel, BorderLayout.PAGE_END);

		mainPanel2 = new JPanel(new BorderLayout());
		mainPanel2.add(mainPanel, BorderLayout.SOUTH);

		return mainPanel2;
	}


	/**
     * This retrieves the data needed for sales within a window of time
     * Retrieves the sales for all orders placed between the two dates by the products sold.
     * @author Connor Callan
     * @param lowDate This is the lower bound date
	 * @param highDate This is the upper bound date
	 * @return String with the sales info
     */
    public String retrieveOrders(String lowDate, String highDate){
		//Get the smallest id on the starting date
		double price = 0;
		String prevOrders = "Sales from "+ lowDate + " to " + highDate + ":\n\n";
		try {
				ResultSet r = db.sendCommand("SELECT name, sum(price) from products where (date >= '"+lowDate+"' AND date <= '" + highDate + "') group by name");
				while (r.next()) {
					prevOrders += r.getString("name") + ":       " + r.getDouble("sum")+"$\n";
				}
			} catch (Exception e) {
				Logger.log(e);
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			}

		return prevOrders;
	}

	/**
     * This retrieves the initial data for the tab.
     * Generates the contents of the order panel
     * @author Connor Callan
     * @param lowDate This is the lower bound date
	 * @param highDate This is the upper bound date
     */
	public void orders(String lowDate, String highDate) {
		orderPanel = new JPanel();
		JLabel title = new JLabel("Orders");
		orderPanel.add(title);

		String prevOrders = retrieveOrders(lowDate,highDate);
		JTextArea content = new JTextArea(prevOrders);
		content.setEditable(false);
		contentPanel.add(content);
		BigOrderPanel.add(orderPanel);
	}

}
