package seedu.innsync.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.innsync.model.Model.PREDICATE_SHOW_STARRED_PERSONS;

import seedu.innsync.logic.Messages;
import seedu.innsync.model.Model;

/**
 * Lists all starred persons in the address book to the user.
 */
public class ListStarCommand extends Command {

    public static final String COMMAND_WORD = "liststar";
    public static final String MESSAGE_SUCCESS = String.format(Messages.MESSAGE_COMMAND_SUCCESS,
            "List starred", "Listing all starred persons in the address book!");

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredPersonList(PREDICATE_SHOW_STARRED_PERSONS);
        return new CommandResult(MESSAGE_SUCCESS);
    }

    @Override
    public boolean requireConfirmation() {
        return false;
    }
}
