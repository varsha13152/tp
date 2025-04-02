package seedu.innsync.logic.commands;

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
    public void execute_unmarkIndexOutOfRange_failure() {
        Index outOfBounds = Index.fromOneBased(model.getPersonList().size() + 1);
        MarkRequestCommand markCommand = new MarkRequestCommand(INDEX_FIRST_PERSON, outOfBounds);
        assertCommandFailure(markCommand, model, MarkRequestCommand.MESSAGE_FAILURE_INVALID_INDEX);
    }

}
