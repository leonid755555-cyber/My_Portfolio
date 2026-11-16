package imhere_backend.businessLogic.exceptions;

public class CustomException extends RuntimeException {
    public CustomException(String message) {
        super(message, null);
    }
}