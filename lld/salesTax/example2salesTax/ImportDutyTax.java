package salesTax.example2salesTax;
public class ImportDutyTax implements TaxRule {

    private static final double TAX_RATE = 0.05;

    @Override
    public double calculate(Item item) {
        
        return item.getPrice() * TAX_RATE;
    }
}
