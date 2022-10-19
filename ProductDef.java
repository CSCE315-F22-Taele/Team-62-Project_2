
//Productdef is a class that takes in all the parameters for the item stored from the menu
//This includes the id, name, price, base item/portions but also the includes the optional amount
//as shown in Pom & Honey's menu
//another class made to reference each product order
public class ProductDef {
    public int id;
    public String name;
    public double price;
    public Integer[] baseItems;
    public Double[] baseItemPortions;
    public Integer[] optionalItems;
    public Double[] optionalItemPortions;

    public ProductDef(int id, String name, double price, Integer[] baseItems, Double[] baseItemPortions, Integer[] optionalItems, Double[] optionalItemPortions){
        this.id = id;
        this.name = name;
        this.price = price;
        this.baseItems = baseItems;
        this.baseItemPortions = baseItemPortions;
        this.optionalItems = optionalItems;
        this.optionalItemPortions = optionalItemPortions;
    }
}
