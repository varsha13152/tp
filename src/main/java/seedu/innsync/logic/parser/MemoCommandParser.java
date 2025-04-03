package seedu.innsync.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.innsync.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.innsync.logic.parser.CliSyntax.PREFIX_MEMO;

import java.util.stream.Stream;

import seedu.innsync.commons.core.index.Index;
import seedu.innsync.logic.Messages;
import seedu.innsync.logic.commands.MemoCommand;
import seedu.innsync.logic.parser.exceptions.ParseException;
import seedu.innsync.model.person.Memo;

/**
 * Parses input arguments and creates a new MemoCommand object
 */
public class MemoCommandParser implements Parser<MemoCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the MemoCommand
     * and returns a MemoCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    @Override
    public MemoCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_MEMO);
        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_MEMO);
        if (!atLeastOnePrefixPresent(argMultimap, PREFIX_MEMO)) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, MemoCommand.MESSAGE_USAGE));
        }
        Index index;
        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (ParseException pe) {
            throw new ParseException(String.format(Messages.MESSAGE_PARSE_EXCEPTION,
                    pe.getMessage(), MemoCommand.MESSAGE_USAGE), pe);
        }
        Memo memo = ParserUtil.parseMemo(argMultimap.getValue(PREFIX_MEMO).orElse(""));
        return new MemoCommand(index, memo);
    }

    private static boolean atLeastOnePrefixPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).anyMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}
