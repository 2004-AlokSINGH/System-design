package salesTax.example2salesTax;


public class BasicSalesTax implements TaxRule {

    private static final double TAX_RATE = 0.10;

    @Override
    public double calculate(Item item) {
        if (item.getCategory() == ItemCategory.BOOK ||
            item.getCategory() == ItemCategory.FOOD ||
            item.getCategory() == ItemCategory.MEDICAL) {
            return 0;
        }
        return item.getPrice() * TAX_RATE;
    }
}
