package seedu.innsync.logic.commands;

import seedu.innsync.logic.Messages;
import seedu.innsync.model.AddressBook;
import seedu.innsync.model.Model;

/**
 * Clears the address book, requiring confirmation.
 */
public class ClearCommand extends Command {

    public static final String COMMAND_WORD = "clear";
    public static final String MESSAGE_SUCCESS = String.format(Messages.MESSAGE_COMMAND_SUCCESS,
            "Clear", "Address book has been cleared!");

    @Override
    public CommandResult execute(Model model) {
        model.setAddressBook(new AddressBook());
        return new CommandResult(MESSAGE_SUCCESS);
    };

    @Override
    public boolean requireConfirmation() {
        return true;
    }
}
