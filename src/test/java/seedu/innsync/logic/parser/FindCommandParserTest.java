package seedu.innsync.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static seedu.innsync.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.innsync.logic.parser.CommandParserTestUtil.assertParseFailure;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import seedu.innsync.logic.commands.FindCommand;
import seedu.innsync.logic.commands.FindCommand.SearchType;
import seedu.innsync.logic.parser.exceptions.ParseException;

public class FindCommandParserTest {

    private FindCommandParser parser = new FindCommandParser();

    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, "     ", String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_emptyKeywords_throwsParseException() {
        // Empty after prefix - check for correct error message from parser
        assertParseFailure(parser, "n/", "Search term cannot be empty for name. "
                + "Please provide at least one search term after n/");
        assertParseFailure(parser, "p/", "Search term cannot be empty for phone."
                + " Please provide at least one search term after p/");
        assertParseFailure(parser, "e/", "Search term cannot be empty for email."
                + " Please provide at least one search term after e/");
        assertParseFailure(parser, "a/", "Search term cannot be empty for address."
                + " Please provide at least one search term after a/");
        assertParseFailure(parser, "t/", "Search term cannot be empty for tag."
                + " Please provide at least one search term after t/");
        // New prefixes
        assertParseFailure(parser, "m/", "Search term cannot be empty for memo."
                + " Please provide at least one search term after m/");
        assertParseFailure(parser, "bd/", "Search term cannot be empty for booking date."
                + " Please provide at least one search term after bd/");
        assertParseFailure(parser, "bp/", "Search term cannot be empty for booking property."
                + " Please provide at least one search term after bp/");
    }

    @Test
    public void parse_validArgsNoPrefix_returnsFindCommand() {
        // Test with multiple words - no prefix
        try {
            FindCommand command = parser.parse("Alice Bob");
            Map<SearchType, List<String>> criteria = command.getSearchCriteria();
            assertEquals(1, criteria.size());
            assertEquals(true, criteria.containsKey(SearchType.NAME));
        } catch (ParseException pe) {
            fail("Failed to parse valid input: " + pe.getMessage());
        }

        // Test with single word - no prefix
        try {
            FindCommand command = parser.parse("Alice");
            Map<SearchType, List<String>> criteria = command.getSearchCriteria();
            assertEquals(1, criteria.size());
            assertEquals(true, criteria.containsKey(SearchType.NAME));
        } catch (ParseException pe) {
            fail("Failed to parse valid input: " + pe.getMessage());
        }
    }

    @Test
    public void parse_validNameArgs_returnsFindCommand() {
        // With n/ prefix and multiple words
        try {
            FindCommand command = parser.parse("n/Alice Bob");
            Map<SearchType, List<String>> criteria = command.getSearchCriteria();
            assertEquals(1, criteria.size());
            assertEquals(true, criteria.containsKey(SearchType.NAME));
        } catch (ParseException pe) {
            fail("Failed to parse valid input: " + pe.getMessage());
        }

        // With apostrophes and hyphens
        try {
            FindCommand command = parser.parse("n/O'Neil Smith-Jones");
            Map<SearchType, List<String>> criteria = command.getSearchCriteria();
            assertEquals(1, criteria.size());
            assertEquals(true, criteria.containsKey(SearchType.NAME));
        } catch (ParseException pe) {
            fail("Failed to parse valid input: " + pe.getMessage());
        }
    }

    @Test
    public void parse_validPhoneArgs_returnsFindCommand() {
        try {
            FindCommand command = parser.parse("p/12345678");
            Map<SearchType, List<String>> criteria = command.getSearchCriteria();
            assertEquals(1, criteria.size());
            assertEquals(true, criteria.containsKey(SearchType.PHONE));
        } catch (ParseException pe) {
            fail("Failed to parse valid input: " + pe.getMessage());
        }

        try {
            FindCommand command = parser.parse("p/12345678 87654321");
            Map<SearchType, List<String>> criteria = command.getSearchCriteria();
            assertEquals(1, criteria.size());
            assertEquals(true, criteria.containsKey(SearchType.PHONE));
        } catch (ParseException pe) {
            fail("Failed to parse valid input: " + pe.getMessage());
        }
    }

