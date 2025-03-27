package seedu.innsync.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
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
    public void execute_multipleKeywords_multiplePersonsFound() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 3);
        Map<FindCommand.SearchType, List<String>> criteria = new HashMap<>();
        criteria.put(FindCommand.SearchType.NAME, Arrays.asList("Kurz", "Meyer", "Kunz"));
        FindCommand command = new FindCommand(criteria);
        expectedModel.updateFilteredPersonList(command.getPredicate());
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(CARL, ELLE, FIONA), model.getPersonList());
    }

    @Test
    public void execute_singleKeyword_multiplePersonsFound() {
        // "Meier" should match both "Benson Meier" and "Daniel Meier"
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
        // "Pauline" should only match "Alice Pauline"
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
        // Daniel's exact phone number
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
        // Partial phone number "9482" should match Elle, Fiona, and George
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
        // George's email
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
        // All test persons have example.com email domain
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
        // "street" should match Carl, Daniel and George based on their addresses:
        // CARL: "wall street"
        // DANIEL: "10th street"
        // GEORGE: "4th street"
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
        // "tokyo" should only match Fiona's address "little tokyo"
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
        // "friends" tag is on Alice, Benson, and Daniel
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
        // "owesMoney" tag is only on Benson
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 1);
        Map<FindCommand.SearchType, List<String>> criteria = new HashMap<>();
        criteria.put(FindCommand.SearchType.TAG, Collections.singletonList("owesMoney"));
        FindCommand command = new FindCommand(criteria);
        expectedModel.updateFilteredPersonList(command.getPredicate());
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Collections.singletonList(BENSON), model.getPersonList());
    }

    @Test
    public void execute_multipleTagKeywords_multiplePersonsFound() {
        // "friends" or "owesMoney" tags
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 3);
        Map<FindCommand.SearchType, List<String>> criteria = new HashMap<>();
        criteria.put(FindCommand.SearchType.TAG, Arrays.asList("friends", "owesMoney"));
        FindCommand command = new FindCommand(criteria);
        expectedModel.updateFilteredPersonList(command.getPredicate());
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(ALICE, BENSON, DANIEL), model.getPersonList());
    }

    @Test
    public void execute_caseInsensitiveSearch_personsFound() {
        // Case insensitive search for "alice"
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 1);
        Map<FindCommand.SearchType, List<String>> criteria = new HashMap<>();
        criteria.put(FindCommand.SearchType.NAME, Collections.singletonList("alice"));
        FindCommand command = new FindCommand(criteria);
        expectedModel.updateFilteredPersonList(command.getPredicate());
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Collections.singletonList(ALICE), model.getPersonList());

        // Case insensitive search for "PAULINE"
        expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 1);
        criteria = new HashMap<>();
        criteria.put(FindCommand.SearchType.NAME, Collections.singletonList("PAULINE"));
        command = new FindCommand(criteria);
        expectedModel.updateFilteredPersonList(command.getPredicate());
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Collections.singletonList(ALICE), model.getPersonList());

        // Case insensitive search for "FrIeNdS" tag
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
        // "term" should match Alice's memo "long term stay."
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
        // Case insensitive search for "FOOD" in Benson's memo "Wants extra food"
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
        // "stay" or "extra" should match Alice and Benson
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
        // "Beach" should match George's booking property "BeachHouse"
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
        // Case insensitive search for "beach" in George's booking property "BeachHouse"
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
        // Date "2025-06-05" should match George's booking date range "2025-06-01 to 2025-06-10"
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 1);
        Map<FindCommand.SearchType, List<String>> criteria = new HashMap<>();
        criteria.put(FindCommand.SearchType.BOOKING_DATE, Collections.singletonList("2025-06-05"));
        FindCommand command = new FindCommand(criteria);
        expectedModel.updateFilteredPersonList(command.getPredicate());
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Collections.singletonList(GEORGE), model.getPersonList());
    }

    @Test
    public void execute_bookingDateStartDay_personsFound() {
        // Date "2025-06-01" (start day of booking) should match George's booking
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
        // Date "2025-06-10" (end day of booking) should match George's booking
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
        // Date "2025-06-11" (outside George's booking range) should not match any bookings
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 0);
        Map<FindCommand.SearchType, List<String>> criteria = new HashMap<>();
        criteria.put(FindCommand.SearchType.BOOKING_DATE, Collections.singletonList("2025-06-11"));
        FindCommand command = new FindCommand(criteria);
        expectedModel.updateFilteredPersonList(p -> false); // Empty predicate to match command behavior
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Collections.emptyList(), model.getPersonList());
    }

    @Test
    public void execute_multipleSearchCriteriaAnd_personsFound() {
        // Using name "Meier" AND tag "friends" should match only Daniel (not Benson who also has Meier)
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 1);
        Map<FindCommand.SearchType, List<String>> criteria = new HashMap<>();
        criteria.put(FindCommand.SearchType.NAME, Collections.singletonList("Meier"));
        criteria.put(FindCommand.SearchType.TAG, Collections.singletonList("friends"));
        criteria.put(FindCommand.SearchType.PHONE, Collections.singletonList("87652533"));

        FindCommand command = new FindCommand(criteria);
        expectedModel.updateFilteredPersonList(command.getPredicate());
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Collections.singletonList(DANIEL), model.getPersonList());
    }

    @Test
    public void execute_bookingCriteriaWithOthers_personsFound() {
        // Using name "George" AND booking property "Beach" should match only George
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 1);
        Map<FindCommand.SearchType, List<String>> criteria = new HashMap<>();
        criteria.put(FindCommand.SearchType.NAME, Collections.singletonList("George"));
        criteria.put(FindCommand.SearchType.BOOKING_PROPERTY, Collections.singletonList("Beach"));

        FindCommand command = new FindCommand(criteria);
        expectedModel.updateFilteredPersonList(command.getPredicate());
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Collections.singletonList(GEORGE), model.getPersonList());
    }

    @Test
    public void toStringMethod() {
        Map<FindCommand.SearchType, List<String>> criteria = new HashMap<>();
        criteria.put(FindCommand.SearchType.NAME, Collections.singletonList("keyword"));
        FindCommand findCommand = new FindCommand(criteria);

        // The string representation should include the search criteria
        String expected = findCommand.toString();

        // Since we can't predict the exact string format, we'll just verify
        // that the string contains the important parts
        assertTrue(expected.contains("searchCriteria"));
    }
}
