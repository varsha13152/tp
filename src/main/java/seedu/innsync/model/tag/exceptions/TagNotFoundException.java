package seedu.innsync.model.tag.exceptions;

/**
 * Signals that the operation will result in a tag not found.
 */
public class TagNotFoundException extends RuntimeException {
    public TagNotFoundException() {
        super("Operation would result in duplicate tags");
    }
}
