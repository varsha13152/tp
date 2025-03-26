package seedu.innsync.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.innsync.logic.parser.CliSyntax.PREFIX_BOOKINGTAG;
import static seedu.innsync.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.Set;

import seedu.innsync.commons.core.index.Index;
import seedu.innsync.logic.commands.TagCommand;
import seedu.innsync.logic.parser.exceptions.ParseException;
import seedu.innsync.model.tag.BookingTag;
import seedu.innsync.model.tag.Tag;

/**
 * Parses input arguments and creates a new TagCommand object
 */
public class TagCommandParser implements Parser<TagCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the TagCommand
     * and returns a TagCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    @Override
    public TagCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_BOOKINGTAG, PREFIX_TAG);

        Index index = ParserUtil.parseIndex(argMultimap.getPreamble());
        Set<BookingTag> bookingTagList = ParserUtil.parseBookingTags(argMultimap.getAllValues(PREFIX_BOOKINGTAG));
        Set<Tag> tagList = ParserUtil.parseTags(argMultimap.getAllValues(PREFIX_TAG));

        return new TagCommand(index, tagList, bookingTagList);
    }
}
