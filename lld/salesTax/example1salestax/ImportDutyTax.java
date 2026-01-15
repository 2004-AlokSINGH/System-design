package salesTax.example1salestax;

public class ImportDutyTax extends Tax{

    private float tax;

    @Override
    public float getTax() {
        return this.tax;
    }

    public ImportDutyTax(float tax){
        this.tax=tax;
    }

   
    
}
