public class testGeneration {
  public static int[] randomItemList(int size){
      int result[] = new int[size];
      for(int i = 0; i<size;i++){
          result[i] = (int)(Math.random() * size);
      }
      return result;
  }

  public static double[] randomPortionList(int size){
      double result[] = new double[size];
      for(int i = 0; i<size;i++){
          result[i] = (int)(Math.random() * 4 + 1);
      }
      return result;
  }

  public static int addRandomProductToDatabase(dbConnection db){
     String items[] = {"Lemon Chicken","Gyro Meat","Falafel","Onions","Cauliflower","Peppers",
              "Olives","Couscous","Slaw","Tomatoes","Cucumbers","Hummus","Jalepeno Feta Dressing",
              "Vinagrette Dressing","Tahini Dressing","Yogurt Dill Dressing","Spicy Hummus",
              "Tzatziki Sauce","Harissa Yogurt"};
      String names[] = {"Grain Bowl", "Salad", "Pita", "Greens and Grains"};

      int itemsInProduct = (int)(Math.random()*3)+4;
      return db.addProductToDatabase(
        names[(int)(Math.random() * (4))],
        7.69,
        randomItemList(itemsInProduct),
        randomPortionList(itemsInProduct)
      );
  }

  public static double addRandomOrderToDatabase(dbConnection db, String date){
      // Returns the total price of the order.
      // Assume all products are 7.69
      double discount = Math.random() < 0.1 ? Math.random() * 0.3 : 0.0;
      int numProducts = (int)(Math.random()*4) + 1;
      int[] productList = new int[numProducts];
      double subtotal = 7.69 * numProducts;

      // Add random products to the database, then add to order
      for(int i=0;i<numProducts;i++){
          int id = addRandomProductToDatabase(db);
          productList[i] = id;
      }
      
      return db.addOrderToDatabase(productList, discount, subtotal, date);
  }

}
