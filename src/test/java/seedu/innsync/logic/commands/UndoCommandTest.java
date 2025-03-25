package seedu.innsync.logic.commands;

import static seedu.innsync.logic.commands.CommandTestUtil.VALID_BOOKINGTAG_BEACHHOUSE;
import static seedu.innsync.logic.commands.CommandTestUtil.VALID_MEMO_AMY;
import static seedu.innsync.logic.commands.CommandTestUtil.VALID_TAG_FRIEND;
import static seedu.innsync.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.innsync.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.innsync.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.innsync.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.innsync.model.Model;
import seedu.innsync.model.ModelManager;
import seedu.innsync.model.UserPrefs;
import seedu.innsync.model.person.Person;
import seedu.innsync.testutil.PersonBuilder;

public class UndoCommandTest {
    private Model model;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    }

    @Test
    public void execute_noModificationsToAddressBook_failure() {
        UndoCommand undoCommand = new UndoCommand();
        assertCommandFailure(undoCommand, model, UndoCommand.MESSAGE_FAILURE);
    }

    @Test
    public void execute_undoDelete_success() {
        Model modifiedModel = new ModelManager(this.model.getAddressBook(), new UserPrefs());
        Person person = modifiedModel.getPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        modifiedModel.deletePerson(person);
        UndoCommand undoCommand = new UndoCommand();
        assertCommandSuccess(undoCommand, modifiedModel, UndoCommand.MESSAGE_SUCCESS, model);
    }

    @Test
    public void execute_undoAdd_success() {
        Model modifiedModel = new ModelManager(this.model.getAddressBook(), new UserPrefs());
        modifiedModel.addPerson(new PersonBuilder().build());
        UndoCommand undoCommand = new UndoCommand();
        assertCommandSuccess(undoCommand, modifiedModel, UndoCommand.MESSAGE_SUCCESS, model);
    }

    @Test
    public void execute_undoStarred_success() {
        Model modifiedModel = new ModelManager(this.model.getAddressBook(), new UserPrefs());
        Person person = modifiedModel.getPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        boolean wasStarred = person.getStarred();
        modifiedModel.setPerson(person, new PersonBuilder(person).withStarred(!wasStarred).build());
        UndoCommand undoCommand = new UndoCommand();
        assertCommandSuccess(undoCommand, modifiedModel, UndoCommand.MESSAGE_SUCCESS, model);
    }

    @Test
    public void execute_undoMemo_success() {
        Model modifiedModel = new ModelManager(this.model.getAddressBook(), new UserPrefs());
        Person person = modifiedModel.getPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        modifiedModel.setPerson(person, new PersonBuilder(person).withMemo(VALID_MEMO_AMY).build());
        UndoCommand undoCommand = new UndoCommand();
        assertCommandSuccess(undoCommand, modifiedModel, UndoCommand.MESSAGE_SUCCESS, model);
    }

    @Test
    public void execute_undoTags_success() {
        Model modifiedModel = new ModelManager(this.model.getAddressBook(), new UserPrefs());
        Person person = modifiedModel.getPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        modifiedModel.setPerson(person, new PersonBuilder(person).withTags(VALID_TAG_FRIEND).build());
        UndoCommand undoCommand = new UndoCommand();
        assertCommandSuccess(undoCommand, modifiedModel, UndoCommand.MESSAGE_SUCCESS, model);
    }

    @Test
    public void execute_undoBookingTags_success() {
        Model modifiedModel = new ModelManager(this.model.getAddressBook(), new UserPrefs());
        Person person = modifiedModel.getPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        modifiedModel.setPerson(person, new PersonBuilder(person).withBookingTags(VALID_BOOKINGTAG_BEACHHOUSE).build());
        UndoCommand undoCommand = new UndoCommand();
        assertCommandSuccess(undoCommand, modifiedModel, UndoCommand.MESSAGE_SUCCESS, model);
    }
}
