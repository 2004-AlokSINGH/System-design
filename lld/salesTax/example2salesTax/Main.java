package salesTax.example2salesTax;
import java.util.List;


public class Main {

    public static void main(String[] args) {

        List<Item> items = List.of(
            new Item("book",   ItemCategory.BOOK,12.49),
            new Item("music CD",  ItemCategory.OTHER,14.99),
            new Item("chocolate bar", ItemCategory.FOOD,99)
        );

        TaxCalculator taxCalculator= new TaxCalculator(
            List.of(
                new BasicSalesTax(),new ImportDutyTax()
            )
        );

        Receipt receipt = new Receipt();
        receipt.printSlip(items,taxCalculator);
    }
}
