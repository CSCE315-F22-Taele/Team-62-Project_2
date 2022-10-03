import java.sql.*;
import java.util.Arrays;

/*
CSCE 331
9-28-2022 Lab
 */

// MAKE SURE YOU ARE ON VPN or TAMU WIFI TO ACCESS DATABASE

public class dbConnection {
  private Connection conn;
    public void connect() {
    // Building the connection with your credentials
    String teamNumber = "62"; // Your team number
    String sectionNumber = "905"; // Your section number
    String dbName = "csce331_" + sectionNumber + "_" + teamNumber;
    String dbConnectionString = "jdbc:postgresql://csce-315-db.engr.tamu.edu/" + dbName;
    dbSetup myCredentials = new dbSetup();

    // Connecting to the database
    try {
			conn = DriverManager.getConnection(dbConnectionString, dbSetup.user, dbSetup.pswd);
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println(e.getClass().getName()+": "+e.getMessage());
			System.exit(0);
    }

    System.out.println("Opened database successfully");
  }

    public void printResultSet(ResultSet result){
      // You will need to output the results differently depeninding on which function you use
      System.out.println("--------------------Query Results--------------------");
      // while (result.next()) {
      // System.out.println(result.getString("column_name"));
      // }
      // OR
      System.out.println(result);
  }

    public ResultSet sendCommand(String cmd) throws SQLException{
        ResultSet result;
        // create a statement object
        Statement stmt = conn.createStatement();
        // Running a query
        String sqlStatement = cmd;
        // send statement to DBMS
        // This executeQuery command is useful for data retrieval
        result = stmt.executeQuery(sqlStatement);
        return result;
   }

    public int addProductToDatabase(String name, double price, int itemList[], double portionList[]){
        int id = 0;
        try{
            ResultSet r = sendCommand("SELECT MAX(id) FROM productstest");
            r.next();
            System.out.println(r.getInt("max"));
            id = r.getInt("max")+1;
        } catch (Exception e){
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }

        String cmd = "";
        cmd += id + ", ";
        cmd += "'" + name + "', ";
        cmd += (price + ", ");
        cmd += "'" + Arrays.toString(itemList).replace("[","{").replace("]","}") + "', ";
        cmd += "'" + Arrays.toString(portionList).replace("[","{").replace("]","}") + "'";
        String full = "INSERT INTO productstest VALUES (" + cmd + ")";
        System.out.println(full);
        try {
            sendCommand(full);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return id;
    }

    public void close(){
      try {
        conn.close();
        System.out.println("Connection Closed.");
      } catch(Exception e) {
        System.out.println("Connection NOT Closed.");
      }//end try catch
    }
}//end Class
