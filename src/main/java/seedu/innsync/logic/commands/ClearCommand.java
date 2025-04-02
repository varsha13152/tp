package seedu.innsync.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.innsync.model.AddressBook;
import seedu.innsync.model.Model;

/**
 * Clears the address book.
 */
public class ClearCommand extends Command {

    public static final String COMMAND_WORD = "clear";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Clears the address book.\n"
            + "Example: " + COMMAND_WORD;
    public static final String MESSAGE_CONFIRMATION = "Are you sure you want to clear the address book? "
            + "Enter 'y' to confirm, any other input will cancel.";
    public static final String MESSAGE_SUCCESS = "Address book has been cleared!";
    public static final String MESSAGE_CANCEL = "Clear command cancelled.";

    private static boolean isActivated = false; // Tracks if 'clear' was issued but not confirmed
    private boolean isConfirmed = false; // Tracks if the action was confirmed by the user

    /**
     * Constructor for when the user initiates the clear command.
     */
    public ClearCommand() {
        isActivated = true;
    }

    /**
     * Constructor for when the user confirms or cancels the operation.
     */
    public ClearCommand(boolean isConfirmed) {
        this.isConfirmed = isConfirmed;
        if (!isConfirmed) {
            isActivated = false;
        }
    }

    public static void setIsActivatedFalse() {
        isActivated = false;
    }

    public static boolean getActivated() {
        return isActivated;
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);

        // Step 1: If the action is not confirmed and clear was initiated, prompt for confirmation
        if (isActivated && !isConfirmed) {
            return new CommandResult(MESSAGE_CONFIRMATION);
        }

        // Step 2: Handle confirmation or cancellation
        if (isConfirmed) {
            // If confirmed, clear the address book
            model.setAddressBook(new AddressBook());
            isActivated = false; // Reset the activation state
            return new CommandResult(MESSAGE_SUCCESS); // Successful clearing message
        }

        // Step 3: If canceled, reset the activation state and return cancellation message
        return new CommandResult(MESSAGE_CANCEL); // Clear command was canceled
    }
}
