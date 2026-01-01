# Strategy Pattern Example - Payment System

This project demonstrates the **Strategy Design Pattern** in Java using a payment system example.

## Structure

- `PaymentStrategy`: Interface for payment methods.
- `UpiPaymentStrategy`: Implements payment via UPI.
- `CardPaymentStrategy`: Implements payment via Card.
- `PaymentSystem`: Context class that uses a `PaymentStrategy`.
- `PaymentClient`: Demo class with `main` method.

## How it works

- The `PaymentSystem` is initialized with a payment strategy (e.g., UPI).
- You can change the strategy at runtime using `setStrategy`.
- The `performOperation` method delegates payment to the current strategy.

## Example Output

```
performing operation
go to passed strategy
paying using upi
price is 500
performing operation
go to passed strategy
paying using card
price is 1000
```

## How to Run

1. Compile all `.java` files in the `strategy` package.
2. Run `PaymentClient`.

```sh
javac strategy/*.java
java strategy.PaymentClient
```