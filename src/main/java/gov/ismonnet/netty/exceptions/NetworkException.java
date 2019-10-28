package gov.ismonnet.netty.exceptions;

/**
 * Exception raised if anything goes wrong inside the netty pipeline
 *
 * @author Ferlo
 */
public class NetworkException extends RuntimeException {

    public NetworkException() {
    }

    public NetworkException(String message) {
        super(message);
    }

    public NetworkException(String message, Throwable cause) {
        super(message, cause);
    }

    public NetworkException(Throwable cause) {
        super(cause);
    }

    public NetworkException(String message,
                            Throwable cause,
                            boolean enableSuppression,
                            boolean writableStackTrace) {

        super(message, cause, enableSuppression, writableStackTrace);
    }
}
