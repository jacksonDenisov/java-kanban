package Exceptions;

public class ManagerTimeAvailableException extends RuntimeException {
    public ManagerTimeAvailableException(final String message) {
        super(message);
    }

    public ManagerTimeAvailableException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public ManagerTimeAvailableException(final Throwable cause) {
        super(cause);
    }

}
