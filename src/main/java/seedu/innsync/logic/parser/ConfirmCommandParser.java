package seedu.innsync.logic.parser;

import seedu.innsync.logic.commands.Command;
import seedu.innsync.logic.commands.ConfirmCommand;
import seedu.innsync.logic.parser.exceptions.ParseException;

/**
 * Parses user input for confirmation commands.
 */
public class ConfirmCommandParser {

    /**
     * Parses the user input and returns a ConfirmCommand to be executed.
     */
    public Command parse(String userInput) throws ParseException {
        if (!ConfirmCommand.isAwaitingConfirmation()) {
            throw new ParseException("No command awaiting confirmation.");
        }
        boolean isConfirmed = userInput.trim().equals("y");
        return new ConfirmCommand(isConfirmed);
    }
}
