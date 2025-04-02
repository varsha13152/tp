package seedu.innsync.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.innsync.logic.Emoticons;
import seedu.innsync.logic.commands.exceptions.CommandException;
import seedu.innsync.model.Model;

/**
 * Undoes the last action performed by the user (which modifies the addressbook)
 */
public class UndoCommand extends Command {
    public static final String COMMAND_WORD = "undo";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Undoes the last change made to the addressbook, "
            + "reverting it to its state before the last modification made to it.\n"
            + "Example: " + COMMAND_WORD;
    public static final String MESSAGE_SUCCESS = "Successfully reverted last modification! " + Emoticons.PROUD;
    public static final String MESSAGE_FAILURE = "Failed to undo changes as no modifications to the current addressbook were found. "
            + Emoticons.SAD + "\n" + MESSAGE_USAGE;


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
