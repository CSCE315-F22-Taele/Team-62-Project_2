///item class made to replicate the exact format that is in our database
//a new item can be added through the gui with these parameters updating automatically
//another class made to reference the main class for duplicate items.

public class Item{
    public int id;
    public double quantity;
    public double minquantity;
    public String units;
    public String name;
    public Item(int id, double quantity, String units, String name, double minquantity){
        this.id = id;
        this.quantity = quantity;
        this.units = units;
        this.name = name;
        this.minquantity = minquantity;
    }
}
