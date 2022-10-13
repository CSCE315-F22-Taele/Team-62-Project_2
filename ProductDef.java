public class ProductDef{
    public int id;
    public String name;
    public double price;
    public int[] baseItems;
    public double[] baseItemPortions;
    public int[] optionalItems;
    public double[] optionalItemPortions;

    public ProductDef(int id, String name, double price, int[] baseItems, double[] baseItemPortions, int[] optionalItems, double[] optionalItemPortions){
        this.id = id;
        this.name = name;
        this.price = price;
        this.baseItems = baseItems;
        this.baseItemPortions = baseItemPortions;
        this.optionalItems = optionalItems;
        this.optionalItemPortions = optionalItemPortions;
    }
}
