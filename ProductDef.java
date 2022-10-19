
/**
* Contains information on a Product Definition (ProductDef) that mirrors the database format.
* Product Definitions contain menu information on a type of product.
*/
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
