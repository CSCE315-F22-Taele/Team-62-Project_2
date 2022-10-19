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

public class Inventory {

	JPanel itemPanel;
	JPanel invetoryPanel;
	JPanel contentPanel;
	JPanel mainPanel;
	JPanel mainPanel2;
	JPanel mainPanel3;
	JPanel verticalView;
	JFrame mainFrame;
	JButton inventoryUpdate = new JButton("Update Inventory");
	JButton PriceUpdate = new JButton("Update Price");
	JPanel seasonalItemPanel;
	Color customPurple = new Color(65, 30, 122);
	Border border = new LineBorder(customPurple, 2);
	dbConnection db;

	/**
     * This is the constructor for the inventory class
     *
     * @author JP Pham
     * @param database  Connection to the database
     */
	public Inventory(dbConnection database) {
		db = database;
		mainFrame = new JFrame("Inventory GUI");
		invetoryPanel = new JPanel();
		contentPanel = new JPanel();
		seasonalItemPanel = new JPanel();

		seasonalMenuItems();
	}
	/**
     * This is the constructor for the inventory calss
     *
     * @author JP Pham
     * @param database  Connection to the database
	 * @param btnBackgroundColor  Background Color
	 * @param btnForColor  Foreground Color
     */
	public Inventory(dbConnection database, Color btnBackgroundColor, Color btnForeColor) {
		db = database;
		mainFrame = new JFrame("Inventory GUI");
		invetoryPanel = new JPanel();
		contentPanel = new JPanel();

		seasonalItemPanel = new JPanel();
		seasonalMenuItems();

		invetoryPanel.setBackground(Color.white);
		contentPanel.setBackground(Color.white);
		contentPanel.setBorder(border);

		// setting button colors
		inventoryUpdate.setBackground(btnBackgroundColor);
		inventoryUpdate.setForeground(btnForeColor);
		PriceUpdate.setBackground(btnBackgroundColor);
		PriceUpdate.setForeground(btnForeColor);
	}

