// PaymentClient demonstrates the Strategy Pattern usage
// Change: Added package, added main method, added documentation, improved demonstration, added imports


package behavioral.strategy;





/**
 * Client class to demonstrate the Strategy Pattern with PaymentSystem.
 */
public class PaymentClient {
    public static void main(String[] args) {
       PaymentStrategy upi =new UpiPaymentStrategy(100);
       PaymentStrategy card= new CardPaymentStrategy();
       PaymentSystem paymentSystem = new PaymentSystem(upi);
       

       paymentSystem.performOperation(22);

       paymentSystem.setStrategy(card);
       paymentSystem.performOperation(44);
    }
}
