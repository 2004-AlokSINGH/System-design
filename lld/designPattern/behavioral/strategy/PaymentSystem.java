// PaymentSystem context class for Strategy Pattern
// Change: Added package, added documentation, improved formatting

package behavioral.strategy;

/**
 * Context class that uses a PaymentStrategy.
 */
public class PaymentSystem {

    private PaymentStrategy strategy;

    public PaymentSystem(PaymentStrategy payStrategy) {
        this.strategy = payStrategy;
    }

    public void setStrategy(PaymentStrategy newStrategy) {
        this.strategy = newStrategy;
    }

    public void performOperation(int amount) {
        System.out.println("performing operation");
        System.out.println("go to passed strategy");
        this.strategy.paymentMethod(amount);
    }
}