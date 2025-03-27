package seedu.innsync.logic.commands;

import static seedu.innsync.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.innsync.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.innsync.model.Model.PREDICATE_SHOW_STARRED_PERSONS;
import static seedu.innsync.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.innsync.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.innsync.model.Model;
import seedu.innsync.model.ModelManager;
import seedu.innsync.model.UserPrefs;

/**
 * Contains integration tests (interaction with the Model) and unit tests for ListStarCommand.
 */
public class ListStarCommandTest {

    private Model model;
    private Model expectedModel;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.updateFilteredPersonList(PREDICATE_SHOW_STARRED_PERSONS);
    }

    @Test
    public void execute_listIsNotFiltered_showsOnlyStarredPersons() {
        assertCommandSuccess(new ListStarCommand(), model, ListStarCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void execute_listIsFiltered_showsOnlyStarredPersons() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);
        assertCommandSuccess(new ListStarCommand(), model, ListStarCommand.MESSAGE_SUCCESS, expectedModel);
    }
}
