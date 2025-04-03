package seedu.innsync.model.tag;

import static java.util.Objects.requireNonNull;
import static seedu.innsync.commons.util.AppUtil.checkAllRules;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.util.Pair;
import seedu.innsync.commons.core.rule.Rule;
import seedu.innsync.commons.core.rule.RuleList;
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
    public static final String MESSAGE_STARTDATE_AFTER_ENDDATE = "Error: Booking tag start date must be before end date.";

    // Regex and parsing patterns
    public static final String REGEX_TOKENS = "^(?<property>.*) from/(?<startDate>.*) to/(?<endDate>.*)$";
    public static final Pattern PATTERN_TOKENS = Pattern.compile(REGEX_TOKENS);
    public static final String REGEX_NOT_EMPTY = "^.+$"; // Ensures non-empty string
    public static final String REGEX_MAX_LENGTH = "^.{1,170}$"; // Ensures length <= 170
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    // Validation rules
    private static final RuleList VALIDATION_RULES_TOKENS = new RuleList(List.of(
            Rule.ofRegex(REGEX_TOKENS, MESSAGE_CONSTRAINTS)));
    private static final RuleList VALIDATION_RULES_PROPERTY = new RuleList(List.of(
            Rule.ofRegex(REGEX_NOT_EMPTY, MESSAGE_FIELD_EMPTY),
            Rule.ofRegex(REGEX_MAX_LENGTH, MESSAGE_PROPERTY_LENGTH)));
    private static final RuleList VALIDATION_RULES_STARTDATE = new RuleList(List.of(
            Rule.ofRegex(REGEX_NOT_EMPTY, MESSAGE_STARTDATE_INVALID),
            Rule.dateRule(date -> isValidDate(date), DATE_FORMATTER, MESSAGE_STARTDATE_INVALID)));
    private static final RuleList VALIDATION_RULES_ENDDATE = new RuleList(List.of(
            Rule.ofRegex(REGEX_NOT_EMPTY, MESSAGE_ENDDATE_INVALID),
            Rule.dateRule(date -> isValidDate(date), DATE_FORMATTER, MESSAGE_ENDDATE_INVALID)));
    private static final RuleList VALIDATION_RULES_DATES = new RuleList(List.of(
            Rule.comparisonRule(
                    (Pair<LocalDate, LocalDate> dates) -> dates.getKey().isBefore(dates.getValue()),
                    MESSAGE_STARTDATE_AFTER_ENDDATE)));

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
        isValidBookingTag(bookingTagName);

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
     * @return true if the string is a valid booking tag.
     * @throws IllegalArgumentException if the string does not pass any of the validation rules.
     * @throws DateTimeParseException if the date is not in the correct format.
     */
    public static boolean isValidBookingTag(String test) throws IllegalArgumentException, DateTimeParseException {
        requireNonNull(test);

        checkAllRules(test, VALIDATION_RULES_TOKENS);

        Matcher matcher = PATTERN_TOKENS.matcher(test);
        matcher.matches();

        String property = matcher.group("property");
        String startDate = matcher.group("startDate");
        String endDate = matcher.group("endDate");

        checkAllRules(property, VALIDATION_RULES_PROPERTY);
        checkAllRules(startDate, VALIDATION_RULES_STARTDATE);
        checkAllRules(endDate, VALIDATION_RULES_ENDDATE);
        checkAllRules((Pair<LocalDate, LocalDate>) new Pair<>(
                LocalDate.parse(startDate, DATE_FORMATTER),
                LocalDate.parse(endDate, DATE_FORMATTER)), VALIDATION_RULES_DATES);

        return true;
    }

    private static boolean isValidDate(String date) throws DateTimeParseException {
        LocalDate parsedDate;

        parsedDate = LocalDate.parse(date, DATE_FORMATTER);

        // leap year check
        if (parsedDate.getDayOfMonth() != Integer.parseInt(date.substring(8, 10))) {
            throw new DateTimeParseException("Invalid date", date, 0);
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
