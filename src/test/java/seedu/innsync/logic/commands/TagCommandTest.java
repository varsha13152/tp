package seedu.innsync.logic.commands;

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

        String expectedMessage = String.format(TagCommand.MESSAGE_SUCCESS, Messages.format(editedPerson));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(firstPerson, editedPerson);

        assertCommandSuccess(tagCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_validTag_success() throws Exception {
        Index indexFirstPerson = Index.fromOneBased(INDEX_FIRST_PERSON.getOneBased());
        Person firstPerson = model.getPersonList().get(indexFirstPerson.getZeroBased());

        Set<Tag> validTags = Set.of(new Tag(VALID_TAG_HUSBAND));

        Person editedPerson = new PersonBuilder(firstPerson)
                .withTags("friends", VALID_TAG_HUSBAND)
                .build();

        TagCommand tagCommand = new TagCommand(indexFirstPerson, validTags, null);

        String expectedMessage = String.format(TagCommand.MESSAGE_SUCCESS, Messages.format(editedPerson));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(firstPerson, editedPerson);

        assertCommandSuccess(tagCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_validTagsAndBookingTags_success() throws Exception {
        Index indexFirstPerson = Index.fromOneBased(INDEX_FIRST_PERSON.getOneBased());
        Person firstPerson = model.getPersonList().get(indexFirstPerson.getZeroBased());

        Set<Tag> validTags = Set.of(new Tag(VALID_TAG_HUSBAND));
        Set<BookingTag> validBookingTags = Set.of(
                new BookingTag(VALID_BOOKINGTAG_HOTEL),
                new BookingTag(VALID_BOOKINGTAG_BEACHHOUSE)
        );

        // since tag command is now additive, old tags need to be added manually
        Person editedPerson = new PersonBuilder(firstPerson)
                .withTags("friends", VALID_TAG_HUSBAND)
                .withBookingTags(VALID_BOOKINGTAG_HOTEL, VALID_BOOKINGTAG_BEACHHOUSE)
                .build();

        TagCommand tagCommand = new TagCommand(indexFirstPerson, validTags, validBookingTags);

        String expectedMessage = String.format(TagCommand.MESSAGE_SUCCESS, Messages.format(editedPerson));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(firstPerson, editedPerson);

        assertCommandSuccess(tagCommand, model, expectedMessage, expectedModel);
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

        BookingTag overlappingBookingTag = new BookingTag(OVERLAPPING_BOOKINGTAG_INN);
        Set<BookingTag> overlappingBookingTags = Set.of(
                overlappingBookingTag,
                new BookingTag(VALID_BOOKINGTAG_BEACHHOUSE)
        );

        TagCommand bookingTagCommand = new TagCommand(indexLastPerson, null, overlappingBookingTags);
        String expectedMessage = String.format(TagCommand.MESSAGE_FAILURE, overlappingBookingTag.toPrettier());

        assertCommandFailure(bookingTagCommand, model, expectedMessage);
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
}
