package seedu.innsync.logic.parser;

import static seedu.innsync.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.innsync.logic.parser.CommandParserTestUtil.assertParseFailure;

import org.junit.jupiter.api.Test;

import seedu.innsync.logic.commands.FindCommand;

public class FindCommandParserTest {

    private FindCommandParser parser = new FindCommandParser();

    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, "     ", String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_emptyKeywords_throwsParseException() {
        // Empty after prefix - make sure this matches your actual error message
        String errorMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE);
        assertParseFailure(parser, "n/", errorMessage);
        assertParseFailure(parser, "p/", errorMessage);
        assertParseFailure(parser, "e/", errorMessage);
        assertParseFailure(parser, "a/", errorMessage);
        assertParseFailure(parser, "t/", errorMessage);
    }

    @Test
    public void parse_invalidNameFormat_throwsParseException() {
        assertParseFailure(parser, "n/Alice1",
                "Invalid name format. Names should only contain alphabets, spaces, apostrophes,"
                        + " and/or hyphens. Invalid keywords: Alice1");
    }

    @Test
    public void parse_invalidPhoneFormat_throwsParseException() {
        assertParseFailure(parser, "p/123abc",
                "Invalid phone format. Phone should only contain digits. Invalid keywords: 123abc");
    }

    @Test
    public void parse_invalidEmailFormat_throwsParseException() {
        assertParseFailure(parser, "e/invalid*email",
                "Invalid email format. Email should only contain alphanumeric characters, dots, '@',"
                        + " underscores, and hyphens. Invalid keywords: invalid*email");
    }

    @Test
    public void parse_invalidAddressFormat_throwsParseException() {
        assertParseFailure(parser, "a/invalid~address",
                "Invalid address format. Address should only contain alphanumeric characters, spaces,"
                        + " hyphens, and hashes. Invalid keywords: invalid~address");
    }

    @Test
    public void parse_invalidTagFormat_throwsParseException() {
        assertParseFailure(parser, "t/invalid-tag",
                "Invalid tag format. Tags should only contain alphanumeric characters. Invalid keywords: invalid-tag");
    }
}
