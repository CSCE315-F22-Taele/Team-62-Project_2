import java.sql.*;
import javax.swing.*;
import java.util.HashMap;
import java.awt.*;
import javax.swing.border.*;

public class summaryView{
    private dbConnection db;
    private JPanel summaryPanel;
    private JPanel pairPanel;
    public JPanel mainPanel;
    Color customPurple = new Color(65, 30, 122);
	Color customWhite = new Color(255, 255, 255);

    public summaryView(dbConnection db){
        this.db = db;
        mainPanel = new JPanel(new BorderLayout());
//        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
    }

    public void update(){
        mainPanel.removeAll();
        loadSummaryPanel();
        loadPairPanel();
        mainPanel.validate();
        mainPanel.revalidate();
    }

    private void loadPairPanel(){
        JPanel pairPanel = new JPanel();
        pairPanel.add(new JLabel("Commmonly Paired Items"));

        String result = "";
        try {
            ResultSet r = db.sendCommand("SELECT t1.name as i1, t2.name as i2, count(*) from products t1 join products t2 on t1.orderId = t2.orderId and t1.name < t2.name group by t1.name, t2.name order by count(*) desc limit 5;");
            while (r.next()) {
                result += r.getString("i1") + " & " + r.getString("i2") + ": " + r.getInt("count") + " occurrences\n";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        JTextArea contents = new JTextArea(result);
		contents.setEditable(false);
		contents.setFont(new Font("Gill Sans Nova Light", Font.PLAIN, 20));
        Border border = new LineBorder(customPurple, 2);
		contents.setBorder(border);

        pairPanel.add(contents);
        mainPanel.add(pairPanel, BorderLayout.CENTER);
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
			e.printStackTrace();
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}
		try {
			ResultSet r = db.sendCommand("SELECT date FROM orders WHERE id = " + id);
			r.next();
			date = r.getString("date");
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}
		try {
			ResultSet r = db.sendCommand("SELECT total FROM orders WHERE date = '" + date + "'");
			while (r.next()) {
				ordersToday++;
				salesToday += r.getDouble("total");
			}
		} catch (Exception e) {
			e.printStackTrace();
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
