package seedu.innsync.logic.parser;

import org.junit.jupiter.api.Test;
import seedu.innsync.logic.Messages;
import seedu.innsync.logic.commands.ClearCommand;
import seedu.innsync.logic.commands.StarCommand;
import seedu.innsync.logic.parser.exceptions.ParseException;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.innsync.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.innsync.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.innsync.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

/**
 * As we are only doing white-box testing, our test cases do not cover path variations
 * outside of the StarCommand code. For example, inputs "1" and "1 abc" take the
 * same path through the StarCommand, and therefore we test only one of them.
 * The path variation for those two cases occur inside the ParserUtil, and
 * therefore should be covered by the ParserUtilTest.
 */
public class ClearCommandParserTest {

    private ClearCommandParser parser = new ClearCommandParser();

    @Test
    public void parse_validArgs_returnsClearCommand() throws ParseException {
        assertTrue(parser.parse("") instanceof ClearCommand);
    }
}
