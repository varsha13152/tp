package seedu.innsync.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.innsync.logic.Messages.MESSAGE_PERSONS_LISTED_OVERVIEW;
import static seedu.innsync.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.innsync.testutil.TypicalPersons.CARL;
import static seedu.innsync.testutil.TypicalPersons.ELLE;
import static seedu.innsync.testutil.TypicalPersons.FIONA;
import static seedu.innsync.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.innsync.model.Model;
import seedu.innsync.model.ModelManager;
import seedu.innsync.model.UserPrefs;

/**
 * Contains integration tests (interaction with the Model) for {@code FindCommand}.
 */
public class FindCommandTest {
    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    private Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void equals() {
        List<String> firstKeywords = Collections.singletonList("first");
        List<String> secondKeywords = Collections.singletonList("second");

        FindCommand findFirstCommand = new FindCommand(firstKeywords, FindCommand.SearchType.NAME);
        FindCommand findSecondCommand = new FindCommand(secondKeywords, FindCommand.SearchType.NAME);

        // same object -> returns true
        assertTrue(findFirstCommand.equals(findFirstCommand));

        // same values -> returns true
        FindCommand findFirstCommandCopy = new FindCommand(firstKeywords, FindCommand.SearchType.NAME);
        assertTrue(findFirstCommand.equals(findFirstCommandCopy));

        // different types -> returns false
        assertFalse(findFirstCommand.equals(1));

        // null -> returns false
        assertFalse(findFirstCommand.equals(null));

        // different person -> returns false
        assertFalse(findFirstCommand.equals(findSecondCommand));
    }

    @Test
    public void execute_zeroKeywords_noPersonFound() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 0);
        List<String> keywords = Collections.singletonList(" ");
        FindCommand command = new FindCommand(keywords, FindCommand.SearchType.NAME);
        expectedModel.updateFilteredPersonList(command.getPredicate());
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Collections.emptyList(), model.getFilteredPersonList());
    }

    @Test
    public void execute_multipleKeywords_multiplePersonsFound() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 3);
        List<String> keywords = Arrays.asList("Kurz", "Elle", "Kunz");
        FindCommand command = new FindCommand(keywords, FindCommand.SearchType.NAME);
        expectedModel.updateFilteredPersonList(command.getPredicate());
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(CARL, ELLE, FIONA), model.getFilteredPersonList());
    }

    @Test
    public void toStringMethod() {
        List<String> keywords = Arrays.asList("keyword");
        FindCommand findCommand = new FindCommand(keywords, FindCommand.SearchType.NAME);
        String expected = "FindCommand{predicate=" + findCommand.getPredicate() + ", searchType=NAME}";
        assertEquals(expected, findCommand.toString());
    }

    /**
     * Parses {@code userInput} into a list of keywords.
     */
    private List<String> prepareKeywords(String userInput) {
        return Arrays.asList(userInput.split("\\s+"));
    }
}
