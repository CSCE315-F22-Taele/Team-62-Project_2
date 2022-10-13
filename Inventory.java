import java.sql.*;
import java.awt.event.*;

import javax.imageio.plugins.jpeg.JPEGHuffmanTable;
import javax.swing.*;

public class Inventory {
    
    JPanel itemPanel;
    JPanel invetoryPanel;
    JFrame mainFrame;
    
    dbConnection db;

    public Inventory(dbConnection database) {
        db = database;
        // create a new frame
        mainFrame = new JFrame("Inventory GUI");
        invetoryPanel = new JPanel();
        
        items();

        mainFrame.add(invetoryPanel);
        mainFrame.setSize(768, 1024);
        mainFrame.show();
    }


    public void items() {
        itemPanel = new JPanel();
        JLabel title = new JLabel("Inventory");
        itemPanel.add(title);
        invetoryPanel.add(itemPanel);

    }

    public void retrivingDBItems() {
        String results = "";
        try {
          //send statement to DBMS
          ResultSet result = db.sendCommand("SELECT * FROM item");
            while (result.next()) {
                results += result.getString("name") + " " +  result.getString("quantity") + " " + 
                result.getString("units") + "\n";
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,"Error accessing Database.");
        }
    }


}
