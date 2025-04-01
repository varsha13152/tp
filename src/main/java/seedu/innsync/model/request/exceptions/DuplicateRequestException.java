package seedu.innsync.model.request.exceptions;

/**
 * Signals that the requested operation would result in duplicate requests.
 * This exception is thrown when an attempt is made to add a request that already exists.
 */
public class DuplicateRequestException extends RuntimeException {

    /**
     * Constructs a {@code DuplicateRequestException} with a default error message.
     */
    public DuplicateRequestException() {
        super("Operation would result in duplicate tags");
    }
}
