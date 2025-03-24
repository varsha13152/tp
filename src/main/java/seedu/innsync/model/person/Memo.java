package seedu.innsync.model.person;

import static java.util.Objects.requireNonNull;

/**
 * Represents a Person's Memo in the address book.
 * Guarantees: immutable; is always valid
 */
public class Memo {
    public final String value;

    /**
     * Constructs an {@code Memo}.
     *
     * @param memo A valid memo.
     */
    public Memo(String memo) {
        requireNonNull(memo);
        value = memo;
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
