import java.sql.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.BorderLayout;

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
		int lowID = 0;
		int highID = 0;
		try {
			ResultSet r = db.sendCommand("SELECT id FROM item WHERE id = (SELECT MIN (id) FROM item)");
			if(r.next()){
				lowID = r.getInt("id");
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
			ResultSet r = db.sendCommand("SELECT id FROM item WHERE id = (SELECT MAX (id) FROM item)");
			if(r.next()){
				highID = r.getInt("id");
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
			double firstQuantity = 0;
			double secondQuantity = 0;
			double totalQuantity = 0;
			double minQuantity = 0;
			try {
			ResultSet r = db.sendCommand("SELECT * FROM item WHERE id = " + i);
			if(r.next()){
				name = r.getString("name");
				minQuantity = r.getDouble("minquantity");
			}
			else{
				return prevOrders;
			}
			}catch (Exception e) {
			e.printStackTrace();
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
			
			try {
			ResultSet r = db.sendCommand("SELECT quantity FROM inventory WHERE (itemid = " + i + " AND date >= '" + lowDate+ "' AND date <= '"+highDate+"')");
			while(r.next()){
				if(firstQuantity == 0){
					firstQuantity = r.getDouble("quantity");
				}
				else if(secondQuantity == 0){
					secondQuantity = r.getDouble("quantity");
					if(secondQuantity > firstQuantity){
						totalQuantity += ((firstQuantity - minQuantity) + (minQuantity*5 - secondQuantity));
						firstQuantity = secondQuantity;
						secondQuantity = 0;
					}
					else{
						totalQuantity += (firstQuantity - secondQuantity);
						firstQuantity = secondQuantity;
						secondQuantity = 0;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
			prevOrders += name + ":       " + totalQuantity+" units sold\n";
		}
		return prevOrders;
	}
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
