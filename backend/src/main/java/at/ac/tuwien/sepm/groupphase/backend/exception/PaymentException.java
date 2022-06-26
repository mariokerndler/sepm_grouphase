package at.ac.tuwien.sepm.groupphase.backend.exception;

public class PaymentException extends RuntimeException {

    public PaymentException() {
    }

    public PaymentException(String message) {
        super(message);
    }

    public PaymentException(String message, Throwable cause) {
        super(message, cause);
    }

    public PaymentException(Exception e) {
        super(e);
    }
}
