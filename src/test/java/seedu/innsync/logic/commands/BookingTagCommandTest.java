package seedu.innsync.logic.commands;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.innsync.logic.commands.CommandTestUtil.VALID_BOOKINGTAG_BEACHHOUSE;
import static seedu.innsync.logic.commands.CommandTestUtil.VALID_BOOKINGTAG_HOTEL;
import static seedu.innsync.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.innsync.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.innsync.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.innsync.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.innsync.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.innsync.commons.core.index.Index;
import seedu.innsync.logic.Messages;
import seedu.innsync.model.AddressBook;
import seedu.innsync.model.Model;
import seedu.innsync.model.ModelManager;
import seedu.innsync.model.UserPrefs;
import seedu.innsync.model.person.Person;
import seedu.innsync.model.tag.BookingTag;
import seedu.innsync.testutil.PersonBuilder;

public class BookingTagCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_validIndexUnfilteredList_success() throws Exception {
        Index indexLastPerson = Index.fromOneBased(model.getPersonList().size());
        Person lastPerson = model.getPersonList().get(indexLastPerson.getZeroBased());

        BookingTag validBookingTag = new BookingTag(VALID_BOOKINGTAG_BEACHHOUSE);

        Person editedPerson = new PersonBuilder(lastPerson)
                .withBookingTags(VALID_BOOKINGTAG_BEACHHOUSE)
                .build();

        BookingTagCommand bookingTagCommand = new BookingTagCommand(indexLastPerson, validBookingTag);

        String expectedMessage = String.format(BookingTagCommand.MESSAGE_SUCCESS, Messages.format(editedPerson));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(lastPerson, editedPerson);

        assertCommandSuccess(bookingTagCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getPersonList().size() + 1);
        BookingTagCommand command = new BookingTagCommand(outOfBoundIndex,
                new BookingTag(VALID_BOOKINGTAG_BEACHHOUSE));

        assertCommandFailure(command, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        final BookingTagCommand standardCommand = new BookingTagCommand(INDEX_FIRST_PERSON,
                new BookingTag(VALID_BOOKINGTAG_BEACHHOUSE));

        // same values -> returns true
        BookingTagCommand commandWithSameValues = new BookingTagCommand(INDEX_FIRST_PERSON,
                new BookingTag(VALID_BOOKINGTAG_BEACHHOUSE));
        assertTrue(standardCommand.equals(commandWithSameValues));

        // same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));

        // null -> returns false
        assertFalse(standardCommand.equals(null));

        // different types -> returns false
        assertFalse(standardCommand.equals(new ClearCommand()));

        // different index -> returns false
        assertFalse(standardCommand.equals(new BookingTagCommand(INDEX_SECOND_PERSON,
                new BookingTag(VALID_BOOKINGTAG_BEACHHOUSE))));

        // same date -> returns true
        assertTrue(standardCommand.equals(new BookingTagCommand(INDEX_FIRST_PERSON,
                new BookingTag(VALID_BOOKINGTAG_HOTEL))));
    }
}
