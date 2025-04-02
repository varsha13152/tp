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

/**
 * Adds a request to a specific person in the system.
 * The person is identified using their displayed index in the person list.
 */

public class RequestCommand extends Command {

    public static final String COMMAND_WORD = "req";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Adds a request to the contact identified by the index number in the displayed person list.\n"
            + "Parameters: INDEX (must be a positive integer) " + PREFIX_REQUEST + "REQUEST\n"
            + "Example: " + COMMAND_WORD + " 1 " + PREFIX_REQUEST + "Need a bottle of champagne every morning";
    public static final String MESSAGE_SUCCESS = "Request successfully added: %s";

    private final Index index;
    private final Set<Request> request;

    /**
     * Creates a {@code RequestCommand} to add a request to the specified person.
     *
     * @param index The index of the person in the displayed person list.
     * @param request The set of requests to be added to the person.
     */
    public RequestCommand(Index index, Set<Request> request) {
        requireNonNull(index);
        requireNonNull(request);
        this.index = index;
        this.request = request;
    }

    /**
     * Executes the request command by adding the request to the specified person.
     *
     * @param model The model which contains the list of persons.
     * @return A {@code CommandResult} indicating the outcome of the command.
     * @throws CommandException If the specified index is invalid (out of bounds).
     */
    @Override
    public CommandResult execute(Model model) throws CommandException {
        List<Person> lastShownList = model.getPersonList();
        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToEdit = lastShownList.get(this.index.getZeroBased());
        Person editedPerson = addRequestToPerson(personToEdit, request);

        model.setPerson(personToEdit, editedPerson);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);

        return new CommandResult(String.format(MESSAGE_SUCCESS, Messages.format(editedPerson)), editedPerson);
    }

    /**
     * Creates a new {@code Person} object with the updated requests.
     *
     * @param personToCopy The original person whose requests are being updated.
     * @param requests The set of requests to add.
     * @return A new {@code Person} instance with the added requests.
     */
    private Person addRequestToPerson(Person personToCopy, Set<Request> requests) {
        Set<Request> updatedRequests = new HashSet<>(personToCopy.getRequests());
        updatedRequests.addAll(requests);
        return new Person(
                personToCopy.getName(),
                personToCopy.getPhone(),
                personToCopy.getEmail(),
                personToCopy.getAddress(),
                personToCopy.getMemo(),
                updatedRequests,
                personToCopy.getBookingTags(),
                personToCopy.getTags(),
                personToCopy.getStarred());
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof RequestCommand)) {
            return false;
        }
        RequestCommand otherRequest = (RequestCommand) other;
        return index.equals(otherRequest.index) && request.equals(otherRequest.request);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("index", index)
                .add("request", request.toString())
                .toString();
    }
}
