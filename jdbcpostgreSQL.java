import java.sql.*;
import java.lang.Math;

/*
CSCE 315
9-27-2021 Lab
 */
public class jdbcpostgreSQL {

    //Commands to run this script
    //This will compile all java files in this directory
    //javac *.java
    //This command tells the file where to find the postgres jar which it needs to execute postgres commands, then executes the code
    //Windows: java -cp ".;postgresql-42.2.8.jar" jdbcpostgreSQL
    //Mac/Linux: java -cp ".:postgresql-42.2.8.jar" jdbcpostgreSQL

    //MAKE SURE YOU ARE ON VPN or TAMU WIFI TO ACCESS DATABASE

    public static void main(String[] args) {
        dbConnection db = new dbConnection();
        String[] cmds = {
                "SELECT SUM(total) FROM orders", // total sales
                "SELECT AVG(total) FROM orders", // average spend per order
                "SELECT COUNT(*) FROM products WHERE name='Grain Bowl'" // number of grain bowls sold

        };
        db.connect();
        for (int i = 0; i < cmds.length; i++) {
            String cmd = cmds[i];
            System.out.println("Executing command: " + cmd);
            try {
                db.printResultSet(db.sendCommand(cmd));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        db.close();
    }
}
