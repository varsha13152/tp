package seedu.innsync.logic.parser;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import seedu.innsync.logic.commands.ClearCommand;
import seedu.innsync.logic.parser.exceptions.ParseException;


/**
 * As we are only doing white-box testing, our test cases do not cover path variations
 * outside of the ClearCommand code. For example, inputs "" and "1 abc" take the
 * same path through the ClearCommand, and therefore we test only one of them.
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
