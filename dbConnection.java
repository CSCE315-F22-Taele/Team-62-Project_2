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

   public void sendUpdate(String cmd) throws SQLException{
        ResultSet result;
        // create a statement object
        Statement stmt = conn.createStatement();
        // Running a query
        String sqlStatement = cmd;
        // send statement to DBMS
        // This executeQuery command is useful for data retrieval
        stmt.executeUpdate(sqlStatement);
        
   }

    public int addProductToDatabase(String name, double price, int itemList[], double portionList[]){
        // Returns the ID of the new product when done.
        int id = 0;
        try{
            ResultSet r = sendCommand("SELECT MAX(id) FROM productstest");
            r.next();
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
        String full = "INSERT INTO product VALUES (" + cmd + ")";
        try {
            sendCommand(full);
        }
        catch (Exception e){
        }
	for(int i = 0; i<itemList.length;i++){
	try{
            sendUpdate("UPDATE item SET quantity = quantity-1 WHERE id = "+ itemList[i]);
        } catch (Exception e){
          e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
}
        return id;
    }

    public double addOrderToDatabase(int productList[], double discount, double subtotal, String date){
        // Returns the total price of the new order when done.
        // Note that SQL Date is formatted as "YYYY-MM-DD"
        int id = 0;
        try{
            ResultSet r = sendCommand("SELECT MAX(id) FROM orderstest");
            r.next();
            id = r.getInt("max")+1;
        } catch (Exception e){
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }

        // Apply discounts and then tax.
        double total = subtotal * (1-discount) * 1.0825;

        String cmd = "";
        cmd += id + ", ";
        cmd += "'" + Arrays.toString(productList).replace("[","{").replace("]","}") + "', ";
        cmd += discount + ", ";
        cmd += subtotal + ", ";
        cmd += total + ", ";
        cmd += "'" + date + "'";
        String full = "INSERT INTO orders VALUES (" + cmd + ")";
        //System.out.println(full);
        try {
            sendCommand(full);
        }
        catch (Exception e){
        }
        return total;
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

