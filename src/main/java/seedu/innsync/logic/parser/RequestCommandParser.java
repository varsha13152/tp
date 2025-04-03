package seedu.innsync.logic.parser;

import static seedu.innsync.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.innsync.logic.parser.CliSyntax.PREFIX_REQUEST;

import java.util.List;

import seedu.innsync.commons.core.index.Index;
import seedu.innsync.logic.Messages;
import seedu.innsync.logic.commands.RequestCommand;
import seedu.innsync.logic.parser.exceptions.ParseException;
import seedu.innsync.model.request.Request;

/**
 * Parses input arguments and creates a new RequestCommand object
 */
public class RequestCommandParser implements Parser<RequestCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the RequestCommand
     * and returns a RequestCommand object for execution.
     * @throws ParseException if the user input does not conform to the expected format
     */
    @Override
    public RequestCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_REQUEST);
        if (!argMultimap.getValue(PREFIX_REQUEST).isPresent()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, RequestCommand.MESSAGE_USAGE));
        }
        Index index;
        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (ParseException pe) {
            throw new ParseException(String.format(Messages.MESSAGE_PARSE_EXCEPTION,
                    pe.getMessage(), RequestCommand.MESSAGE_USAGE), pe);
        }
        List<Request> requestList = ParserUtil.parseRequests(argMultimap.getAllValues(PREFIX_REQUEST));

        return new RequestCommand(index, requestList);
    }
}
