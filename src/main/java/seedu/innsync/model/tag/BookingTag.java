package seedu.innsync.model.tag;

import static java.util.Objects.requireNonNull;
import static seedu.innsync.commons.util.AppUtil.checkArgument;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Represents a Tag in the address book.
 * Guarantees: immutable; name is valid as declared in {@link #isValidBookingTagName(String)}
 */
public class BookingTag {

    public static final String MESSAGE_CONSTRAINTS = "Booking tags should be of the format "
            + "{property} from/{start-date} to/{end-date} "
            + "where start-date and end-date are in the format yyyy-MM-dd. "
            + "The start-date should be before the end-date.";
    public static final String VALIDATION_REGEX = ".* from/\\d{4}-\\d{2}-\\d{2} to/\\d{4}-\\d{2}-\\d{2}";
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public final String bookingTagName;
    public final LocalDateTime startDate;
    public final LocalDateTime endDate;

    /**
     * Constructs a {@code Tag}.
     *
     * @param bookingTagName A valid tag name.
     */
    public BookingTag(String bookingTagName) {
        requireNonNull(bookingTagName);
        checkArgument(isValidBookingTagName(bookingTagName), MESSAGE_CONSTRAINTS);

        String[] split = bookingTagName.split(" from/| to/");
        this.bookingTagName = bookingTagName;
        this.startDate = LocalDateTime.parse(split[1], DATE_TIME_FORMATTER);
        this.endDate = LocalDateTime.parse(split[2], DATE_TIME_FORMATTER);
    }

    /**
     * Returns true if a given string is a valid tag name.
     */
    public static boolean isValidBookingTagName(String test) {
        if (test.matches(VALIDATION_REGEX)) {
            String[] split = test.split(" from/| to/");

            LocalDateTime startDate;
            LocalDateTime endDate;

            try {
                startDate = LocalDateTime.parse(split[1], DATE_TIME_FORMATTER);
                endDate = LocalDateTime.parse(split[2], DATE_TIME_FORMATTER);
            } catch (DateTimeParseException e) {
                return false;
            }

            return startDate.isBefore(endDate);
        } else {
            return false;
        }
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
