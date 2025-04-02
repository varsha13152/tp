package seedu.innsync.model.tag;

import static java.util.Objects.requireNonNull;
import static seedu.innsync.commons.util.AppUtil.checkArgument;

import java.time.LocalDate;
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

    public static final String MESSAGE_PROPERTY_EMPTY = "Error: Booking tag property should not be empty.";
    public static final String MESSAGE_PROPERTY_LENGTH = "Error: Booking tag property must not exceed 170 characters.";
    public static final String MESSAGE_STARTDATE_EMPTY = "Error: Booking tag start date should not be empty.";
    public static final String MESSAGE_STARTDATE_INVALID = "Error: Booking tag start date is invalid.";
    public static final String MESSAGE_ENDDATE_EMPTY = "Error: Booking tag end date should not be empty.";
    public static final String MESSAGE_ENDDATE_INVALID = "Error: Booking tag end date is invalid.";
    public static final String MESSAGE_STARTDATE_AFTER_ENDDATE =
            "Error: Booking tag start date must be before end date.";

    public static final String REGEX_NOT_EMPTY = "^.+$"; // Ensures non-empty string
    public static final String REGEX_MAX_LENGTH = "^.{1,170}$"; // Ensures length <= 170
    public static final String REGEX_STARTDATE = "^\\d{4}-\\d{2}-\\d{2}$"; // Ensures valid date format
    public static final String REGEX_ENDDATE = "^\\d{4}-\\d{2}-\\d{2}$"; // Ensures valid date format
    public static final String REGEX_TOKENS = " from/| to/"; // Ensures valid tokens

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private static String errorMessage = "Error: Booking tag is invalid.";

    public final String bookingTagName;
    public final LocalDate startDate;
    public final LocalDate endDate;

    /**
     * Constructs a {@code BookingTag}.
     *
     * @param bookingTag Valid booking tag parts.
     */
    public BookingTag(String bookingTagName) {
        requireNonNull(bookingTagName);
        checkArgument(isValidBookingTag(bookingTagName), errorMessage);

        String[] parts = bookingTagName.split(REGEX_TOKENS);
        this.bookingTagName = parts[0];
        this.startDate = LocalDate.parse(parts[1], DATE_FORMATTER);
        this.endDate = LocalDate.parse(parts[2], DATE_FORMATTER);
    }

    /**
     * Returns true if a given string is a valid booking tag.
     * Else sets the error message to the specific error and returns false.
     */
    public static boolean isValidBookingTag(String test) {
        if (!test.matches(REGEX_NOT_EMPTY)) {
            errorMessage = MESSAGE_PROPERTY_EMPTY;
            return false;
        }

        String[] parts = test.split(REGEX_TOKENS);
        return isValidBookingTagParts(parts);
    }

    /**
     * Returns true if a given array of strings is a valid booking tag.
     * Else sets the error message to the specific error and returns false.
     */
    private static boolean isValidBookingTagParts(String[] parts) {
        if (parts.length != 3) {
            errorMessage = MESSAGE_CONSTRAINTS;
            return false;
        }

        if (!parts[0].matches(REGEX_NOT_EMPTY)) {
            errorMessage = MESSAGE_PROPERTY_EMPTY;
            return false;
        }
        if (!parts[0].matches(REGEX_MAX_LENGTH)) {
            errorMessage = MESSAGE_PROPERTY_LENGTH;
            return false;
        }
        if (!parts[1].matches(REGEX_STARTDATE)) {
            errorMessage = MESSAGE_STARTDATE_INVALID;
            return false;
        }
        if (!parts[2].matches(REGEX_ENDDATE)) {
            errorMessage = MESSAGE_ENDDATE_INVALID;
            return false;
        }

        LocalDate startDate;
        LocalDate endDate;
        try {
            startDate = LocalDate.parse(parts[1], DATE_FORMATTER);
        } catch (DateTimeParseException e) {
            errorMessage = MESSAGE_STARTDATE_INVALID;
            return false;
        }
        try {
            endDate = LocalDate.parse(parts[2], DATE_FORMATTER);
        } catch (DateTimeParseException e) {
            errorMessage = MESSAGE_STARTDATE_INVALID;
            return false;
        }
        if (startDate.isAfter(endDate)) {
            errorMessage = MESSAGE_STARTDATE_AFTER_ENDDATE;
            return false;
        }

        return true;
    }

    public String getFullBookingTag() {
        return bookingTagName + " from/" + startDate.format(DATE_FORMATTER) + " to/" + endDate.format(DATE_FORMATTER);
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
        return getFullBookingTag().equals(otherBookingTag.getFullBookingTag());
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
        return bookingTagName + " " + startDate.format(formatter) + " to " + endDate.format(formatter);
    }
}
