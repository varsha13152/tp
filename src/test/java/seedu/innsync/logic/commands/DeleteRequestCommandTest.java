package seedu.innsync.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static seedu.innsync.logic.commands.CommandTestUtil.VALID_REQUEST_AMY;
import static seedu.innsync.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.innsync.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.innsync.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.innsync.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.innsync.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.innsync.commons.core.index.Index;
import seedu.innsync.logic.Messages;
import seedu.innsync.logic.commands.exceptions.CommandException;
import seedu.innsync.model.AddressBook;
import seedu.innsync.model.Model;
import seedu.innsync.model.ModelManager;
import seedu.innsync.model.UserPrefs;
import seedu.innsync.model.person.Person;
import seedu.innsync.model.request.Request;
import seedu.innsync.testutil.PersonBuilder;

public class DeleteRequestCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_validIndices_success() throws CommandException {
        Index contactIndex = INDEX_SECOND_PERSON;
        Person personToEdit = model.getPersonList().get(contactIndex.getZeroBased());

        // Add a request to the person
        List<Request> requestToAdd = new ArrayList<>();
        requestToAdd.add(new Request(VALID_REQUEST_AMY));
        Person personWithRequest = new PersonBuilder(personToEdit)
                .withRequests(VALID_REQUEST_AMY)
                .build();

        // Set the person with request in the model
        Model setupModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        setupModel.setPerson(personToEdit, personWithRequest);

        // Create the delete request command to delete the first request
        Index requestIndex = Index.fromZeroBased(0);
        DeleteRequestCommand deleteRequestCommand = new DeleteRequestCommand(contactIndex, requestIndex);

        // Expected result is the original person without the request
        Person expectedPerson = new PersonBuilder(personToEdit).build();
        String expectedMessage = String.format(DeleteRequestCommand.MESSAGE_SUCCESS,
                VALID_REQUEST_AMY, expectedPerson.getName());

        // Create expected model
        Model expectedModel = new ModelManager(new AddressBook(setupModel.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(personWithRequest, expectedPerson);

        assertCommandSuccess(deleteRequestCommand, setupModel, expectedMessage, expectedModel, expectedPerson);
    }

    @Test
    public void execute_invalidContactIndex_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getPersonList().size() + 1);
        Index validRequestIndex = Index.fromZeroBased(0);

        DeleteRequestCommand deleteRequestCommand = new DeleteRequestCommand(outOfBoundIndex, validRequestIndex);

        assertCommandFailure(deleteRequestCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_invalidRequestIndex_throwsCommandException() throws CommandException {
        Index contactIndex = INDEX_FIRST_PERSON;
        Person personToEdit = model.getPersonList().get(contactIndex.getZeroBased());

        // Add a request to the person
        List<Request> requestToAdd = new ArrayList<>();
        requestToAdd.add(new Request(VALID_REQUEST_AMY));
        Person personWithRequest = new PersonBuilder(personToEdit)
                .withRequests(VALID_REQUEST_AMY)
                .build();

        // Set the person with request in the model
        Model setupModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        setupModel.setPerson(personToEdit, personWithRequest);

        // Create the delete request command with an invalid request index
        Index invalidRequestIndex = Index.fromZeroBased(1);
        DeleteRequestCommand deleteRequestCommand = new DeleteRequestCommand(contactIndex, invalidRequestIndex);

        assertCommandFailure(deleteRequestCommand, setupModel, DeleteRequestCommand.MESSAGE_INVALID_REQUEST_INDEX);
    }

    @Test
    public void equals() {
        Index firstContactIndex = INDEX_FIRST_PERSON;
        Index secondContactIndex = INDEX_SECOND_PERSON;
        Index firstRequestIndex = Index.fromZeroBased(0);
        Index secondRequestIndex = Index.fromZeroBased(1);

        DeleteRequestCommand deleteFirstRequestCommand = new DeleteRequestCommand(
            firstContactIndex, firstRequestIndex);
        DeleteRequestCommand deleteFirstRequestCommandCopy = new DeleteRequestCommand(
            firstContactIndex, firstRequestIndex);
        DeleteRequestCommand deleteSecondRequestCommand = new DeleteRequestCommand(
            firstContactIndex, secondRequestIndex);
        DeleteRequestCommand deleteOtherContactRequestCommand = new DeleteRequestCommand(
            secondContactIndex, firstRequestIndex);

        // same object -> returns true
        assertEquals(deleteFirstRequestCommand, deleteFirstRequestCommand);

        // same values -> returns true
        assertEquals(deleteFirstRequestCommand, deleteFirstRequestCommandCopy);

        // different request index -> returns false
        assertNotEquals(deleteFirstRequestCommand, deleteSecondRequestCommand);

        // different contact index -> returns false
        assertNotEquals(deleteFirstRequestCommand, deleteOtherContactRequestCommand);

        // different types -> returns false
        assertNotEquals(deleteFirstRequestCommand, 1);

        // null -> returns false
        assertNotEquals(deleteFirstRequestCommand, null);
    }

    @Test
    public void toStringMethod() {
        Index targetIndex = Index.fromOneBased(1);
        DeleteRequestCommand deleteRequestCommand = new DeleteRequestCommand(targetIndex, targetIndex);
        String expected = DeleteRequestCommand.class.getCanonicalName() + "{contactIndex=" + targetIndex
                + ", requestIndex=" + targetIndex + "}";
        assertEquals(expected, deleteRequestCommand.toString());
    }

    @Test
    public void requireComfirmation_returnFalse() {
        Index targetIndex = Index.fromOneBased(1);
        DeleteRequestCommand deleteRequestCommand = new DeleteRequestCommand(targetIndex, targetIndex);
        assertFalse(deleteRequestCommand.requireConfirmation());
    }
}

