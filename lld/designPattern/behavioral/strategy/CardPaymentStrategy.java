// CardPaymentStrategy implements PaymentStrategy (Strategy Pattern)
// Change: Added package, made class public, added documentation

package behavioral.strategy;

/**
 * Concrete strategy for card payments.
 */
public class CardPaymentStrategy implements PaymentStrategy {

    @Override
    public void paymentMethod(int amt) {
        System.out.println("paying using card");
        System.out.println("price is " + amt);
    }
}