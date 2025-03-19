package seedu.innsync.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.innsync.model.Model.PREDICATE_SHOW_STARRED_PERSONS;

import seedu.innsync.model.Model;

/**
 * Lists all starred persons in the address book to the user.
 */
public class ListStarCommand extends Command {

    public static final String COMMAND_WORD = "liststar";

    public static final String MESSAGE_SUCCESS = "Listed all starred persons";

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredPersonList(PREDICATE_SHOW_STARRED_PERSONS);
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
