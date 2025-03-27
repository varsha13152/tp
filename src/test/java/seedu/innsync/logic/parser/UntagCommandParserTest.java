package seedu.innsync.logic.parser;

import static seedu.innsync.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.innsync.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.innsync.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.innsync.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.innsync.logic.commands.CommandTestUtil.VALID_TAG_FRIEND;

import org.junit.jupiter.api.Test;

import seedu.innsync.logic.Messages;
import seedu.innsync.logic.commands.UntagCommand;

public class UntagCommandParserTest {

    private UntagCommandParser parser = new UntagCommandParser();

    @Test
    public void parse_validArgs_returnsUntagCommand() {
        assertParseSuccess(parser, "1 t/" + VALID_TAG_FRIEND,
                new UntagCommand(INDEX_FIRST_PERSON, VALID_TAG_FRIEND, ""));
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        assertParseFailure(parser, "a", String.format(MESSAGE_INVALID_COMMAND_FORMAT, UntagCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_multipleTags_throwsParseException() {
        assertParseFailure(parser, "1 t/friend t/husband",
                String.format(Messages.getErrorMessageForDuplicatePrefixes(CliSyntax.PREFIX_TAG)));
    }
}
