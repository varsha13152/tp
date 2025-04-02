package seedu.innsync.logic.parser;

import static seedu.innsync.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.innsync.logic.parser.CliSyntax.PREFIX_BOOKINGTAG;
import static seedu.innsync.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.stream.Stream;

import seedu.innsync.commons.core.index.Index;
import seedu.innsync.logic.commands.UntagCommand;
import seedu.innsync.logic.parser.exceptions.ParseException;
import seedu.innsync.model.tag.Tag;

/**
 * Parses input arguments and creates a new UntagCommand object
 */
public class UntagCommandParser implements Parser<UntagCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the TagCommand
     * and returns a TagCommand object for execution.
     * Only allows one tag to be removed at a time.
     * @throws ParseException if the user input does not conform the expected format
     */
    @Override
    public UntagCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_BOOKINGTAG, PREFIX_TAG);

        if (!onlyOnePrefixPresent(argMultimap, PREFIX_BOOKINGTAG, PREFIX_TAG)) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, UntagCommand.MESSAGE_USAGE));
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_BOOKINGTAG, PREFIX_TAG);
        Index index;
        try {
            index = ParserUtil.parseIndex(args);
        } catch (ParseException pe) {
            throw new ParseException(String.format("%s\n%s", 
                    pe.getMessage(), UntagCommand.MESSAGE_USAGE), pe);
        }
        Tag tag = null;
        if (argMultimap.getValue(PREFIX_TAG).isPresent()) {
            tag = ParserUtil.parseTag(argMultimap.getValue(PREFIX_TAG).get());
        }

        String bookingTag = argMultimap.getValue(PREFIX_BOOKINGTAG).orElse("");

        if (tag == null && bookingTag.isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, UntagCommand.MESSAGE_USAGE));
        }

        return new UntagCommand(index, tag, bookingTag);
    }

    /**
     * Returns true if only one of the prefixes is present in the {@code ArgumentMultimap}.
     */
    private static boolean onlyOnePrefixPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).filter(prefix -> argumentMultimap.getValue(prefix).isPresent()).count() == 1;
    }
}
