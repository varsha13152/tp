package seedu.innsync.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.innsync.logic.commands.CommandTestUtil.VALID_REQUEST_BOB;
import static seedu.innsync.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.innsync.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.innsync.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.innsync.testutil.TypicalPersons.AMY;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.innsync.commons.core.index.Index;
import seedu.innsync.logic.Messages;
import seedu.innsync.model.AddressBook;
import seedu.innsync.model.Model;
import seedu.innsync.model.ModelManager;
import seedu.innsync.model.UserPrefs;
import seedu.innsync.model.person.Person;
import seedu.innsync.model.request.Request;
import seedu.innsync.testutil.PersonBuilder;

/**
 * Contains integration tests (interaction with the Model) and unit tests for
 * {@code StarCommand}.
 */
public class UnmarkRequestCommandTest {

    private Model model;
    private Model requestMarkedModel;
    private Model requestUnmarkedModel;

    @BeforeEach
    public void setUp() {
        model = getNoRequestSinglePersonModel();
        requestMarkedModel = getNoRequestSinglePersonModel();
        requestUnmarkedModel = getNoRequestSinglePersonModel();
        Person person = requestMarkedModel.getPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Request request = new Request(VALID_REQUEST_BOB);
        request.markAsCompleted();
        person.addRequest(request);
        Person unmarkedPerson = requestUnmarkedModel.getPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Request unmarkedRequest = new Request(VALID_REQUEST_BOB);
        unmarkedPerson.addRequest(unmarkedRequest);
    }

    private Model getNoRequestSinglePersonModel() {
        Model model = new ModelManager(new AddressBook(), new UserPrefs());
        model.addPerson(new PersonBuilder(AMY).withRequests(new String[0]).build());
        return model;
    }

    @Test
    public void execute_unmarkMarkedRequest_success() {
        Model markedModel = new ModelManager(requestMarkedModel.getAddressBook(), new UserPrefs());
        Person unmarkedPerson = requestUnmarkedModel.getPersonList().get(INDEX_FIRST_PERSON.getZeroBased());

        UnmarkRequestCommand unmarkCommand = new UnmarkRequestCommand(INDEX_FIRST_PERSON,
                INDEX_FIRST_PERSON);
        assertCommandSuccess(unmarkCommand, markedModel, String.format(UnmarkRequestCommand.MESSAGE_SUCCESS,
                VALID_REQUEST_BOB), requestUnmarkedModel, unmarkedPerson);
    }

    @Test
    public void execute_unmarkUnmarkedRequest_failure() {
        Model unmarkedModel = new ModelManager(requestUnmarkedModel.getAddressBook(), new UserPrefs());

        UnmarkRequestCommand unmarkCommand = new UnmarkRequestCommand(INDEX_FIRST_PERSON,
                INDEX_FIRST_PERSON);
        assertCommandFailure(unmarkCommand, unmarkedModel,
                String.format(UnmarkRequestCommand.MESSAGE_FAILURE, VALID_REQUEST_BOB));
    }

    @Test
    public void execute_unmarkIndexOutOfRange_failure() {
        Index outOfBounds = Index.fromOneBased(model.getPersonList().size() + 1);
        UnmarkRequestCommand unmarkCommand = new UnmarkRequestCommand(outOfBounds,
                INDEX_FIRST_PERSON);
        assertCommandFailure(unmarkCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_unmarkRequestIndexOutOfRange_failure() {
        Index outOfBounds = Index.fromOneBased(model.getPersonList().get(INDEX_FIRST_PERSON.getZeroBased())
                .getRequests().size() + 1);
        UnmarkRequestCommand unmarkCommand = new UnmarkRequestCommand(INDEX_FIRST_PERSON, outOfBounds);
        assertCommandFailure(unmarkCommand, model, UnmarkRequestCommand.MESSAGE_INVALID_REQUEST_INDEX);
    }

    @Test
    public void equals() {
        Index targetIndex = Index.fromOneBased(1);
        UnmarkRequestCommand unmarkRequestCommand = new UnmarkRequestCommand(targetIndex, targetIndex);
        // null unmarkRequestCommand returns false
        assertFalse(unmarkRequestCommand.equals(null));

        // unmarkRequestCommand itself returns true
        assertTrue(unmarkRequestCommand.equals(unmarkRequestCommand));
    }

    @Test
    public void hasConfirmationTest_returnsFalse() {
        Index targetIndex = Index.fromOneBased(1);
        UnmarkRequestCommand unmarkRequestCommand = new UnmarkRequestCommand(targetIndex, targetIndex);
        assertFalse(unmarkRequestCommand.requireConfirmation());
    }

    @Test
    public void toStringMethod() {
        Index targetIndex = Index.fromOneBased(1);
        UnmarkRequestCommand unmarkRequestCommand = new UnmarkRequestCommand(targetIndex, targetIndex);
        String expected = UnmarkRequestCommand.class.getCanonicalName() + "{index=" + targetIndex + "}";
        assertEquals(expected, unmarkRequestCommand.toString());
    }

}
