package salesTax.example1salestax;

public class Book implements SaleItem{
    private double price;

    public Book(double price){
        this.price=price;
    }

    @Override
    public double getPrice() {
        return this.price;
        
    }
    
}
