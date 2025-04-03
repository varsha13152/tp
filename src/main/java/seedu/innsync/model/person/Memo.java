package seedu.innsync.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.innsync.commons.util.AppUtil.checkArgument;

/**
 * Represents a Person's Memo in the address book.
 * Guarantees: immutable; is always valid
 */
public class Memo {


    public static final String MESSAGE_LENGTH = "Error: Memo value must not exceed 500 characters.";
    public static final String REGEX_MAX_LENGTH = "^.{0,500}$";
    public static final String MESSAGE_CONSTRAINTS = "Memo can take any value and must not exceed 500 characters.";
    public final String value;

    /**
     * Constructs an {@code Memo}.
     *
     * @param memo A valid memo.
     */
    public Memo(String memo) {
        requireNonNull(memo);
        checkArgument(isValidMemo(memo), MESSAGE_LENGTH);
        this.value = memo;
    }

    /**
     * Returns true if a given string is a valid memo format.
     */
    public static boolean isValidMemo(String test) {
        return test.matches(REGEX_MAX_LENGTH);
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
