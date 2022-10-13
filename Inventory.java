import java.sql.*;
import java.awt.event.*;

import javax.imageio.plugins.jpeg.JPEGHuffmanTable;
import javax.swing.*;

public class Inventory {
    
    JPanel itemPanel;
    JPanel invetoryPanel;
    JPanel contentPanel;
    JFrame mainFrame;
    
    dbConnection db;

    public Inventory(dbConnection database) {
        db = database;
        // create a new frame
        mainFrame = new JFrame("Inventory GUI");
        invetoryPanel = new JPanel();
        contentPanel = new JPanel();
        
        items();

        // reordering the panels
        int mainWidth = 700;
        int mainHeight = 700;
        int mainX = 500;
        int mainY = 0;
        invetoryPanel.setBounds(mainX, mainY, mainWidth, 50);
        contentPanel.setBounds(mainX, 50, mainWidth, mainHeight);
        
        mainFrame.add(invetoryPanel);
        mainFrame.add(contentPanel);
        
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(1500, 1000);
        mainFrame.setLayout(null);
        mainFrame.show();
    }


    public void items() {
        itemPanel = new JPanel();
        JLabel title = new JLabel("Inventory");
        itemPanel.add(title);
        
        String itemList = retrivingDBItems();
        JTextArea content = new JTextArea(itemList);
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
				name += result.getString("name") + "    " + result.getString("quantity") + " lbs\n";
			}
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Error accessing Database.");
		}
        return name;
    }


}
