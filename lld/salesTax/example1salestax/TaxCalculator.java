package salesTax.example1salestax;

import java.util.ArrayList;
import java.util.List;

public class TaxCalculator {

    

    public static void main(String[] args) {
        Book b1= new Book(100);
        List<SaleItem> excludedItems=new ArrayList<>();
        excludedItems.add(b1);

        List<SaleItem> allItems=new ArrayList<>();
        allItems.add(b1);

        SalesTax sl=new SalesTax(10);
        SalesTax sl2=new SalesTax(5);

        List<Tax> taxs=new ArrayList<>();
        taxs.add(sl2);
        taxs.add(sl);


        

        for(SaleItem item:allItems){
            double price=0;
            for (Tax t:taxs){
                price+=t.calculateTaxPrice(item.getPrice());
            }
            System.out.println(price);
        }

    }

    
}