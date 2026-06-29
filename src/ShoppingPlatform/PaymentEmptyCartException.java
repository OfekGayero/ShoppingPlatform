package ShoppingPlatform;

public class PaymentEmptyCartException extends Exception {
    private static final String errorMessage = "\nCant pay with empty shopping cart\n";
    public PaymentEmptyCartException(){
        super(errorMessage);
    }
}
