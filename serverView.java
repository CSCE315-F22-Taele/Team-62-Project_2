import java.sql.*;
import java.awt.event.*;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.*;
import java.util.HashMap;


public class serverView {
    private JPanel mainPanel;
    private JPanel productPanel;
    private JPanel itemPanel;
    private JPanel receiptPanel;
    private JPanel infoPanel;
    private HashMap<JButton, JPanel> productPanels = new HashMap<>(); // Maps instantiated product buttons to their editing panels
    private HashMap<JButton, Product> productDataMap = new HashMap<>(); // Maps instantiated product buttons to their product data
    private HashMap<Integer, Item> itemMap; // Maps item IDs to info

    private ProductDef[] productDefs = testGeneration.productDefs;

    private GUI gui;

    private void loadProductPanels(){
        for(ProductDef p : productDefs){
            JButton b = new JButton(p.name);
            b.addActionListener(e -> addNewProduct(p.id));
            productPanel.add(b);
        }
    }

    public serverView(JPanel p, GUI g, HashMap<Integer, Item> i){
        itemMap = i;
        mainPanel = p;
        gui = g;
        productPanel = new JPanel(new GridLayout(0,1));
        itemPanel = new JPanel();
        receiptPanel = new JPanel(new GridLayout(0,1));
        infoPanel = new JPanel();
        infoPanel.add(new JLabel("Server View"));
        productPanel.add(new JLabel("Products"));
        receiptPanel.add(new JLabel("Receipts"));
        JButton switchButton = new JButton("Manager View");
        switchButton.addActionListener(e -> gui.switchToManagerView());
        infoPanel.add(switchButton);

        mainPanel.add(infoPanel, BorderLayout.PAGE_START);
        mainPanel.add(productPanel, BorderLayout.LINE_START);
        mainPanel.add(receiptPanel, BorderLayout.LINE_END);
        mainPanel.add(itemPanel, BorderLayout.PAGE_END);

        loadProductPanels();

    }
    public void setVisible(boolean v){
        mainPanel.setVisible(v);
    }
    public void addNewProduct(int id){
        // Create and show a new product panel
        ProductDef p = productDefs[id];
        JPanel productItemPanel = new JPanel(new GridLayout(5,5));
        Product productData = new Product(p.name, p.price);
        // Create a button and add it to the receipt panel.
        JButton productButton = new JButton(p.name + " - " + p.price);
        receiptPanel.add(productButton);

        for(int i=0;i<p.optionalItems.length;i++){
            int itemId = p.optionalItems[i];
            double portion = p.optionalItemPortions[i];
            Item item = itemMap.get(itemId);
            JToggleButton itemButton = new JToggleButton(item.name + ": " + portion + item.units);
            itemButton.addActionListener(e -> productData.toggleItem(itemId, portion, itemButton.isSelected()));
            productItemPanel.add(itemButton);
        }
        JToggleButton deleteButton = new JToggleButton("Remove Product");
        productItemPanel.add(deleteButton);
        itemPanel.add(productItemPanel);

        // Map the button to the panel in the hashmap, then add an event listener accordingly
        productPanels.put(productButton, productItemPanel);
        productDataMap.put(productButton, productData);
        productButton.addActionListener(e -> switchToProduct(productPanels.get(productButton)));
        switchToProduct(productItemPanel);

        // Delete product button should remove the product
        deleteButton.addActionListener(e -> removeProduct(productButton));
    }
    public void switchToProduct(JPanel p){
        // Hide every panel and show the one we want
        productPanels.forEach((i, panel) -> panel.setVisible(false));
        p.setVisible(true);
    }
    public void removeProduct(JButton b){
        JPanel p = productPanels.get(b);
        productDataMap.remove(b);
        productPanels.remove(b);
        GUI.deleteComponent(itemPanel, p);
        GUI.deleteComponent(receiptPanel, b);
    }
}
