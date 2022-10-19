import java.sql.*;
import java.awt.event.*;
import java.awt.BorderLayout;
import javax.imageio.plugins.jpeg.JPEGHuffmanTable;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.GridLayout;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
public class summaryView{
    private dbConnection db;
    private JPanel summaryPanel;
    private JPanel pairPanel;
    public JPanel mainPanel;
    Color customPurple = new Color(65, 30, 122);
	Color customWhite = new Color(255, 255, 255);
	String todayDate = "";

    public summaryView(dbConnection db){
        this.db = db;
        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		try {
			ResultSet r = db.sendCommand("SELECT CAST( (SELECT CURRENT_TIMESTAMP) AS Date )");
			r.next();
			todayDate = r.getString("current_timestamp");
		} catch (Exception e) {
			Logger.log(e);
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}
    }

    public void update(){
        mainPanel.removeAll();
        loadSummaryPanel();
        loadPairPanel();
		loadRestockPanel();
		loadExcessPanel(todayDate);
        mainPanel.validate();
        mainPanel.revalidate();
    }

    private void loadPairPanel(){
        JPanel pairPanel = new JPanel();
        pairPanel.add(new JLabel("Commmonly Paired Items"));

        String result = "";
        try {
            ResultSet r = db.sendCommand("SELECT t1.name as i1, t2.name as i2, count(*) from products t1 join products t2 on t1.orderId = t2.orderId and t1.name < t2.name group by t1.name, t2.name order by count(*) desc limit 6;");
            while (r.next()) {
                result += r.getString("i1") + " & " + r.getString("i2") + ": " + r.getInt("count") + " occurrences\n";
            }
        } catch (Exception e) {
            Logger.log(e);
        }

        JTextArea contents = new JTextArea(result);
		contents.setEditable(false);
		contents.setFont(new Font("Gill Sans Nova Light", Font.PLAIN, 20));
        Border border = new LineBorder(customPurple, 2);
		contents.setBorder(border);

        pairPanel.add(contents);
        mainPanel.add(pairPanel, BorderLayout.CENTER);
    }

	private void loadRestockPanel(){
        JPanel restockPanel = new JPanel();
        restockPanel.add(new JLabel("Restock Items"));

        String result = restockitems();

        JTextArea contents = new JTextArea(result, 5, 10);
		contents.setEditable(false);
		contents.setLineWrap(true);
		contents.setWrapStyleWord(true);
		contents.setFont(new Font("Gill Sans Nova Light", Font.PLAIN, 20));
        Border border = new LineBorder(customPurple, 2);
		contents.setBorder(border);
		JScrollPane pane = new JScrollPane(contents);
		pane.setBounds(10, 11, 455, 249);
		pane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        restockPanel.add(pane);
        mainPanel.add(restockPanel, BorderLayout.CENTER);
    }

	//Restock Report
	///////////////////////////////////////
	public String restockitems(){

		int min_restock=2;
		String result = "";
		try {
			//ResultSet size = db.sendCommand("SELECT COUNT(item) FROM item");
			//Double[] curr_quantity = new Double[size.getInt("count")];
			try {
				ResultSet order_data = db.sendCommand("SELECT * FROM item where quantity < minquantity");
				while (order_data.next()) {
					result+=(order_data.getString("name")+"\n");
				}
			} catch (Exception e) {
				Logger.log(e);
			}
		} catch (Exception e) {
			Logger.log(e);
		}

		// String[] final_result = new String[j];
		// for (int k = 0; k < j; k += 1) {
		// 	final_result[k] = temp[k];
		// }
		return result;

	}

	//Excess sales report

