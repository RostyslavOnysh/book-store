package spring.boot.bookstore.exception;

public class MessageSenderException extends RuntimeException {
    public MessageSenderException(String message, Throwable cause) {
        super(message, cause);
    }
}
