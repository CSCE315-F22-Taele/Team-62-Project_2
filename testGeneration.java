import java.util.HashMap;

public class testGeneration {
    /*
     *
     */
    public static int[] randomItemList(int size) {
        int[] result = new int[size];
        for (int i = 0; i < size; i++) {
            result[i] = (int) (Math.random() * (22 - 1 + 1)) + 1;
        }
        return result;
    }

    /*
     *
     */
    public static double[] randomPortionList(int size) {
        double[] result = new double[size];
        for (int i = 0; i < size; i++) {
            result[i] = (int) (Math.random() * 4 + 1);
        }
        return result;
    }

    /*
     *
     */
    public static Product addRandomProductToDatabase(dbConnection db) {
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
        p.addToDatabase(db);
        // Return the product, which will now contain its ID in the database
        return p;
    }

    /*
     *
     */
    public static double addRandomOrderToDatabase(dbConnection db, String date){
        // Returns the total price of the order.
        double discount = Math.random() < 0.1 ? Math.random() * 0.3 : 0.0;
        int numProducts = (int) (Math.random() * 4) + 1;
        int[] productList = new int[numProducts];
        double subtotal = 0;

        // Add random products to the database, then add to order
        for (int i = 0; i < numProducts; i++) {
            Product p = addRandomProductToDatabase(db);
            if(p.id == -1){
                System.out.println("Error adding order to database (product ID not assigned).");
                System.exit(0);
            }
            productList[i] = p.id;
            subtotal += p.price;
        }

        return db.addOrderToDatabase(productList, discount, subtotal, date);
    }

    /*
     *
     */
    public static void populateDatabaseWithOrders(dbConnection db) {
        testGeneration.addItems(db);
        testGeneration.addProductDefs(db);
        System.out.println("Removing orders and products...");
        try {
            db.sendUpdate("DELETE FROM orders;");
            db.sendUpdate("DELETE FROM products;");
        }
        catch (Exception error) {
            error.printStackTrace();
            System.exit(0);
        }
        System.out.println("Successfully deleted.");

        double price = 0;
        int num = 0;
        for (int day = 1; day <= 30; day++) {
            int orderCount;
            if (day == 17 || day == 10) {
                orderCount = 800;
            } else {
                orderCount = 200;
            }
            String date = "2022-09-" + String.format("%2d", day).replace(" ", "0");
            System.out.println("Adding orders for " + date);
            for (int i = 0; i < orderCount; i++) {
                num += 1;
                price += addRandomOrderToDatabase(db, date);
            }
            takeInventory(db, date);

        }
    }

    public static void takeInventory(dbConnection db, String date){
        HashMap<Integer, Item> items = db.getItemHashmap();
        for(int i : items.keySet()){
            Item item = items.get(i);
            double quantity = item.quantity;
            if(quantity < item.minquantity){
                System.out.println("Restocking " + item.name);
                System.out.println(" - Current Inventory: " + quantity);
                quantity += item.minquantity * 5;
                try {
                    db.sendUpdate("UPDATE item SET quantity = " + quantity + " WHERE id = " + i + "");
                    db.sendUpdate("UPDATE item SET lastrestock = '" + date + "' WHERE id = " + i + "");
                } catch (Exception error) {
                    error.printStackTrace();
                    System.exit(0);
                }
                System.out.println(" - New Inventory: " + quantity);
            }
            try {
                db.sendUpdate("INSERT INTO inventory VALUES (" + i + ", " + quantity + ", '" + date + "')");
            } catch (Exception error) {
                error.printStackTrace();
                System.exit(0);
            }
        }
        System.out.println("Inventory taken for " + date + ", day ended.");
    }

    public static void addItems(dbConnection db) {
        System.out.println("Resetting Items...");
        try {
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
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Items Reset.");
    }

    public static void addProductDefs(dbConnection db) {
        System.out.println("Resetting ProducDefs...");
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
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("ProducDefs Reset.");
    }

}
