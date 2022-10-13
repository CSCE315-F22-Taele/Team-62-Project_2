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
    public static int addRandomProductToDatabase(dbConnection db) {
        String[] items = {"Rice Pilaf", "White Rice", "Buttered Chicken", "Lemon Chicken", "Gyro Meat", 
        "Falafel", "Onions", "Cauliflower", "Peppers","Olives", "Couscous", "Slaw", "Tomatoes", 
        "Cucumbers", "Hummus", "Jalepeno Feta Dressing","Vinagrette Dressing", "Tahini Dressing", 
        "Yogurt Dill Dressing", "Spicy Hummus","Tzatziki Sauce", "Harissa Yogurt", "drink", "2 falafels",
        "Hummus & Pita", "Vegan Box", "Garlic Fries", "Pita"};

        String[] names = {"Grain Bowl", "Salad", "Pita", "Greens and Grains"};

        int itemsInProduct = (int) (Math.random() * 3) + 4;
        return db.addProductToDatabase(
                names[(int) (Math.random() * (4))],
                7.69,
                randomItemList(itemsInProduct),
                randomPortionList(itemsInProduct)
        );
    }

    /*
     * 
     */
    public static double addRandomOrderToDatabase(dbConnection db, String date) {
        // Returns the total price of the order.
        // Assume all products are 7.69
        double discount = Math.random() < 0.1 ? Math.random() * 0.3 : 0.0;
        int numProducts = (int) (Math.random() * 4) + 1;
        int[] productList = new int[numProducts];
        double subtotal = 7.69 * numProducts;

        // Add random products to the database, then add to order
        for (int i = 0; i < numProducts; i++) {
            int id = addRandomProductToDatabase(db);
            productList[i] = id;
        }

        return db.addOrderToDatabase(productList, discount, subtotal, date);
    }

    /*
     * 
     */
    public static void populateDatabaseWithOrders(dbConnection db) {
        double price = 0;
        int num = 0;
        for (int day = 9; day <= 30; day++) {
            int orderCount;
            if (day == 24 || day == 17) {
                orderCount = 800;
            } else {
                orderCount = 200;
            }
            for (int i = 0; i < orderCount; i++) {
                num += 1;
                price += addRandomOrderToDatabase(db, "2022-08-" + String.format("%2d", day).replace(" ", "0"));
            }
        }
    }

}
