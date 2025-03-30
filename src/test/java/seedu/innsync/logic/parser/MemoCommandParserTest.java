package seedu.innsync.logic.parser;

import static seedu.innsync.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.innsync.logic.commands.CommandTestUtil.MEMO_DESC_AMY;
import static seedu.innsync.logic.commands.CommandTestUtil.VALID_MEMO_AMY;
import static seedu.innsync.logic.parser.CliSyntax.PREFIX_MEMO;
import static seedu.innsync.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.innsync.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.innsync.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import org.junit.jupiter.api.Test;

import seedu.innsync.logic.Messages;
import seedu.innsync.logic.commands.MemoCommand;
import seedu.innsync.model.person.Memo;

/**
 * As we are only doing white-box testing, our test cases do not cover path variations
 * outside of the MemoCommand code. For example, inputs "1" and "1 abc" take the
 * same path through the MemoCommand, and therefore we test only one of them.
 * The path variation for those two cases occur inside the ParserUtil, and
 * therefore should be covered by the ParserUtilTest.
 */
public class MemoCommandParserTest {

    private static final Memo MEMO_STUB = new Memo(VALID_MEMO_AMY);
    private MemoCommandParser parser = new MemoCommandParser();

    @Test
    public void parse_validArgs_returnsMemoCommand() {
        assertParseSuccess(parser, "1" + MEMO_DESC_AMY, new MemoCommand(INDEX_FIRST_PERSON, MEMO_STUB));
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        assertParseFailure(parser, "a", String.format(MESSAGE_INVALID_COMMAND_FORMAT, MemoCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_mutipleMemos_throwsParseException() {
        assertParseFailure(parser, "1 m/test1 m/test2 m/test3",
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_MEMO));
    }
}
