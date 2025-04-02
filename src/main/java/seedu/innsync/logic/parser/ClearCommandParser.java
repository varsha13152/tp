package seedu.innsync.logic.parser;

import seedu.innsync.logic.commands.ClearCommand;
import seedu.innsync.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new ClearCommand object.
 */
public class ClearCommandParser implements Parser<ClearCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the ClearCommand
     * and returns a ClearCommand object for execution.
     * @throws ParseException if the user input does not conform to the expected format
     */
    @Override
    public ClearCommand parse(String args) throws ParseException {
        return new ClearCommand();
    }
}
