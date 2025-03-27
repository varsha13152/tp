package seedu.innsync.logic.parser;

import static seedu.innsync.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.innsync.logic.commands.CommandTestUtil.VALID_BOOKINGTAG_BEACHHOUSE;
import static seedu.innsync.logic.commands.CommandTestUtil.VALID_BOOKINGTAG_HOTEL;
import static seedu.innsync.logic.commands.CommandTestUtil.VALID_TAG_FRIEND;
import static seedu.innsync.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.innsync.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.innsync.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.innsync.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.innsync.logic.commands.TagCommand;
import seedu.innsync.model.tag.BookingTag;
import seedu.innsync.model.tag.Tag;

public class TagCommandParserTest {

    private TagCommandParser parser = new TagCommandParser();

    @Test
    public void parse_validTagArgs_returnsTagCommand() {
        assertParseSuccess(parser, "1 t/" + VALID_TAG_FRIEND,
                new TagCommand(INDEX_FIRST_PERSON, Set.of(new Tag(VALID_TAG_FRIEND)), null));
    }

    @Test
    public void parse_validBookingTagArgs_returnsTagCommand() {
        assertParseSuccess(parser, "1 b/" + VALID_BOOKINGTAG_BEACHHOUSE,
                new TagCommand(INDEX_FIRST_PERSON, null, Set.of(new BookingTag(VALID_BOOKINGTAG_BEACHHOUSE))));
    }

    @Test
    public void parse_multipleValidTags_returnsTagCommand() {
        assertParseSuccess(parser, "1 t/" + VALID_TAG_FRIEND + " t/" + VALID_TAG_HUSBAND,
                new TagCommand(INDEX_FIRST_PERSON, Set.of(new Tag(VALID_TAG_FRIEND), new Tag(VALID_TAG_HUSBAND)),
                        null));
    }

    @Test
    public void parse_multipleValidBookingTags_returnsTagCommand() {
        assertParseSuccess(parser, "1 b/" + VALID_BOOKINGTAG_BEACHHOUSE + " b/" + VALID_BOOKINGTAG_HOTEL,
                new TagCommand(INDEX_FIRST_PERSON, null,
                        Set.of(new BookingTag(VALID_BOOKINGTAG_BEACHHOUSE), new BookingTag(VALID_BOOKINGTAG_HOTEL))));
    }

    @Test
    public void parse_validTagAndBookingTagArgs_returnsTagCommand() {
        assertParseSuccess(parser, "1 t/" + VALID_TAG_FRIEND + " b/" + VALID_BOOKINGTAG_BEACHHOUSE,
                new TagCommand(INDEX_FIRST_PERSON, Set.of(new Tag(VALID_TAG_FRIEND)),
                        Set.of(new BookingTag(VALID_BOOKINGTAG_BEACHHOUSE))));
    }

    @Test
    public void parse_invalidIndex_throwsParseException() {
        assertParseFailure(parser, "a", String.format(MESSAGE_INVALID_COMMAND_FORMAT, TagCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidBookingTagArgs_throwsParseException() {
        assertParseFailure(parser, "1 b/a",
                String.format(BookingTag.MESSAGE_CONSTRAINTS));
    }
}
