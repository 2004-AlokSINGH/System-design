package salesTax.example1salestax;

public class SalesTax extends Tax{
    private float taxPercent;

    @Override
    public float getTax() {
        return this.taxPercent;
    }   

    public SalesTax(float tax){
        this.taxPercent=tax;
    }    
    
}
