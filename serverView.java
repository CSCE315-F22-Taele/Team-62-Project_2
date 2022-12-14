import java.sql.*;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.*;
import java.util.HashMap;
import java.awt.*;

/**
* The serverView class manages the UI and logic behind the server UI, which
* allows the user to create and submit orders.
*/
public class serverView {
    private JPanel mainPanel;
    private JPanel productPanel;
    private JPanel itemPanel;
    private JPanel receiptPanel;
    private JPanel infoPanel;
    private JLabel subtotal;
    private JLabel total;
    private JSpinner discount;

    private HashMap<JButton, JPanel> productPanels; // Maps instantiated product buttons to their editing panels
    private HashMap<JButton, Product> productDataMap = new HashMap<>(); // Maps instantiated product buttons to their product data
    private HashMap<Integer, Item> itemMap; // Maps item IDs to info

    private dbConnection db;
    private ProductDef[] productDefs;

    Color customPink = new Color(176, 48, 147);
    Color customGrey = new Color(104, 119, 108);
    Color customPurple = new Color(65, 30, 122);
    Color customWhite = new Color(255, 255, 255);
    Color maroon = new Color(141, 6, 6);

    private void loadProductPanels(){
        for(ProductDef p : productDefs){
            JButton b = new JButton(p.name);

            b.setBackground(customPink);
            b.setForeground(customWhite);
            b.setMargin(new Insets(10, 10, 10, 10));
            b.setOpaque(true);
            b.setBorderPainted(false);

            b.addActionListener(e -> addNewProduct(p.id));
            productPanel.add(b);
        }
    }

    /**
    * Create a new serverView inside of panel p.
    * @param p  The JPanel in which to initialize the ServerView.
    * @param database  A database connection to use.
    */
    public serverView(JPanel p, dbConnection database) {
        mainPanel = p;
        mainPanel.setBackground(customWhite);
        mainPanel.setSize(1500, 750);
        db = database;
        init();
    }

    /*
    * Initializes the server view with the latest values.
    */
    public void init(){
        productPanels = new HashMap<>();
        productDataMap = new HashMap<>();
        mainPanel.removeAll();
        PomAndHoneyGUI.refresh(mainPanel);
        itemMap = db.getItemHashmap();
        productDefs = db.getProductDefs();
        productPanel = new JPanel(new GridLayout(0,1));
        itemPanel = new JPanel();
        receiptPanel = new JPanel(new GridLayout(0,1));
        infoPanel = new JPanel();
        infoPanel.add(new JLabel("Server View"));
        productPanel.add(new JLabel("Products"));
        receiptPanel.add(new JLabel("Receipts"));
        subtotal = new JLabel("Subtotal: $0.00");
        total = new JLabel("Total: $0.00");
        receiptPanel.add(subtotal);
        receiptPanel.add(total);
        SpinnerModel model = new SpinnerNumberModel(0, 0, 100, 5);
        discount = new JSpinner(model);
        receiptPanel.add(new JLabel("Discount %:"));
        receiptPanel.add(discount);

        JButton finalizeButton = new JButton("Finalize Order");
        finalizeButton.setOpaque(true);
        finalizeButton.setBorderPainted(false);
        finalizeButton.addActionListener(e -> finalizeOrder());
        finalizeButton.setBackground(customPurple);
        finalizeButton.setForeground(customWhite);
        finalizeButton.setMargin(new Insets(20, 20, 20, 20));

        mainPanel.add(infoPanel, BorderLayout.PAGE_START);
        mainPanel.add(productPanel, BorderLayout.LINE_START);
        mainPanel.add(receiptPanel, BorderLayout.LINE_END);
        mainPanel.add(itemPanel, BorderLayout.CENTER);
        itemPanel.setSize(800,800);
        mainPanel.add(finalizeButton, BorderLayout.PAGE_END);

        loadProductPanels();
    }

