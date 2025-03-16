package seedu.innsync.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.innsync.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import javafx.collections.ObservableList;
import seedu.innsync.commons.core.index.Index;
import seedu.innsync.logic.Messages;
import seedu.innsync.logic.commands.exceptions.CommandException;
import seedu.innsync.model.Model;
import seedu.innsync.model.person.Person;

/**
 * Stars a contact in the addressbook
 */
public class StarCommand extends Command {

    public static final String COMMAND_WORD = "star";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Stars the contact identified by the index number used in the displayed person list.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";
    public static final String MESSAGE_SUCCESS = "Contact '%s' has been successfully starred!";
    public static final String MESSAGE_FAILURE = "Contact '%s' is already starred!";

    private final Index index;

    /**
     * @param index of the person in the filtered person list to edit
     */
    public StarCommand(Index index) {
        requireNonNull(index);
        this.index = index;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        ObservableList<Person> lastShownList = model.getFilteredPersonList();
        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person person = lastShownList.get(this.index.getZeroBased());
        if (person.getStarred()) {
            throw new CommandException(String.format(MESSAGE_FAILURE, person.getName()));
        }
        Person starredPerson = getStarredPerson(person);
        model.setPerson(person, starredPerson);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        return new CommandResult(String.format(MESSAGE_SUCCESS, person.getName()));
    }

    private Person getStarredPerson(Person personToCopy) {
        return new Person(personToCopy.getName(),
                personToCopy.getPhone(),
                personToCopy.getEmail(),
                personToCopy.getAddress(),
                personToCopy.getBookingTags(),
                personToCopy.getTags(),
                true);
    }
}
