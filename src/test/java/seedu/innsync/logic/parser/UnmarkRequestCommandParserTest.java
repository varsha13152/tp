package seedu.innsync.logic.parser;

import static seedu.innsync.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.innsync.logic.parser.CliSyntax.PREFIX_REQUEST;
import static seedu.innsync.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.innsync.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.innsync.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import org.junit.jupiter.api.Test;

import seedu.innsync.commons.core.index.Index;
import seedu.innsync.logic.Messages;
import seedu.innsync.logic.commands.UnmarkRequestCommand;

public class UnmarkRequestCommandParserTest {

    private UnmarkRequestCommandParser parser = new UnmarkRequestCommandParser();

    @Test
    public void parse_validArgs_returnsMarkRequestCommand() {
        assertParseSuccess(parser, "1 " + PREFIX_REQUEST + "1",
                new UnmarkRequestCommand(INDEX_FIRST_PERSON, Index.fromZeroBased(1)));
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        assertParseFailure(parser, "a", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                UnmarkRequestCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_multipleRequest_throwsParseException() {
        assertParseFailure(parser, "1 " + PREFIX_REQUEST + "1 " + PREFIX_REQUEST + "2",
                String.format(Messages.getErrorMessageForDuplicatePrefixes(PREFIX_REQUEST)));
    }
}
