package seedu.innsync.logic.commands;

import static java.util.Objects.requireNonNull;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.logging.Logger;

import seedu.innsync.commons.util.ToStringBuilder;
import seedu.innsync.logic.Messages;
import seedu.innsync.logic.commands.exceptions.CommandException;
import seedu.innsync.model.Model;
import seedu.innsync.model.person.Person;
import seedu.innsync.model.tag.BookingTag;

/**
 * Finds and lists persons in address book whose details match the given keywords.
 * Supports searching by multiple fields simultaneously: name, phone, email, address, tags, booking tags, memo.
 * Uses OR logic between different search types - a person matches if they match ANY of the search criteria.
 * Uses OR logic between keywords of the same type - a person matches a search type if they match ANY of its keywords.
 */
public class FindCommand extends Command {

    public static final String COMMAND_WORD = "find";
    private static final Logger logger = Logger.getLogger(FindCommand.class.getName());

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds persons whose fields contain any of "
            + "the specified keywords (case-insensitive) and displays them as a list.\n"
            + "Parameters: [n/KEYWORD]... [p/KEYWORD]... [e/KEYWORD]... [a/KEYWORD]... [t/KEYWORD]... "
            + "[m/KEYWORD]... [bd/DATE]... [bp/KEYWORD]...\n"
            + "Example: " + COMMAND_WORD + " n/John n/Doe p/91234567 e/example.com t/friend";

    private final Map<SearchType, List<String>> searchCriteria;

    /**
     * Enum to represent the type of search being performed
     */
    public enum SearchType {
        NAME, PHONE, EMAIL, ADDRESS, TAG, BOOKING_DATE, BOOKING_PROPERTY, MEMO
    }

    /**
     * Constructor for searching by multiple fields
     */
    public FindCommand(Map<SearchType, List<String>> searchCriteria) {
        requireNonNull(searchCriteria);
        this.searchCriteria = searchCriteria;
    }

    public Map<SearchType, List<String>> getSearchCriteria() {
        return this.searchCriteria;
    }

    /**
     * Returns a predicate based on the search criteria.
     * This method is primarily used for testing purposes.
     * @return a predicate that can be used to filter persons based on the search criteria
     */
    public Predicate<Person> getPredicate() {
        return this::matchesAnySearchCriteria;
    }

    /**
     * Checks if a person matches any of the search criteria (OR logic between different search types)
     *
     * @param person The person to check against search criteria
     * @return true if the person matches any search criteria, false otherwise
     */
    private boolean matchesAnySearchCriteria(Person person) {
        requireNonNull(person);

        return searchCriteria.entrySet().stream()
                .anyMatch(entry -> matchesAnyKeyword(person, entry.getKey(), entry.getValue()));
    }

    /**
     * Checks if a person matches any of the keywords for a specific field type
     *
     * @param person The person to check
     * @param type The search type (NAME, PHONE, etc.)
     * @param keywords The list of keywords to check against
     * @return true if the person matches any of the keywords for the given field, false otherwise
     */
    private boolean matchesAnyKeyword(Person person, SearchType type, List<String> keywords) {
        requireNonNull(person);
        requireNonNull(type);
        requireNonNull(keywords);

        if (keywords.isEmpty()) {
            return false;
        }

        return keywords.stream()
                .filter(Objects::nonNull)
                .anyMatch(keyword -> matchField(person, keyword.toLowerCase(), type));
    }

    /**
     * Matches a person against a keyword for a specific search type
     *
     * @param person The person to check
     * @param keyword The (lowercase) keyword to match against
     * @param searchType The type of field to check (NAME, PHONE, etc.)
     * @return true if the person's field contains the keyword, false otherwise
     */
    private boolean matchField(Person person, String keyword, SearchType searchType) {
        requireNonNull(person);
        requireNonNull(keyword);
        requireNonNull(searchType);

        if (keyword.isEmpty()) {
            return false;
        }

        switch (searchType) {
        case NAME:
            return matchNameField(person, keyword);
        case PHONE:
            return matchPhoneField(person, keyword);
        case EMAIL:
            return matchEmailField(person, keyword);
        case ADDRESS:
            return matchAddressField(person, keyword);
        case TAG:
            return matchTagField(person, keyword);
        case BOOKING_DATE:
            return matchBookingDateField(person, keyword);
        case BOOKING_PROPERTY:
            return matchBookingPropertyField(person, keyword);
        case MEMO:
            return matchMemoField(person, keyword);
        default:
            logger.warning("Unknown SearchType encountered: " + searchType);
            return false;
        }
    }

    /**
     * Matches a person's name against a keyword
     *
     * @param person The person to check
     * @param keyword The (lowercase) keyword to match against
     * @return true if the person's name contains the keyword, false otherwise
     */
    private boolean matchNameField(Person person, String keyword) {
        return person.getName() != null && person.getName().fullName != null
                && person.getName().fullName.toLowerCase().contains(keyword);
    }

    /**
     * Matches a person's phone against a keyword
     *
     * @param person The person to check
     * @param keyword The (lowercase) keyword to match against
     * @return true if the person's phone contains the keyword, false otherwise
     */
    private boolean matchPhoneField(Person person, String keyword) {
        return person.getPhone() != null
                && person.getPhone().value != null
                && person.getPhone().value.toLowerCase().contains(keyword);
    }

