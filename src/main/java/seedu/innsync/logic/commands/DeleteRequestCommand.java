package seedu.innsync.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.innsync.logic.parser.CliSyntax.PREFIX_REQUEST;
import static seedu.innsync.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.List;

import seedu.innsync.commons.core.index.Index;
import seedu.innsync.commons.util.ToStringBuilder;
import seedu.innsync.logic.Messages;
import seedu.innsync.logic.commands.exceptions.CommandException;
import seedu.innsync.model.Model;
import seedu.innsync.model.person.Person;
import seedu.innsync.model.request.Request;

/**
 * Deletes a request from a specific person in the system.
 * The person is identified using the displayed index in the person list.
 */
public class DeleteRequestCommand extends Command {

    public static final String COMMAND_WORD = "deletereq";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes a request from the contact identified by the index number in the displayed person list.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + PREFIX_REQUEST + "REQUEST_INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1 " + PREFIX_REQUEST + "1";

    public static final String MESSAGE_SUCCESS = String.format(
            Messages.MESSAGE_COMMAND_SUCCESS, "Delete request",
            "%s has been deleted from %s's request list!", "%s", "%s");
    public static final String MESSAGE_INVALID_REQUEST_INDEX = String.format(
            Messages.MESSAGE_INVALID_ITEM_INDEX, "request");

    private final Index contactIndex;
    private final Index requestIndex;

    /**
     * Creates a {@code DeleteRequestCommand} to delete a request from the specified person.
     *
     * @param contactIndex The index of the person in the displayed person list.
     * @param requestIndex The index of the request in the displayed request list of the person.
     */
    public DeleteRequestCommand(Index contactIndex, Index requestIndex) {
        requireNonNull(contactIndex);
        requireNonNull(requestIndex);
        this.contactIndex = contactIndex;
        this.requestIndex = requestIndex;
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

        if (contactIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToEdit = lastShownList.get(this.contactIndex.getZeroBased());
        List<Request> requests = personToEdit.getRequests();

        // Check if requestIndex is valid
        if (requestIndex.getZeroBased() >= requests.size()) {
            throw new CommandException(MESSAGE_INVALID_REQUEST_INDEX);
        }

        // Get the request to be deleted
        Request requestToDelete = requests.get(requestIndex.getZeroBased());

        // Create edited person with the request removed
        Person editedPerson = createEditedPerson(personToEdit, requestToDelete);

        model.setPerson(personToEdit, editedPerson);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);

        return new CommandResult(
            String.format(MESSAGE_SUCCESS, requestToDelete.getRequestName(),
                    editedPerson.getName()), editedPerson);
    }

    /**
     * Creates a new Person with the request removed.
     *
     * @param personToEdit The person to edit.
     * @param requestToDelete The request to be deleted.
     * @return A new Person with the request removed.
     * @throws CommandException If the request is not found in the person's requests.
     */
    private Person createEditedPerson(
            Person personToEdit,
            Request requestToDelete) throws CommandException {
        Person tempPerson = new Person(personToEdit);
        tempPerson.removeRequest(requestToDelete);

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
        return contactIndex.equals(otherDeleteRequest.contactIndex)
                && requestIndex.equals(otherDeleteRequest.requestIndex);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("contactIndex", contactIndex)
                .add("requestIndex", requestIndex)
                .toString();
    }

    @Override
    public boolean requireConfirmation() {
        return false;
    }
}
