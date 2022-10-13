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
    private HashMap<Integer, Item> itemMap; // Maps item IDs to info

    private ProductDef[] testProducts = {
        new ProductDef(0, "Gyro", 7.49, new int[]{0,1,2}, new double[]{0.5, 1.0, 1.5}, new int[]{3,4,5}, new double[]{0.3, 0.4, 0.5}),
        new ProductDef(1, "Bowl", 7.59, new int[]{0,1,2}, new double[]{0.5, 1.0, 1.5}, new int[]{6,7,8}, new double[]{0.35, 0.45, 0.55}),
        new ProductDef(2, "Drink", 2.09, new int[]{0,1,2}, new double[]{0.5, 1.0, 1.5}, new int[]{}, new double[]{}),
    };

    private GUI gui;

    private void loadProductPanels(){
        for(ProductDef p : testProducts){
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
        ProductDef p = testProducts[id];
        JPanel productItemPanel = new JPanel(new GridLayout(5,5));
        for(int i=0;i<p.optionalItems.length;i++){
            int itemId = p.optionalItems[i];
            double portion = p.optionalItemPortions[i];
            Item item = itemMap.get(itemId);
            JToggleButton itemButton = new JToggleButton(item.name + ": " + portion + item.units);
            productItemPanel.add(itemButton);
        }
        itemPanel.add(productItemPanel);

        // Create a button and add it to the receipt panel.
        JButton productButton = new JButton(p.name + " - " + p.price);
        receiptPanel.add(productButton);

        // Map the button to the panel in the hashmap, then add an event listener accordingly
        productPanels.put(productButton, productItemPanel);
        productButton.addActionListener(e -> switchToProduct(productPanels.get(productButton)));
        switchToProduct(productItemPanel);
    }
    public void switchToProduct(JPanel p){
        // Hide every panel and show the one we want
        productPanels.forEach((i, panel) -> panel.setVisible(false));
        p.setVisible(true);
    }
}
