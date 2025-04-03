package seedu.innsync.model.person;

import static java.util.Objects.requireNonNull;

import seedu.innsync.logic.Messages;

/**
 * Represents a Person's Memo in the address book.
 * Guarantees: immutable; is always valid
 */
public class Memo {
    public static final String MESSAGE_CONSTRAINTS = "Memo can take any value and must not exceed 500 characters.";
    public static final String MESSAGE_LENGTH = String.format(Messages.MESSAGE_MAX_LENGTH_EXCEEDED,
            "Memo", 500);

    public static final String REGEX_MAX_LENGTH = "^.{0,500}$";

    public final String value;

    /**
     * Constructs an {@code Memo}.
     *
     * @param memo A valid memo.
     */
    public Memo(String memo) {
        requireNonNull(memo);
        checkValidMemo(memo);
        this.value = memo;
    }

    /**
     * Checks if a given string is a valid memo format.
     *
     * @param test The string to be validated.
     * @throws IllegalArgumentException if the string is not a valid memo format.
     */
    public static void checkValidMemo(String test) {
        requireNonNull(test);

        if (!test.matches(REGEX_MAX_LENGTH)) {
            throw new IllegalArgumentException(MESSAGE_LENGTH);
        }
    }

    /**
     * Returns true if a given string is a valid memo format.
     *
     * @param test The string to be validated.
     * @return true if the string is a valid memo format.
     */
    public static boolean isValidMemo(String test) {
        requireNonNull(test);
        try {
            checkValidMemo(test);
        } catch (IllegalArgumentException e) {
            return false;
        }
        return true;
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
