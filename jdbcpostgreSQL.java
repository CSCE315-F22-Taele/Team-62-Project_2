import java.sql.*;
import java.lang.Math;



/**
 * CSCE 315
 * 9-27-2021 Lab
 */
public class jdbcpostgreSQL {

    /**
     * Commands to run this script
     * This will compile all java files in this directory
     * javac *.java
     * This command tells the file where to find the postgres jar which it needs to execute postgres commands, then executes the code
     * Windows: java -cp ".;postgresql-42.2.8.jar" jdbcpostgreSQL
     * Mac/Linux: java -cp ".:postgresql-42.2.8.jar" jdbcpostgreSQL
     * MAKE SURE YOU ARE ON VPN or TAMU WIFI TO ACCESS DATABASE
     */

    public static void main(String[] args) {
        dbConnection db = new dbConnection();
        db.connect();
        GUI g = new GUI(db); 
        
        // Inventory I = new Inventory(db);
    }

    public static void queryTest(dbConnection db){
        // List of query commands to be run through the database
        String[] cmds = {
                "SELECT SUM(total) FROM orders", // total sales
                "SELECT AVG(total) FROM orders", // average spend per order
                "SELECT COUNT(*) FROM products WHERE name='Grain Bowl'", // number of grain bowls sold

                "SELECT AVG(cardinality) FROM (SELECT cardinality(itemList) FROM products) AS size", //Average amount of items per product
                "SELECT * FROM item WHERE quantity < 100", //Returns table of items that are below an arbitrary amount
                "SELECT COUNT(*) FROM orders WHERE discount > 0", //Returns the amount of orders that used a discount

                "SELECT MIN(subtotal) from orders",  // the minimum subtotal from order
                "SELECT AVG(total) FROM orders GROUP BY id",  //  the average total group by id
                "SELECT COUNT(*) FROM item WHERE quantity > 430",   // number of quantity of food greater than 430

                "SELECT MAX(total) FROM orders", // finds the max someone has payed for an order
                "SELECT COUNT(productlist) FROM orders WHERE cardinality(productlist) = 1", // keeps return how many orders that contain one product
                "SELECT COUNT(orders) FROM orders WHERE date='2022-08-24'", // returns how many orders that were made on a given date, in this case 8/24/22, can be used to show game days have more sales

                "SELECT COUNT(orders) FROM orders WHERE date='2022-08-23'", // returns sales of a specific day which is a  regular day
                "SELECT MAX(subtotal) from orders", // find the max orders for subtotal price
                "SELECT COUNT(*) FROM item WHERE quantity > 200" //find number of quantitity of food greater than 200


        };
        db.connect();
        //Output the result of each query
        for(int i=0;i<cmds.length;i++){
            String cmd = cmds[i];
            System.out.println("Executing command: " + cmd);
            try{
                db.printResultSet(db.sendCommand(cmd));
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
    }

}
