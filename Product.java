import java.util.HashMap;

public class Product {
    public String name;
    public double price;
    private HashMap<Integer, Double> itemsAndPortions = new HashMap<>(); // Maps items to their respective portions.
    public Product(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public void addItem(int itemID, double portionSize) {
        itemsAndPortions.put(itemID, portionSize);
    }

    public void removeItem(int itemID){
        itemsAndPortions.remove(itemID);
    }

    public void toggleItem(int itemID, double portionSize, boolean add){
        if(add){
            addItem(itemID, portionSize);
        }
        else{
            removeItem(itemID);
        }
    }

    public int addToDatabase(dbConnection db){
        int[] itemList = new int[itemsAndPortions.size()];
        double[] portionList = new double[itemsAndPortions.size()];
        int i=0;
        for(int item : itemsAndPortions.keySet()){
            itemList[i] = item;
            portionList[i] = itemsAndPortions.get(item);
            i += 1;
        }
        return db.addProductToDatabase(name, price, itemList, portionList);
    }
}
