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

public class GUI extends JFrame implements ActionListener {
    static JFrame f;

    public static void initialize(dbConnection db)
    {
      //TODO STEP 1
      db.connect();
      JOptionPane.showMessageDialog(null,"Opened database successfully");

      String name = "";
      try{
        //send statement to DBMS
        ResultSet result = db.sendCommand("SELECT * FROM item");
        while (result.next()) {
          name += result.getString("name") + " " +  result.getString("quantity") + " " + result.getString("units") + "\n";
        }
      } catch (Exception e){
          e.printStackTrace();
          JOptionPane.showMessageDialog(null,"Error accessing Database.");
      }
      // create a new frame
      f = new JFrame("DB GUI");

      // create a object
      GUI s = new GUI();

      // create a panel
      JPanel p = new JPanel();

      JButton b = new JButton("Close");

      // add actionlistener to button
      b.addActionListener(s);

      //TODO Step 3
      JTextArea area = new JTextArea(name);
      area.setEditable(false);
      p.add(area);

      //TODO Step 4

      // add button to panel
      p.add(b);

      // add panel to frame
      f.add(p);

      // set the size of frame
      f.setSize(768, 1024);

      f.show();

    }

    // if button is pressed
    public void actionPerformed(ActionEvent e)
    {
        String s = e.getActionCommand();
        if (s.equals("Close")) {
            f.dispose();
        }
    }
}
