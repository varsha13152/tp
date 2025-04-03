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
public class MarkRequestCommandTest {

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
    public void execute_markRequest_success() {
        Model unmarkedModel = new ModelManager(requestUnmarkedModel.getAddressBook(), new UserPrefs());
        Person markedPerson = requestMarkedModel.getPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Request request = markedPerson.getRequests().get(INDEX_FIRST_PERSON.getZeroBased());

        MarkRequestCommand markCommand = new MarkRequestCommand(INDEX_FIRST_PERSON,
                INDEX_FIRST_PERSON);
        assertCommandSuccess(markCommand, unmarkedModel, String.format(MarkRequestCommand.MESSAGE_SUCCESS,
                request.getRequestName()), requestMarkedModel, markedPerson);
    }

    @Test
    public void execute_markMarkedRequest_failure() {
        Model markedModel = new ModelManager(requestMarkedModel.getAddressBook(), new UserPrefs());
        Request request = requestMarkedModel.getPersonList().get(INDEX_FIRST_PERSON.getZeroBased())
                .getRequests().get(INDEX_FIRST_PERSON.getZeroBased());
        MarkRequestCommand markCommand = new MarkRequestCommand(INDEX_FIRST_PERSON,
                INDEX_FIRST_PERSON);
        assertCommandFailure(markCommand, markedModel,
                String.format(MarkRequestCommand.MESSAGE_FAILURE_ALREADY_MARKED, request.getRequestName()));
    }

    @Test
    public void execute_markRequestIndexOutOfRange_failure() {
        Index outOfBounds = Index.fromOneBased(model.getPersonList().get(INDEX_FIRST_PERSON.getZeroBased())
                .getRequests().size() + 1);
        MarkRequestCommand markCommand = new MarkRequestCommand(INDEX_FIRST_PERSON, outOfBounds);
        assertCommandFailure(markCommand, model, MarkRequestCommand.MESSAGE_FAILURE_INVALID_REQUEST_INDEX);
    }

    @Test
    public void execute_markIndexOutOfRange_failure() {
        Index outOfBounds = Index.fromOneBased(model.getPersonList().size() + 1);
        MarkRequestCommand markCommand = new MarkRequestCommand(outOfBounds, INDEX_FIRST_PERSON);
        assertCommandFailure(markCommand, model, MarkRequestCommand.MESSAGE_FAILURE_INVALID_INDEX);
    }

    @Test
    public void equals() {
        Index targetIndex = Index.fromOneBased(1);
        MarkRequestCommand markRequestCommand = new MarkRequestCommand(targetIndex, targetIndex);
        // null markRequestCommand returns false
        assertFalse(markRequestCommand.equals(null));

        // markRequestCommand itself returns true
        assertTrue(markRequestCommand.equals(markRequestCommand));
    }

    @Test
    public void hasConfirmationTest_returnsFalse() {
        Index targetIndex = Index.fromOneBased(1);
        MarkRequestCommand markRequestCommand = new MarkRequestCommand(targetIndex, targetIndex);
        assertFalse(markRequestCommand.requireConfirmation());
    }

    @Test
    public void toStringMethod() {
        Index targetIndex = Index.fromOneBased(1);
        MarkRequestCommand markRequestCommand = new MarkRequestCommand(targetIndex, targetIndex);
        String expected = MarkRequestCommand.class.getCanonicalName() + "{index=" + targetIndex + "}";
        assertEquals(expected, markRequestCommand.toString());
    }

}
