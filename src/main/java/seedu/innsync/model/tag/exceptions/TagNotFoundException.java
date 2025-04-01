package seedu.innsync.model.tag.exceptions;

public class TagNotFoundException extends RuntimeException {
    public TagNotFoundException() {
        super("Operation would result in duplicate tags");
    }
}
