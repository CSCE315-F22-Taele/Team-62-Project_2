import java.util.HashMap;

/**
* Contains information about the product entity that mirrors the format stored on the database.
*/
public class Product {
    public String name;
    public double price;
    private HashMap<Integer, Double> itemsAndPortions = new HashMap<>(); // Maps items to their respective portions.
    public int id = -1;

    /**
     * This creates a Product object
     *
     * @author Kevin
     * @param name This is the product name
	 * @param price This is the product price
     */
    public Product(String name, double price) {
        this.name = name;
        this.price = price;
    }
    /**
     * This adds an item to a product
     *
     * @author Kevin
     * @param itemID This is the id of item to be added
	 * @param portionSize This is the portion size of the item
     */
    public void addItem(int itemID, double portionSize) {
        itemsAndPortions.put(itemID, portionSize);
    }
    /**
     * This adds removes an item
     *
     * @author Kevin
     * @param itemID This is the id of item to be removed
     */
    public void removeItem(int itemID){
        itemsAndPortions.remove(itemID);
    }
    /**
     * This toggles whether an item should be added or removed
     *
     * @author Kevin
     * @param itemID This is the idem of item to be added or removed
	 * @param portionSize This is the portion size of the item
     * @param add Whether an item should be added or removed
     */
    public void toggleItem(int itemID, double portionSize, boolean add){
        if(add){
            addItem(itemID, portionSize);
        }
        else{
            removeItem(itemID);
        }
    }
    /**
     * This adds an item to a product
     *
     * @author Kevin
     * @param db This is holds the connection to database.
	 * @param date This is date of the sale
     * @param orderId Id of order that holds the product
     * @return Return the id of the added product
     */
    public int addToDatabase(dbConnection db, String date, int orderId){
        int[] itemList = new int[itemsAndPortions.size()];
        double[] portionList = new double[itemsAndPortions.size()];
        int i=0;
        for(int item : itemsAndPortions.keySet()){
            itemList[i] = item;
            portionList[i] = itemsAndPortions.get(item);
            i += 1;
        }
        this.id = db.addProductToDatabase(name, price, itemList, portionList, date, orderId);
        return this.id;
    }
}
