package seedu.innsync.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static seedu.innsync.logic.parser.CommandParserTestUtil.assertParseFailure;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import seedu.innsync.logic.Emoticons;
import seedu.innsync.logic.Messages;
import seedu.innsync.logic.commands.FindCommand;
import seedu.innsync.logic.commands.FindCommand.SearchType;
import seedu.innsync.logic.parser.exceptions.ParseException;

public class FindCommandParserTest {

    private FindCommandParser parser = new FindCommandParser();

    @Test
    public void validateArgsNotNull_nullArgs_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> parser.parse(null));
    }

    @Test
    //test empty keywords in find command
    public void parse_emptyKeywords_throwsParseException() {
        assertParseFailure(parser, "find n/",
                "Error: Please enter a keyword after n/ when searching by name. "
                        + Emoticons.ANGRY);
        assertParseFailure(parser, "find p/",
                "Error: Please enter a keyword after p/ when searching by phone number. "
                        + Emoticons.ANGRY);
        assertParseFailure(parser, "find e/",
                "Error: Please enter a keyword after e/ when searching by email. "
                        + Emoticons.ANGRY);
        assertParseFailure(parser, "find a/",
                "Error: Please enter a keyword after a/ when searching by address. "
                        + Emoticons.ANGRY);
        assertParseFailure(parser, "find t/",
                "Error: Please enter a keyword after t/ when searching by tag. "
                        + Emoticons.ANGRY);
        assertParseFailure(parser, "find m/",
                "Error: Please enter a keyword after m/ when searching by memo. "
                        + Emoticons.ANGRY);
        assertParseFailure(parser, "find bd/",
                "Error: Please enter a keyword after bd/ when searching by booking date. "
                        + Emoticons.ANGRY);
        assertParseFailure(parser, "find bp/",
                "Error: Please enter a keyword after bp/ "
                        + "when searching by booking property. " + Emoticons.ANGRY);
    }

    @Test
    public void parse_validArgsNoPrefix_returnsFindCommand() {
        //test parsing of single prefix
        try {
            FindCommand command = parser.parse("find n/Alice n/Bob");
            Map<SearchType, List<String>> criteria = command.getSearchCriteria();
            assertEquals(1, criteria.size());
            assertTrue(criteria.containsKey(SearchType.NAME));
        } catch (ParseException pe) {
            fail("Failed to parse valid input: " + pe.getMessage());
        }

        try {
            //test parsing of multiple prefix
            FindCommand command = parser.parse("find n/Alice");
            Map<SearchType, List<String>> criteria = command.getSearchCriteria();
            assertEquals(1, criteria.size());
            assertTrue(criteria.containsKey(SearchType.NAME));
        } catch (ParseException pe) {
            fail("Failed to parse valid input: " + pe.getMessage());
        }
    }

    @Test
    public void parse_validNameArgs_returnsFindCommand() {
        //test parsing of names with special characters
        try {
            FindCommand command = parser.parse("n/muthu $a/p vara");
            Map<SearchType, List<String>> criteria = command.getSearchCriteria();
            assertTrue(criteria.containsKey(SearchType.NAME));
        } catch (ParseException pe) {
            fail("Failed to parse valid input: " + pe.getMessage());
        }

        try {
            FindCommand command = parser.parse("find n/O'Neil Smith-Jones");
            Map<SearchType, List<String>> criteria = command.getSearchCriteria();
            assertTrue(criteria.containsKey(SearchType.NAME));
        } catch (ParseException pe) {
            fail("Failed to parse valid input: " + pe.getMessage());
        }
    }

    @Test
    public void parse_validPhoneArgs_returnsFindCommand() {
        //test parsing of phone numbers
        try {
            FindCommand command = parser.parse("find p/12345678");
            Map<SearchType, List<String>> criteria = command.getSearchCriteria();
            assertTrue(criteria.containsKey(SearchType.PHONE));
        } catch (ParseException pe) {
            fail("Failed to parse valid input: " + pe.getMessage());
        }

        try {
            FindCommand command = parser.parse("find p/12345678 p/87654321");
            Map<SearchType, List<String>> criteria = command.getSearchCriteria();
            assertTrue(criteria.containsKey(SearchType.PHONE));
        } catch (ParseException pe) {
            fail("Failed to parse valid input: " + pe.getMessage());
        }

        try {
            FindCommand command = parser.parse("find p/+12345678");
            Map<SearchType, List<String>> criteria = command.getSearchCriteria();
            assertTrue(criteria.containsKey(SearchType.PHONE));
        } catch (ParseException pe) {
            fail("Failed to parse valid input: " + pe.getMessage());
        }
    }

    @Test
    public void parse_validEmailArgs_returnsFindCommand() {
        //test parsing of email addresses
        try {
            FindCommand command = parser.parse("find e/test@example.com");
            Map<SearchType, List<String>> criteria = command.getSearchCriteria();
            assertTrue(criteria.containsKey(SearchType.EMAIL));
        } catch (ParseException pe) {
            fail("Failed to parse valid input: " + pe.getMessage());
        }

        try {
            FindCommand command = parser.parse("find e/test.user-name@example.com");
            Map<SearchType, List<String>> criteria = command.getSearchCriteria();
            assertTrue(criteria.containsKey(SearchType.EMAIL));
        } catch (ParseException pe) {
            fail("Failed to parse valid input: " + pe.getMessage());
        }
    }

    @Test
    public void parse_validMemoArgs_returnsFindCommand() {
        //test parsing of memo
        try {
            FindCommand command = parser.parse("find m/important");
            Map<SearchType, List<String>> criteria = command.getSearchCriteria();
            assertTrue(criteria.containsKey(SearchType.MEMO));
        } catch (ParseException pe) {
            fail("Failed to parse valid input: " + pe.getMessage());
        }

        try {
            FindCommand command = parser.parse("find m/call back urgent");
            Map<SearchType, List<String>> criteria = command.getSearchCriteria();
            assertTrue(criteria.containsKey(SearchType.MEMO));
        } catch (ParseException pe) {
            fail("Failed to parse valid input: " + pe.getMessage());
        }

        try {
            FindCommand command = parser.parse("find m/Meeting scheduled for 10:30 AM! m/find food");
            Map<SearchType, List<String>> criteria = command.getSearchCriteria();
            assertTrue(criteria.containsKey(SearchType.MEMO));
        } catch (ParseException pe) {
            fail("Failed to parse valid input: " + pe.getMessage());
        }
    }

    @Test
    public void parse_validBookingDateArgs_returnsFindCommand() {
        //test parsing of booking dates
        try {
            FindCommand command = parser.parse("find bd/2025-06-01");
            Map<SearchType, List<String>> criteria = command.getSearchCriteria();
            assertTrue(criteria.containsKey(SearchType.BOOKING_DATE));
        } catch (ParseException pe) {
            fail("Failed to parse valid input: " + pe.getMessage());
        }

        try {
            FindCommand command = parser.parse("find bd/2025-06-01 bd/2025-07-15");
            Map<SearchType, List<String>> criteria = command.getSearchCriteria();
            assertTrue(criteria.containsKey(SearchType.BOOKING_DATE));
        } catch (ParseException pe) {
            fail("Failed to parse valid input: " + pe.getMessage());
        }
    }

    @Test
    public void parse_validBookingPropertyArgs_returnsFindCommand() {
        //test parsing of booking property
        try {
            FindCommand command = parser.parse("find bp/Beach House");
            Map<SearchType, List<String>> criteria = command.getSearchCriteria();
            assertTrue(criteria.containsKey(SearchType.BOOKING_PROPERTY));
        } catch (ParseException pe) {
            fail("Failed to parse valid input: " + pe.getMessage());
        }

        try {
            FindCommand command = parser.parse("find bp/Beach bp/House");
            Map<SearchType, List<String>> criteria = command.getSearchCriteria();
            assertTrue(criteria.containsKey(SearchType.BOOKING_PROPERTY));
        } catch (ParseException pe) {
            fail("Failed to parse valid input: " + pe.getMessage());
        }

        try {
            FindCommand command = parser.parse("find bp/Beach-Villa bp/Resort123");
            Map<SearchType, List<String>> criteria = command.getSearchCriteria();
            assertTrue(criteria.containsKey(SearchType.BOOKING_PROPERTY));
        } catch (ParseException pe) {
            fail("Failed to parse valid input: " + pe.getMessage());
        }
    }

    @Test
    public void parse_multipleValidFields_returnsFindCommand() {
        //test parsing of multiple search prefixes
        try {
            FindCommand command = parser.parse("find n/John p/12345678");
            Map<SearchType, List<String>> criteria = command.getSearchCriteria();
            assertTrue(criteria.containsKey(SearchType.NAME));
            assertTrue(criteria.containsKey(SearchType.PHONE));
        } catch (ParseException pe) {
            fail("Failed to parse valid input: " + pe.getMessage());
        }
    }

    @Test
    public void parse_mixedFormatWithNewPrefixes_returnsFindCommand() {
        //test parsing of multiple search prefixes
        try {
            FindCommand command = parser.parse("find n/John m/important");
            Map<SearchType, List<String>> criteria = command.getSearchCriteria();
            assertTrue(criteria.containsKey(SearchType.NAME));
            assertTrue(criteria.containsKey(SearchType.MEMO));
        } catch (ParseException pe) {
            fail("Failed to parse valid input: " + pe.getMessage());
        }
    }


    @Test
    public void parse_validAddressArgs_returnsFindCommand() {
        //test parsing of addresses
        try {
            FindCommand command = parser.parse("find a/Clementi Ave 6");
            Map<SearchType, List<String>> criteria = command.getSearchCriteria();
            assertTrue(criteria.containsKey(SearchType.ADDRESS));
        } catch (ParseException pe) {
            fail("Failed to parse valid input: " + pe.getMessage());
        }

        try {
            FindCommand command = parser.parse("find a/Blk 123 #01-456");
            Map<SearchType, List<String>> criteria = command.getSearchCriteria();
            assertTrue(criteria.containsKey(SearchType.ADDRESS));
        } catch (ParseException pe) {
            fail("Failed to parse valid input: " + pe.getMessage());
        }
    }

    @Test
    public void parse_validTagArgs_returnsFindCommand() {
        //test parsing of tags
        try {
            FindCommand command = parser.parse("find t/friends");
            Map<SearchType, List<String>> criteria = command.getSearchCriteria();
            assertTrue(criteria.containsKey(SearchType.TAG));
        } catch (ParseException pe) {
            fail("Failed to parse valid input: " + pe.getMessage());
        }

        try {
            FindCommand command = parser.parse("find t/friends t/family t/colleagues");
            Map<SearchType, List<String>> criteria = command.getSearchCriteria();
            assertTrue(criteria.containsKey(SearchType.TAG));
        } catch (ParseException pe) {
            fail("Failed to parse valid input: " + pe.getMessage());
        }
    }

    @Test
    public void parse_invalidPrefix_throwsParseException() {
        //test usage of invalid prefix
        assertParseFailure(parser, "find x/keyword",
                String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidNameFormat_throwsParseException() {
        //test parsing of invalid name value
        String longName = "A".repeat(256);
        assertParseFailure(parser, "find n/" + longName,
                "Error: Name values should not exceed 170 characters. Invalid keyword(s): " + longName
                        + " " + Emoticons.ANGRY);
    }

    @Test
    public void parse_invalidPhoneFormat_throwsParseException() {
        //test parsing of invalid phone value
        assertParseFailure(parser, "find p/123abc",
                "Error: Invalid phone format. Phone numbers should contain digits, with an optional '+' "
                        + "at the beginning. Invalid keyword(s): 123abc " + Emoticons.ANGRY);

        assertParseFailure(parser, "find p/abc+123456",
                "Error: Invalid phone format. Phone numbers should contain digits, with an optional '+' "
                        + "at the beginning. Invalid keyword(s): abc+123456 " + Emoticons.ANGRY);
    }

    @Test
    public void parse_invalidEmailFormat_throwsParseException() {
        //test parsing of invalid email value
        String expectedMessage1 = "Error: Invalid email format. "
                + "Email values may only contain alphanumeric characters, '@', and these special characters: + _ . - "
                + "Invalid keyword(s): invalid,email " + Emoticons.ANGRY;

        String expectedMessage2 = "Error: Invalid email format. "
                + "Email values may only contain alphanumeric characters, '@', and these special characters: + _ . - "
                + "Invalid keyword(s): test)com " + Emoticons.ANGRY;

        assertParseFailure(parser, "find e/invalid,email", expectedMessage1);
        assertParseFailure(parser, "find e/test)com", expectedMessage2);
    }

    @Test
    public void parse_invalidAddressFormat_throwsParseException() {
        //test parsing of address value
        String longAddress = "A".repeat(501); // Create a 501-character address
        String expectedMessage = "Error: Address values should not exceed 500 characters. Invalid keyword(s): "
                + longAddress + " " + Emoticons.ANGRY;
        assertParseFailure(parser, "find a/" + longAddress, expectedMessage);
    }

    @Test
    public void parse_invalidTagFormat_throwsParseException() {
        //test parsing of invalid tag value
        String longTag = "A".repeat(256);
        String expectedMessage = "Error: Tag values should not exceed 170 characters. Invalid keyword(s): "
                + longTag + " " + Emoticons.ANGRY;
        assertParseFailure(parser, "find t/" + longTag, expectedMessage);

    }

    @Test
    public void parse_invalidMemoFormat_throwsParseException() {
        //test parsing of invalid memo value
        String longMemo = "A".repeat(501);
        String expectedMessage = "Error: Memo values should not exceed 500 characters. Invalid keyword(s): "
                + longMemo + " " + Emoticons.ANGRY;
        assertParseFailure(parser, "find m/" + longMemo, expectedMessage);
    }

    @Test
    public void parse_invalidBookingDateFormat_throwsParseException() {
        //test parsing of invalid date format
        String msg1 = "Error: Invalid booking date format. Dates should be in the format yyyy-MM-dd "
                + "(e.g., 2024-10-15). Invalid keyword(s): 20-06-2025 " + Emoticons.ANGRY;

        String msg2 = "Error: Invalid booking date format. Dates should be in the format yyyy-MM-dd "
                + "(e.g., 2024-10-15). Invalid keyword(s): 2025/06/01 " + Emoticons.ANGRY;
        assertParseFailure(parser, "find bd/2025/06/01", msg2);
    }

    @Test
    public void parse_invalidBookingPropertyFormat_throwsParseException() {
        //test parsing of invalid booking property value
        String longProperty = "A".repeat(256); // Create a 256-character property name
        String expectedMessage = "Error: Booking property values should not exceed 170 characters. Invalid keyword(s): "
                + longProperty + " " + Emoticons.ANGRY;
        assertParseFailure(parser, "find bp/" + longProperty, expectedMessage);
    }


    @Test
    public void parse_mixedValidInvalidKeywords_throwsParseException() {
        //test parsing of multiple invalid values
        assertParseFailure(parser, "find p/12345678 p/9876-5432",
                "Error: Invalid phone format. Phone numbers should contain digits, with an optional "
                        + "'+' at the beginning. Invalid keyword(s): 9876-5432 " + Emoticons.ANGRY);

        assertParseFailure(parser, "find bd/2025-06-01 bd/06-01-2025",
                "Error: Invalid booking date format. Dates should be in the format yyyy-MM-dd "
                        + "(e.g., 2024-10-15). Invalid keyword(s): 06-01-2025 " + Emoticons.ANGRY);

        // Test with excessive character length keywords mixed with valid ones
        String longName = "A".repeat(256);
        assertParseFailure(parser, "find n/John n/" + longName,
                "Error: Name values should not exceed 170 characters. Invalid keyword(s): "
                        + longName + " " + Emoticons.ANGRY);

        String longTag = "T".repeat(256);
        assertParseFailure(parser, "find t/friends t/" + longTag,
                "Error: Tag values should not exceed 170 characters. Invalid keyword(s): "
                        + longTag + " " + Emoticons.ANGRY);

        String longMemo = "M".repeat(501);
        assertParseFailure(parser, "find m/important m/" + longMemo,
                "Error: Memo values should not exceed 500 characters. Invalid keyword(s): "
                        + longMemo + " " + Emoticons.ANGRY);
    }


    @Test
    public void parse_allValidFieldsTogether_returnsFindCommand() {
        //test parsing of all search prefixes
        try {
            FindCommand command = parser.parse("find n/John p/12345678 e/john@example.com a/Clementi t/friends "
                    + "m/important bd/2025-06-01 bp/Beach");
            Map<SearchType, List<String>> criteria = command.getSearchCriteria();
            assertTrue(criteria.containsKey(SearchType.NAME));
            assertTrue(criteria.containsKey(SearchType.PHONE));
            assertTrue(criteria.containsKey(SearchType.EMAIL));
            assertTrue(criteria.containsKey(SearchType.ADDRESS));
            assertTrue(criteria.containsKey(SearchType.TAG));
            assertTrue(criteria.containsKey(SearchType.MEMO));
            assertTrue(criteria.containsKey(SearchType.BOOKING_DATE));
            assertTrue(criteria.containsKey(SearchType.BOOKING_PROPERTY));
        } catch (ParseException pe) {
            fail("Failed to parse valid input: " + pe.getMessage());
        }
    }

    @Test
    public void parse_edgeCasesInValidation_handledCorrectly() {
        //test parsing of invalid email values
        try {
            FindCommand command = parser.parse("find e/john.doe_123+test@example-domain.com");
            Map<SearchType, List<String>> criteria = command.getSearchCriteria();
            assertEquals(1, criteria.size());
            assertTrue(criteria.containsKey(SearchType.EMAIL));
        } catch (ParseException pe) {
            fail("Failed to parse valid input: " + pe.getMessage());
        }
    }

    @Test
    public void parse_dateFormatValidation_worksProperly() {
        //test parsing of date format
        try {
            FindCommand command = parser.parse("find bd/2024-02-29"); // Leap year
            Map<SearchType, List<String>> criteria = command.getSearchCriteria();
            assertTrue(criteria.containsKey(SearchType.BOOKING_DATE));
        } catch (ParseException pe) {
            fail("Failed to parse valid input: " + pe.getMessage());
        }
    }
}
