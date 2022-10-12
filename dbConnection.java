import java.sql.*;
import java.util.Arrays;

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

    public int addProductToDatabase(String name, double price, int[] itemList, double[] portionList) {
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
        cmd += "'" + Arrays.toString(portionList).replace("[", "{").replace("]", "}") + "'";
        String full = "INSERT INTO product VALUES (" + cmd + ")";
        try {
            sendCommand(full);
        } catch (Exception e) {
        }
        for (int i = 0; i < itemList.length; i++) {
            try {
                sendUpdate("UPDATE item SET quantity = quantity-1 WHERE id = " + itemList[i]);
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
        try {
            sendCommand(full);
        } catch (Exception e) {
        }
        return total;
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
