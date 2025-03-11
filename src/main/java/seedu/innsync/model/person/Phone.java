package seedu.innsync.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.innsync.commons.util.AppUtil.checkArgument;

/**
 * Represents a Person's phone number in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidPhone(String)}
 */
public class Phone {


    public static final String MESSAGE_CONSTRAINTS = """
            Phone numbers should be in format +[COUNTRY_CODE] [NUMBER],
            the country code must be valid and the number should be
            at least 7 digits long and at most 15 digits long.
            """;
    public static final String VALIDATION_REGEX = "\\+\\d{1,4}\\s(?:\\d\\s?){6,14}\\d";
    public final String value;

    /**
     * Constructs a {@code Phone}.
     *
     * @param phone A valid phone number.
     */
    public Phone(String phone) {
        requireNonNull(phone);
        checkArgument(isValidPhone(phone), MESSAGE_CONSTRAINTS);
        this.value = phone;
    }

    /**
     * Returns true if a given string is a valid phone number.
     */
    public static boolean isValidPhone(String test) {
        // String[] parsed = test.split(" ", 2);
        return test.matches(VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return this.value;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Phone)) {
            return false;
        }

        Phone otherPhone = (Phone) other;
        return value.replace(" ", "").equals(otherPhone.value.replace(" ", ""));
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}
