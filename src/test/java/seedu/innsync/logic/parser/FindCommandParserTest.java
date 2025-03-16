package seedu.innsync.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;
import static seedu.innsync.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.innsync.logic.parser.CommandParserTestUtil.assertParseFailure;

import org.junit.jupiter.api.Test;

import seedu.innsync.logic.commands.FindCommand;
import seedu.innsync.logic.parser.exceptions.ParseException;

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
    public void parse_validArgsNoPrefix_returnsFindCommand() {
        // Test with multiple words - no prefix
        try {
            FindCommand command = parser.parse("Alice Bob");
            assertEquals(FindCommand.SearchType.NAME, command.getSearchType());
            assertNotNull(command.getPredicate());
        } catch (ParseException pe) {
            fail("Failed to parse valid input: " + pe.getMessage());
        }

        // Test with single word - no prefix
        try {
            FindCommand command = parser.parse("Alice");
            assertEquals(FindCommand.SearchType.NAME, command.getSearchType());
            assertNotNull(command.getPredicate());
        } catch (ParseException pe) {
            fail("Failed to parse valid input: " + pe.getMessage());
        }
    }

    @Test
    public void parse_validNameArgs_returnsFindCommand() {
        // With n/ prefix and multiple words
        try {
            FindCommand command = parser.parse("n/Alice Bob");
            assertEquals(FindCommand.SearchType.NAME, command.getSearchType());
            assertNotNull(command.getPredicate());
        } catch (ParseException pe) {
            fail("Failed to parse valid input: " + pe.getMessage());
        }

        // With apostrophes and hyphens
        try {
            FindCommand command = parser.parse("n/O'Neil Smith-Jones");
            assertEquals(FindCommand.SearchType.NAME, command.getSearchType());
            assertNotNull(command.getPredicate());
        } catch (ParseException pe) {
            fail("Failed to parse valid input: " + pe.getMessage());
        }
    }

    @Test
    public void parse_validPhoneArgs_returnsFindCommand() {
        try {
            FindCommand command = parser.parse("p/12345678");
            assertEquals(FindCommand.SearchType.PHONE, command.getSearchType());
            assertNotNull(command.getPredicate());
        } catch (ParseException pe) {
            fail("Failed to parse valid input: " + pe.getMessage());
        }

        try {
            FindCommand command = parser.parse("p/12345678 87654321");
            assertEquals(FindCommand.SearchType.PHONE, command.getSearchType());
            assertNotNull(command.getPredicate());
        } catch (ParseException pe) {
            fail("Failed to parse valid input: " + pe.getMessage());
        }
    }

    @Test
    public void parse_validEmailArgs_returnsFindCommand() {
        try {
            FindCommand command = parser.parse("e/test@example.com");
            assertEquals(FindCommand.SearchType.EMAIL, command.getSearchType());
            assertNotNull(command.getPredicate());
        } catch (ParseException pe) {
            fail("Failed to parse valid input: " + pe.getMessage());
        }

        try {
            FindCommand command = parser.parse("e/test.user-name@example.com");
            assertEquals(FindCommand.SearchType.EMAIL, command.getSearchType());
            assertNotNull(command.getPredicate());
        } catch (ParseException pe) {
            fail("Failed to parse valid input: " + pe.getMessage());
        }
    }

    @Test
    public void parse_validAddressArgs_returnsFindCommand() {
        try {
            FindCommand command = parser.parse("a/jurong west");
            assertEquals(FindCommand.SearchType.ADDRESS, command.getSearchType());
            assertNotNull(command.getPredicate());
        } catch (ParseException pe) {
            fail("Failed to parse valid input: " + pe.getMessage());
        }

        try {
            FindCommand command = parser.parse("a/Block123 #01-02");
            assertEquals(FindCommand.SearchType.ADDRESS, command.getSearchType());
            assertNotNull(command.getPredicate());
        } catch (ParseException pe) {
            fail("Failed to parse valid input: " + pe.getMessage());
        }
    }

    @Test
    public void parse_validTagArgs_returnsFindCommand() {
        try {
            FindCommand command = parser.parse("t/friends");
            assertEquals(FindCommand.SearchType.TAG, command.getSearchType());
            assertNotNull(command.getPredicate());
        } catch (ParseException pe) {
            fail("Failed to parse valid input: " + pe.getMessage());
        }

        try {
            FindCommand command = parser.parse("t/friends family");
            assertEquals(FindCommand.SearchType.TAG, command.getSearchType());
            assertNotNull(command.getPredicate());
        } catch (ParseException pe) {
            fail("Failed to parse valid input: " + pe.getMessage());
        }
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
                "Invalid tag format. Tags should only contain alphanumeric characters."
                        + " Invalid keywords: invalid-tag");
    }

    @Test
    public void parse_mixedValidInvalidKeywords_throwsParseException() {
        // Name with valid and invalid keywords
        assertParseFailure(parser, "n/John Doe123",
                "Invalid name format. Names should only contain alphabets, spaces, apostrophes,"
                        + " and/or hyphens. Invalid keywords: Doe123");

        // Phone with valid and invalid keywords
        assertParseFailure(parser, "p/12345678 9876-5432",
                "Invalid phone format. Phone should only contain digits. Invalid keywords: 9876-5432");

        // Tag with valid and invalid keywords
        assertParseFailure(parser, "t/friends family-members",
                "Invalid tag format. Tags should only contain alphanumeric characters. "
                        + "Invalid keywords: family-members");
    }
}
