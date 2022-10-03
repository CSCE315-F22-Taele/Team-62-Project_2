import java.sql.*;

/*
CSCE 331
9-28-2022 Lab
 */
public class dbConnection {
  private Connection conn;
  // MAKE SURE YOU ARE ON VPN or TAMU WIFI TO ACCESS DATABASE
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

  public void sendCommand(String cmd){
     try{
       // create a statement object
       Statement stmt = conn.createStatement();

       // Running a query
       String sqlStatement = cmd;

       // send statement to DBMS
       // This executeQuery command is useful for data retrieval
       ResultSet result = stmt.executeQuery(sqlStatement);
       // OR
       // This executeUpdate command is useful for updating data
       // int result = stmt.executeUpdate(sqlStatement);

       // OUTPUT
       // You will need to output the results differently depeninding on which function you use
       System.out.println("--------------------Query Results--------------------");
       // while (result.next()) {
       // System.out.println(result.getString("column_name"));
       // }
       // OR
       System.out.println(result);
     }
     catch (Exception e){
         e.printStackTrace();
         System.err.println(e.getClass().getName()+": "+e.getMessage());
     }
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
