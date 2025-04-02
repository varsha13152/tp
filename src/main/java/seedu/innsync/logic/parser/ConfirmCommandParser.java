package seedu.innsync.logic.parser;

import seedu.innsync.logic.parser.exceptions.ParseException;

/**
 * Parses user input for confirmation commands.
 */
public class ConfirmCommandParser {

    /**
     * Parses the user input and returns a ConfirmCommand to be executed.
     */
    public boolean parse(String userInput) throws ParseException {
        boolean isConfirmed = userInput.trim().toLowerCase().equals("y");
        return isConfirmed;
    }
}
