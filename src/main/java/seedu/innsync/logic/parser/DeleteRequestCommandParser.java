package seedu.innsync.logic.parser;

import static seedu.innsync.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.innsync.logic.parser.CliSyntax.PREFIX_REQUEST;

import seedu.innsync.commons.core.index.Index;
import seedu.innsync.commons.exceptions.IllegalValueException;
import seedu.innsync.logic.commands.DeleteRequestCommand;
import seedu.innsync.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new DeleteRequestCommand object
 */
public class DeleteRequestCommandParser implements Parser<DeleteRequestCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the DeleteRequestCommand
     * and returns a DeleteRequestCommand object for execution.
     * @throws ParseException if the user input does not conform to the expected format
     */
    @Override
    public DeleteRequestCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_REQUEST);
        if (!argMultimap.getValue(PREFIX_REQUEST).isPresent()) {
            throw new ParseException(
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteRequestCommand.MESSAGE_USAGE));
        }

        Index contactIndex;
        try {
            contactIndex = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (ParseException pe) {
            throw new ParseException(String.format("%s\n%s", pe.getMessage(), DeleteRequestCommand.MESSAGE_USAGE), pe);
        }

        Index requestIndex;
        try {
            requestIndex = ParserUtil.parseIndex(argMultimap.getValue(PREFIX_REQUEST).get());
        } catch (IllegalValueException ive) {
            throw new ParseException(String.format(
                MESSAGE_INVALID_COMMAND_FORMAT, DeleteRequestCommand.MESSAGE_USAGE), ive);
        }

        return new DeleteRequestCommand(contactIndex, requestIndex);
    }
}
