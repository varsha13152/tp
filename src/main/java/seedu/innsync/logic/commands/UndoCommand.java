package seedu.innsync.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.innsync.logic.commands.exceptions.CommandException;
import seedu.innsync.model.Model;

/**
 * Undoes the last action performed by the user (which modifies the addressbook)
 */
public class UndoCommand extends Command {
    public static final String COMMAND_WORD = "undo";
    public static final String MESSAGE_SUCCESS = "Successfully reverted last modification!";
    public static final String MESSAGE_FAILURE = """
            Failed to undo changes as no modifications to the current addressbook were found.
            """;


    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        if (!model.revertToLastModified()) {
            throw new CommandException(MESSAGE_FAILURE);
        }
        return new CommandResult(MESSAGE_SUCCESS);
    }

    @Override
    public boolean requireConfirmation() {
        return false;
    }
}
