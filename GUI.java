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
      int id = 0;
      String date = "";
      int ordersToday = 0;
      int salesToday = 0;
      int ordersWeek = 0;
      int salesWeek = 0;
      try {
            ResultSet r = db.sendCommand("SELECT MAX(id) FROM orders");
            r.next();
            id = r.getInt("max");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        try {
            ResultSet r = db.sendCommand("SELECT date FROM orders WHERE id = "+id);
            r.next();
            date = r.getString("date");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        try {
            ResultSet r = db.sendCommand("SELECT total FROM orders WHERE date = '"+date+"'");
            while (r.next()) {
              ordersToday++;
              salesToday += r.getInt("total");
        }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }

      // create a new frame
      //System.out.println(ordersToday + " " + salesToday);
      String today = "                 TODAY                  \n Revenue: "+ salesToday+"       Orders: "+ordersToday+"\n";
      for(int i = 0; i<7;i++){
        try {
            ResultSet r = db.sendCommand("SELECT total FROM orders WHERE date = '"+date+"'");
            while (r.next()) {
              ordersWeek++;
              salesWeek += r.getInt("total");
        }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        try {
            ResultSet r = db.sendCommand("SELECT date FROM orders WHERE date = (date '"+date+"' - integer '1')");
            r.next();
              date = r.getString("date");
        }
         catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
      }
      String week = "                 Week                  \n Revenue: "+ salesWeek+"       Orders: "+ordersWeek + "       Avg. Order: "+ (double)salesWeek/ordersWeek;
        JTextArea contents = new JTextArea(today+week);
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
        String name = "";
      try{
        //send statement to DBMS
        ResultSet result = db.sendCommand("SELECT * FROM item");
        while (result.next()) {
          name += result.getString("name")+ "    " + result.getString("quantity")+" lbs\n";
        }
      } catch (Exception e){
          e.printStackTrace();
          JOptionPane.showMessageDialog(null,"Error accessing Database.");
      }
        JTextArea contents = new JTextArea(name);
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
        int id = 0;
        double price = 0;
        String date = "";
        String prevOrders = "";
      
      try {
            ResultSet r = db.sendCommand("SELECT MAX(id) FROM orders");
            r.next();
            id = r.getInt("max");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        try {
          for(int i = 0; i<20;i++){
            ResultSet r = db.sendCommand("SELECT * FROM orders WHERE id = "+id);
            if(r.next() == false){
              break;
            }
            price = r.getDouble("total");
            date = r.getString("date");
            prevOrders+=price + "       " + date +"\n";
            id--;
            
          }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        

      // create a new frame
      //System.out.println(ordersToday + " " + salesToday);
        JTextArea contents = new JTextArea(prevOrders);
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
