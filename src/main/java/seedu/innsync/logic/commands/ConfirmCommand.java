package seedu.innsync.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.innsync.logic.commands.exceptions.CommandException;
import seedu.innsync.model.Model;

/**
 * Handles confirmation for commands that require user confirmation.
 */
public class ConfirmCommand extends Command {

    public static final String MESSAGE_CONFIRMATION = "Are you sure you want to execute this command? "
            + "Enter 'y' to confirm, any other input will cancel.";
    public static final String MESSAGE_CANCEL = "Command cancelled.";
    private static Command pendingCommand = null;
    private final boolean isConfirmed;

    /**
     * Constructor for when a command requires confirmation.
     */
    public ConfirmCommand(Command command) {
        isConfirmed = false;
        ConfirmCommand.setConfirmCommand(command);
    }

    /**
     * Constructor for when the user confirms or cancels the operation.
     */
    public ConfirmCommand(boolean isConfirmed) {
        this.isConfirmed = isConfirmed;
        if (!isConfirmed && ConfirmCommand.getConfirmCommand() != null) {
            pendingCommand = null;
        }
    }

    public static void setConfirmCommand(Command command) {
        ConfirmCommand.pendingCommand = command;
    }

    public static Command getConfirmCommand() {
        return ConfirmCommand.pendingCommand;
    }

    /**
     * Checks if a command is awaiting confirmation.
     */
    public static boolean isAwaitingConfirmation() {
        return pendingCommand != null;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        if (isAwaitingConfirmation() && isConfirmed) {
            return pendingCommand.execute(model);
        } else if (isAwaitingConfirmation()) {
            return new CommandResult(MESSAGE_CONFIRMATION);
        }
        return new CommandResult(MESSAGE_CANCEL);
    }
}
