/**
* Replicates/stores the item format in the database.
*/
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
