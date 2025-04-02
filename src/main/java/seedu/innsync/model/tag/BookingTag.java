package seedu.innsync.model.tag;

import static java.util.Objects.requireNonNull;
import static seedu.innsync.commons.util.AppUtil.checkArgument;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Represents a Tag in the address book.
 * Guarantees: immutable; name is valid as declared in {@link #isValidBookingTagName(String)}
 */
public class BookingTag {

    public static final String MESSAGE_CONSTRAINTS = "Booking tags should be of the format "
            + "PROPERTY from/START_DATE to/END_DATE "
            + "where START_DATE and END_DATE are in the format yyyy-MM-dd.\n"
            + "The START_DATE must be before END_DATE.\n"
            + "PROPERTY must have 1 to 170 characters.\n";

    public static final String VALIDATION_REGEX = "\\S{1,170} from/\\d{4}-\\d{2}-\\d{2} to/\\d{4}-\\d{2}-\\d{2}";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public final String bookingTagName;
    public final String bookingTag;
    public final LocalDateTime startDate;
    public final LocalDateTime endDate;

    /**
     * Constructs a {@code BookingTag}.
     *
     * @param bookingTagName A valid tag name.
     */
    public BookingTag(String bookingTagName) {
        requireNonNull(bookingTagName);
        checkArgument(isValidBookingTagName(bookingTagName), MESSAGE_CONSTRAINTS);
        String[] split = bookingTagName.split(" from/| to/");
        this.bookingTagName = bookingTagName;
        this.bookingTag = split[0];
        this.startDate = LocalDate.parse(split[1], DATE_FORMATTER).atStartOfDay();
        this.endDate = LocalDate.parse(split[2], DATE_FORMATTER).atStartOfDay();
    }

    /**
     * Returns true if a given string is a valid tag name.
     */
    public static boolean isValidBookingTagName(String test) {
        if (test.matches(VALIDATION_REGEX)) {
            String[] split = test.split(" from/| to/");

            LocalDate startDate;
            LocalDate endDate;

            try {
                startDate = LocalDate.parse(split[1], DATE_FORMATTER);
                endDate = LocalDate.parse(split[2], DATE_FORMATTER);
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
        if (!(other instanceof BookingTag)) {
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
     * Format state as text for storage.
     */
    public String toString() {
        return '[' + bookingTagName + ']';
    }

    /**
     * Format state as text for viewing.
     */
    public String toPrettier() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yy");
        return bookingTag + " " + startDate.format(formatter) + " to " + endDate.format(formatter);
    }
}