    @Test
    public void parse_validEmailArgs_returnsFindCommand() {
        try {
            FindCommand command = parser.parse("e/test@example.com");
            Map<SearchType, List<String>> criteria = command.getSearchCriteria();
            assertEquals(1, criteria.size());
            assertEquals(true, criteria.containsKey(SearchType.EMAIL));
        } catch (ParseException pe) {
            fail("Failed to parse valid input: " + pe.getMessage());
        }

        try {
            FindCommand command = parser.parse("e/test.user-name@example.com");
            Map<SearchType, List<String>> criteria = command.getSearchCriteria();
            assertEquals(1, criteria.size());
            assertEquals(true, criteria.containsKey(SearchType.EMAIL));
        } catch (ParseException pe) {
            fail("Failed to parse valid input: " + pe.getMessage());
        }
    }

    @Test
    public void parse_validAddressArgs_returnsFindCommand() {
        try {
            FindCommand command = parser.parse("a/jurong west");
            Map<SearchType, List<String>> criteria = command.getSearchCriteria();
            assertEquals(1, criteria.size());
            assertEquals(true, criteria.containsKey(SearchType.ADDRESS));
        } catch (ParseException pe) {
            fail("Failed to parse valid input: " + pe.getMessage());
        }

        try {
            FindCommand command = parser.parse("a/Block123 #01-02");
            Map<SearchType, List<String>> criteria = command.getSearchCriteria();
            assertEquals(1, criteria.size());
            assertEquals(true, criteria.containsKey(SearchType.ADDRESS));
        } catch (ParseException pe) {
            fail("Failed to parse valid input: " + pe.getMessage());
        }
    }

    @Test
    public void parse_validTagArgs_returnsFindCommand() {
        try {
            FindCommand command = parser.parse("t/friends");
            Map<SearchType, List<String>> criteria = command.getSearchCriteria();
            assertEquals(1, criteria.size());
            assertEquals(true, criteria.containsKey(SearchType.TAG));
        } catch (ParseException pe) {
            fail("Failed to parse valid input: " + pe.getMessage());
        }

        try {
            FindCommand command = parser.parse("t/friends family");
            Map<SearchType, List<String>> criteria = command.getSearchCriteria();
            assertEquals(1, criteria.size());
            assertEquals(true, criteria.containsKey(SearchType.TAG));
        } catch (ParseException pe) {
            fail("Failed to parse valid input: " + pe.getMessage());
        }
    }

    // Add new tests for memo search
    @Test
    public void parse_validMemoArgs_returnsFindCommand() {
        try {
            FindCommand command = parser.parse("m/important");
            Map<SearchType, List<String>> criteria = command.getSearchCriteria();
            assertEquals(1, criteria.size());
            assertEquals(true, criteria.containsKey(SearchType.MEMO));
        } catch (ParseException pe) {
            fail("Failed to parse valid input: " + pe.getMessage());
        }

        try {
            FindCommand command = parser.parse("m/call back urgent");
            Map<SearchType, List<String>> criteria = command.getSearchCriteria();
            assertEquals(1, criteria.size());
            assertEquals(true, criteria.containsKey(SearchType.MEMO));
        } catch (ParseException pe) {
            fail("Failed to parse valid input: " + pe.getMessage());
        }

        try {
            FindCommand command = parser.parse("m/Meeting scheduled for 10:30 AM!");
            Map<SearchType, List<String>> criteria = command.getSearchCriteria();
            assertEquals(1, criteria.size());
            assertEquals(true, criteria.containsKey(SearchType.MEMO));
        } catch (ParseException pe) {
            fail("Failed to parse valid input: " + pe.getMessage());
        }
    }

    // Add new tests for booking date search
    @Test
    public void parse_validBookingDateArgs_returnsFindCommand() {
        try {
            FindCommand command = parser.parse("bd/2025-06-01");
            Map<SearchType, List<String>> criteria = command.getSearchCriteria();
            assertEquals(1, criteria.size());
            assertEquals(true, criteria.containsKey(SearchType.BOOKING_DATE));
        } catch (ParseException pe) {
            fail("Failed to parse valid input: " + pe.getMessage());
        }

        try {
            FindCommand command = parser.parse("bd/2025-06-01 2025-07-15");
            Map<SearchType, List<String>> criteria = command.getSearchCriteria();
            assertEquals(1, criteria.size());
            assertEquals(true, criteria.containsKey(SearchType.BOOKING_DATE));
        } catch (ParseException pe) {
            fail("Failed to parse valid input: " + pe.getMessage());
        }
    }

