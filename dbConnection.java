import java.sql.*;
import java.util.Arrays;
import java.util.HashMap;
import java.math.BigDecimal;

public class dbConnection {
    private Connection conn;
    /**
    * Constructs an SQL connection to the AWS database as a certain user.
    * Note: the TAMU Wifi or VPN must be used for this connection to work.
    * @author Team 62
    */
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
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }

        System.out.println("Opened database successfully");
    }

    public void printResultSet(ResultSet result) throws SQLException {
        // You will need to output the results differently depeninding on which function you use
        System.out.println("--------------------Query Results--------------------");
        ResultSetMetaData rsmd = result.getMetaData();
        int columnsNumber = rsmd.getColumnCount();

        while (result.next()) {
            for (int i = 1; i <= columnsNumber; i++) {
                if (i > 1) System.out.print(",  ");
                String columnValue = result.getString(i);
                System.out.print(rsmd.getColumnName(i) + ": " + columnValue);
            }
            System.out.println();
        }
    }
    /**
     * Returns the result of the command that was sent to the database
     * @author Ezra Lane
     * @param cmd  The actual command that is sent to the database
     * @return the result from the command that was just executed
     * @throws SQLException if there is any error with the command
     */
    public ResultSet sendCommand(String cmd) throws SQLException {
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

    /**
     * Function that does not return anything, but string is coonstly being updated
     * @author Adidev Mohapatra
     * @param cmd  The actual command that is sent to the database constantly being updated
     * @throws SQLException if there is any error with the command
     */

    public void sendUpdate(String cmd) throws SQLException {
        ResultSet result;
        // create a statement object
        Statement stmt = conn.createStatement();
        // Running a query
        String sqlStatement = cmd;
        // send statement to DBMS
        // This executeQuery command is useful for data retrieval
        stmt.executeUpdate(sqlStatement);

    }

    public void addItemToDatabase(int id, double quantity, String units, String newItem, int minQuantity) {
        try{
            Statement stmt = conn.createStatement();
            String sqlStatement = "INSERT INTO item VALUES (" + String.valueOf(id) +"," + String.valueOf(quantity) + "," + "'" + units + "'" + "," + "'" + newItem + "'" + "," + String.valueOf(minQuantity) + "," + "'2022-10-18'" + ")";
            int result = stmt.executeUpdate(sqlStatement);

        } catch (Exception e){
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }

    }

    /** *
    * <p>
    * @param  name   Name of the product
    * @param  price price of the product
    * @param  itemList   List of items in the product
    * @param  portionList List of the portions of each item in the product
    * @return The id of the newly created product
    */
    public int addProductToDatabase(String name, double price, int[] itemList, double[] portionList, String date, int orderId) {
        // Returns the ID of the new product when done.
        int id = 0;
        try {
            ResultSet r = sendCommand("SELECT MAX(id) FROM products");
            r.next();
            id = r.getInt("max") + 1;
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }

        String cmd = "";
        cmd += id + ", ";
        cmd += "'" + name + "', ";
        cmd += (price + ", ");
        cmd += "'" + Arrays.toString(itemList).replace("[", "{").replace("]", "}") + "', ";
        cmd += "'" + Arrays.toString(portionList).replace("[", "{").replace("]", "}") + "', ";
        cmd += "'" + date + "',";
        cmd += orderId;
        String full = "INSERT INTO products VALUES (" + cmd + ")";
        try {
            sendCommand(full);
        } catch (Exception e) {
        }
        for (int i = 0; i < itemList.length; i++) {
            try {
                sendUpdate("UPDATE item SET quantity = quantity-" + portionList[i] + " WHERE id = " + itemList[i]);
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println(e.getClass().getName() + ": " + e.getMessage());
                System.exit(0);
            }
        }
        return id;
    }

    /**
     * This method adds the Order to the Database
     * This method returns immediately or throws an error
     * sends a query to the database to see orders
     * calculate the discount
     * recieve productList and insert order values
     *
     * @author JP Pham
     * @param productList  A list of product as input
     * @param discount  The discount amount
     * @param subtotal  The total before taxes and discount
     * @param date  The date the order was added
     * @throws  throws error if sendCommand does not work
     * @return  the total accounting in taxes
     */
    public double addOrderToDatabase(int[] productList, double discount, double subtotal, String date) {
        // Returns the total price of the new order when done.
        // Note that SQL Date is formatted as "YYYY-MM-DD"
        int id = 0;
        try {
            ResultSet r = sendCommand("SELECT MAX(id) FROM orders");
            r.next();
            id = r.getInt("max") + 1;
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }

        // Apply discounts and then tax.
        double total = subtotal * (1 - discount) * 1.0825;

        String cmd = "";
        cmd += id + ", ";
        cmd += "'" + Arrays.toString(productList).replace("[", "{").replace("]", "}") + "', ";
        cmd += discount + ", ";
        cmd += subtotal + ", ";
        cmd += total + ", ";
        cmd += "'" + date + "'";
        String full = "INSERT INTO orders VALUES (" + cmd + ")";
        //System.out.println(full);
        String prevDate = "";
        try {
            sendCommand(full);
        } catch (Exception e) {
        }
        try {
			ResultSet r = sendCommand("SELECT date from orders where date = (date "+ date + "- integer '1'");
			r.next();
			prevDate = r.getString("date");
		} catch (Exception e) {
			prevDate = "";
		}
        if(prevDate != date){
            takeInventory(this,date);
        }
        return total;
    }

    public static void takeInventory(dbConnection db, String date){
        HashMap<Integer, Item> items = db.getItemHashmap();
        for(int i : items.keySet()){
            Item item = items.get(i);
            double quantity = item.quantity;
            try {
                db.sendUpdate("INSERT INTO inventory VALUES (" + i + ", " + quantity + ", '" + date + "', false)");
            } catch (Exception error) {
                error.printStackTrace();
                System.exit(0);
            }
            // // /*if(quantity < item.minquantity){
            // //    // System.out.println("Restocking " + item.name);
            // //    // System.out.println(" - Current Inventory: " + quantity);
            // //     quantity += item.minquantity * 5;
            // //     try {
            // //         db.sendUpdate("UPDATE item SET quantity = " + quantity + " WHERE id = " + i + "");
            // //         db.sendUpdate("UPDATE item SET lastrestock = '" + date + "' WHERE id = " + i + "");
            // //     } catch (Exception error) {
            // //         error.printStackTrace();
            // //         System.exit(0);
            // //     }*/
            // //    // System.out.println(" - New Inventory: " + quantity);
            //     try {
            //         db.sendUpdate("INSERT INTO inventory VALUES (" + i + ", " + quantity + ", '" + date + "', true)");
            //     } catch (Exception error) {
            //         error.printStackTrace();
            //         System.exit(0);
            //     }
            // }
        }
       // System.out.println("Inventory taken for " + date + ", day ended.");
    }
    

    public HashMap<Integer, Item> getItemHashmap(){
        HashMap<Integer, Item> resultMap = new HashMap<>();
        try {
			ResultSet result = sendCommand("SELECT * FROM item");
			while (result.next()) {
                Item newItem = new Item(result.getInt("id"), result.getDouble("quantity"), result.getString("units"), result.getString("name"), result.getDouble("minquantity"));
                resultMap.put(result.getInt("id"), newItem);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
        return resultMap;
    }

    public ProductDef[] getProductDefs() {
        try {
            ResultSet count = sendCommand("SELECT COUNT(productDef) FROM productDef");
            count.next();
            ProductDef[] data = new ProductDef[count.getInt("count")];
            try {
                ResultSet result = sendCommand("SELECT * FROM productDef");
                int i = 0;
                while (result.next()) {
                    data[i] = new ProductDef(
                        result.getInt("id"),
                        result.getString("name"),
                        result.getDouble("price"),
                        (Integer[]) result.getArray("baseitemlist").getArray(),
                        bigDecimalArrayToDoubleArray((BigDecimal[]) result.getArray("baseportionlist").getArray()),
                        (Integer[]) result.getArray("optionalitemlist").getArray(),
                        bigDecimalArrayToDoubleArray((BigDecimal[]) result.getArray("optionalportionlist").getArray())
                    );
                    i += 1;
                }
                return data;
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        ProductDef[] bad_data = new ProductDef[0];
        return bad_data;
    }

    Double[] bigDecimalArrayToDoubleArray(BigDecimal[] in){
        Double[] out = new Double[in.length];
        for(int i=0;i<in.length;i++){
            out[i] = in[i].doubleValue();
        }
        return out;
    }

    public void close() {
        try {
            conn.close();
            System.out.println("Connection Closed.");
        } catch (Exception e) {
            System.out.println("Connection NOT Closed.");
        }//end try catch
    }
}//end Class
