import java.sql.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.BorderLayout;
import java.util.Vector;

/*
  TODO:
  1) Change credentials for your own team's database
  2) Change SQL command to a relevant query that retrieves a small amount of data
  3) Create a JTextArea object using the queried data
  4) Add the new object to the JPanel p
*/

public class GUI extends JFrame {
	private JFrame mainFrame;
	private JPanel mainPanel;
	private JPanel managerViewSummary;
	private JPanel managerViewInventory;
	private JPanel managerViewOrders;
	private serverView serverView;
	private dbConnection db;

	private JPanel verticalInventoryView;
	String currentDate = "";
	String lowerDate = "";
	String upperDate = "";

	public GUI(dbConnection database) {
		db = database;
		//Get Current date
		try {
			ResultSet r = db.sendCommand("SELECT CAST( (SELECT CURRENT_TIMESTAMP) AS Date )");
			r.next();
			currentDate = r.getString("current_timestamp");
			upperDate = currentDate;
			lowerDate = upperDate;
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}
		// create a new frame
		mainFrame = new JFrame("DB GUI");
		mainPanel = new JPanel();

		// reordering the panels
		int mainWidth = 1000;
		int mainHeight = 700;
		int mainX = 300;
		int mainY = 0;
		int frameWidth = 1500;
		int frameHeight = 1000;
		mainPanel.setBounds(mainX, mainY, mainWidth, mainHeight);

		loadManagerViewOrders(lowerDate,upperDate);
		loadManagerViewSummary();
		loadManagerViewInventory();
		loadServerView();

		// set the size of frame
		mainFrame.add(mainPanel);

		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//mainFrame.setLayout(null);

		mainFrame.setSize(frameWidth, frameHeight);
		switchToManagerViewInventory();
		mainFrame.show();
	}

    public static void deleteComponent(JComponent parent, JComponent child){
        parent.remove(child);
        parent.revalidate();
        parent.repaint();
    }

    public static void refresh(JComponent c){
        c.revalidate();
        c.repaint();
    }

	/**
	 * add in the buttons: summary, Inventory, Orders
	 *
	 */
	public void switchToManagerView() {
		switchToManagerViewSummary();
	}

	private void loadServerView() {
		JPanel serverPanel = new JPanel(new BorderLayout());
		serverView = new serverView(serverPanel, this, db);
		mainPanel.add(serverPanel);
	}

	public void loadManagerHeader(JPanel p) {
		JButton Server = new JButton("Server View");
		Server.addActionListener(e -> switchToServerView());
		p.add(Server);

		JButton Summary = new JButton("Summary");
		Summary.addActionListener(e -> switchToManagerViewSummary());
		p.add(Summary);

		JButton Inventory = new JButton("Inventory");
		Inventory.addActionListener(e -> switchToManagerViewInventory());
		p.add(Inventory);

		JButton Orders = new JButton("Orders");
		Orders.addActionListener(e -> switchToManagerViewOrders());
		p.add(Orders);
	}