    /**
    * Show or hide the server view.
    */
    public void setVisible(boolean v){
        mainPanel.setVisible(v);
    }

    private void addNewProduct(int id){
        // Create and show a new product panel
        ProductDef p = productDefs[0];
        for(int i=0;i<productDefs.length;i++){
            if(productDefs[i].id == id){
                p = productDefs[i];
                break;
            }
        }
        JPanel productItemPanel = new JPanel(new GridLayout(0,3));
        Product productData = new Product(p.name, p.price);

        // Add base items to product.
        for(int i=0;i<p.baseItems.length;i++){
            productData.addItem(p.baseItems[i], p.baseItemPortions[i]);
        }

        // Create a button and add it to the receipt panel.
        JButton productButton = new JButton(p.name + " - " + p.price);

        productButton.setBackground(customPurple);
        productButton.setForeground(customWhite);

        receiptPanel.add(productButton);

        for(int i=0;i<p.optionalItems.length;i++){
            int itemId = p.optionalItems[i];
            double portion = p.optionalItemPortions[i];
            Item item = itemMap.get(itemId);
            JToggleButton itemButton = new JToggleButton(item.name + ": " + portion + item.units);

            // setting the item button color and margin
            itemButton.setBackground(customGrey);
            itemButton.setForeground(customWhite);
            itemButton.setMargin(new Insets(20, 20, 20, 20));

        itemButton.addActionListener(e -> productData.toggleItem(itemId, portion, itemButton.isSelected()));
            productItemPanel.add(itemButton);
        }
        JToggleButton deleteButton = new JToggleButton("Remove Product");
        productItemPanel.add(deleteButton);

        deleteButton.setBackground(maroon);
        deleteButton.setForeground(customWhite);
        deleteButton.setMargin(new Insets(20, 20, 20, 20));

        itemPanel.add(productItemPanel);

        // Map the button to the panel in the hashmap, then add an event listener accordingly
        productPanels.put(productButton, productItemPanel);
        productDataMap.put(productButton, productData);
        productButton.addActionListener(e -> switchToProduct(productPanels.get(productButton)));
        switchToProduct(productItemPanel);

        // Delete product button should remove the product
        deleteButton.addActionListener(e -> removeProduct(productButton));
        updatePrices();
    }

    private void switchToProduct(JPanel p){
        // Hide every panel and show the one we want
        productPanels.forEach((i, panel) -> panel.setVisible(false));
        p.setVisible(true);
    }

    private void removeProduct(JButton b){
        JPanel p = productPanels.get(b);
        productDataMap.remove(b);
        productPanels.remove(b);
        PomAndHoneyGUI.deleteComponent(itemPanel, p);
        PomAndHoneyGUI.deleteComponent(receiptPanel, b);
        updatePrices();
    }

    private void updatePrices(){
        int price = 0;
        for(Product p : productDataMap.values()){
            price += p.price;
        }
        subtotal.setText("Subtotal: $" + price);
        total.setText("Total: $" + (price * 1.0825));
        PomAndHoneyGUI.refresh(receiptPanel);
    }

    private void finalizeOrder(){
        int price = 0;
        int[] productList = new int[productDataMap.size()];
        int i = 0;
         int orderid = 0;
         String currentDate = "";
	    try {
			ResultSet r = db.sendCommand("SELECT CAST( (SELECT CURRENT_TIMESTAMP) AS Date )");
			r.next();
			currentDate = r.getString("current_timestamp");
		} catch (Exception e) {
			Logger.log(e);
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}
        try {
            ResultSet r = db.sendCommand("SELECT MAX(id) FROM orders");
            r.next();
            orderid = r.getInt("max") + 1;
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        for(Product p : productDataMap.values()){
            price += p.price;
            productList[i] = p.addToDatabase(db,currentDate,orderid);
            i += 1;
        }
        db.addOrderToDatabase(productList, (double)((Integer) discount.getValue() / 100.0), price, currentDate);
        init();
    }
}
