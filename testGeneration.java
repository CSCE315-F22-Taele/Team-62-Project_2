import java.util.HashMap;
import java.sql.*;

/**
 * This class is used to create all of our test data.
 */
public class testGeneration {
    /**
     * Creates an array of random integers.
     * @param size the size of the array to be returned.
     * @return an array of random integers.
     */
    public static int[] randomItemList(int size) {
        int[] result = new int[size];
        for (int i = 0; i < size; i++) {
            result[i] = (int) (Math.random() * (22 - 1 + 1)) + 1;
        }
        return result;
    }

    /**
     * Creates a random array of doubles.
     * @param size size of the array to be returned.
     * @return array of random doubles.
     */
    public static double[] randomPortionList(int size) {
        double[] result = new double[size];
        for (int i = 0; i < size; i++) {
            result[i] = (int) (Math.random() * 4 + 1);
        }
        return result;
    }

    /**
     * Adds a random product to the database.
     * @param db current database connection.
     * @param date date that you want for the order.
     * @param orderId order ID.
     * @return the product that was added.
     */
    public static Product addRandomProductToDatabase(dbConnection db, String date, int orderId) {
        ProductDef[] productDefs = db.getProductDefs();
        ProductDef productToUse = productDefs[(int) (Math.random() *  productDefs.length)];

        Product p = new Product(productToUse.name, productToUse.price);
        // Add all base items to product.
        for(int i=0;i<productToUse.baseItems.length;i++){
            p.addItem(productToUse.baseItems[i], productToUse.baseItemPortions[i]);
        }
        // Add some (~25%) optional items to product.
        for(int i=0;i<productToUse.optionalItems.length;i++){
            if(Math.random() < 0.25){
                p.addItem(productToUse.optionalItems[i], productToUse.optionalItemPortions[i]);
            }
        }

        // Add the product to the database.
        p.addToDatabase(db, date, orderId);
        // Return the product, which will now contain its ID in the database
        return p;
    }

