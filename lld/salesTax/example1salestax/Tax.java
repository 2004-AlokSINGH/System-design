package salesTax.example1salestax;

public abstract class Tax {

    abstract float getTax();

    public double calculateTaxPrice(double amt){
        return (getTax()*amt)/100;
    }

   
    
}