	public String[] excessSales(String date) {
		//NOTE for when you're coding: current date is stored in currentDate
		String[] temp = new String[1000];
		Double[] quantity_holder = new Double[1000];
		int j = 0; //used for iterating through temp array
		int i = 0; //used for iterating through the quantity array
		// Get the stock at the given date
		try {
			ResultSet size = db.sendCommand("SELECT COUNT(inventory) FROM inventory WHERE date='" + date + "'");
			size.next();
			Double[] temp_quantity = new Double[size.getInt("count")];
			try {
				ResultSet data = db.sendCommand("SELECT * FROM inventory WHERE date='" + date + "'");
				while (data.next()) {
					temp_quantity[i] = (data.getDouble("quantity"));
					quantity_holder[i] = temp_quantity[i];
					i += 1;
				}
			} catch (Exception e) {
				Logger.log(e);
			}
		} catch (Exception e) {
			Logger.log(e);
		}
		Double[] quantity = new Double[i];
		for (int k = 0; k < i; k += 1) {
			quantity[k] = quantity_holder[k];
		}
		//get the current stock
		try {
			ResultSet size2 = db.sendCommand("SELECT COUNT(item) FROM item");
			size2.next();
			Double[] curr_quantity = new Double[size2.getInt("count")];
			try {
				ResultSet data2 = db.sendCommand("SELECT * FROM item ORDER BY id ASC");
				int l = 0;
				while (data2.next()) {
					curr_quantity[l] = (data2.getDouble("quantity"));
					// This is where the less than 10% check happens
					if (curr_quantity[l] > (quantity[l] * 0.9)) {
						temp[j] = data2.getString("name");
						j += 1;
					}
					l += 1;
				}
			} catch (Exception e) {
				Logger.log(e);
			}
		} catch (Exception e) {
			Logger.log(e);
		}
		String[] final_result = new String[j];
		for (int k = 0; k < j; k += 1) {
			final_result[k] = temp[k];
		}
		return final_result;
	}


	private void loadExcessPanel(String date){
        JPanel excessPanel = new JPanel();
		JButton excessButton = new JButton("Generate Excess");
        excessPanel.add(new JLabel("Excess sales"));
		JTextField text = new JTextField(10);
        String[] excess = excessSales(todayDate);
        excessButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String update = text.getText();
				todayDate = update;
				update();
			}
		});
		String result = "Excess Sales on " + todayDate + ": \n";
		for(String item : excess){
			result +=(item+"\n");
		}
		Logger.log("Here: " +date + result);
        JTextArea contents = new JTextArea(result, 5, 10);
		contents.setEditable(false);
		contents.setLineWrap(true);
		contents.setWrapStyleWord(true);
		contents.setFont(new Font("Gill Sans Nova Light", Font.PLAIN, 20));
        Border border = new LineBorder(customPurple, 2);
		contents.setBorder(border);
		JScrollPane pane = new JScrollPane(contents);
		pane.setBounds(10, 10, 10, 10);
		pane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		excessPanel.add(excessButton);
		excessPanel.add(text);
        excessPanel.add(pane);
        mainPanel.add(excessPanel);
    }

    private void loadSummaryPanel(){
        int id = 0;
		String date = "";
		int ordersToday = 0;
		double salesToday = 0;
		int ordersWeek = 0;
		double salesWeek = 0;
		try {
			ResultSet r = db.sendCommand("SELECT MAX(id) FROM orders");
			r.next();
			id = r.getInt("max");
		} catch (Exception e) {
			Logger.log(e);
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}
		try {
			ResultSet r = db.sendCommand("SELECT date FROM orders WHERE id = " + id);
			r.next();
			date = r.getString("date");
		} catch (Exception e) {
			Logger.log(e);
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}
		try {
			ResultSet r = db.sendCommand("SELECT total FROM orders WHERE date = '" + date + "'");
			while (r.next()) {
				ordersToday++;
				salesToday += r.getDouble("total");
			}
		} catch (Exception e) {
			Logger.log(e);
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}

		String today = "                 TODAY                  \n Revenue: " + salesToday + "       Orders: " + ordersToday + "\n";
		for (int i = 0; i < 7; i++) {
			if (date != "") {
				try {
					ResultSet r = db.sendCommand("SELECT total FROM orders WHERE date = '" + date + "'");
					while (r.next()) {
						ordersWeek++;
						salesWeek += r.getDouble("total");
					}
				} catch (Exception e) {
					break;
				}
			}
			try {
				ResultSet r = db.sendCommand("SELECT date FROM orders WHERE date = (date '" + date + "' - integer '1')");
				r.next();
				date = r.getString("date");
			} catch (Exception e) {
				date = "";
				continue;
			}
		}
		String week = "                 Week                  \n Revenue: " + salesWeek + "       Orders: " + ordersWeek + "       Avg. Order: " + (double) salesWeek / ordersWeek;
		JTextArea contents = new JTextArea(today + week);
		contents.setEditable(false);
		contents.setFont(new Font("Gill Sans Nova Light", Font.PLAIN, 20));

		Border border = new LineBorder(customPurple, 2);
		contents.setBorder(border);

        summaryPanel = new JPanel();
        summaryPanel.add(new JLabel("Summary"));
		summaryPanel.add(contents);
        mainPanel.add(summaryPanel, BorderLayout.NORTH);
    }
}
