package seedu.innsync.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.innsync.logic.parser.CliSyntax.PREFIX_REQUEST;
import static seedu.innsync.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import seedu.innsync.commons.core.index.Index;
import seedu.innsync.commons.util.ToStringBuilder;
import seedu.innsync.logic.Messages;
import seedu.innsync.logic.commands.exceptions.CommandException;
import seedu.innsync.model.Model;
import seedu.innsync.model.person.Person;
import seedu.innsync.model.request.Request;
import seedu.innsync.model.request.exceptions.RequestNotFoundException;

/**
 * Deletes a request from a specific person in the system.
 * The person is identified using the displayed index in the person list.
 */
public class DeleteRequestCommand extends Command {

    public static final String COMMAND_WORD = "deletereq";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes a request from the contact identified by the index number in the displayed person list.\n"
            + "Parameters: INDEX (must be a positive integer) " + PREFIX_REQUEST + "REQUEST\n"
            + "Example: " + COMMAND_WORD + " 1 " + PREFIX_REQUEST + "Need a bottle of champagne every morning";

    public static final String MESSAGE_SUCCESS = "Request successfully deleted from %s";
    public static final String MESSAGE_REQUEST_NOT_FOUND = "This contact does not have this request.";

    private final Index index;
    private final Set<Request> requests;

    /**
     * Creates a {@code DeleteRequestCommand} to delete a request from the specified person.
     *
     * @param index The index of the person in the displayed person list.
     * @param requests The set of requests to be deleted from the person.
     */
    public DeleteRequestCommand(Index index, Set<Request> requests) {
        requireNonNull(index);
        requireNonNull(requests);
        this.index = index;
        this.requests = requests;
    }

    /**
     * Executes the delete request command by removing the request from the specified person.
     *
     * @param model The model which contains the list of persons.
     * @return A {@code CommandResult} indicating the outcome of the command.
     * @throws CommandException If the specified index is invalid or the request is not found.
     */
    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getPersonList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToEdit = lastShownList.get(this.index.getZeroBased());
        Person editedPerson = createEditedPerson(personToEdit);

        model.setPerson(personToEdit, editedPerson);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);

        return new CommandResult(String.format(MESSAGE_SUCCESS, Messages.format(editedPerson)), editedPerson);
    }

    /**
     * Creates a new Person with the request removed.
     *
     * @param personToEdit The person to edit.
     * @return A new Person with the request removed.
     * @throws CommandException If the request is not found in the person's requests.
     */
    private Person createEditedPerson(Person personToEdit) throws CommandException {
        Person tempPerson = new Person(
                personToEdit.getName(),
                personToEdit.getPhone(),
                personToEdit.getEmail(),
                personToEdit.getAddress(),
                personToEdit.getMemo(),
                new HashSet<>(personToEdit.getRequests()),
                personToEdit.getBookingTags(),
                personToEdit.getTags(),
                personToEdit.getStarred());

        for (Request request : requests) {
            try {
                tempPerson.removeRequest(request);
            } catch (RequestNotFoundException e) {
                throw new CommandException(MESSAGE_REQUEST_NOT_FOUND);
            }
        }

        return tempPerson;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof DeleteRequestCommand)) {
            return false;
        }
        DeleteRequestCommand otherDeleteRequest = (DeleteRequestCommand) other;
        return index.equals(otherDeleteRequest.index) && requests.equals(otherDeleteRequest.requests);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("index", index)
                .add("request", requests.toString())
                .toString();
    }
}
