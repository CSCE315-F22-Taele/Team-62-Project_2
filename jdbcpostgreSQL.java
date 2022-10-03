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

  public static String itemList(String items[], int size){
        String ret  = "'{";
        for(int i = 0; i<size;i++){
            if(i!=size-1){
            ret+=((int)(Math.random() * (items.length))) + ", ";
            }
            else{
                ret+=((int)(Math.random() * (items.length))) + "}'";
            }
        }
        
        
        return ret;
    }
    
    public static String portionList(String items[], int size){
        String ret  = "'{";
        for(int i = 0; i<size;i++){
            if(i!=size-1){
            ret+=((int)(Math.random() * (4))+1) + ".0, ";
            }
            else{
                ret+=((int)(Math.random() * (4))+1)  + ".0}'";
            }
        }
        
        
        return ret;
    }
  public static void main(String args[]) {

    //Building the connection with your credentials
    Connection conn = null;
    String teamNumber = "62";
    String sectionNumber = "905";
    String dbName = "csce331_" + sectionNumber + "_" + teamNumber;
    String dbConnectionString = "jdbc:postgresql://csce-315-db.engr.tamu.edu/" + dbName;
    dbSetup myCredentials = new dbSetup(); 

    //Connecting to the database
    try {
        conn = DriverManager.getConnection(dbConnectionString, dbSetup.user, dbSetup.pswd);
     } catch (Exception e) {
        e.printStackTrace();
        System.err.println(e.getClass().getName()+": "+e.getMessage());
        System.exit(0);
     }

     System.out.println("Opened database successfully");
	int id = 0;
	try{
       //create a statement object
       Statement s = conn.createStatement();
	String maxId = "SELECT MAX(id) FROM productstest";
	ResultSet r = s.executeQuery(maxId);
	r.next();
	System.out.println(r.getInt("max"));
	id = r.getInt("max")+1;
       

   } catch (Exception e){
       e.printStackTrace();
       System.err.println(e.getClass().getName()+": "+e.getMessage());
       System.exit(0);
   }
       String items[] = {"Lemon Chicken","Gyro Meat","Falafel","Onions","Cauliflower","Peppers",
			"Olives","Couscous","Slaw","Tomatoes","Cucumbers","Hummus","Jalepeno Feta Dressing",
			"Vinagrette Dressing","Tahini Dressing","Yogurt Dill Dressing","Spicy Hummus",
			"Tzatziki Sauce","Harissa Yogurt"};
	String names[] = {"Grain Bowl", "Salad", "Pita", "Greens and Grains"};
  for(int i = 0; i<100; i++){
	int item = (int)(Math.random() * (items.length));
	int size = (int)(Math.random()*3)+4;
	String cmd = "";
	cmd +=(id + ", ");
	id++;
	cmd +=( "'" + names[(int)(Math.random() * (4))]+ "', ");
	cmd +=("7.69, ");
	cmd += itemList(items,size) + ", ";
	cmd += portionList(items,size);
	String full = "INSERT INTO productstest VALUES (" + cmd + ")";
     try{
       //create a statement object
       Statement stmt = conn.createStatement();

       //Running a query
       //TODO: update the sql command here
       //String sqlStatement = "INSERT INTO teammembers VALUES ('t','905','test', '2022-10-31')";

       //send statement to DBMS
       //This executeQuery command is useful for data retrieval
       //ResultSet result = stmt.executeQuery(full);
       //OR
       //This executeUpdate command is useful for updating data
       int result = stmt.executeUpdate(full);

       //OUTPUT
       //You will need to output the results differently depeninding on which function you use
       //System.out.println("--------------------Query Results--------------------");
       //while (result.next()) {
       //System.out.println(result.getString("column_name"));
       //}
       //OR
       //System.out.println(result);
   } catch (Exception e){
       e.printStackTrace();
       System.err.println(e.getClass().getName()+": "+e.getMessage());
       System.exit(0);
   }
  }

    //closing the connection
    try {
      conn.close();
      System.out.println("Connection Closed.");
    } catch(Exception e) {
      System.out.println("Connection NOT Closed.");
    }//end try catch
  }//end main
}//end Class
