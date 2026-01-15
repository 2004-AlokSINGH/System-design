package salesTax.example2salesTax;

import java.util.List;

public class Receipt {

    private double totalTaxes = 0;
    private double totalAmount = 0;

    public void printSlip(List<Item> items, TaxCalculator taxCalculator) {
        for (Item item : items) {
            double tax = taxCalculator.calculateTotalTax(item);
            double finalPrice = item.getPrice() + tax;

            totalTaxes += tax;
            totalAmount += finalPrice;

            System.out.printf("1 %s: %.2f%n", item.getName(), finalPrice);
        }

        System.out.printf("Sales Taxes: %.2f%n", totalTaxes);
        System.out.printf("Total: %.2f%n", totalAmount);
    }
}