	/**
     * This creates the main panel for inventory that outputs items and products
     *
     * @author Connor Callan
     * @param verticalPanel  Connection to the database
	 * @return A jPanel for the inventory results
     */
	public JPanel mainInventoryPanel(JPanel verticalPanel) {

		mainPanel = verticalPanel;

		items();

		// reordering the panels
		int mainWidth = 2000;
		int mainHeight = 1000;
		int mainX = 450;
		int mainY = 0;
		invetoryPanel.setBounds(mainX, mainY, mainWidth, 50);
		contentPanel.setBounds(mainX, 150, mainWidth, mainHeight);
		JTextField text = new JTextField(10);
		inventoryUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String update = text.getText();
				String[] input = update.split(" ");
				String name = "";
				//Retrieve item name and new quantity
				for(int i = 0; i<input.length-1;i++){
					if(i == input.length-2){
						name+=input[i];
					}
					else{
						name+=(input[i]+" ");
					}
				}
				String q = input[input.length-1];
				//Update the item with it's new quantity
				try {
					db.sendUpdate("UPDATE item SET quantity = " + q + " WHERE name = '" + name + "'");
				} catch (Exception error) {
					error.printStackTrace();
					System.err.println(error.getClass().getName() + ": " + error.getMessage());
					System.exit(0);
				}
				contentPanel.removeAll();
				String itemList = retrivingDBItems();
				String itemList2 = retrivingDBItems2();
				JTextArea content = new JTextArea("Item Inventory:\n " + itemList + "\nProduct Prices:\n" +itemList2);
				content.setEditable(false);
				contentPanel.add(content);
				contentPanel.validate();
				contentPanel.revalidate();
			}
		});
        JTextField textPrices = new JTextField(10);
		PriceUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String updatePrice = textPrices.getText();
				String[] input = updatePrice.split(" ");
				String Pname = "";
				String newPrice = "";
				//Retrieve item name and new price
				for(int i = 0; i<input.length-1;i++){
					if(i == input.length-2){
						Pname+=input[i];
					}
					else{
						Pname+=(input[i]+" ");
					}
				}
				newPrice = input[input.length-1];
				//Update the item's price
				try {
					db.sendUpdate("UPDATE productDef SET price = " + newPrice + " WHERE name = '" + Pname + "'");
				} catch (Exception error) {
					error.printStackTrace();
					System.err.println(error.getClass().getName() + ": " + error.getMessage());
					System.exit(0);
				}
				//contentPanel.removeAll();
				contentPanel.removeAll();
				String itemList = retrivingDBItems();
				String itemList2 = retrivingDBItems2();
				JTextArea content2 = new JTextArea("Item Inventory:\n " + itemList + "\nProduct Prices:\n" +itemList2);
				content2.setEditable(false);
				contentPanel.add(content2);
				contentPanel.validate();
				contentPanel.revalidate();
			}
		});

		// vertical layout of inventory section
		mainPanel.add(text, BorderLayout.PAGE_START);
		mainPanel.add(inventoryUpdate, BorderLayout.CENTER);
		mainPanel.add(invetoryPanel, BorderLayout.LINE_END);
		mainPanel.add(contentPanel, BorderLayout.PAGE_END);

		mainPanel2 = new JPanel(new BorderLayout());
		mainPanel2.add(textPrices, BorderLayout.LINE_START);
		mainPanel2.add(PriceUpdate, BorderLayout.CENTER);
		mainPanel2.add(mainPanel, BorderLayout.SOUTH);

		mainPanel3 = new JPanel(new BorderLayout());
		mainPanel3.add(mainPanel2, BorderLayout.EAST);
		mainPanel3.add(seasonalItemPanel, BorderLayout.NORTH);

		return mainPanel3;
	}

    /*public JPanel mainpriceupdatepanel(JPanel verticalPanel){



    }*/

	//Creates the item panel and generates the content needed
	/**
     * This function creates the initiaal values for the items panel
     *
     * @author Connor Callan
     */
	public void items() {
		itemPanel = new JPanel();
		JLabel title = new JLabel("Inventory");
		itemPanel.add(title);

		String itemList = retrivingDBItems();
		String itemList2 = retrivingDBItems2();
		JTextArea content = new JTextArea("Item Inventory:\n " + itemList + "\nProduct Prices:\n" +itemList2);
		content.setEditable(false);
		contentPanel.add(content);
		invetoryPanel.add(itemPanel);
	}

	//Retrieves every item from the database
	/**
     * This allows you to add a new item to the database.
     *
     * @author JP Pham
     */
	private void seasonalMenuItems() {

		JButton seasonItemBtn = new JButton("Seasonal Menu Items");
		seasonItemBtn.setBackground(customPurple);
		seasonItemBtn.setForeground(Color.white);
		seasonItemBtn.setOpaque(true);
		JTextField textSeasonItem = new JTextField(10);
		seasonItemBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String newItem = textSeasonItem.getText();
				db.addItemToDatabase(30, 34.34, "Kg", newItem, 2);
			}
		});

		seasonalItemPanel.add(seasonItemBtn);
		seasonalItemPanel.add(textSeasonItem);
	}
	/**
     * Retrieves the items from the database
     *
     * @author Connor Callan
	 * @return A string with all items in the database and their quantities
     */
	public String retrivingDBItems() {
		String name = "";
		try {
			//send statement to DBMS
			ResultSet result = db.sendCommand("SELECT * FROM item");
			while (result.next()) {
				name += result.getString("name") + "    " + result.getString("quantity") + " units\n";
			}
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Error accessing Database.");
		}
		return name;
	}
	//Retrieves every product definition from the database
	/**
     * Retrieves all products from the database
     *
     * @author Connor Callan
	 * @return A string with the products and their prices.
     */
	public String retrivingDBItems2() {
		String name = "";
		try {
			//send statement to DBMS
			ResultSet result = db.sendCommand("SELECT * FROM productDef");
			while (result.next()) {
				name += result.getString("name") + "    " + result.getString("price") + "$\n";
			}
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Error accessing Database.");
		}
		return name;
	}
}
