package seedu.innsync.logic.commands;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.innsync.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.innsync.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.innsync.logic.commands.CommandTestUtil.showPersonAtIndex;
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

    private static final String BOOKING_TAG_STUB = "test from/2025-07-01 to/2025-07-10";

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_addBookingTagUnfilteredList_success() {
        Person firstPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person editedPerson = new PersonBuilder(firstPerson).withBookingTags(BOOKING_TAG_STUB).build();

        BookingTagCommand bookingTagCommand =
                new BookingTagCommand(INDEX_FIRST_PERSON, new BookingTag(BOOKING_TAG_STUB));
        String expectedMessage = String.format(BookingTagCommand.MESSAGE_SUCCESS, Messages.format(editedPerson));
        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(firstPerson, editedPerson);
        assertCommandSuccess(bookingTagCommand, model, expectedMessage, expectedModel);
    }
}
