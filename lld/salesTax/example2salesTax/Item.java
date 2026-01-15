package salesTax.example2salesTax;

public class Item {
    private final String name;
    private final double price;
    private final ItemCategory category;

    public Item(String name,  ItemCategory category,double price) {
        this.name = name;
        this.price = price;
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public ItemCategory getCategory() {
        return category;
    }
}
