package seedu.innsync.model.tag;

import static java.util.Objects.requireNonNull;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import seedu.innsync.logic.Messages;

/**
 * Represents a Tag in the address book.
 * Guarantees: immutable; name is valid as declared in
 * {@link #isValidBookingTagName(String)}
 */
public class BookingTag {

    // Error messages
    public static final String MESSAGE_CONSTRAINTS = "Booking tags should be of the format "
            + "PROPERTY from/START_DATE to/END_DATE "
            + "where START_DATE and END_DATE are in the format yyyy-MM-dd.\n"
            + "The START_DATE must be before END_DATE.\n"
            + "PROPERTY must have 1 to 170 characters.\n";

    public static final String MESSAGE_FIELD_EMPTY = String.format(Messages.MESSAGE_EMPTY_FIELD,
            "No booking tag fields");
    public static final String MESSAGE_PROPERTY_LENGTH = String.format(Messages.MESSAGE_MAX_LENGTH_EXCEEDED,
            "Booking tag", 170);
    public static final String MESSAGE_STARTDATE_INVALID = String.format(Messages.MESSAGE_EMPTY_FIELD, "Start date");
    public static final String MESSAGE_ENDDATE_INVALID = String.format(Messages.MESSAGE_EMPTY_FIELD, "End date");
    public static final String MESSAGE_STARTDATE_AFTER_ENDDATE =
            "Error: Booking tag start date must be before end date.";

    // Regex and parsing patterns
    public static final String REGEX_TOKENS = "^(?<property>.*) from/(?<startDate>.*) to/(?<endDate>.*)$";
    public static final Pattern PATTERN_TOKENS = Pattern.compile(REGEX_TOKENS);
    public static final String REGEX_NOT_EMPTY = "^.+$"; // Ensures non-empty string
    public static final String REGEX_MAX_LENGTH = "^.{1,170}$"; // Ensures length <= 170
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    // Booking tag properties
    public final Matcher matcher;
    public final String bookingTagName;
    public final LocalDate startDate;
    public final LocalDate endDate;

    /**
     * Constructs a {@code BookingTag}.
     *
     * @param bookingTagName Valid booking tag parts.
     */
    public BookingTag(String bookingTagName) {
        requireNonNull(bookingTagName);
        checkValidBookingTag(bookingTagName);

        this.matcher = PATTERN_TOKENS.matcher(bookingTagName);
        this.matcher.matches();
        String property = matcher.group("property");
        String startDate = matcher.group("startDate");
        String endDate = matcher.group("endDate");

        this.bookingTagName = property;
        this.startDate = LocalDate.parse(startDate, DATE_FORMATTER);
        this.endDate = LocalDate.parse(endDate, DATE_FORMATTER);
    }

    /**
     * Returns true if a given string is a valid booking tag.
     * Else sets the error message to the specific error and returns false.
     *
     * @param test The string to be validated.
     */
    public static void checkValidBookingTag(String test) {
        requireNonNull(test);
        Matcher matcher = PATTERN_TOKENS.matcher(test);

        String property;
        String startDate;
        String endDate;

        try {
            matcher.matches();
            property = matcher.group("property");
            startDate = matcher.group("startDate");
            endDate = matcher.group("endDate");
        } catch (IllegalStateException e) {
            throw new IllegalArgumentException(MESSAGE_CONSTRAINTS);
        }

        if (!property.matches(REGEX_NOT_EMPTY)) {
            throw new IllegalArgumentException(MESSAGE_FIELD_EMPTY);
        }
        if (!property.matches(REGEX_MAX_LENGTH)) {
            throw new IllegalArgumentException(MESSAGE_PROPERTY_LENGTH);
        }

        if (!startDate.matches(REGEX_NOT_EMPTY)) {
            throw new IllegalArgumentException(MESSAGE_STARTDATE_INVALID);
        }
        if (!endDate.matches(REGEX_NOT_EMPTY)) {
            throw new IllegalArgumentException(MESSAGE_ENDDATE_INVALID);
        }
        try {
            checkValidDate(startDate);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException(MESSAGE_STARTDATE_INVALID);
        }
        try {
            checkValidDate(endDate);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException(MESSAGE_ENDDATE_INVALID);
        }

        if (LocalDate.parse(startDate, DATE_FORMATTER).isAfter(LocalDate.parse(endDate, DATE_FORMATTER))) {
            throw new IllegalArgumentException(MESSAGE_STARTDATE_AFTER_ENDDATE);
        }
    }

    /**
     * Returns true if a given string is a valid booking tag.
     *
     * @param test The string to be validated.
     * @return true if the string is a valid booking tag.
     */
    public static boolean isValidBookingTag(String test) {
        requireNonNull(test);
        try {
            checkValidBookingTag(test);
        } catch (IllegalArgumentException e) {
            return false;
        }
        return true;
    }

    private static void checkValidDate(String date) {
        LocalDate parsedDate;

        parsedDate = LocalDate.parse(date, DATE_FORMATTER);

        // check if parser rounded down the date
        if (parsedDate.getDayOfMonth() != Integer.parseInt(date.substring(8, 10))) {
            throw new IllegalArgumentException();
        }
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
