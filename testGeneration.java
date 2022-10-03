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

  public static void addRandomProductToDatabase(dbConnection db){
     String items[] = {"Lemon Chicken","Gyro Meat","Falafel","Onions","Cauliflower","Peppers",
              "Olives","Couscous","Slaw","Tomatoes","Cucumbers","Hummus","Jalepeno Feta Dressing",
              "Vinagrette Dressing","Tahini Dressing","Yogurt Dill Dressing","Spicy Hummus",
              "Tzatziki Sauce","Harissa Yogurt"};
      String names[] = {"Grain Bowl", "Salad", "Pita", "Greens and Grains"};

      int itemsInProduct = (int)(Math.random()*3)+4;
      db.addProductToDatabase(
        names[(int)(Math.random() * (4))],
        7.69,
        randomItemList(itemsInProduct),
        randomPortionList(itemsInProduct)
      );
  }
}
