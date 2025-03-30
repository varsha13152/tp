package seedu.innsync.logic.commands;

import static java.util.Objects.requireNonNull;
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
 * Adds a request to a person in the address book.
 */
public class RequestCommand extends Command {

    public static final String COMMAND_WORD = "addReq";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Adds a request to the contact identified by the index number in the displayed person list.\n"
            + "Parameters: INDEX (must be a positive integer) r/STUFF\n"
            + "Example: " + COMMAND_WORD + " 1 r/Need a bottle of champagne every morning";
    public static final String MESSAGE_SUCCESS = "Request successfully added: %s";

    private final Index index;
    private final Set<Request> request;

    /**
     * Creates a RequestCommand to add the specified {@code index}
     * and {@code request}
     */
    public RequestCommand(Index index, Set<Request> request) {
        requireNonNull(index);
        requireNonNull(request);
        this.index = index;
        this.request = request;
    }

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

        return new CommandResult(String.format(MESSAGE_SUCCESS, Messages.format(editedPerson)));
    }

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
