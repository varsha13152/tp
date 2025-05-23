package seedu.innsync.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.innsync.logic.commands.CommandTestUtil.OVERLAPPING_BOOKINGTAG_INN;
import static seedu.innsync.logic.commands.CommandTestUtil.VALID_BOOKINGTAG_BEACHHOUSE;
import static seedu.innsync.logic.commands.CommandTestUtil.VALID_BOOKINGTAG_HOTEL;
import static seedu.innsync.logic.commands.CommandTestUtil.VALID_TAG_FRIEND;
import static seedu.innsync.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.innsync.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.innsync.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.innsync.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.innsync.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.innsync.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.innsync.commons.core.index.Index;
import seedu.innsync.logic.Messages;
import seedu.innsync.logic.commands.exceptions.CommandException;
import seedu.innsync.model.AddressBook;
import seedu.innsync.model.Model;
import seedu.innsync.model.ModelManager;
import seedu.innsync.model.UserPrefs;
import seedu.innsync.model.person.Person;
import seedu.innsync.model.tag.BookingTag;
import seedu.innsync.model.tag.Tag;
import seedu.innsync.testutil.PersonBuilder;

public class TagCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_validBookingTags_success() throws Exception {
        Index indexFirstPerson = Index.fromOneBased(INDEX_FIRST_PERSON.getOneBased());
        Person firstPerson = model.getPersonList().get(indexFirstPerson.getZeroBased());

        Set<BookingTag> validBookingTags = Set.of(
                new BookingTag(VALID_BOOKINGTAG_BEACHHOUSE),
                new BookingTag(VALID_BOOKINGTAG_HOTEL)
        );

        Person editedPerson = new PersonBuilder(firstPerson)
                .withBookingTags(VALID_BOOKINGTAG_BEACHHOUSE, VALID_BOOKINGTAG_HOTEL)
                .build();

        TagCommand tagCommand = new TagCommand(indexFirstPerson, null, validBookingTags);

        String expectedMessage = String.format(TagCommand.MESSAGE_SUCCESS, editedPerson.getName());

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(firstPerson, editedPerson);

