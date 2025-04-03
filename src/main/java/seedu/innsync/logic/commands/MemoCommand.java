package seedu.innsync.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.innsync.commons.util.CollectionUtil.requireAllNonNull;
import static seedu.innsync.logic.parser.CliSyntax.PREFIX_MEMO;
import static seedu.innsync.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.List;

import seedu.innsync.commons.core.index.Index;
import seedu.innsync.commons.util.ToStringBuilder;
import seedu.innsync.logic.Messages;
import seedu.innsync.logic.commands.exceptions.CommandException;
import seedu.innsync.model.Model;
import seedu.innsync.model.person.Memo;
import seedu.innsync.model.person.Person;

/**
 * Adds a memo to the person in the address book.
 */
public class MemoCommand extends Command {

    public static final String COMMAND_WORD = "memo";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the memo of the person identified "
            + "by the index number used in the last person listing. "
            + "Existing memo will be overwritten by the input.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + PREFIX_MEMO + "[MEMO]\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_MEMO + "Likes to swim.";

    public static final String MESSAGE_ADD_MEMO_SUCCESS = String.format(Messages.MESSAGE_COMMAND_SUCCESS,
            "Add memo", "Added memo to %s!");
    public static final String MESSAGE_DELETE_MEMO_SUCCESS = String.format(Messages.MESSAGE_COMMAND_SUCCESS,
            "Delete memo", "Deleted memo from %s!");

    private final Index index;
    private final Memo memo;
    /**
     * Creates an MemoCommand to add the specified {@code index}
     */
    public MemoCommand(Index index, Memo memo) {
        requireAllNonNull(index, memo);
        this.index = index;
        this.memo = memo;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getPersonList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToEdit = lastShownList.get(index.getZeroBased());
        Person editedPerson = new Person(personToEdit.getName(), personToEdit.getPhone(), personToEdit.getEmail(),
                personToEdit.getAddress(), memo, personToEdit.getRequests(), personToEdit.getBookingTags(),
                personToEdit.getTags(), personToEdit.getStarred());

        model.setPerson(personToEdit, editedPerson);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);

        return new CommandResult(generateSuccessMessage(editedPerson), editedPerson);
    }

    /**
     * Generates a command execution success message based on whether the memo is added to or removed from
     * {@code personToEdit}.
     */
    private String generateSuccessMessage(Person personToEdit) {
        if (memo.value.isEmpty()) {
            return String.format(MESSAGE_DELETE_MEMO_SUCCESS, personToEdit.getName());
        }
        return String.format(MESSAGE_ADD_MEMO_SUCCESS, personToEdit.getName());
    }

    @Override
    public boolean requireConfirmation() {
        return false;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof MemoCommand)) {
            return false;
        }

        MemoCommand otherMemo = (MemoCommand) other;
        return index.equals(otherMemo.index) && memo.equals(otherMemo.memo);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("index", index)
                .add("memo", memo)
                .toString();
    }
}
