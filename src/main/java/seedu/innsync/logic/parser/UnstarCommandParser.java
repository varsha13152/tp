package seedu.innsync.logic.parser;

import seedu.innsync.commons.core.index.Index;
import seedu.innsync.logic.commands.UnstarCommand;
import seedu.innsync.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new UnstarCommand object
 */
public class UnstarCommandParser implements Parser<UnstarCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the UnstarCommand
     * and returns an UnstarCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    @Override
    public UnstarCommand parse(String args) throws ParseException {
       
        try {
            Index index = ParserUtil.parseIndex(args);
            return new UnstarCommand(index);
        } catch (ParseException pe) {
            throw new ParseException(
                    String.format("%s\n%s", pe.getMessage(), UnstarCommand.MESSAGE_USAGE), pe);
        }
    }

}
