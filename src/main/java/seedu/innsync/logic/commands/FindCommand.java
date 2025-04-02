package seedu.innsync.logic.commands;

import static java.util.Objects.requireNonNull;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.logging.Logger;

import seedu.innsync.commons.util.ToStringBuilder;
import seedu.innsync.logic.Emoticons;
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
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds persons by one or more fields simultaneously. \n"
            + "Parameters: \n"
            + "  By Name: n/KEYWORD [MORE_KEYWORDS]...\n"
            + "  By Phone: p/KEYWORD [MORE_KEYWORDS]...\n"
            + "  By Email: e/KEYWORD [MORE_KEYWORDS]...\n"
            + "  By Address: a/KEYWORD [MORE_KEYWORDS]...\n"
            + "  By Tag: t/KEYWORD [MORE_KEYWORDS]...\n"
            + "  By Memo: m/KEYWORD [MORE_KEYWORDS]...\n"
            + "  By BookingTag (Date): bd/DATE [MORE_DATES]...\n"
            + "  By BookingTag (Property): bp/PROPERTY [MORE_KEYWORDS]...\n"
            + "Examples: \n"
            + "  " + COMMAND_WORD + " John\n"
            + "  " + COMMAND_WORD + " n/John\n"
            + "  " + COMMAND_WORD + " p/91234567\n"
            + "  " + COMMAND_WORD + " t/friend\n"
            + "  " + COMMAND_WORD + " m/breakfast \n"
            + "  " + COMMAND_WORD + " bd/2024-10-15\n"
            + "  " + COMMAND_WORD + " bp/BeachHouse\n"
            + "  " + COMMAND_WORD + " n/John a/Clementi\n"
            + "  " + COMMAND_WORD + " n/John p/91234567 t/friend m/breakfast";

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
        return createCombinedPredicate();
    }

    /**
     * Creates a combined predicate based on all search criteria
     * Uses OR logic between different search types - a person matches if they match ANY of the search criteria
     */
    private Predicate<Person> createCombinedPredicate() {
        return person -> matchesAnySearchCriteria(person);
    }

    /**
     * Checks if a person matches any of the search criteria (OR logic between different search types)
     */
    private boolean matchesAnySearchCriteria(Person person) {
        if (person == null) {
            return false;
        }

        return searchCriteria.entrySet().stream()
                .filter(entry -> entry.getKey() != null && entry.getValue() != null)
                .anyMatch(entry -> matchesAnyKeyword(person, entry.getKey(), entry.getValue()));
    }

    /**
     * Checks if a person matches any of the keywords for a specific field type
     * @throws IllegalArgumentException if keywords or type is null
     */
    private boolean matchesAnyKeyword(Person person, SearchType type, List<String> keywords) {
        if (person == null || type == null || keywords == null) {
            return false;
        }

        return keywords.stream()
                .filter(Objects::nonNull)
                .map(String::toLowerCase)
                .anyMatch(keyword -> matchField(person, keyword, type));
    }

    /**
     * Matches a person against a keyword for a specific search type
     * @throws IllegalArgumentException if person or keyword is null or searchType is invalid
     */
    private boolean matchField(Person person, String keyword, SearchType searchType) {
        if (person == null || keyword == null) {
            return false;
        }

        if (searchType == null) {
            logger.severe("SearchType cannot be null - possible programming error in FindCommand");
            throw new IllegalArgumentException("SearchType cannot be null");
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
     */
    private boolean matchNameField(Person person, String keyword) {
        return person.getName() != null && person.getName().fullName != null
                && person.getName().fullName.toLowerCase().contains(keyword);
    }

    /**
     * Matches a person's phone against a keyword
     */
    private boolean matchPhoneField(Person person, String keyword) {
        return person.getPhone() != null
                && person.getPhone().value != null
                && person.getPhone().value.toLowerCase().contains(keyword);
    }

    /**
     * Matches a person's email against a keyword
     */
    private boolean matchEmailField(Person person, String keyword) {
        return person.getEmail() != null
                && person.getEmail().value != null
                && person.getEmail().value.toLowerCase().contains(keyword);
    }

    /**
     * Matches a person's address against a keyword
     */
    private boolean matchAddressField(Person person, String keyword) {
        return person.getAddress() != null && person.getAddress().value != null
                && person.getAddress().value.toLowerCase().contains(keyword);
    }

    /**
     * Matches a person's tags against a keyword
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
     */
    private boolean matchBookingDateField(Person person, String keyword) {
        if (person == null || keyword == null || keyword.isEmpty() || person.getBookingTags() == null) {
            return false;
        }

        return person.getBookingTags().stream()
                .filter(Objects::nonNull)
                .anyMatch(bookingTag -> isDateInBookingPeriod(keyword, bookingTag));
    }

    /**
     * Matches a person's booking tags based on property name against a keyword
     */
    private boolean matchBookingPropertyField(Person person, String keyword) {
        if (person == null || keyword == null || keyword.isEmpty() || person.getBookingTags() == null) {
            return false;
        }

        return person.getBookingTags().stream()
                .filter(Objects::nonNull)
                .anyMatch(bookingTag -> bookingTag.bookingTag != null
                        && bookingTag.bookingTag.toLowerCase().contains(keyword));
    }

    /**
     * Matches a person's memo against a keyword
     */
    private boolean matchMemoField(Person person, String keyword) {
        if (person == null || keyword == null || keyword.isEmpty() || person.getMemo() == null
                || person.getMemo().value == null || person.getMemo().value.isEmpty()) {
            return false;
        }

        return person.getMemo().value.toLowerCase().contains(keyword);
    }

    /**
     * Checks if a date is within a booking period
     * @param dateString the date string to check in yyyy-MM-dd format
     * @param bookingTag the booking tag to check against
     * @return true if the date is within the booking period, false otherwise
     */
    private boolean isDateInBookingPeriod(String dateString, BookingTag bookingTag) {
        if (dateString == null || bookingTag == null || bookingTag.startDate == null || bookingTag.endDate == null) {
            return false;
        }

        try {
            LocalDateTime date = LocalDateTime.parse(dateString + "T00:00:00");
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
     * @throws CommandException if an error occurs during command execution or if search criteria is invalid
     * @throws NullPointerException if model is null
     */
    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        try {
            Predicate<Person> combinedPredicate = createCombinedPredicate();
            model.updateFilteredPersonList(combinedPredicate);
            int resultCount = model.getPersonList().size();

            return new CommandResult(
                    String.format(Messages.MESSAGE_PERSONS_LISTED_OVERVIEW, resultCount));
        } catch (IllegalArgumentException e) {
            logger.severe("Error executing find command: " + e.getMessage());
            throw new CommandException(e.getMessage());
        }
    }

    /**
     * Checks if this FindCommand is equal to the specified object.
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean requireConfirmation() {
        return false;
    }

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
     * @return a string representation of this FindCommand
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("searchCriteria", searchCriteria)
                .toString();
    }
}
