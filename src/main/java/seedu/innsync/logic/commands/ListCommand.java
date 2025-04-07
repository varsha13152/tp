package seedu.innsync.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.innsync.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import seedu.innsync.logic.Messages;
import seedu.innsync.model.Model;

/**
 * Lists all persons in the address book to the user.
 */
public class ListCommand extends Command {

    public static final String COMMAND_WORD = "list";

    public static final String MESSAGE_SUCCESS = String.format(Messages.MESSAGE_COMMAND_SUCCESS,
            "List", "Listing all persons in the address book!");
    public static final String MESSAGE_EMPTY = "There are no contacts in the address book. Use the 'add' command to"
            + " create new contacts!";

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        if (model.getAddressBook().getPersonList().isEmpty()) {
            return new CommandResult(MESSAGE_EMPTY);
        }
        return new CommandResult(MESSAGE_SUCCESS);
    }

    @Override
    public boolean requireConfirmation() {
        return false;
    }
}
