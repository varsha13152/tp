package seedu.innsync.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.function.Predicate;

import seedu.innsync.commons.util.ToStringBuilder;
import seedu.innsync.logic.Messages;
import seedu.innsync.model.Model;
import seedu.innsync.model.person.Person;

/**
 * Finds and lists persons in address book whose details match the given keywords.
 * Supports searching by one field at a time: name, phone, email, address, or tags.
 */
public class FindCommand extends Command {

    public static final String COMMAND_WORD = "find";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds persons by a specific field. \n"
            + "Parameters: \n"
            + "  By Name: n/KEYWORD [MORE_KEYWORDS]...\n"
            + "  By Phone: p/KEYWORD [MORE_KEYWORDS]...\n"
            + "  By Email: e/KEYWORD [MORE_KEYWORDS]...\n"
            + "  By Address: a/KEYWORD [MORE_KEYWORDS]...\n"
            + "  By Tag: t/KEYWORD [MORE_KEYWORDS]...\n"
            + "Examples: \n"
            + "  " + COMMAND_WORD + " n/John\n"
            + "  " + COMMAND_WORD + " p/91234567\n"
            + "  " + COMMAND_WORD + " t/friend";

    private final Predicate<Person> predicate;
    private final SearchType searchType;

    /**
     * Enum to represent the type of search being performed
     */
    public enum SearchType {
        NAME, PHONE, EMAIL, ADDRESS, TAG
    }

    /**
     * Constructor for searching by a specific field
     */
    public FindCommand(List<String> keywords, SearchType searchType) {
        this.predicate = createPredicate(keywords, searchType);
        this.searchType = searchType;
    }

    /**
     * Creates a predicate based on the search type and keywords
     */
    private Predicate<Person> createPredicate(List<String> keywords, SearchType searchType) {
        return person -> {
            // Convert keywords to lowercase for case-insensitive matching
            return keywords.stream()
                    .map(String::toLowerCase)
                    .anyMatch(keyword -> matchField(person, keyword, searchType));
        };
    }

    /**
     * Matches a person against a keyword for a specific search type
     */
    private boolean matchField(Person person, String keyword, SearchType searchType) {
        switch (searchType) {
            case NAME:
                return person.getName().fullName.toLowerCase().contains(keyword);
            case PHONE:
                return person.getPhone() != null &&
                        person.getPhone().value.toLowerCase().contains(keyword);
            case EMAIL:
                return person.getEmail() != null &&
                        person.getEmail().value.toLowerCase().contains(keyword);
            case ADDRESS:
                return person.getAddress() != null &&
                        person.getAddress().value.toLowerCase().contains(keyword);
            case TAG:
                return person.getTags().stream()
                        .anyMatch(tag -> tag.tagName.toLowerCase().contains(keyword));
            default:
                return false;
        }
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredPersonList(predicate);
        return new CommandResult(
                String.format(Messages.MESSAGE_PERSONS_LISTED_OVERVIEW, model.getFilteredPersonList().size()));
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
        return this.predicate.equals(otherFindCommand.predicate) &&
                this.searchType == otherFindCommand.searchType;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("predicate", predicate)
                .add("searchType", searchType)
                .toString();
    }
}