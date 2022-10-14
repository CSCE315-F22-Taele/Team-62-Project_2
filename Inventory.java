import java.sql.*;
import java.awt.event.*;
import java.awt.BorderLayout;
import javax.imageio.plugins.jpeg.JPEGHuffmanTable;
import javax.swing.*;
import javax.swing.border.Border;

import java.awt.GridLayout;

public class Inventory {

	JPanel itemPanel;
	JPanel invetoryPanel;
	JPanel contentPanel;
	JPanel mainPanel;
	JPanel mainPanel2;
	JPanel verticalView;
	JFrame mainFrame;

	dbConnection db;

	public Inventory(dbConnection database) {
		db = database;
		mainFrame = new JFrame("Inventory GUI");
		invetoryPanel = new JPanel();
		contentPanel = new JPanel();
	}


	public JPanel mainInventoryPanel(JPanel verticalPanel) {

		mainPanel = verticalPanel;
		invetoryPanel = new JPanel();
		contentPanel = new JPanel();

		items();

		// reordering the panels
		int mainWidth = 2000;
		int mainHeight = 1000;
		int mainX = 450;
		int mainY = 0;
		invetoryPanel.setBounds(mainX, mainY, mainWidth, 50);
		contentPanel.setBounds(mainX, 150, mainWidth, mainHeight);
		JTextField text = new JTextField(10);
		JButton inventoryUpdate = new JButton("Update Inventory");
		inventoryUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String update = text.getText();
				String[] input = update.split(" ");
				String name = "";
				for(int i = 0; i<input.length-1;i++){
					if(i == input.length-2){
						name+=input[i];
					}
					else{
						name+=(input[i]+" ");
					}
				}
				String q = input[input.length-1];
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
		JButton PriceUpdate = new JButton("Update Price");
		PriceUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String updatePrice = textPrices.getText();
				String[] input = updatePrice.split(" ");
				String Pname = "";
				String newPrice = "";
				for(int i = 0; i<input.length-1;i++){
					if(i == input.length-2){
						Pname+=input[i];
					}
					else{
						Pname+=(input[i]+" ");
					}
				}
				newPrice = input[input.length-1];
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

		return mainPanel2;
	}

    /*public JPanel mainpriceupdatepanel(JPanel verticalPanel){



    }*/


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