	/**
	 * creates the summary panel
	 */
	public void loadManagerViewSummary() {
		managerViewSummary = new JPanel();
		loadManagerHeader(managerViewSummary);
		JLabel title = new JLabel("Summary");
		managerViewSummary.add(title);
		int id = 0;
		String date = currentDate;
		int ordersToday = 0;
		double salesToday = 0;
		int ordersWeek = 0;
		double salesWeek = 0;
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
			if(date!=""){
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
		managerViewSummary.add(contents);
		String pairs = "";
        try {
            ResultSet r = db.sendCommand("SELECT t1.name as i1, t2.name as i2, count(*) from products t1 join products t2 on t1.orderId = t2.orderId and t1.name < t2.name group by t1.name, t2.name order by count(*) desc limit 10;");
            while (r.next()) {
                pairs += r.getString("i1") + " & " + r.getString("i2") + ": " + r.getInt("count") + " occurrences\n";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
		JTextArea contents2 = new JTextArea(pairs);
		contents2.setEditable(false);
		managerViewSummary.add(contents2);
		mainPanel.add(managerViewSummary);
	}

	/**
	 * creates the Inventory panel
	 */
	public void loadManagerViewInventory() {
		// create a panel
		managerViewInventory = new JPanel();
		loadManagerHeader(managerViewInventory);

		// create a vertical panel
		JPanel verticalPanel = new JPanel(new BorderLayout());

		// using the inventory class
		Inventory inventory = new Inventory(db);
		JTextField text = new JTextField(30);
		managerViewInventory.add(inventory.mainInventoryPanel(verticalPanel));


		mainPanel.add(managerViewInventory);
	}


    //Retrieves sales within date window

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
				e.printStackTrace();
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			}
		
		return prevOrders;
	}

	//Excess sales report

	public String[] excessSales(String date) {
		//NOTE for when you're coding: current date is stored in currentDate
		String[] temp = new String[100];
		Double[] quantity_holder = new Double[100];
		int j = 0; //used for iterating through temp array
		int i = 0; //used for iterating through the quantity array
		// Get the stock at the given date
		try {
			ResultSet size = db.sendCommand("SELECT COUNT(inventory) FROM inventory WHERE date='" + date + "'");
			Double[] temp_quantity = new Double[size.getInt("count")];
			try {
				ResultSet data = db.sendCommand("SELECT * FROM inventory WHERE date='" + date + "'");
				while (data.next()) {
					temp_quantity[i] = (data.getDouble("quantity"));
					quantity_holder[i] = temp_quantity[i];
					i += 1;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		Double[] quantity = new Double[i];
		for (int k = 0; k < i; k += 1) {
			quantity[k] = quantity_holder[k];
		}
		//get the current stock
		try {
			ResultSet size2 = db.sendCommand("SELECT COUNT(item) FROM item");
			Double[] curr_quantity = new Double[size2.getInt("count")];
			try {
				ResultSet data2 = db.sendCommand("SELECT * FROM item ORDER BY id ASC");
				int l = 0;
				while (data2.next()) {
					curr_quantity[l] = (data2.getDouble("quantity"));
					l += 1;
					// This is where the less than 10% check happens
					if (curr_quantity[l] > (quantity[l] * 0.9)) {
						temp[j] = data2.getString("name");
						j += 1;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		String[] final_result = new String[j];
		for (int k = 0; k < j; k += 1) {
			final_result[k] = temp[k];
		}
		return final_result;
	}
	//Restock Report 
	///////////////////////////////////////
	public String[] restockitems(String restock){

		int min_restock=2;
		try {
			ResultSet size = db.sendCommand("SELECT COUNT(item) FROM item");
			Double[] curr_quantity = new Double[size.getInt("count")];
			try {
				ResultSet order_data = db.sendCommand("SELECT * FROM item ORDER BY id ASC");
				int l = 0;
				while (order_data.next()) {
					curr_quantity[l] = (order_data.getDouble("minquantity"));
					l += 1;
					if (curr_quantity[l] > min_restock) {
						temp[j] = order_data.getString("name");
						j += 1;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		String[] final_result = new String[j];
		for (int k = 0; k < j; k += 1) {
			final_result[k] = temp[k];
		}
		return final_result;

	}

	////////////////////////////////////////

	/**
	 * creates the Orders panal
	 */
	public void loadManagerViewOrders(String lowDate, String highDate) {
		managerViewOrders = new JPanel();
		loadManagerHeader(managerViewOrders);
		JLabel title = new JLabel("Orders");
		managerViewOrders.add(title);
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
				managerViewOrders.removeAll();
				managerViewOrders.validate();
				managerViewOrders.revalidate();
				loadManagerViewOrders(lowerDate,upperDate);
				
			}
		});
		contents.setEditable(false);
		JScrollPane pane = new JScrollPane(contents);
		pane.setBounds(10, 11, getWidth()+5, getHeight());
		pane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		managerViewOrders.add(dateUpdate);
		managerViewOrders.add(text);
		managerViewOrders.add(pane);
		mainPanel.add(managerViewOrders);
	}

	/**
	 * sets all panel to invisible
	 */
	public void hideAllPanels() {
		managerViewSummary.setVisible(false);
		managerViewInventory.setVisible(false);
		managerViewOrders.setVisible(false);
		serverView.setVisible(false);
	}

	public void switchToServerView() {
		hideAllPanels();
		serverView.setVisible(true);
	}

	/**
	 * hides all panel first
	 * switch panel to Summary
	 */
	public void switchToManagerViewSummary() {
		System.out.println("Switching to manager summary");
		hideAllPanels();
		loadManagerViewSummary();
		managerViewSummary.setVisible(true);
	}

	/**
	 * hides all panel first
	 * switch panel to Orders
	 */
	public void switchToManagerViewOrders() {
		System.out.println("Switching to manager orders");
		hideAllPanels();
		loadManagerViewOrders(lowerDate,upperDate);
		managerViewOrders.setVisible(true);
	}

	/**
	 * hides all panel first
	 * switch panel to Inventory
	 */
	public void switchToManagerViewInventory() {
		System.out.println("Switching to manager inventory");
		hideAllPanels();
		loadManagerViewInventory();
		managerViewInventory.setVisible(true);

		// retrive items from DB
		String results = "";
		try {
			//send statement to DBMS
			ResultSet result = db.sendCommand("SELECT * FROM item");
			while (result.next()) {
				results += result.getString("name") + " " + result.getString("quantity") + " " +
						result.getString("units") + "\n";
			}
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Error accessing Database.");
		}
	}

}
