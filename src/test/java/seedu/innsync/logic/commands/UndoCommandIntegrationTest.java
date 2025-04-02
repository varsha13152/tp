package seedu.innsync.logic.commands;

import static seedu.innsync.logic.commands.CommandTestUtil.VALID_BOOKINGTAG_BEACHHOUSE;
import static seedu.innsync.logic.commands.CommandTestUtil.VALID_MEMO_AMY;
import static seedu.innsync.logic.commands.CommandTestUtil.VALID_REQUEST_BOB;
import static seedu.innsync.logic.commands.CommandTestUtil.VALID_TAG_FRIEND;
import static seedu.innsync.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.innsync.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.innsync.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.innsync.testutil.TypicalPersons.AMY;
import static seedu.innsync.testutil.TypicalPersons.BOB;
import static seedu.innsync.testutil.TypicalPersons.HOON;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.innsync.logic.commands.exceptions.CommandException;
import seedu.innsync.model.AddressBook;
import seedu.innsync.model.Model;
import seedu.innsync.model.ModelManager;
import seedu.innsync.model.UserPrefs;
import seedu.innsync.model.person.Memo;
import seedu.innsync.model.person.Person;
import seedu.innsync.model.request.Request;
import seedu.innsync.model.tag.BookingTag;
import seedu.innsync.model.tag.Tag;
import seedu.innsync.testutil.PersonBuilder;

public class UndoCommandIntegrationTest {
    private Model model;

    @BeforeEach
    public void setUp() {
        model = getNoRequestsNoTagsTwoPersonModel();
    }

    @Test
    public void execute_undoClear_success() {
        Model modifiedModel = new ModelManager(this.model.getAddressBook(), new UserPrefs());
        ClearCommand clearCommand = new ClearCommand();
        clearCommand.execute(modifiedModel);
        UndoCommand undoCommand = new UndoCommand();
        assertCommandSuccess(undoCommand, modifiedModel, UndoCommand.MESSAGE_SUCCESS, model);
    }

    @Test
    public void execute_undoDelete_success() throws CommandException {
        Model modifiedModel = new ModelManager(this.model.getAddressBook(), new UserPrefs());
        DeleteCommand deleteCommand = new DeleteCommand(INDEX_FIRST_PERSON);
        deleteCommand.execute(modifiedModel);
        UndoCommand undoCommand = new UndoCommand();
        assertCommandSuccess(undoCommand, modifiedModel, UndoCommand.MESSAGE_SUCCESS, model);
    }

    @Test
    public void execute_undoAdd_success() throws CommandException {
        Model modifiedModel = new ModelManager(this.model.getAddressBook(), new UserPrefs());
        AddCommand addCommand = new AddCommand(HOON);
        addCommand.execute(modifiedModel);
        UndoCommand undoCommand = new UndoCommand();
        assertCommandSuccess(undoCommand, modifiedModel, UndoCommand.MESSAGE_SUCCESS, model);
    }

    @Test
    public void execute_undoStarred_success() throws CommandException {
        Model modifiedModel = new ModelManager(this.model.getAddressBook(), new UserPrefs());
        StarCommand starCommand = new StarCommand(INDEX_FIRST_PERSON);
        starCommand.execute(modifiedModel);
        UndoCommand undoCommand = new UndoCommand();
        assertCommandSuccess(undoCommand, modifiedModel, UndoCommand.MESSAGE_SUCCESS, model);
    }

    @Test
    public void execute_undoUnstarred_success() throws CommandException {
        Model starredModel = new ModelManager(this.model.getAddressBook(), new UserPrefs());
        Person person = starredModel.getPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        starredModel.setPerson(person, new PersonBuilder(person).withStarred(true).build());

        Model modifiedModel = new ModelManager(starredModel.getAddressBook(), new UserPrefs());
        UnstarCommand unstarCommand = new UnstarCommand(INDEX_FIRST_PERSON);
        unstarCommand.execute(modifiedModel);
        UndoCommand undoCommand = new UndoCommand();
        assertCommandSuccess(undoCommand, modifiedModel, UndoCommand.MESSAGE_SUCCESS, starredModel);
    }

