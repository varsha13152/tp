package seedu.innsync.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.innsync.commons.util.AppUtil.checkArgument;

/**
 * Represents a Person's name in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidName(String)}
 */
public class Name implements Comparable<Name> {

    public static final String MESSAGE_EMPTY = "Error: Name cannot be empty.";
    public static final String MESSAGE_LENGTH = "Error: Name cannot exceed 170 characters.";

    public static final String REGEX_NOT_EMPTY = "^(?!\\s)(?=.*\\S).*$";
    public static final String REGEX_MAX_LENGTH = "^.{1,170}$";


    public final String fullName;

    /**
     * Constructs a {@code Name}.
     *
     * @param name A valid name.
     */
    public Name(String name) {
        requireNonNull(name);
        checkArgument(isValidName(name), getErrorMessage(name));
        fullName = name;
    }

    /**
     * Returns true if a given string is a valid name.
     */
    public static boolean isValidName(String test) {
        return test.matches(REGEX_NOT_EMPTY) && test.matches(REGEX_MAX_LENGTH);
    }

    /**
     * Determines the specific error message based on the invalid request name.
     */
    public static String getErrorMessage(String test) {
        if (!test.matches(REGEX_NOT_EMPTY)) {
            return MESSAGE_EMPTY;
        }
        return MESSAGE_LENGTH;
    }

    @Override
    public String toString() {
        return fullName;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Name)) {
            return false;
        }

        Name otherName = (Name) other;
        return fullName.equals(otherName.fullName);
    }

    @Override
    public int hashCode() {
        return fullName.hashCode();
    }

    @Override
    public int compareTo(Name other) {
        return this.fullName.compareTo(other.fullName); // Lexicographical order
    }

}
