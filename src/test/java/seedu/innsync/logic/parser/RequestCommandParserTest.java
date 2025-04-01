package seedu.innsync.logic.parser;

import static seedu.innsync.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.innsync.logic.commands.CommandTestUtil.VALID_REQUEST_AMY;
import static seedu.innsync.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.innsync.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.innsync.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.innsync.logic.commands.RequestCommand;
import seedu.innsync.model.request.Request;


public class RequestCommandParserTest {

    private RequestCommandParser parser = new RequestCommandParser();

    @Test
    public void parse_validArgs_returnsRequestCommand() {
        // Test valid request name, assuming the parser expects a "requestName"
        assertParseSuccess(parser, "1 r/" + VALID_REQUEST_AMY,
                new RequestCommand(INDEX_FIRST_PERSON, Set.of(new Request(VALID_REQUEST_AMY))));
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        // Test invalid arguments that don't follow the expected format for the request name
        assertParseFailure(parser, "a", String.format(MESSAGE_INVALID_COMMAND_FORMAT, RequestCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidRequestName_throwsParseException() {
        // Test an invalid request name containing '/' (which should be disallowed)
        String expectedMessage = Request.MESSAGE_CONSTRAINTS;
        assertParseFailure(parser, "1 r/Invalid/Request", expectedMessage);
    }

    @Test
    public void parse_requestNameTooLong_throwsParseException() {
        // Test a request name that exceeds 255 characters
        String longRequestName = "a".repeat(256); // Create a string with 256 characters
        String expectedMessage = Request.MESSAGE_CONSTRAINTS;
        assertParseFailure(parser, "1 r/" + longRequestName, expectedMessage);
    }

    @Test
    public void parse_multipleRequestNames_throwsParseException() {
        // Test multiple request names or invalid input
        assertParseSuccess(parser, "1 r/requestName1 r/requestName2",
                new RequestCommand(INDEX_FIRST_PERSON,
                        Set.of(new Request("requestName1"), new Request("requestName2"))));
    }
}
