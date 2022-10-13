import java.sql.*;
import java.awt.event.*;
import javax.swing.*;

/*
  TODO:
  1) Change credentials for your own team's database
  2) Change SQL command to a relevant query that retrieves a small amount of data
  3) Create a JTextArea object using the queried data
  4) Add the new object to the JPanel p
*/

public class GUI extends JFrame {
    JFrame mainFrame;
    JPanel mainPanel;
    JPanel serverView;
    JPanel managerViewSummary;
    JPanel managerViewInventory;
    JPanel managerViewOrders;
    dbConnection db;
    
    public GUI(dbConnection database) {
        db = database;
        
        // create a new frame
        mainFrame = new JFrame("DB GUI");
        mainPanel = new JPanel();
        loadManagerViewOrders();
        loadManagerViewSummary();
        loadManagerViewInventory();
        
        // set the size of frame
        mainFrame.add(mainPanel);
        mainFrame.setSize(768, 1024);
        mainFrame.show();
        switchToManagerViewInventory();
    }

    /**
     * add in the buttons: summary, Inventory, Orders
     * @param p
     */
    public void loadManagerHeader(JPanel p){
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
    public void loadManagerViewSummary(){
        managerViewSummary = new JPanel();
        loadManagerHeader(managerViewSummary);
        JLabel title = new JLabel("Summary");
        managerViewSummary.add(title);
        JTextArea contents = new JTextArea("Contents Go Here");
        contents.setEditable(false);
        managerViewSummary.add(contents);
        mainPanel.add(managerViewSummary);
    }

    /**
     * creates the Inventory panel
     */
    public void loadManagerViewInventory(){
        // create a panel
        managerViewInventory = new JPanel();
        loadManagerHeader(managerViewInventory);
        JLabel title = new JLabel("Inventory");
        managerViewInventory.add(title);
        JTextArea contents = new JTextArea("Contents Go Here");
        contents.setEditable(false);
        managerViewInventory.add(contents);
        mainPanel.add(managerViewInventory);
    }

    /**
     * creates the Orders panal
     */
    public void loadManagerViewOrders(){
        managerViewOrders = new JPanel();
        loadManagerHeader(managerViewOrders);
        JLabel title = new JLabel("Orders");
        managerViewOrders.add(title);
        JTextArea contents = new JTextArea("Contents Go Here");
        contents.setEditable(false);
        managerViewOrders.add(contents);
        mainPanel.add(managerViewOrders);
    }

    /**
     * sets all panel to invisible
     */
    public void hideAllPanels(){
        managerViewSummary.setVisible(false);
        managerViewInventory.setVisible(false);
        managerViewOrders.setVisible(false);
    }

    /**
     * hides all panel first
     * switch panel to Summary
     */
    public void switchToManagerViewSummary(){
        System.out.println("Switching to manager summary");
        hideAllPanels();
        managerViewSummary.setVisible(true);
    }

    /**
     * hides all panel first
     * switch panel to Orders
     */
    public void switchToManagerViewOrders(){
        System.out.println("Switching to manager orders");
        hideAllPanels();
        managerViewOrders.setVisible(true);
    }

    /**
     * hides all panel first
     * switch panel to Inventory
     */
    public void switchToManagerViewInventory(){
        System.out.println("Switching to manager inventory");
        hideAllPanels();
        managerViewInventory.setVisible(true);


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

    // if button is pressed
    public void testEvent(ActionEvent e)
    {
        System.out.println("Test");
    }
}
