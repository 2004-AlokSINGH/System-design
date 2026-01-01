// PaymentStrategy interface for different payment methods (Strategy Pattern)

package behavioral.strategy;

/**
 * Strategy interface for payment methods.
 */
public interface PaymentStrategy {
    void paymentMethod(int amt);
}
