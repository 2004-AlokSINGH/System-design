package salesTax.example2salesTax;

import java.util.List;

public class TaxCalculator {

    private final List<TaxRule> taxRules;

    public TaxCalculator(List<TaxRule> taxRules){
        this.taxRules=taxRules;
    }


    public double calculateTotalTax(Item item) {
        double totalTax = 0;
        for (TaxRule rule : taxRules) {
            totalTax += rule.calculate(item);
        }
        return roundUpToNearestFiveCents(totalTax);
    }

    private double roundUpToNearestFiveCents(double tax) {
        return Math.ceil(tax * 20) / 20.0;
    }
    
}
