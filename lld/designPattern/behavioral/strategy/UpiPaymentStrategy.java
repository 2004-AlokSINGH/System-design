package behavioral.strategy;

public class UpiPaymentStrategy implements PaymentStrategy{

    private int amount;

    UpiPaymentStrategy(int amt){
        this.amount=amt;
        System.out.println("processing the upi payement of Rs. "+this.amount);
    }

    @Override
    public void paymentMethod(int amt) {
        System.out.println("the extra charge for payemnt using upi Rs. "+this.amount);

        System.out.println("paying using upi");
        System.out.println("price is " + amt);
    }

}
