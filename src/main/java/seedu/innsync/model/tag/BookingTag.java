package seedu.innsync.model.tag;

import static java.util.Objects.requireNonNull;
import static seedu.innsync.commons.util.AppUtil.checkArgument;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a Tag in the address book.
 * Guarantees: immutable; name is valid as declared in {@link #isValidBookingTagName(String)}
 */
public class BookingTag {

    public static final String MESSAGE_CONSTRAINTS = "Date tag names should in the format of YYYY-MM-DD";
    public static final String VALIDATION_REGEX = "\\d{4}-\\d{2}-\\d{2}";
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public final LocalDateTime bookingTagDate;
    public final String bookingTagName;


    /**
     * Constructs a {@code Tag}.
     *
     * @param bookingTagName A valid tag name.
     */
    public BookingTag(String bookingTagName) {
        requireNonNull(bookingTagName);
        checkArgument(isValidBookingTagName(bookingTagName), MESSAGE_CONSTRAINTS);
        this.bookingTagName = bookingTagName;
        this.bookingTagDate = LocalDateTime.parse(bookingTagName, DATE_TIME_FORMATTER);
    }

    /**
     * Returns true if a given string is a valid tag name.
     */
    public static boolean isValidBookingTagName(String test) {
        return test.matches(VALIDATION_REGEX);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Tag)) {
            return false;
        }

        BookingTag otherBookingTag = (BookingTag) other;
        return bookingTagName.equals(otherBookingTag.bookingTagName);
    }

    @Override
    public int hashCode() {
        return bookingTagName.hashCode();
    }

    /**
     * Format state as text for viewing.
     */
    public String toString() {
        return '[' + bookingTagName + ']';
    }

}
