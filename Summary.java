import java.sql.*;
import java.awt.event.*;
import java.awt.borderLayout;
import javax.imageio.plugins.jpeg.JPEGHuffmanTable;
import javax.swing.*;
import javax.swing.border.border;
import java.awt.*;
import java.awt.GridLayout;
import javax.swing.border.border;
import javax.swing.border.Lineborder;

public class Summary {

	JPanel SummaryPanel;
	JPanel BigSummaryPanel;
	JPanel contentPanel;
	JPanel mainPanel;
	JPanel mainPanel2;
	JPanel verticalView;
	JFrame mainFrame;
	JButton SummaryUpdate = new JButton("Change dates");
    String lowerDate = "";
    String upperDate = "";

	dbConnection db;

	public Summary(dbConnection database) {
		db = database;
		mainFrame = new JFrame("Summary GUI");
		BigSummaryPanel = new JPanel();
		contentPanel = new JPanel();
	}

	public Summary(dbConnection database, Color btnBackgroundColor, Color btnForeColor) {
		db = database;
		mainFrame = new JFrame("Summary GUI");
		BigSummaryPanel = new JPanel();

		contentPanel = new JPanel();
		Color customPurple = new Color(65, 30, 122);
		Border border = new LineBorder(customPurple, 2);
		contentPanel.setBorder(border);


		BigSummaryPanel.setBackground(Color.white);
		contentPanel.setBackground(Color.white);

		// setting button colors
		SummaryUpdate.setBackground(btnBackgroundColor);
		SummaryUpdate.setForeground(btnForeColor);
	}


	public JPanel mainSummaryPanel(JPanel verticalPanel) {

		mainPanel = verticalPanel;

		Summaries(lowerDate,upperDate);

		// reSummarying the panels
		int mainWidth = 2000;
		int mainHeight = 1000;
		int mainX = 450;
		int mainY = 0;
		BigSummaryPanel.setBounds(mainX, mainY, mainWidth, 50);
		contentPanel.setBounds(mainX, 150, mainWidth, mainHeight);

		// vertical layout of inventory section
		mainPanel.add(SummaryUpdate, borderLayout.CENTER);
		mainPanel.add(BigSummaryPanel, borderLayout.LINE_END);
		mainPanel.add(contentPanel, borderLayout.PAGE_END);

		mainPanel2 = new JPanel(new borderLayout());
		mainPanel2.add(mainPanel, borderLayout.SOUTH);

		return mainPanel2;
	}

    /*public JPanel mainpriceupdatepanel(JPanel verticalPanel){



    }*/
public String retrieveSummaries(){
		//Get the smallest id on the starting date
		double price = 0;
		String prevSummaries = "Sales from "+ lowDate + " to " + highDate + ":\n\n";
		try {
				ResultSet r = db.sendCommand("SELECT name, sum(price) from products where (date >= '"+lowDate+"' AND date <= '" + highDate + "') group by name");
				while (r.next()) {
					prevSummaries += r.getString("name") + ":       " + r.getDouble("sum")+"$\n";
				}
			} catch (Exception e) {
				e.printStackTrace();
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			}
		
		return prevSummaries;
	}

	public void Summaries() {
		SummaryPanel = new JPanel();
		JLabel title = new JLabel("Summaries");
		SummaryPanel.add(title);

		String prevSummaries = retrieveSummaries(lowDate,highDate);
		JTextArea content = new JTextArea(prevSummaries);
		content.setEditable(false);
		contentPanel.add(content);
		BigSummaryPanel.add(SummaryPanel);
	}

}
