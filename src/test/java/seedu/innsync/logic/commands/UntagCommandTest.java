package seedu.innsync.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.innsync.logic.commands.CommandTestUtil.VALID_BOOKINGTAG_BEACHHOUSE;
import static seedu.innsync.logic.commands.CommandTestUtil.VALID_BOOKINGTAG_HOTEL;
import static seedu.innsync.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.innsync.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.innsync.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.innsync.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.innsync.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.innsync.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.innsync.commons.core.index.Index;
import seedu.innsync.logic.Messages;
import seedu.innsync.model.AddressBook;
import seedu.innsync.model.Model;
import seedu.innsync.model.ModelManager;
import seedu.innsync.model.UserPrefs;
import seedu.innsync.model.person.Person;
import seedu.innsync.model.tag.BookingTag;
import seedu.innsync.model.tag.Tag;
import seedu.innsync.testutil.PersonBuilder;

public class UntagCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    }

    @Test
    public void execute_removeValidBookingTag_success() {
        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());

        Person taggedPerson = new PersonBuilder().withBookingTags(VALID_BOOKINGTAG_BEACHHOUSE).build();
        model.addPerson(taggedPerson);
        Index index = Index.fromZeroBased(model.getPersonList().indexOf(taggedPerson));

        Person editedPerson = new PersonBuilder(taggedPerson).withBookingTags().build();
        expectedModel.addPerson(editedPerson);

        BookingTag bookingTagToRemove = new BookingTag(VALID_BOOKINGTAG_BEACHHOUSE);
        UntagCommand untagCommand = new UntagCommand(index, null, bookingTagToRemove);
        String expectedMessage = String.format(UntagCommand.MESSAGE_SUCCESS, bookingTagToRemove.toPrettier());

        assertCommandSuccess(untagCommand, model, expectedMessage, expectedModel, editedPerson);
    }

    @Test
    public void execute_removeValidTag_success() {
        Index index = Index.fromZeroBased(INDEX_FIRST_PERSON.getZeroBased());
        Person personToEdit = model.getPersonList().get(index.getZeroBased());
        Person personToEditCopy = new PersonBuilder(personToEdit).build();

        Tag tagToRemove = new Tag(VALID_TAG_HUSBAND);
        personToEdit.addTag(tagToRemove);
        assertTrue(personToEdit.getTags().contains(new Tag(VALID_TAG_HUSBAND)));

        UntagCommand untagCommand = new UntagCommand(index, tagToRemove, null);
        String expectedMessage = String.format(UntagCommand.MESSAGE_SUCCESS, tagToRemove);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(personToEdit, personToEditCopy);

        assertCommandSuccess(untagCommand, model, expectedMessage, expectedModel, personToEditCopy);

        personToEdit.removeTag(tagToRemove);
        assertTrue(!personToEdit.getTags().contains(tagToRemove));
    }

    @Test
    public void execute_removeNonExistingBookingTag_failure() {
        Index index = Index.fromZeroBased(INDEX_FIRST_PERSON.getZeroBased());

        BookingTag bookingTagToRemove = new BookingTag(VALID_BOOKINGTAG_HOTEL);
        UntagCommand untagCommand = new UntagCommand(index, null, bookingTagToRemove);
        String expectedMessage = String.format(UntagCommand.MESSAGE_FAILURE_BOOKINGTAG,
                bookingTagToRemove.toPrettier());

        assertCommandFailure(untagCommand, model, expectedMessage);
    }

    @Test
    public void execute_removeNonExistingTag_failure() {
        Index index = Index.fromZeroBased(INDEX_FIRST_PERSON.getZeroBased());
        Tag tagToRemove = new Tag(VALID_TAG_HUSBAND);
        UntagCommand untagCommand = new UntagCommand(index, tagToRemove, null);
        String expectedMessage = String.format(UntagCommand.MESSAGE_FAILURE_TAG, tagToRemove);

        assertCommandFailure(untagCommand, model, expectedMessage);
    }

    @Test
    public void execute_invalidIndex_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getPersonList().size() + 1);
        UntagCommand untagCommand = new UntagCommand(outOfBoundIndex, new Tag(VALID_TAG_HUSBAND), null);
        assertCommandFailure(untagCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void removeTagsPerson() {
        Index targetIndex = Index.fromOneBased(1);
        Tag tagToRemove = new Tag(VALID_TAG_HUSBAND);
        UntagCommand untagCommand = new UntagCommand(targetIndex, tagToRemove, null);
        assertCommandFailure(untagCommand, model, String.format(UntagCommand.MESSAGE_FAILURE_TAG, tagToRemove));
    }

    @Test
    public void equals() {
        UntagCommand untagFirstCommand = new UntagCommand(INDEX_FIRST_PERSON, new Tag("testTag1"),
                new BookingTag(VALID_BOOKINGTAG_HOTEL));
        UntagCommand untagSecondCommand = new UntagCommand(INDEX_SECOND_PERSON, new Tag("testTag2"),
                new BookingTag(VALID_BOOKINGTAG_BEACHHOUSE));

        // same object -> returns true
        assertTrue(untagFirstCommand.equals(untagFirstCommand));

        // same values -> returns true
        UntagCommand untagFirstCommandCopy = new UntagCommand(INDEX_FIRST_PERSON, new Tag("testTag1"),
                new BookingTag(VALID_BOOKINGTAG_HOTEL));
        assertTrue(untagFirstCommand.equals(untagFirstCommandCopy));

        // different types -> returns false
        assertFalse(untagFirstCommand.equals(1));

        // null -> returns false
        assertFalse(untagFirstCommand.equals(null));

        // different person -> returns false
        assertFalse(untagFirstCommand.equals(untagSecondCommand));
    }

    @Test
    public void hasConfirmationTest_returnsFalse() {
        Index targetIndex = Index.fromOneBased(1);
        UntagCommand untagCommand = new UntagCommand(targetIndex, new Tag(VALID_TAG_HUSBAND), null);
        assertFalse(untagCommand.requireConfirmation());
    }

    // NOTE: BOOKING TAG TO STRING IS NOT FULLY CORRECT
    @Test
    public void toStringMethod() {
        Index targetIndex = Index.fromOneBased(1);
        UntagCommand untagCommand = new UntagCommand(targetIndex, new Tag("test1"),
                new BookingTag(VALID_BOOKINGTAG_HOTEL));
        String expected1 = UntagCommand.class.getCanonicalName() + "{index=" + targetIndex
                + ", tag=[test1]"
                + ", bookingTag=[[Hotel]]}";
        assertEquals(expected1, untagCommand.toString());

        untagCommand = new UntagCommand(targetIndex, null, new BookingTag(VALID_BOOKINGTAG_HOTEL));
        String expected2 = UntagCommand.class.getCanonicalName() + "{index=" + targetIndex
                + ", tag=null"
                + ", bookingTag=[[Hotel]]}";
        assertEquals(expected2, untagCommand.toString());

        untagCommand = new UntagCommand(targetIndex, new Tag("test1"), null);
        String expected3 = UntagCommand.class.getCanonicalName() + "{index=" + targetIndex
                + ", tag=[test1]"
                + ", bookingTag=[null]}";
        assertEquals(expected3, untagCommand.toString());
    }
}
