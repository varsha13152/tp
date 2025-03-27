package seedu.innsync.logic.commands;

import static java.util.Objects.requireNonNull;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import seedu.innsync.commons.util.ToStringBuilder;
import seedu.innsync.logic.Messages;
import seedu.innsync.logic.commands.exceptions.CommandException;
import seedu.innsync.model.Model;
import seedu.innsync.model.person.Person;
import seedu.innsync.model.tag.BookingTag;

/**
 * Finds and lists persons in address book whose details match the given keywords.
 * Supports searching by multiple fields simultaneously: name, phone, email, address, tags, booking tags, memo.
 */
public class FindCommand extends Command {

    public static final String COMMAND_WORD = "find";

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
            + "  " + COMMAND_WORD + " n/John\n"
            + "  " + COMMAND_WORD + " p/91234567\n"
            + "  " + COMMAND_WORD + " t/friend\n"
            + "  " + COMMAND_WORD + " m/important\n"
            + "  " + COMMAND_WORD + " bd/2024-10-15\n"
            + "  " + COMMAND_WORD + " bp/BeachHouse\n"
            + "  " + COMMAND_WORD + " n/John a/Clementi\n"
            + "  " + COMMAND_WORD + " n/John p/91234567 t/friend m/important";


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
        this.searchCriteria = searchCriteria;
    }

    public Map<SearchType, List<String>> getSearchCriteria() {
        return this.searchCriteria;
    }

    /**
     * Returns a predicate based on the search criteria.
     * This method is primarily used for testing purposes.
     * @return a predicate that can be used to filter persons based on the search criteria
     * @throws IllegalStateException if search criteria map is null or empty
     */
    public Predicate<Person> getPredicate() {
        return createCombinedPredicate();
    }

    /**
     * Creates a combined predicate based on all search criteria
     * @throws IllegalStateException if search criteria map is null or empty
     */
    private Predicate<Person> createCombinedPredicate() {
        validateSearchCriteria();
        return person -> matchesAllSearchCriteria(person);
    }

    /**
     * Validates that the search criteria is not null or empty
     */
    private void validateSearchCriteria() {
        // Assert search criteria is not null
        assert searchCriteria != null : "Search criteria map cannot be null";

        // Throw IllegalStateException if map is empty
        if (searchCriteria.isEmpty()) {
            throw new IllegalStateException("At least one search criterion must be provided");
        }
    }

    /**
     * Checks if a person matches all search criteria
     */
    private boolean matchesAllSearchCriteria(Person person) {
        // Must match ALL specified field types (AND logic between fields)
        return searchCriteria.entrySet().stream().allMatch(entry -> {
            SearchType type = entry.getKey();
            List<String> keywords = entry.getValue();

            // Assert that the key and value are not null
            assert type != null : "Search type cannot be null";
            assert keywords != null : "Keywords list cannot be null for search type: " + type;

            // For each field type, match ANY of the keywords (OR logic within a field)
            return matchesAnyKeyword(person, type, keywords);
        });
    }

    /**
     * Checks if a person matches any of the keywords for a specific field type
     */
    private boolean matchesAnyKeyword(Person person, SearchType type, List<String> keywords) {
        return keywords.stream()
                .map(keyword -> {
                    // Assert each keyword is not null
                    assert keyword != null : "Keyword cannot be null for search type: " + type;
                    return keyword.toLowerCase();
                })
                .anyMatch(keyword -> matchField(person, keyword, type));
    }

    /**
     * Matches a person against a keyword for a specific search type
     * @throws IllegalArgumentException if person or keyword is null or searchType is invalid
     */
    private boolean matchField(Person person, String keyword, SearchType searchType) {
        validateMatchFieldParameters(person, keyword, searchType);
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
            throw new IllegalArgumentException("Unsupported search type: " + searchType);
        }
    }

    /**
     * Validates parameters for matchField method
     */
    private void validateMatchFieldParameters(Person person, String keyword, SearchType searchType) {
        // Validate parameters
        if (person == null) {
            throw new IllegalArgumentException("Person cannot be null");
        }
        if (keyword == null) {
            throw new IllegalArgumentException("Keyword cannot be null");
        }
        if (searchType == null) {
            throw new IllegalArgumentException("SearchType cannot be null");
        }

        // Assert parameters are valid
        assert !keyword.isEmpty() : "Keyword cannot be empty";
        assert person.getName() != null : "Person name cannot be null";
    }

    /**
     * Matches a person's name against a keyword
     */
    private boolean matchNameField(Person person, String keyword) {
        return person.getName().fullName.toLowerCase().contains(keyword);
    }

    /**
     * Matches a person's phone against a keyword
     */
    private boolean matchPhoneField(Person person, String keyword) {
        return person.getPhone() != null
                && person.getPhone().value.toLowerCase().contains(keyword);
    }

    /**
     * Matches a person's email against a keyword
     */
    private boolean matchEmailField(Person person, String keyword) {
        return person.getEmail() != null
                && person.getEmail().value.toLowerCase().contains(keyword);
    }

    /**
     * Matches a person's address against a keyword
     */
    private boolean matchAddressField(Person person, String keyword) {
        return person.getAddress() != null
                && person.getAddress().value.toLowerCase().contains(keyword);
    }

    /**
     * Matches a person's tags against a keyword
     */
    private boolean matchTagField(Person person, String keyword) {
        // Verify tags collection is not null before attempting to stream
        return person.getTags() != null && person.getTags().stream()
                .filter(tag -> tag != null) // Filter out any null tags for robustness
                .anyMatch(tag -> tag.tagName != null && tag.tagName.toLowerCase().contains(keyword));
    }

    /**
     * Matches a person's booking tags based on date against a keyword
     * @throws IllegalArgumentException if person or keyword is null
     */
    private boolean matchBookingDateField(Person person, String keyword) {
        // Validate parameters
        if (person == null) {
            throw new IllegalArgumentException("Person cannot be null when matching booking date");
        }
        if (keyword == null) {
            throw new IllegalArgumentException("Keyword cannot be null when matching booking date");
        }

        // Assert parameters are valid
        assert !keyword.isEmpty() : "Booking date keyword cannot be empty";

        // Early return for null booking tags
        if (person.getBookingTags() == null) {
            return false;
        }

        return person.getBookingTags().stream()
                .filter(tag -> tag != null) // Filter out any null booking tags for robustness
                .anyMatch(bookingTag -> isDateInBookingPeriod(keyword, bookingTag));
    }

    /**
     * Matches a person's booking tags based on property name against a keyword
     * @throws IllegalArgumentException if person or keyword is null
     */
    private boolean matchBookingPropertyField(Person person, String keyword) {
        // Validate parameters
        if (person == null) {
            throw new IllegalArgumentException("Person cannot be null when matching booking property");
        }
        if (keyword == null) {
            throw new IllegalArgumentException("Keyword cannot be null when matching booking property");
        }

        // Assert parameters are valid
        assert !keyword.isEmpty() : "Booking property keyword cannot be empty";

        // Early return for null booking tags
        if (person.getBookingTags() == null) {
            return false;
        }

        return person.getBookingTags().stream()
                .filter(tag -> tag != null) // Filter out any null booking tags for robustness
                .anyMatch(bookingTag -> bookingTag.bookingTag != null
                        && bookingTag.bookingTag.toLowerCase().contains(keyword));
    }

    /**
     * Matches a person's memo against a keyword
     * @throws IllegalArgumentException if person or keyword is null
     */
    private boolean matchMemoField(Person person, String keyword) {
        // Validate parameters
        if (person == null) {
            throw new IllegalArgumentException("Person cannot be null when matching memo");
        }
        if (keyword == null) {
            throw new IllegalArgumentException("Keyword cannot be null when matching memo");
        }

        // Assert parameters are valid
        assert !keyword.isEmpty() : "Memo keyword cannot be empty";

        return person.getMemo() != null
                && person.getMemo().value != null
                && !person.getMemo().value.isEmpty()
                && person.getMemo().value.toLowerCase().contains(keyword);
    }

    /**
     * Checks if a date is within a booking period
     * @param dateString the date string to check in yyyy-MM-dd format
     * @param bookingTag the booking tag to check against
     * @return true if the date is within the booking period, false otherwise
     * @throws IllegalArgumentException if dateString or bookingTag is null
     */
    private boolean isDateInBookingPeriod(String dateString, BookingTag bookingTag) {
        // Validate parameters
        if (dateString == null) {
            throw new IllegalArgumentException("Date string cannot be null");
        }
        if (bookingTag == null) {
            throw new IllegalArgumentException("Booking tag cannot be null");
        }

        // Assert parameters are valid
        assert !dateString.isEmpty() : "Date string cannot be empty";

        try {
            LocalDateTime date = LocalDateTime.parse(dateString + "T00:00:00");

            // Check for null dates in booking tag
            if (bookingTag.startDate == null || bookingTag.endDate == null) {
                return false;
            }

            return !date.isBefore(bookingTag.startDate) && !date.isAfter(bookingTag.endDate);
        } catch (Exception e) {
            // Log the exception for debugging
            System.err.println("Error parsing date: " + e.getMessage());
            return false;
        }
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        try {
            // Create and apply the predicate
            Predicate<Person> combinedPredicate = createCombinedPredicate();
            model.updateFilteredPersonList(combinedPredicate);

            return new CommandResult(
                    String.format(Messages.MESSAGE_PERSONS_LISTED_OVERVIEW, model.getPersonList().size()));
        } catch (IllegalStateException | IllegalArgumentException e) {
            // Just pass through the error message
            throw new CommandException(e.getMessage());
        } catch (Exception e) {
            // Just pass through the error message
            throw new CommandException(e.getMessage());
        }
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

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("searchCriteria", searchCriteria)
                .toString();
    }
}