    @Test
    public void execute_undoMemo_success() throws CommandException {
        Model modifiedModel = new ModelManager(this.model.getAddressBook(), new UserPrefs());
        MemoCommand memoCommand = new MemoCommand(INDEX_FIRST_PERSON, new Memo(VALID_MEMO_AMY));
        memoCommand.execute(modifiedModel);
        UndoCommand undoCommand = new UndoCommand();
        assertCommandSuccess(undoCommand, modifiedModel, UndoCommand.MESSAGE_SUCCESS, model);
    }

    @Test
    public void execute_undoTags_success() throws CommandException {
        Model modifiedModel = new ModelManager(this.model.getAddressBook(), new UserPrefs());
        TagCommand tagCommand = new TagCommand(INDEX_FIRST_PERSON,
                Set.of(new Tag(VALID_TAG_FRIEND)),
                null);
        tagCommand.execute(modifiedModel);
        UndoCommand undoCommand = new UndoCommand();
        assertCommandSuccess(undoCommand, modifiedModel, UndoCommand.MESSAGE_SUCCESS, model);
    }

    @Test
    public void execute_undoBookingTags_success() throws CommandException {
        Model modifiedModel = new ModelManager(this.model.getAddressBook(), new UserPrefs());
        TagCommand tagCommand = new TagCommand(INDEX_FIRST_PERSON,
                null,
                Set.of(new BookingTag(VALID_BOOKINGTAG_BEACHHOUSE)));
        tagCommand.execute(modifiedModel);
        UndoCommand undoCommand = new UndoCommand();
        assertCommandSuccess(undoCommand, modifiedModel, UndoCommand.MESSAGE_SUCCESS, model);
    }

    @Test
    public void execute_undoAddRequest_success() throws CommandException {
        Model modifiedModel = getNoRequestsNoTagsTwoPersonModel();
        RequestCommand requestCommand = new RequestCommand(INDEX_FIRST_PERSON,
                new ArrayList<>(Arrays.asList(new Request(VALID_REQUEST_BOB))));
        requestCommand.execute(modifiedModel);
        UndoCommand undoCommand = new UndoCommand();
        assertCommandSuccess(undoCommand, modifiedModel, UndoCommand.MESSAGE_SUCCESS, model);
    }

    @Test
    public void execute_undoMarkRequest_success() throws CommandException {
        Model requestModel = getNoRequestsNoTagsTwoPersonModel();
        RequestCommand requestCommand = new RequestCommand(INDEX_SECOND_PERSON,
                new ArrayList<>(Arrays.asList(new Request(VALID_REQUEST_BOB))));
        requestCommand.execute(requestModel);
        Model modifiedModel = new ModelManager(requestModel.getAddressBook(), new UserPrefs());
        MarkRequestCommand markCommand = new MarkRequestCommand(INDEX_SECOND_PERSON,
                INDEX_FIRST_PERSON);
        markCommand.execute(modifiedModel);
        UndoCommand undoCommand = new UndoCommand();
        assertCommandSuccess(undoCommand, modifiedModel, UndoCommand.MESSAGE_SUCCESS, requestModel);
    }

    @Test
    public void execute_undoUnmarkRequest_success() throws CommandException {
        Model requestModel = getNoRequestsNoTagsTwoPersonModel();
        RequestCommand requestCommand = new RequestCommand(INDEX_SECOND_PERSON,
                new ArrayList<>(Arrays.asList(new Request(VALID_REQUEST_BOB))));
        requestCommand.execute(requestModel);
        MarkRequestCommand markCommand = new MarkRequestCommand(INDEX_SECOND_PERSON,
                INDEX_FIRST_PERSON);
        markCommand.execute(requestModel);
        Model modifiedModel = new ModelManager(requestModel.getAddressBook(), new UserPrefs());
        UnmarkRequestCommand unmarkCommand = new UnmarkRequestCommand(INDEX_SECOND_PERSON,
                INDEX_FIRST_PERSON);
        unmarkCommand.execute(modifiedModel);
        UndoCommand undoCommand = new UndoCommand();
        assertCommandSuccess(undoCommand, modifiedModel, UndoCommand.MESSAGE_SUCCESS, requestModel);
    }

    private Model getNoRequestsNoTagsTwoPersonModel() {
        Model model = new ModelManager(new AddressBook(), new UserPrefs());
        model.addPerson(new PersonBuilder(AMY).withRequests(new String[0]).withTags(new HashSet<>()).build());
        model.addPerson(new PersonBuilder(BOB).withRequests(new String[0]).withTags(new HashSet<>()).build());
        return model;
    }
}
