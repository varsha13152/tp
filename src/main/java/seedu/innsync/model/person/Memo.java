package seedu.innsync.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.innsync.commons.util.AppUtil.checkArgument;

/**
 * Represents a Person's Memo in the address book.
 * Guarantees: immutable; is always valid
 */
public class Memo {

    public static final String MESSAGE_CONSTRAINTS = "Memo names should not contain the '/' character, "
            + "and must not exceed 255 characters.";
    public static final String VALIDATION_REGEX = "^[^/]{0,255}$";
    public final String value;

    /**
     * Constructs an {@code Memo}.
     *
     * @param memo A valid memo.
     */
    public Memo(String memo) {
        requireNonNull(memo);
        checkArgument(isValidMemo(memo), MESSAGE_CONSTRAINTS);
        this.value = memo;
    }

    /**
     * Returns true if a given string is a valid memo format.
     */
    public static boolean isValidMemo(String test) {
        return test.matches(VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Memo // instanceof handles nulls
                && value.equals(((Memo) other).value)); // state check
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
