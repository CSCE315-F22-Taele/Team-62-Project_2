public class addItems{
  public static void addItems(String args[]){
    dbConnection db = new dbConnection();
    db.connect();
    try{
        db.sendCommand("INSERT INTO item VALUES (4, 10, 'lbs', 'Lemon Chicken')");
        db.sendCommand("INSERT INTO item VALUES (5, 32, 'lbs', 'Gyro Meat')");
        db.sendCommand("INSERT INTO item VALUES (6, 14, 'lbs', 'Falafel')");
        db.sendCommand("INSERT INTO item VALUES (7, 70, 'lbs', 'Onions')");
        db.sendCommand("INSERT INTO item VALUES (8, 70, 'lbs', 'Cauliflower')");
        db.sendCommand("INSERT INTO item VALUES (9, 70, 'lbs', 'Peppers')");
        db.sendCommand("INSERT INTO item VALUES (10, 70, 'lbs', 'Olives')");
        db.sendCommand("INSERT INTO item VALUES (11, 70, 'lbs', 'Couscous')");
        db.sendCommand("INSERT INTO item VALUES (12, 70, 'lbs', 'Slaw')");
        db.sendCommand("INSERT INTO item VALUES (13, 70, 'lbs', 'Tomatoes')");
        db.sendCommand("INSERT INTO item VALUES (14, 70, 'lbs', 'Cucumbers')");
        db.sendCommand("INSERT INTO item VALUES (15, 70, 'lbs', 'Hummus')");
        db.sendCommand("INSERT INTO item VALUES (16, 70, 'lbs', 'Jalepeno Feta Dressing')");
        db.sendCommand("INSERT INTO item VALUES (17, 70, 'lbs', 'Vinagrette Dressing')");
        db.sendCommand("INSERT INTO item VALUES (18, 70, 'lbs', 'Tahini Dressing')");
        db.sendCommand("INSERT INTO item VALUES (19, 70, 'lbs', 'Yogurt Dill Dressing')");
        db.sendCommand("INSERT INTO item VALUES (20, 70, 'lbs', 'Spicy Hummus')");
        db.sendCommand("INSERT INTO item VALUES (21, 70, 'lbs', 'Tzatziki Sauce')");
        db.sendCommand("INSERT INTO item VALUES (22, 70, 'lbs', 'Harissa Yogurt')");
    }
    catch (Exception e){
        e.printStackTrace();
        System.err.println(e.getClass().getName()+": "+e.getMessage());
        System.exit(0);        
    }
    db.close();
  }
}
