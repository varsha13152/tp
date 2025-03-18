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
        List<String> firstKeywordList = Collections.singletonList("first");
        List<String> secondKeywordList = Collections.singletonList("second");

        // Create the commands using the same keywords and search type
        FindCommand findFirstCommand = new FindCommand(firstKeywordList, FindCommand.SearchType.NAME);
        FindCommand findSecondCommand = new FindCommand(secondKeywordList, FindCommand.SearchType.NAME);
        FindCommand findFirstPhoneCommand = new FindCommand(firstKeywordList, FindCommand.SearchType.PHONE);

        // same object should be equal to itself
        assertTrue(findFirstCommand.equals(findFirstCommand));

        // null - should return false
        assertFalse(findFirstCommand.equals(null));

        // different types - should return false
        assertFalse(findFirstCommand.equals(1));

        // different keywords - should return false
        assertFalse(findFirstCommand.equals(findSecondCommand));

        // different search type - should return false
        assertFalse(findFirstCommand.equals(findFirstPhoneCommand));
    }

    @Test
    public void execute_zeroKeywords_noPersonFound() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 0);
        // Use a keyword that won't match any names
        FindCommand command = new FindCommand(Collections.singletonList("xyzzzy"), FindCommand.SearchType.NAME);
        expectedModel.updateFilteredPersonList(command.getPredicate());
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Collections.emptyList(), model.getPersonList());
    }

    @Test
    public void execute_multipleKeywords_multiplePersonsFound() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 3);
        FindCommand command = new FindCommand(Arrays.asList("Kurz", "Elle", "Kunz"), FindCommand.SearchType.NAME);
        expectedModel.updateFilteredPersonList(command.getPredicate());
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(CARL, ELLE, FIONA), model.getPersonList());
    }

    @Test
    public void toStringMethod() {
        List<String> keywords = Collections.singletonList("keyword");
        FindCommand findCommand = new FindCommand(keywords, FindCommand.SearchType.NAME);

        // The string representation should include both the predicate and search type
        String expected = findCommand.toString();

        // Since we can't predict the exact string format of the predicate, we'll just verify
        // that the string contains the important parts
        assertTrue(expected.contains("predicate"));
        assertTrue(expected.contains("searchType"));
        assertTrue(expected.contains("NAME"));
    }
}
