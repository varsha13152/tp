package seedu.innsync.logic.commands;

import seedu.innsync.logic.commands.exceptions.CommandException;
import seedu.innsync.model.Model;

/**
 * Handles confirmation for commands that require user confirmation.
 */
public class ConfirmCommand extends Command {

    public static final String MESSAGE_CONFIRMATION = "Are you sure you want to execute this command? "
            + "Enter 'y' to confirm, any other input will cancel.";

    /**
     * Constructor for when the user confirms or cancels the operation.
     */
    public ConfirmCommand() { }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        return new CommandResult(MESSAGE_CONFIRMATION);
    }

    @Override
    public boolean requireConfirmation() {
        return false;
    }
}
