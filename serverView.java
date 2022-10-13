import java.sql.*;
import java.awt.event.*;
import java.awt.BorderLayout;
import javax.swing.*;
import java.util.HashMap;


public class serverView {
    private JPanel mainPanel;
    private JPanel productPanel;
    private JPanel itemPanel;
    private JPanel receiptPanel;
    private JPanel infoPanel;
    private HashMap<Integer, JPanel> productPanels = new HashMap<>();

    private ProductDef[] testProducts = {
        new ProductDef(0, "Gyro", 7.49, new int[]{0,1,2}, new double[]{0.5, 1.0, 1.5}, new int[]{3,4,5}, new double[]{0.3, 0.4, 0.5}),
        new ProductDef(1, "Bowl", 7.59, new int[]{0,1,2}, new double[]{0.5, 1.0, 1.5}, new int[]{6,7,8}, new double[]{0.3, 0.4, 0.5}),
    };

    private GUI gui;

    private void loadProductPanels(){
        for(ProductDef p : testProducts){
            JButton b = new JButton(p.name);
            b.addActionListener(e -> switchToProductPanel(p.id));
            productPanel.add(b);

            JPanel productItemPanel = new JPanel();
            productItemPanel.add(new JLabel("Customize " + p.name));
            productItemPanel.setVisible(false);
            // Store the product panel in the hashmap for referencing later.
            productPanels.put(p.id, productItemPanel);
            itemPanel.add(productItemPanel);
        }
    }

    public serverView(JPanel p, GUI g){
        mainPanel = p;
        gui = g;
        productPanel = new JPanel();
        itemPanel = new JPanel();
        receiptPanel = new JPanel();
        infoPanel = new JPanel();
        infoPanel.add(new JLabel("Server View"));
        productPanel.add(new JLabel("Products"));
        receiptPanel.add(new JLabel("Receipts"));
        itemPanel.add(new JLabel("Items"));
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
    public void switchToProductPanel(int id){
        System.out.println("I want product panel " + id);
        // Hide every panel
        productPanels.forEach((i, p) -> p.setVisible(false));
        // Show the one we want
        productPanels.get(id).setVisible(true);
    }
}