    /**
     * Matches a person's email against a keyword
     *
     * @param person The person to check
     * @param keyword The (lowercase) keyword to match against
     * @return true if the person's email contains the keyword, false otherwise
     */
    private boolean matchEmailField(Person person, String keyword) {
        return person.getEmail() != null
                && person.getEmail().value != null
                && person.getEmail().value.toLowerCase().contains(keyword);
    }

    /**
     * Matches a person's address against a keyword
     *
     * @param person The person to check
     * @param keyword The (lowercase) keyword to match against
     * @return true if the person's address contains the keyword, false otherwise
     */
    private boolean matchAddressField(Person person, String keyword) {
        return person.getAddress() != null && person.getAddress().value != null
                && person.getAddress().value.toLowerCase().contains(keyword);
    }

    /**
     * Matches a person's tags against a keyword
     *
     * @param person The person to check
     * @param keyword The (lowercase) keyword to match against
     * @return true if any of the person's tags contain the keyword, false otherwise
     */
    private boolean matchTagField(Person person, String keyword) {
        if (person.getTags() == null) {
            return false;
        }

        return person.getTags().stream()
                .filter(Objects::nonNull)
                .anyMatch(tag -> tag.tagName != null
                        && tag.tagName.toLowerCase().contains(keyword));
    }

    /**
     * Matches a person's booking tags based on date against a keyword
     *
     * @param person The person to check
     * @param keyword The (lowercase) keyword to match against (date string)
     * @return true if any of the person's booking tags include the date, false otherwise
     */
    private boolean matchBookingDateField(Person person, String keyword) {
        requireNonNull(person);
        requireNonNull(keyword);

        if (keyword.isEmpty() || person.getBookingTags() == null || person.getBookingTags().isEmpty()) {
            return false;
        }

        return person.getBookingTags().stream()
                .filter(Objects::nonNull)
                .anyMatch(bookingTag -> isDateInBookingPeriod(keyword, bookingTag));
    }

    /**
     * Matches a person's booking tags based on property name against a keyword
     *
     * @param person The person to check
     * @param keyword The (lowercase) keyword to match against
     * @return true if any of the person's booking property names contain the keyword, false otherwise
     */
    private boolean matchBookingPropertyField(Person person, String keyword) {
        requireNonNull(person);
        requireNonNull(keyword);

        if (keyword.isEmpty() || person.getBookingTags() == null || person.getBookingTags().isEmpty()) {
            return false;
        }

        return person.getBookingTags().stream()
                .filter(Objects::nonNull)
                .anyMatch(bookingTag -> bookingTag.bookingTagName != null
                        && bookingTag.bookingTagName.toLowerCase().contains(keyword.toLowerCase()));
    }

    /**
     * Matches a person's memo against a keyword
     *
     * @param person The person to check
     * @param keyword The (lowercase) keyword to match against
     * @return true if the person's memo contains the keyword, false otherwise
     */
    private boolean matchMemoField(Person person, String keyword) {
        requireNonNull(person);
        requireNonNull(keyword);

        if (keyword.isEmpty() || person.getMemo() == null
                || person.getMemo().value == null || person.getMemo().value.isEmpty()) {
            return false;
        }

        return person.getMemo().value.toLowerCase().contains(keyword);
    }

    /**
     * Checks if a date is within a booking period
     *
     * @param dateString The date string to check in yyyy-MM-dd format
     * @param bookingTag The booking tag to check against
     * @return true if the date is within the booking period, false otherwise
     */
    private boolean isDateInBookingPeriod(String dateString, BookingTag bookingTag) {
        requireNonNull(dateString);
        requireNonNull(bookingTag);
        requireNonNull(bookingTag.startDate);
        requireNonNull(bookingTag.endDate);

        try {
            LocalDate date = LocalDate.parse(dateString);
            // Check if the date is on or after the start date and on or before the end date
            return !date.isBefore(bookingTag.startDate) && !date.isAfter(bookingTag.endDate);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Executes the find command to filter persons based on the search criteria.
     * Uses OR logic between different search types - a person matches if they match ANY of the search criteria.
     * Uses OR logic between keywords of the same type - a person matches a type if they match ANY of its keywords.
     *
     * @param model the model to update with the filtered person list
     * @return a CommandResult containing a message with the number of persons found
     * @throws CommandException if an error occurs during command execution
     */
    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        Predicate<Person> predicate = getPredicate();
        model.updateFilteredPersonList(predicate);

        return new CommandResult(
                String.format(Messages.MESSAGE_PERSONS_LISTED_OVERVIEW, model.getPersonList().size()));
    }


    @Override
    public boolean requireConfirmation() {
        return false;
    }

    /**
     * Checks if this FindCommand is equal to the specified object.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof FindCommand)) {
            return false;
        }

        FindCommand otherFindCommand = (FindCommand) other;
        return this.searchCriteria.equals(otherFindCommand.searchCriteria);
    }

    /**
     * Returns a string representation of this FindCommand.
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("searchCriteria", searchCriteria)
                .toString();
    }
}
