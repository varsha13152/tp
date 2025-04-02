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

import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.innsync.commons.core.index.Index;
import seedu.innsync.logic.Messages;
import seedu.innsync.model.AddressBook;
import seedu.innsync.model.Model;
import seedu.innsync.model.ModelManager;
import seedu.innsync.model.UserPrefs;
import seedu.innsync.model.person.Person;
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

        UntagCommand untagCommand = new UntagCommand(index, null, VALID_BOOKINGTAG_BEACHHOUSE);
        String expectedMessage = String.format(UntagCommand.MESSAGE_SUCCESS, VALID_BOOKINGTAG_BEACHHOUSE);

        assertCommandSuccess(untagCommand, model, expectedMessage, expectedModel, editedPerson);
    }

    @Test
    public void execute_removeValidTag_success() {
        Index index = Index.fromZeroBased(INDEX_FIRST_PERSON.getZeroBased());
        Person personToEdit = model.getPersonList().get(index.getZeroBased());

        // get tag from person
        Set<Tag> tags = personToEdit.getTags();
        Tag tagToRemove = tags.iterator().next();
        assert (tagToRemove != null);

        model.getTagElseCreate(tagToRemove);

        UntagCommand untagCommand = new UntagCommand(index, tagToRemove, null);
        String expectedMessage = String.format(UntagCommand.MESSAGE_SUCCESS, tagToRemove);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());

        assertCommandSuccess(untagCommand, model, expectedMessage, expectedModel, personToEdit);

        // add tag back to person
        personToEdit.addTag(tagToRemove);
        assertTrue(personToEdit.getTags().contains(tagToRemove));
    }

    @Test
    public void execute_removeNonExistingBookingTag_failure() {
        Index index = Index.fromZeroBased(INDEX_FIRST_PERSON.getZeroBased());

        UntagCommand untagCommand = new UntagCommand(index, null, VALID_BOOKINGTAG_HOTEL);
        String expectedMessage = String.format(UntagCommand.MESSAGE_FAILURE_BOOKINGTAG, VALID_BOOKINGTAG_HOTEL);

        assertCommandFailure(untagCommand, model, expectedMessage);
    }

    @Test
    public void execute_removeNonExistingTag_failure() {
        Index index = Index.fromZeroBased(INDEX_FIRST_PERSON.getZeroBased());

        UntagCommand untagCommand = new UntagCommand(index, new Tag(VALID_TAG_HUSBAND), null);
        String expectedMessage = String.format(UntagCommand.MESSAGE_FAILURE_TAG, VALID_TAG_HUSBAND);

        assertCommandFailure(untagCommand, model, expectedMessage);
    }

    @Test
    public void execute_invalidIndex_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getPersonList().size() + 1);
        UntagCommand untagCommand = new UntagCommand(outOfBoundIndex, new Tag(VALID_TAG_HUSBAND), null);
        assertCommandFailure(untagCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        UntagCommand untagFirstCommand = new UntagCommand(INDEX_FIRST_PERSON, new Tag("testTag1"), "testBookingTag1");
        UntagCommand untagSecondCommand = new UntagCommand(INDEX_SECOND_PERSON, new Tag("testTag2"), "testBookingTag2");

        // same object -> returns true
        assertTrue(untagFirstCommand.equals(untagFirstCommand));

        // same values -> returns true
        UntagCommand untagFirstCommandCopy = new UntagCommand(INDEX_FIRST_PERSON, new Tag("testTag1"),
                "testBookingTag1");
        assertTrue(untagFirstCommand.equals(untagFirstCommandCopy));

        // different types -> returns false
        assertFalse(untagFirstCommand.equals(1));

        // null -> returns false
        assertFalse(untagFirstCommand.equals(null));

        // different person -> returns false
        assertFalse(untagFirstCommand.equals(untagSecondCommand));
    }

    @Test
    public void toStringMethod() {
        Index targetIndex = Index.fromOneBased(1);
        UntagCommand untagCommand = new UntagCommand(targetIndex, new Tag("test1"), "test2");
        String expected1 = UntagCommand.class.getCanonicalName() + "{index=" + targetIndex
                + ", tag=test1"
                + ", bookingTag=test2}";
        assertEquals(expected1, untagCommand.toString());

        untagCommand = new UntagCommand(targetIndex, null, "test2");
        String expected2 = UntagCommand.class.getCanonicalName() + "{index=" + targetIndex
                + ", tag=null"
                + ", bookingTag=test2}";
        assertEquals(expected2, untagCommand.toString());

        untagCommand = new UntagCommand(targetIndex, new Tag("test1"), null);
        String expected3 = UntagCommand.class.getCanonicalName() + "{index=" + targetIndex
                + ", tag=test1"
                + ", bookingTag=null}";
        assertEquals(expected3, untagCommand.toString());
    }
}
