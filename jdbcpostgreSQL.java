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

    public static void main(String args[]) {
      dbConnection db = new dbConnection();
      db.connect();
      double price = 0;
      int num = 0;
      for(int day = 9;day<=30;day++){
          int orderCount;
          if(day == 24 || day == 17){
              orderCount = 800;
          }
          else{
              orderCount = 200;
          }
          for(int i=0;i<orderCount;i++){
              num += 1;
              price += testGeneration.addRandomOrderToDatabase(db, "2022-08-" + String.format("%2d", day).replace(" ", "0"));
          }
      }
      db.close();
    }
}
