package seedu.innsync.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.innsync.logic.Messages.MESSAGE_PERSONS_LISTED_OVERVIEW;
import static seedu.innsync.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.innsync.testutil.TypicalPersons.ALICE;
import static seedu.innsync.testutil.TypicalPersons.BENSON;
import static seedu.innsync.testutil.TypicalPersons.CARL;
import static seedu.innsync.testutil.TypicalPersons.DANIEL;
import static seedu.innsync.testutil.TypicalPersons.ELLE;
import static seedu.innsync.testutil.TypicalPersons.FIONA;
import static seedu.innsync.testutil.TypicalPersons.GEORGE;
import static seedu.innsync.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import seedu.innsync.commons.util.ToStringBuilder;
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
    //test for null in constructor
    public void constructor_nullSearchCriteria_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new FindCommand(null));
    }

    @Test
    public void equals() {
        Map<FindCommand.SearchType, List<String>> firstCriteria = new HashMap<>();
        firstCriteria.put(FindCommand.SearchType.NAME, Collections.singletonList("first"));

        Map<FindCommand.SearchType, List<String>> secondCriteria = new HashMap<>();
        secondCriteria.put(FindCommand.SearchType.NAME, Collections.singletonList("second"));

        Map<FindCommand.SearchType, List<String>> phoneCriteria = new HashMap<>();
        phoneCriteria.put(FindCommand.SearchType.PHONE, Collections.singletonList("first"));

        Map<FindCommand.SearchType, List<String>> emailCriteria = new HashMap<>();
        emailCriteria.put(FindCommand.SearchType.EMAIL, Collections.singletonList("first"));

        Map<FindCommand.SearchType, List<String>> addressCriteria = new HashMap<>();
        addressCriteria.put(FindCommand.SearchType.ADDRESS, Collections.singletonList("first"));

        Map<FindCommand.SearchType, List<String>> tagCriteria = new HashMap<>();
        tagCriteria.put(FindCommand.SearchType.TAG, Collections.singletonList("first"));

        Map<FindCommand.SearchType, List<String>> memoCriteria = new HashMap<>();
        memoCriteria.put(FindCommand.SearchType.MEMO, Collections.singletonList("first"));

        Map<FindCommand.SearchType, List<String>> bookingDateCriteria = new HashMap<>();
        bookingDateCriteria.put(FindCommand.SearchType.BOOKING_DATE, Collections.singletonList("2025-06-01"));

        Map<FindCommand.SearchType, List<String>> bookingPropertyCriteria = new HashMap<>();
        bookingPropertyCriteria.put(FindCommand.SearchType.BOOKING_PROPERTY, Collections.singletonList("Beach"));

        // Commands with different parameters
        FindCommand findFirstCommand = new FindCommand(firstCriteria);
        FindCommand findSecondCommand = new FindCommand(secondCriteria);

        // Different search types
        FindCommand findFirstPhoneCommand = new FindCommand(phoneCriteria);
        FindCommand findFirstEmailCommand = new FindCommand(emailCriteria);
        FindCommand findFirstAddressCommand = new FindCommand(addressCriteria);
        FindCommand findFirstTagCommand = new FindCommand(tagCriteria);
        FindCommand findFirstMemoCommand = new FindCommand(memoCriteria);
        FindCommand findFirstBookingDateCommand = new FindCommand(bookingDateCriteria);
        FindCommand findFirstBookingPropertyCommand = new FindCommand(bookingPropertyCriteria);

        // Same object - equal
        assertTrue(findFirstCommand.equals(findFirstCommand));

        // Null - not equal
        assertFalse(findFirstCommand.equals(null));

        // Different type - not equal
        assertFalse(findFirstCommand.equals(1));

        // Different keyword list - not equal
        assertFalse(findFirstCommand.equals(findSecondCommand));

        // Different search types - not equal
        assertFalse(findFirstCommand.equals(findFirstPhoneCommand));
        assertFalse(findFirstCommand.equals(findFirstEmailCommand));
        assertFalse(findFirstCommand.equals(findFirstAddressCommand));
        assertFalse(findFirstCommand.equals(findFirstTagCommand));
        assertFalse(findFirstCommand.equals(findFirstMemoCommand));
        assertFalse(findFirstCommand.equals(findFirstBookingDateCommand));
        assertFalse(findFirstCommand.equals(findFirstBookingPropertyCommand));
    }

    @Test
    public void execute_zeroKeywords_noPersonFound() {
        //test for no matches found
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 0);
        // Use a keyword that won't match any names
        Map<FindCommand.SearchType, List<String>> criteria = new HashMap<>();
        criteria.put(FindCommand.SearchType.NAME, Collections.singletonList("xyzzzy"));
        FindCommand command = new FindCommand(criteria);
        expectedModel.updateFilteredPersonList(p -> false); // Empty predicate to match command behavior
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Collections.emptyList(), model.getPersonList());
    }

    @Test
    public void execute_findName_noExceptionThrown() {
        //test for find name
        Map<FindCommand.SearchType, List<String>> criteria = new HashMap<>();
        criteria.put(FindCommand.SearchType.NAME, Collections.singletonList("Alice"));
    }

    @Test
    public void execute_emptySearchCriteria_returnsEmptyResult() {
        //test empty search criteria
        Map<FindCommand.SearchType, List<String>> criteria = new HashMap<>();
        FindCommand command = new FindCommand(criteria);

        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 0);
        expectedModel.updateFilteredPersonList(p -> false);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_nullKeywordInList_nullsFilteredOut() {
        // test null keywords
        Map<FindCommand.SearchType, List<String>> criteria = new HashMap<>();
        List<String> keywordsWithNull = Arrays.asList("valid", null, "keyword");
        criteria.put(FindCommand.SearchType.NAME, keywordsWithNull);
    }

    @Test
    public void execute_multipleSearchTypes_multiplePersonsFound() {
        //test multiple search types with multiple people found
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 3);
        Map<FindCommand.SearchType, List<String>> criteria = new HashMap<>();
        criteria.put(FindCommand.SearchType.NAME, Collections.singletonList("Kurz"));
        criteria.put(FindCommand.SearchType.ADDRESS, Collections.singletonList("tokyo"));
        criteria.put(FindCommand.SearchType.TAG, Collections.singletonList("owes money"));

        FindCommand command = new FindCommand(criteria);
        expectedModel.updateFilteredPersonList(command.getPredicate());
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(BENSON, CARL, FIONA), model.getPersonList());
    }

    @Test
    public void execute_singleKeyword_multiplePersonsFound() {
        //test single keyword but returns multiple matches
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 2);
        Map<FindCommand.SearchType, List<String>> criteria = new HashMap<>();
        criteria.put(FindCommand.SearchType.NAME, Collections.singletonList("Meier"));
        FindCommand command = new FindCommand(criteria);
        expectedModel.updateFilteredPersonList(command.getPredicate());
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(BENSON, DANIEL), model.getPersonList());
    }

    @Test
    public void execute_uniqueNameKeyword_onePersonFound() {
        //test single key word with one person found
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 1);
        Map<FindCommand.SearchType, List<String>> criteria = new HashMap<>();
        criteria.put(FindCommand.SearchType.NAME, Collections.singletonList("Pauline"));
        FindCommand command = new FindCommand(criteria);
        expectedModel.updateFilteredPersonList(command.getPredicate());
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Collections.singletonList(ALICE), model.getPersonList());
    }

    @Test
    public void execute_phoneKeyword_personFound() {
        //test search by phone number
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 1);
        Map<FindCommand.SearchType, List<String>> criteria = new HashMap<>();
        criteria.put(FindCommand.SearchType.PHONE, Collections.singletonList("87652533"));
        FindCommand command = new FindCommand(criteria);
        expectedModel.updateFilteredPersonList(command.getPredicate());
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Collections.singletonList(DANIEL), model.getPersonList());
    }

    @Test
    public void execute_partialPhoneKeyword_multiplePersonsFound() {
        //test partial keyword match
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 3);
        Map<FindCommand.SearchType, List<String>> criteria = new HashMap<>();
        criteria.put(FindCommand.SearchType.PHONE, Collections.singletonList("9482"));
        FindCommand command = new FindCommand(criteria);
        expectedModel.updateFilteredPersonList(command.getPredicate());
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(ELLE, FIONA, GEORGE), model.getPersonList());
    }

    @Test
    public void execute_emailKeyword_personFound() {
        //test find by email keyword
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 1);
        Map<FindCommand.SearchType, List<String>> criteria = new HashMap<>();
        criteria.put(FindCommand.SearchType.EMAIL, Collections.singletonList("anna"));
        FindCommand command = new FindCommand(criteria);
        expectedModel.updateFilteredPersonList(command.getPredicate());
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Collections.singletonList(GEORGE), model.getPersonList());
    }

    @Test
    public void execute_domainEmailKeyword_allPersonsFound() {
        //test find by email keyword which is a domain
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 7);
        Map<FindCommand.SearchType, List<String>> criteria = new HashMap<>();
        criteria.put(FindCommand.SearchType.EMAIL, Collections.singletonList("example.com"));
        FindCommand command = new FindCommand(criteria);
        expectedModel.updateFilteredPersonList(command.getPredicate());
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(ALICE, BENSON, CARL, DANIEL, ELLE, FIONA, GEORGE), model.getPersonList());
    }

    @Test
    public void execute_addressKeyword_personsFound() {
        //test find by address
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 3);
        Map<FindCommand.SearchType, List<String>> criteria = new HashMap<>();
        criteria.put(FindCommand.SearchType.ADDRESS, Collections.singletonList("street"));
        FindCommand command = new FindCommand(criteria);
        expectedModel.updateFilteredPersonList(command.getPredicate());
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(CARL, DANIEL, GEORGE), model.getPersonList());
    }

    @Test
    public void execute_uniqueAddressKeyword_onePersonFound() {
        //test find by address which only returns 1 match
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 1);
        Map<FindCommand.SearchType, List<String>> criteria = new HashMap<>();
        criteria.put(FindCommand.SearchType.ADDRESS, Collections.singletonList("tokyo"));
        FindCommand command = new FindCommand(criteria);
        expectedModel.updateFilteredPersonList(command.getPredicate());
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Collections.singletonList(FIONA), model.getPersonList());
    }

    @Test
    public void execute_tagKeyword_multiplePersonsFound() {
        //test find by tag keyword which only returns multiple matches
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 3);
        Map<FindCommand.SearchType, List<String>> criteria = new HashMap<>();
        criteria.put(FindCommand.SearchType.TAG, Collections.singletonList("friends"));
        FindCommand command = new FindCommand(criteria);
        expectedModel.updateFilteredPersonList(command.getPredicate());
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(ALICE, BENSON, DANIEL), model.getPersonList());
    }

    @Test
    public void execute_uniqueTagKeyword_onePersonFound() {
        //test find by address keyword which only returns 1 match
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 1);
        Map<FindCommand.SearchType, List<String>> criteria = new HashMap<>();
        criteria.put(FindCommand.SearchType.TAG, Collections.singletonList("owes money"));
        FindCommand command = new FindCommand(criteria);
        expectedModel.updateFilteredPersonList(command.getPredicate());
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Collections.singletonList(BENSON), model.getPersonList());
    }

    @Test
    public void execute_multipleTagKeywords_multiplePersonsFound() {
        //test find by multiple tag keywords which returns multiple matches
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 3);
        Map<FindCommand.SearchType, List<String>> criteria = new HashMap<>();
        criteria.put(FindCommand.SearchType.TAG, Arrays.asList("friends", "owes money"));
        FindCommand command = new FindCommand(criteria);
        expectedModel.updateFilteredPersonList(command.getPredicate());
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(ALICE, BENSON, DANIEL), model.getPersonList());
    }

    @Test
    public void execute_caseInsensitiveSearch_personsFound() {
        //test case insensitivity
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 1);
        Map<FindCommand.SearchType, List<String>> criteria = new HashMap<>();
        criteria.put(FindCommand.SearchType.NAME, Collections.singletonList("alice"));
        FindCommand command = new FindCommand(criteria);
        expectedModel.updateFilteredPersonList(command.getPredicate());
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Collections.singletonList(ALICE), model.getPersonList());

        expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 1);
        criteria = new HashMap<>();
        criteria.put(FindCommand.SearchType.NAME, Collections.singletonList("PAULINE"));
        command = new FindCommand(criteria);
        expectedModel.updateFilteredPersonList(command.getPredicate());
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Collections.singletonList(ALICE), model.getPersonList());

        expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 3);
        criteria = new HashMap<>();
        criteria.put(FindCommand.SearchType.TAG, Collections.singletonList("FrIeNdS"));
        command = new FindCommand(criteria);
        expectedModel.updateFilteredPersonList(command.getPredicate());
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(ALICE, BENSON, DANIEL), model.getPersonList());
    }

    @Test
    public void execute_memoKeyword_personsFound() {
        //test find by memo
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 1);
        Map<FindCommand.SearchType, List<String>> criteria = new HashMap<>();
        criteria.put(FindCommand.SearchType.MEMO, Collections.singletonList("term"));
        FindCommand command = new FindCommand(criteria);
        expectedModel.updateFilteredPersonList(command.getPredicate());
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Collections.singletonList(ALICE), model.getPersonList());
    }

    @Test
    public void execute_memoCaseInsensitive_personsFound() {
        //test case insensitivity in memo keyword
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 1);
        Map<FindCommand.SearchType, List<String>> criteria = new HashMap<>();
        criteria.put(FindCommand.SearchType.MEMO, Collections.singletonList("FOOD"));
        FindCommand command = new FindCommand(criteria);
        expectedModel.updateFilteredPersonList(command.getPredicate());
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Collections.singletonList(BENSON), model.getPersonList());
    }

    @Test
    public void execute_multipleMemoKeywords_multiplePersonsFound() {
        //test find by multiple memo keywords and multiple matches found
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 2);
        Map<FindCommand.SearchType, List<String>> criteria = new HashMap<>();
        criteria.put(FindCommand.SearchType.MEMO, Arrays.asList("stay", "extra"));
        FindCommand command = new FindCommand(criteria);
        expectedModel.updateFilteredPersonList(command.getPredicate());
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(ALICE, BENSON), model.getPersonList());
    }

    @Test
    public void execute_bookingPropertyKeyword_personsFound() {
        //test find by booking property
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 1);
        Map<FindCommand.SearchType, List<String>> criteria = new HashMap<>();
        criteria.put(FindCommand.SearchType.BOOKING_PROPERTY, Collections.singletonList("Beach"));
        FindCommand command = new FindCommand(criteria);
        expectedModel.updateFilteredPersonList(command.getPredicate());
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Collections.singletonList(GEORGE), model.getPersonList());
    }

    @Test
    public void execute_bookingPropertyCaseInsensitive_personsFound() {
        //test case sensitivity of booking property keyword search
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 1);
        Map<FindCommand.SearchType, List<String>> criteria = new HashMap<>();
        criteria.put(FindCommand.SearchType.BOOKING_PROPERTY, Collections.singletonList("beach"));
        FindCommand command = new FindCommand(criteria);
        expectedModel.updateFilteredPersonList(command.getPredicate());
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Collections.singletonList(GEORGE), model.getPersonList());
    }

    @Test
    public void execute_bookingDateKeyword_personsFound() {
        //test find by booking date
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 1);
        Map<FindCommand.SearchType, List<String>> criteria = new HashMap<>();
        criteria.put(FindCommand.SearchType.BOOKING_DATE, Collections.singletonList("2025-06-05"));
        FindCommand command = new FindCommand(criteria);
        expectedModel.updateFilteredPersonList(command.getPredicate());
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Collections.singletonList(GEORGE), model.getPersonList());
    }

    @Test
    public void execute_bookingDateInvalidFormat_noExceptionThrown() {
        //test invalid booking date format
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 0);
        Map<FindCommand.SearchType, List<String>> criteria = new HashMap<>();
        criteria.put(FindCommand.SearchType.BOOKING_DATE, Collections.singletonList("06-05-2025"));
        FindCommand command = new FindCommand(criteria);
        expectedModel.updateFilteredPersonList(p -> false);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Collections.emptyList(), model.getPersonList());
    }

    @Test
    public void execute_bookingDateStartDay_personsFound() {
        //test find by booking date
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 1);
        Map<FindCommand.SearchType, List<String>> criteria = new HashMap<>();
        criteria.put(FindCommand.SearchType.BOOKING_DATE, Collections.singletonList("2025-06-01"));
        FindCommand command = new FindCommand(criteria);
        expectedModel.updateFilteredPersonList(command.getPredicate());
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Collections.singletonList(GEORGE), model.getPersonList());
    }

    @Test
    public void execute_bookingDateEndDay_personsFound() {
        //test find by booking date
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 1);
        Map<FindCommand.SearchType, List<String>> criteria = new HashMap<>();
        criteria.put(FindCommand.SearchType.BOOKING_DATE, Collections.singletonList("2025-06-10"));
        FindCommand command = new FindCommand(criteria);
        expectedModel.updateFilteredPersonList(command.getPredicate());
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Collections.singletonList(GEORGE), model.getPersonList());
    }

    @Test
    public void execute_bookingDateOutsideRange_noPersonsFound() {
        //test find by booking date with no results
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 0);
        Map<FindCommand.SearchType, List<String>> criteria = new HashMap<>();
        criteria.put(FindCommand.SearchType.BOOKING_DATE, Collections.singletonList("2025-06-11"));
        FindCommand command = new FindCommand(criteria);
        expectedModel.updateFilteredPersonList(p -> false); // Empty predicate to match command behavior
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Collections.emptyList(), model.getPersonList());
    }

    @Test
    public void execute_multipleSearchCriteriaOr_personsFound() {
       //  test multiple search fields
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 3);
        Map<FindCommand.SearchType, List<String>> criteria = new HashMap<>();
        criteria.put(FindCommand.SearchType.NAME, Collections.singletonList("Meier"));
        criteria.put(FindCommand.SearchType.TAG, Collections.singletonList("friends"));
        criteria.put(FindCommand.SearchType.PHONE, Collections.singletonList("87652533"));

        FindCommand command = new FindCommand(criteria);
        expectedModel.updateFilteredPersonList(command.getPredicate());
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(ALICE, BENSON, DANIEL), model.getPersonList());
    }

    @Test
    public void execute_bookingCriteriaWithOthers_personsFound() {
        //test multiple search fields
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 3);
        Map<FindCommand.SearchType, List<String>> criteria = new HashMap<>();
        criteria.put(FindCommand.SearchType.NAME, Collections.singletonList("George"));
        criteria.put(FindCommand.SearchType.BOOKING_PROPERTY, Collections.singletonList("Beach"));
        criteria.put(FindCommand.SearchType.ADDRESS, Collections.singletonList("street"));

        FindCommand command = new FindCommand(criteria);
        expectedModel.updateFilteredPersonList(command.getPredicate());
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(CARL, DANIEL, GEORGE), model.getPersonList());
    }

    @Test
    public void toStringMethod() {
        //test to string method
        Map<FindCommand.SearchType, List<String>> criteria = new HashMap<>();
        criteria.put(FindCommand.SearchType.NAME, Collections.singletonList("keyword"));
        FindCommand findCommand = new FindCommand(criteria);

        String expected = new ToStringBuilder(findCommand)
                .add("searchCriteria", criteria)
                .toString();

        assertEquals(expected, findCommand.toString());
    }
}
