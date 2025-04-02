package seedu.innsync.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.innsync.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.innsync.logic.Messages.MESSAGE_UNKNOWN_COMMAND;
import static seedu.innsync.logic.commands.CommandTestUtil.BOOKINGTAG_DESC_AMY;
import static seedu.innsync.logic.commands.CommandTestUtil.MEMO_DESC_AMY;
import static seedu.innsync.logic.commands.CommandTestUtil.TAG_DESC_FRIEND;
import static seedu.innsync.logic.commands.CommandTestUtil.VALID_BOOKINGTAG_BEACHHOUSE;
import static seedu.innsync.logic.commands.CommandTestUtil.VALID_MEMO_AMY;
import static seedu.innsync.logic.commands.CommandTestUtil.VALID_TAG_FRIEND;
import static seedu.innsync.testutil.Assert.assertThrows;
import static seedu.innsync.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.innsync.logic.commands.AddCommand;
import seedu.innsync.logic.commands.ClearCommand;
import seedu.innsync.logic.commands.ConfirmCommand;
import seedu.innsync.logic.commands.DeleteCommand;
import seedu.innsync.logic.commands.EditCommand;
import seedu.innsync.logic.commands.EditCommand.EditPersonDescriptor;
import seedu.innsync.logic.commands.ExitCommand;
import seedu.innsync.logic.commands.FindCommand;
import seedu.innsync.logic.commands.HelpCommand;
import seedu.innsync.logic.commands.ListCommand;
import seedu.innsync.logic.commands.ListStarCommand;
import seedu.innsync.logic.commands.MemoCommand;
import seedu.innsync.logic.commands.StarCommand;
import seedu.innsync.logic.commands.TagCommand;
import seedu.innsync.logic.commands.UndoCommand;
import seedu.innsync.logic.commands.UnstarCommand;
import seedu.innsync.logic.parser.exceptions.ParseException;
import seedu.innsync.model.person.Memo;
import seedu.innsync.model.person.Person;
import seedu.innsync.model.tag.BookingTag;
import seedu.innsync.model.tag.Tag;
import seedu.innsync.testutil.EditPersonDescriptorBuilder;
import seedu.innsync.testutil.PersonBuilder;
import seedu.innsync.testutil.PersonUtil;

public class AddressBookParserTest {

    private final AddressBookParser parser = new AddressBookParser();

    @Test
    public void parseCommand_add() throws Exception {
        Person person = new PersonBuilder().build();
        AddCommand command = (AddCommand) parser.parseCommand(PersonUtil.getAddCommand(person));
        assertEquals(new AddCommand(person), command);
    }

    @Test
    public void parseCommand_star() throws Exception {
        StarCommand command = (StarCommand) parser.parseCommand(StarCommand.COMMAND_WORD + " 1");
        assertEquals(new StarCommand(INDEX_FIRST_PERSON), command);
    }

    @Test
    public void parseCommand_unstar() throws Exception {
        UnstarCommand command = (UnstarCommand) parser.parseCommand(UnstarCommand.COMMAND_WORD + " 1");
        assertEquals(new UnstarCommand(INDEX_FIRST_PERSON), command);
    }

    @Test
    public void parseCommand_tag() throws Exception {
        TagCommand command = (TagCommand) parser.parseCommand(TagCommand.COMMAND_WORD + " 1 " + BOOKINGTAG_DESC_AMY);
        assertEquals(new TagCommand(INDEX_FIRST_PERSON,
                null, Set.of(new BookingTag(VALID_BOOKINGTAG_BEACHHOUSE))), command);
    }

    @Test
    public void parseCommand_tag2() throws Exception {
        TagCommand command = (TagCommand) parser.parseCommand(TagCommand.COMMAND_WORD + " 1 "
            + TAG_DESC_FRIEND + BOOKINGTAG_DESC_AMY);
        assertEquals(new TagCommand(INDEX_FIRST_PERSON,
                Set.of(new Tag(VALID_TAG_FRIEND)), Set.of(new BookingTag(VALID_BOOKINGTAG_BEACHHOUSE))), command);
    }

    @Test
    public void parseCommand_listStar() throws Exception {
        assertTrue(parser.parseCommand(ListStarCommand.COMMAND_WORD) instanceof ListStarCommand);
        assertTrue(parser.parseCommand(ListStarCommand.COMMAND_WORD + " 3") instanceof ListStarCommand);
    }

    @Test
    public void parseCommand_undo() throws Exception {
        assertTrue(parser.parseCommand(UndoCommand.COMMAND_WORD) instanceof UndoCommand);
        assertTrue(parser.parseCommand(UndoCommand.COMMAND_WORD + " 3") instanceof UndoCommand);
    }

    @Test
    public void parseCommand_memo() throws Exception {
        MemoCommand command = (MemoCommand) parser.parseCommand(MemoCommand.COMMAND_WORD + " 1" + MEMO_DESC_AMY);
        assertEquals(new MemoCommand(INDEX_FIRST_PERSON, new Memo(VALID_MEMO_AMY)), command);
    }

    @Test
    public void parseCommand_clear() throws Exception {
        assertTrue(parser.parseCommand(ClearCommand.COMMAND_WORD) instanceof ConfirmCommand);
        assertTrue(parser.parseCommand(ClearCommand.COMMAND_WORD + " 3") instanceof ConfirmCommand);
    }

    @Test
    public void parseCommand_delete() throws Exception {
        DeleteCommand command = (DeleteCommand) parser.parseCommand(
                DeleteCommand.COMMAND_WORD + " " + INDEX_FIRST_PERSON.getOneBased());
        assertEquals(new DeleteCommand(INDEX_FIRST_PERSON), command);
    }

    @Test
    public void parseCommand_edit() throws Exception {
        Person person = new PersonBuilder().build();
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder(person).build();
        EditCommand command = (EditCommand) parser.parseCommand(EditCommand.COMMAND_WORD + " "
                + INDEX_FIRST_PERSON.getOneBased() + " " + PersonUtil.getEditPersonDescriptorDetails(descriptor));
        assertEquals(new EditCommand(INDEX_FIRST_PERSON, descriptor), command);
    }

    @Test
    public void parseCommand_exit() throws Exception {
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD) instanceof ExitCommand);
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD + " 3") instanceof ExitCommand);
    }

    @Test
    public void parseCommand_find() throws Exception {
        // Test original format (backward compatibility for name search)
        FindCommand command = (FindCommand) parser.parseCommand(
                FindCommand.COMMAND_WORD + " foo bar baz");
        assertTrue(command instanceof FindCommand);
    }

    @Test
    public void parseCommand_help() throws Exception {
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD) instanceof HelpCommand);
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD + " 3") instanceof HelpCommand);
    }

    @Test
    public void parseCommand_list() throws Exception {
        assertTrue(parser.parseCommand(ListCommand.COMMAND_WORD) instanceof ListCommand);
        assertTrue(parser.parseCommand(ListCommand.COMMAND_WORD + " 3") instanceof ListCommand);
    }

    @Test
    public void parseCommand_unrecognisedInput_throwsParseException() {
        assertThrows(ParseException.class, String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE), ()
            -> parser.parseCommand(""));
    }

    @Test
    public void parseCommand_unknownCommand_throwsParseException() {
        assertThrows(ParseException.class, MESSAGE_UNKNOWN_COMMAND, () -> parser.parseCommand("unknownCommand"));
    }
}