    /**
     * Adds a random order to the database.
     * @param db current database connection.
     * @param date date for the order.
     * @return the total of the order.
     */
    public static double addRandomOrderToDatabase(dbConnection db, String date){
        // Returns the total price of the order.
        double discount = Math.random() < 0.1 ? Math.random() * 0.3 : 0.0;
        int numProducts = (int) (Math.random() * 4) + 1;
        int[] productList = new int[numProducts];
        double subtotal = 0;

        // Add random products to the database, then add to order
        int orderid = 0;
        try {
            ResultSet r = db.sendCommand("SELECT MAX(id) FROM orders");
            r.next();
            orderid = r.getInt("max") + 1;
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        for (int i = 0; i < numProducts; i++) {
            Product p = addRandomProductToDatabase(db,date,orderid);
            if(p.id == -1){
                Logger.log("Error adding order to database (product ID not assigned).");
                System.exit(0);
            }
            productList[i] = p.id;
            subtotal += p.price;
        }

        return db.addOrderToDatabase(productList, discount, subtotal, date);
    }


    /**
     * Populates the database with random orders.
     * @param db current database connection.
     * @param startDay what day to start adding to the database.
     */
    public static void populateDatabaseWithOrders(dbConnection db, int startDay) {
        testGeneration.addItems(db);
        testGeneration.addProductDefs(db);
        if(startDay == 1){
            Logger.log("Removing orders and products...");
            try {
                db.sendUpdate("DELETE FROM orders;");
                db.sendUpdate("DELETE FROM products;");
            }
            catch (Exception error) {
                error.printStackTrace();
                System.exit(0);
            }
            Logger.log("Successfully deleted.");
        }
        double price = 0;
        int num = 0;
        for (int day = startDay; day <= 30; day++) {
            int orderCount;
            if (day == 17 || day == 10) {
                orderCount = 30;
            } else {
                orderCount = 10;
            }
            String date = "2022-09-" + String.format("%2d", day).replace(" ", "0");
            Logger.log("Adding orders for " + date);
            for (int i = 0; i < orderCount; i++) {
                num += 1;
                addRandomOrderToDatabase(db, date);
            }
            //takeInventory(db, date);

        }
    }

    /**
     * Takes the inventory at for the inventory table in the database.
     * @param db current database connection.
     * @param date current date.
     */
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
            if(quantity < item.minquantity){
               // Logger.log("Restocking " + item.name);
               // Logger.log(" - Current Inventory: " + quantity);
                quantity += item.minquantity * 5;
                try {
                    db.sendUpdate("UPDATE item SET quantity = " + quantity + " WHERE id = " + i + "");
                    db.sendUpdate("UPDATE item SET lastrestock = '" + date + "' WHERE id = " + i + "");
                } catch (Exception error) {
                    error.printStackTrace();
                    System.exit(0);
                }
               // Logger.log(" - New Inventory: " + quantity);
                try {
                    db.sendUpdate("INSERT INTO inventory VALUES (" + i + ", " + quantity + ", '" + date + "', true)");
                } catch (Exception error) {
                    error.printStackTrace();
                    System.exit(0);
                }
            }
        }
       // Logger.log("Inventory taken for " + date + ", day ended.");
    }

    /**
     * Adds the items into the database.
     * @param db current database connection.
     */
    public static void addItems(dbConnection db) {
        Logger.log("Resetting Items...");
        try {
            db.sendUpdate("DELETE FROM inventory;");
            db.sendUpdate("DELETE FROM item;");
            db.sendUpdate("INSERT INTO item VALUES (1, 40, 'kg', 'Rice Pilaf', 8)");
            db.sendUpdate("INSERT INTO item VALUES (2, 40, 'kg', 'White Rice', 8)");
            db.sendUpdate("INSERT INTO item VALUES (3, 60, 'kg', 'Buttered Chicken', 12)");
            db.sendUpdate("INSERT INTO item VALUES (4, 60, 'kg', 'Lemon Chicken', 12)");
            db.sendUpdate("INSERT INTO item VALUES (5, 60, 'kg', 'Gyro Meat', 12)");
            db.sendUpdate("INSERT INTO item VALUES (6, 1000, '', 'Falafel', 200)");
            db.sendUpdate("INSERT INTO item VALUES (7, 30, 'kg', 'Onions', 6)");
            db.sendUpdate("INSERT INTO item VALUES (8, 30, 'kg', 'Cauliflower', 6)");
            db.sendUpdate("INSERT INTO item VALUES (9, 30, 'kg', 'Peppers', 6)");
            db.sendUpdate("INSERT INTO item VALUES (10, 30, 'kg', 'Olives', 6)");
            db.sendUpdate("INSERT INTO item VALUES (11, 30, 'kg', 'Couscous', 6)");
            db.sendUpdate("INSERT INTO item VALUES (12, 30, 'kg', 'Slaw', 6)");
            db.sendUpdate("INSERT INTO item VALUES (13, 30, 'kg', 'Tomatoes', 6)");
            db.sendUpdate("INSERT INTO item VALUES (14, 30, 'kg', 'Cucumbers', 6)");
            db.sendUpdate("INSERT INTO item VALUES (15, 7.5, 'kg', 'Hummus', 2)");
            db.sendUpdate("INSERT INTO item VALUES (16, 7.5, 'kg', 'Jalepeno Feta Dressing', 2)");
            db.sendUpdate("INSERT INTO item VALUES (17, 7.5, 'kg', 'Vinagrette Dressing', 2)");
            db.sendUpdate("INSERT INTO item VALUES (18, 7.5, 'kg', 'Tahini Dressing', 2)");
            db.sendUpdate("INSERT INTO item VALUES (19, 7.5, 'kg', 'Yogurt Dill Dressing', 2)");
            db.sendUpdate("INSERT INTO item VALUES (20, 7.5, 'kg', 'Spicy Hummus', 2)");
            db.sendUpdate("INSERT INTO item VALUES (21, 7.5, 'kg', 'Tzatziki Sauce', 2)");
            db.sendUpdate("INSERT INTO item VALUES (22, 7.5, 'kg', 'Harissa Yogurt', 2)");
            db.sendUpdate("INSERT INTO item VALUES (23, 500, '', 'Paper Cup', 100)");
            db.sendUpdate("INSERT INTO item VALUES (24, 2000, '', 'Plastic Bowl', 400)");
            db.sendUpdate("INSERT INTO item VALUES (25, 500, '', 'Pita Bread', 100)");
        } catch (Exception e) {
            Logger.log(e);
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        Logger.log("Items Reset.");
    }

    /**
     * Adds the productDefs into the database.
     * @param db current database connection.
     */
    public static void addProductDefs(dbConnection db) {
        Logger.log("Resetting ProducDefs...");
        try {
            db.sendUpdate("DELETE FROM productDef;");
            db.sendUpdate("INSERT INTO productDef VALUES (1, 'Gyro', 8.09, '{24, 25}', '{1.0, 1.0}', '{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22}', '{0.075, 0.075, 0.120, 0.120, 0.120, 2.0, 0.05, 0.05, 0.05, 0.05, 0.05, 0.05, 0.05, 0.05, 0.015, 0.015, 0.015, 0.015, 0.015, 0.015, 0.015, 0.015}')");
            db.sendUpdate("INSERT INTO productDef VALUES (2, 'Bowl', 8.09, '{24}', '{1.0}', '{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22}', '{0.075, 0.075, 0.120, 0.120, 0.120, 2.0, 0.05, 0.05, 0.05, 0.05, 0.05, 0.05, 0.05, 0.05, 0.015, 0.015, 0.015, 0.015, 0.015, 0.015, 0.015, 0.015}')");
            db.sendUpdate("INSERT INTO productDef VALUES (3, 'Hummus and Pita', 3.49, '{24, 25}', '{1.0 , 0.5}', '{15, 20}', '{0.015, 0.015}')");
            db.sendUpdate("INSERT INTO productDef VALUES (4, 'Two Falafels', 3.49, '{24, 6}', '{1.0, 2.0}', '{}', '{}')");
            db.sendUpdate("INSERT INTO productDef VALUES (5, 'Extra Protein', 1.99, '{}', '{}', '{3, 4, 5, 6}', '{0.120, 0.120, 0.120, 2.0}')");
            db.sendUpdate("INSERT INTO productDef VALUES (6, 'Extra Dressing', 0.39, '{}', '{}', '{15,16,17,18,19,20,21,22}', '{0.015, 0.015, 0.015, 0.015, 0.015, 0.015, 0.015, 0.015}')");
            db.sendUpdate("INSERT INTO productDef VALUES (7, 'Fountain Drink', 2.45, '{23}', '{1.0}', '{}', '{}')");
        } catch (Exception e) {
            Logger.log(e);
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        Logger.log("ProducDefs Reset.");
    }

}