    // Add new tests for booking property search
    @Test
    public void parse_validBookingPropertyArgs_returnsFindCommand() {
        try {
            FindCommand command = parser.parse("bp/BeachHouse");
            Map<SearchType, List<String>> criteria = command.getSearchCriteria();
            assertEquals(1, criteria.size());
            assertEquals(true, criteria.containsKey(SearchType.BOOKING_PROPERTY));
        } catch (ParseException pe) {
            fail("Failed to parse valid input: " + pe.getMessage());
        }

        try {
            FindCommand command = parser.parse("bp/Beach House");
            Map<SearchType, List<String>> criteria = command.getSearchCriteria();
            assertEquals(1, criteria.size());
            assertEquals(true, criteria.containsKey(SearchType.BOOKING_PROPERTY));
        } catch (ParseException pe) {
            fail("Failed to parse valid input: " + pe.getMessage());
        }

        try {
            FindCommand command = parser.parse("bp/Beach-Villa Resort123");
            Map<SearchType, List<String>> criteria = command.getSearchCriteria();
            assertEquals(1, criteria.size());
            assertEquals(true, criteria.containsKey(SearchType.BOOKING_PROPERTY));
        } catch (ParseException pe) {
            fail("Failed to parse valid input: " + pe.getMessage());
        }
    }

    // Test for multiple field searches
    @Test
    public void parse_multipleValidFields_returnsFindCommand() {
        try {
            FindCommand command = parser.parse("n/John p/12345678");
            Map<SearchType, List<String>> criteria = command.getSearchCriteria();
            assertEquals(2, criteria.size());
            assertEquals(true, criteria.containsKey(SearchType.NAME));
            assertEquals(true, criteria.containsKey(SearchType.PHONE));
        } catch (ParseException pe) {
            fail("Failed to parse valid input: " + pe.getMessage());
        }

        try {
            FindCommand command = parser.parse("n/John m/important bd/2025-06-01");
            Map<SearchType, List<String>> criteria = command.getSearchCriteria();
            assertEquals(3, criteria.size());
            assertEquals(true, criteria.containsKey(SearchType.NAME));
            assertEquals(true, criteria.containsKey(SearchType.MEMO));
            assertEquals(true, criteria.containsKey(SearchType.BOOKING_DATE));
        } catch (ParseException pe) {
            fail("Failed to parse valid input: " + pe.getMessage());
        }

        try {
            FindCommand command = parser.parse("t/friends bp/Beach");
            Map<SearchType, List<String>> criteria = command.getSearchCriteria();
            assertEquals(2, criteria.size());
            assertEquals(true, criteria.containsKey(SearchType.TAG));
            assertEquals(true, criteria.containsKey(SearchType.BOOKING_PROPERTY));
        } catch (ParseException pe) {
            fail("Failed to parse valid input: " + pe.getMessage());
        }
    }

    // Test for mixed format (unprefixed keywords + prefixed)
    @Test
    public void parse_mixedFormatWithNewPrefixes_returnsFindCommand() {
        try {
            FindCommand command = parser.parse("John m/important");
            Map<SearchType, List<String>> criteria = command.getSearchCriteria();
            assertEquals(2, criteria.size());
            assertEquals(true, criteria.containsKey(SearchType.NAME));
            assertEquals(true, criteria.containsKey(SearchType.MEMO));
        } catch (ParseException pe) {
            fail("Failed to parse valid input: " + pe.getMessage());
        }

        try {
            FindCommand command = parser.parse("Smith bp/Beach");
            Map<SearchType, List<String>> criteria = command.getSearchCriteria();
            assertEquals(2, criteria.size());
            assertEquals(true, criteria.containsKey(SearchType.NAME));
            assertEquals(true, criteria.containsKey(SearchType.BOOKING_PROPERTY));
        } catch (ParseException pe) {
            fail("Failed to parse valid input: " + pe.getMessage());
        }
    }

    @Test
    public void parse_invalidNameFormat_throwsParseException() {
        assertParseFailure(parser, "n/Alice1",
                "Invalid name format. Names should only contain alphabets, spaces, apostrophes,"
                        + " and/or hyphens. Invalid search terms(s): Alice1");
    }

    @Test
    public void parse_invalidPhoneFormat_throwsParseException() {
        assertParseFailure(parser, "p/123abc",
                "Invalid phone format. Phone numbers should only contain digits. Invalid search terms(s): 123abc");
    }

