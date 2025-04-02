package seedu.innsync.model.request.exceptions;

/**
 * Signals that the requested operation could not find the specified request.
 * This exception is thrown when an attempt is made to access a request that does not exist.
 */
public class RequestNotFoundException extends RuntimeException {

    /**
     * Constructs a {@code RequestNotFoundException} with a default error message.
     */
    public RequestNotFoundException() {
        super("Operation would result in duplicate request");
    }
}