        assertCommandSuccess(tagCommand, model, expectedMessage, expectedModel, editedPerson);
    }

    @Test
    public void execute_validTag_success() throws Exception {
        Index indexFirstPerson = Index.fromOneBased(INDEX_FIRST_PERSON.getOneBased());
        Person firstPerson = model.getPersonList().get(indexFirstPerson.getZeroBased());
        firstPerson.clearTags();

        Set<Tag> validTags = Set.of(new Tag(VALID_TAG_FRIEND), new Tag(VALID_TAG_HUSBAND));

        Person editedPerson = new PersonBuilder(firstPerson)
                .withTags(VALID_TAG_FRIEND, VALID_TAG_HUSBAND)
                .build();

        TagCommand tagCommand = new TagCommand(indexFirstPerson, validTags, null);

        String expectedMessage = String.format(TagCommand.MESSAGE_SUCCESS, editedPerson.getName());

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(firstPerson, editedPerson);

        assertCommandSuccess(tagCommand, model, expectedMessage, expectedModel, editedPerson);

        firstPerson.clearTags();
        firstPerson.addTag(new Tag("friends"));
    }

    @Test
    public void execute_validTagsAndBookingTags_success() throws Exception {
        Index indexFirstPerson = Index.fromOneBased(INDEX_FIRST_PERSON.getOneBased());
        Person firstPerson = model.getPersonList().get(indexFirstPerson.getZeroBased());

        firstPerson.clearTags();

        Set<Tag> validTags = Set.of(new Tag(VALID_TAG_FRIEND), new Tag(VALID_TAG_HUSBAND));
        Set<BookingTag> validBookingTags = Set.of(
                new BookingTag(VALID_BOOKINGTAG_HOTEL),
                new BookingTag(VALID_BOOKINGTAG_BEACHHOUSE)
        );

        // since tag command is now additive, old tags need to be added manually
        Person editedPerson = new PersonBuilder(firstPerson)
                .withTags(VALID_TAG_FRIEND, VALID_TAG_HUSBAND)
                .withBookingTags(VALID_BOOKINGTAG_HOTEL, VALID_BOOKINGTAG_BEACHHOUSE)
                .build();

        TagCommand tagCommand = new TagCommand(indexFirstPerson, validTags, validBookingTags);

        String expectedMessage = String.format(TagCommand.MESSAGE_SUCCESS, editedPerson.getName());

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(firstPerson, editedPerson);

        assertCommandSuccess(tagCommand, model, expectedMessage, expectedModel, editedPerson);

        firstPerson.clearTags();
        firstPerson.addTag(new Tag("friends"));
    }

    @Test
    public void execute_validIndex_unsuccessful() throws CommandException {
        // Index has needs to have friends tag (Index 1 has it)
        Index indexFirstPerson = INDEX_FIRST_PERSON;
        Tag validTag = new Tag("friends");
        Set<Tag> validTags = Set.of(validTag);
        TagCommand tagCommand = new TagCommand(indexFirstPerson, validTags, null);
        assertCommandFailure(tagCommand, model, String.format(Messages.MESSAGE_DUPLICATE_FIELD, validTag));
    }


    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getPersonList().size() + 1);
        TagCommand command = new TagCommand(outOfBoundIndex, null, null);
        assertCommandFailure(command, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_overlappingBookingTags_failure() {
        Index indexLastPerson = Index.fromOneBased(model.getPersonList().size());

        BookingTag originalBookingTag = new BookingTag(VALID_BOOKINGTAG_HOTEL);
        BookingTag overlappingBookingTag = new BookingTag(OVERLAPPING_BOOKINGTAG_INN);

        TagCommand bookingTagCommand = new TagCommand(indexLastPerson, null,
                Set.of(originalBookingTag, overlappingBookingTag));

        String expectedMessage1 = String.format(TagCommand.MESSAGE_FAILURE_OVERLAP, overlappingBookingTag.toPrettier());
        String expectedMessage2 = String.format(TagCommand.MESSAGE_FAILURE_OVERLAP, originalBookingTag.toPrettier());

        assertCommandFailure(bookingTagCommand, model, new String[] {expectedMessage1, expectedMessage2});
    }

    @Test
    public void equals() {
        final TagCommand standardCommand = new TagCommand(INDEX_FIRST_PERSON,
                Set.of(new Tag(VALID_TAG_FRIEND)),
                Set.of(new BookingTag(VALID_BOOKINGTAG_BEACHHOUSE)));

        // same values -> returns true
        TagCommand commandWithSameValues = new TagCommand(INDEX_FIRST_PERSON,
                Set.of(new Tag(VALID_TAG_FRIEND)),
                Set.of(new BookingTag(VALID_BOOKINGTAG_BEACHHOUSE)));
        assertTrue(standardCommand.equals(commandWithSameValues));

        // same values -> returns true
        assertEquals(commandWithSameValues, standardCommand);

        // same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));

        // null -> returns false
        assertFalse(standardCommand.equals(null));

        // different types -> returns false
        assertFalse(standardCommand.equals(new ClearCommand()));

        // different index -> returns false
        assertFalse(standardCommand.equals(new TagCommand(INDEX_SECOND_PERSON,
                Set.of(new Tag(VALID_TAG_FRIEND)),
                Set.of(new BookingTag(VALID_BOOKINGTAG_BEACHHOUSE)))));
    }

    @Test
    public void toStringMethod() {
        Index targetIndex = Index.fromOneBased(1);
        TagCommand tagCommand = new TagCommand(targetIndex, Set.of(new Tag(VALID_TAG_FRIEND)),
                Set.of(new BookingTag(VALID_BOOKINGTAG_BEACHHOUSE)));
        String expected = TagCommand.class.getCanonicalName() + "{index=" + targetIndex
                + ", tagList=[" + new Tag(VALID_TAG_FRIEND)
                + "], bookingTagList=[" + new BookingTag(VALID_BOOKINGTAG_BEACHHOUSE) + "]}";
        assertEquals(expected, tagCommand.toString());
    }
}