    @Test
    public void parse_invalidEmailFormat_throwsParseException() {
        assertParseFailure(parser, "e/invalid*email",
                "Invalid email format. Emails should only contain alphanumeric characters, dots, '@',"
                        + " underscores, and hyphens. Invalid search terms(s): invalid*email");
    }

    @Test
    public void parse_invalidAddressFormat_throwsParseException() {
        assertParseFailure(parser, "a/invalid~address",
                "Invalid address format. Addresses should only contain alphanumeric characters, spaces,"
                        + " hyphens, and hashes. Invalid search terms(s): invalid~address");
    }

    @Test
    public void parse_invalidTagFormat_throwsParseException() {
        assertParseFailure(parser, "t/invalid-tag",
                "Invalid tag format. Tags should only contain alphanumeric characters."
                        + " Invalid search terms(s): invalid-tag");
    }

    // New tests for invalid formats with new search types
    @Test
    public void parse_invalidMemoFormat_throwsParseException() {
        assertParseFailure(parser, "m/invalid~memo",
                "Invalid memo format. Memos should only contain alphanumeric characters, spaces, punctuation,"
                        + " and basic symbols. Invalid search terms(s): invalid~memo");
    }

    @Test
    public void parse_invalidBookingDateFormat_throwsParseException() {
        assertParseFailure(parser, "bd/20-06-2025",
                "Invalid booking date format. Dates should be in the format yyyy-MM-dd "
                        + "(e.g., 2024-10-15). Invalid search terms(s): 20-06-2025");

        assertParseFailure(parser, "bd/2025/06/01",
                "Invalid booking date format. Dates should be in the format yyyy-MM-dd "
                        + "(e.g., 2024-10-15). Invalid search terms(s): 2025/06/01");
    }

    @Test
    public void parse_invalidBookingPropertyFormat_throwsParseException() {
        assertParseFailure(parser, "bp/Beach~House",
                "Invalid booking property format. Property names should only contain alphanumeric characters,"
                        + " spaces, and hyphens. Invalid search terms(s): Beach~House");
    }

    @Test
    public void parse_mixedValidInvalidKeywords_throwsParseException() {
        // Name with valid and invalid keywords
        assertParseFailure(parser, "n/John Doe123",
                "Invalid name format. Names should only contain alphabets, spaces, apostrophes,"
                        + " and/or hyphens. Invalid search terms(s): Doe123");

        // Phone with valid and invalid keywords
        assertParseFailure(parser, "p/12345678 9876-5432",
                "Invalid phone format. Phone numbers should only contain digits. Invalid search terms(s): 9876-5432");

        // Tag with valid and invalid keywords
        assertParseFailure(parser, "t/friends family-members",
                "Invalid tag format. Tags should only contain alphanumeric characters. "
                        + "Invalid search terms(s): family-members");

        // New search types with mixed valid and invalid keywords
        assertParseFailure(parser, "m/important notes~ reminder",
                "Invalid memo format. Memos should only contain alphanumeric characters, spaces, punctuation,"
                        + " and basic symbols. Invalid search terms(s): notes~");

        assertParseFailure(parser, "bd/2025-06-01 06-01-2025",
                "Invalid booking date format. Dates should be in the format yyyy-MM-dd "
                        + "(e.g., 2024-10-15). Invalid search terms(s): 06-01-2025");

        assertParseFailure(parser, "bp/Beach House Resort~ Villa",
                "Invalid booking property format. Property names should only contain alphanumeric characters,"
                        + " spaces, and hyphens. Invalid search terms(s): Resort~");
    }

    @Test
    public void parse_duplicateFields_throwsParseException() {
        assertParseFailure(parser, "n/John n/Alice",
                "Duplicate search field: n/. Each field can only be specified once.");

        assertParseFailure(parser, "m/important m/urgent",
                "Duplicate search field: m/. Each field can only be specified once.");

        assertParseFailure(parser, "bd/2025-06-01 bd/2025-07-01",
                "Duplicate search field: bd/. Each field can only be specified once.");

        assertParseFailure(parser, "bp/Beach bp/Villa",
                "Duplicate search field: bp/. Each field can only be specified once.");
    }

    @Test
    public void parse_invalidPrefix_throwsParseException() {
        assertParseFailure(parser, "x/keyword",
                "Invalid search field: 'x/'. Valid prefixes are: \n name: n/ \n phone: p/ \n email: e/ \n"
                        + "address: a/ \n tag: t/ \n memo: m/ \n booking date: bd/ \n booking property: bp/");
    }
}
