import java.sql.*;
import java.awt.event.*;

import javax.imageio.plugins.jpeg.JPEGHuffmanTable;
import javax.swing.*;

public class Inventory {
    
    JPanel itemPanel;
    JPanel invetoryPanel;
    JPanel contentPanel;
    JPanel mainPanel;
    JFrame mainFrame;
    
    dbConnection db;

    public Inventory(dbConnection database) {
        db = database;
        mainFrame = new JFrame("Inventory GUI");
        invetoryPanel = new JPanel();
        contentPanel = new JPanel();
    }

    public JPanel mainInventoryPanel() {
        mainPanel = new JPanel();
        invetoryPanel = new JPanel();
        contentPanel = new JPanel();
        
        items();

        // reordering the panels
        int mainWidth = 1300;
        int mainHeight = 700;
        int mainX = 500;
        int mainY = 0;
        invetoryPanel.setBounds(mainX, mainY, mainWidth, 50);
        contentPanel.setBounds(mainX, 150, mainWidth, mainHeight);
        JTextField text = new JTextField(10);
        JButton inventoryUpdate = new JButton("Update");
      inventoryUpdate.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {     
            String update = text.getText();
            String[] input = update.split(" ");
            try {
                db.sendUpdate("UPDATE item SET quantity = " + input[1] + " WHERE name = '" + input[0]+"'");
            } catch (Exception error) {
                error.printStackTrace();
                System.err.println(error.getClass().getName() + ": " + error.getMessage());
                System.exit(0);
            }
            contentPanel.removeAll();
            String itemList = retrivingDBItems();
            JTextArea content = new JTextArea(itemList);
		    content.setEditable(false);
            contentPanel.add(content);
            //mainPanel.add(contentPanel);
            
            

         }
      }); 
        mainPanel.add(text);
        mainPanel.add(inventoryUpdate);
        mainPanel.add(invetoryPanel);
        mainPanel.add(contentPanel);
        
        return mainPanel;
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
				name += result.getString("name") + "    " + result.getString("quantity") + " units\n";
			}
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Error accessing Database.");
		}
        return name;
    }
}
