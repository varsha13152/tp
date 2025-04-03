package seedu.innsync.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.innsync.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static seedu.innsync.logic.commands.CommandTestUtil.VALID_REQUEST_AMY;
import static seedu.innsync.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.innsync.testutil.Assert.assertThrows;
import static seedu.innsync.testutil.TypicalPersons.ALICE;
import static seedu.innsync.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.innsync.model.person.Person;
import seedu.innsync.model.person.exceptions.DuplicatePersonException;
import seedu.innsync.model.request.Request;
import seedu.innsync.model.tag.Tag;
import seedu.innsync.testutil.PersonBuilder;

public class AddressBookTest {

    private final AddressBook addressBook = new AddressBook();

    @Test
    public void constructor() {
        assertEquals(Collections.emptyList(), addressBook.getPersonList());
        assertEquals(Collections.emptyList(), addressBook.getTagList());
    }

    @Test
    public void resetData_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> addressBook.resetData(null));
    }

    @Test
    public void resetData_withValidReadOnlyAddressBook_replacesData() {
        AddressBook newData = getTypicalAddressBook();
        addressBook.resetData(newData);
        assertEquals(newData, addressBook);
    }

    @Test
    public void resetData_withDuplicatePersons_throwsDuplicatePersonException() {
        // Two persons with the same identity fields
        Person editedAlice = new PersonBuilder(ALICE).withAddress(VALID_ADDRESS_BOB).withTags(VALID_TAG_HUSBAND)
                .build();
        List<Person> newPersons = Arrays.asList(ALICE, editedAlice);
        AddressBookStub newData = new AddressBookStub(newPersons);

        assertThrows(DuplicatePersonException.class, () -> addressBook.resetData(newData));
    }

    @Test
    public void hasPerson_nullPerson_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> addressBook.hasPerson(null));
    }

    @Test
    public void hasPerson_personNotInAddressBook_returnsFalse() {
        assertFalse(addressBook.hasPerson(ALICE));
    }

    @Test
    public void hasPerson_personInAddressBook_returnsTrue() {
        addressBook.addPerson(ALICE);
        assertTrue(addressBook.hasPerson(ALICE));
    }

    @Test
    public void hasRequest_nullRequest_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> addressBook.hasRequest(null));
    }

    @Test
    public void hasRequest_requestNotInAddressBook_returnsFalse() {
        Request validRequest = new Request(VALID_REQUEST_AMY);
        assertFalse(addressBook.hasRequest(validRequest));
    }

    @Test
    public void getRequest_nullRequest_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> addressBook.getRequest(null));
    }

    @Test
    public void getRequest_requestInAddressBook_returnsTrue() {
        Request validRequest = new Request(VALID_REQUEST_AMY);
        addressBook.addRequest(validRequest);
        assertTrue(addressBook.getRequest(validRequest).equals(validRequest));
    }

    @Test
    public void removeRequest_nullRequest_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> addressBook.removeRequest(null));
    }

    @Test
    public void removeRequest_requestInAddressBook_returnsTrue() {
        Request validRequest = new Request(VALID_REQUEST_AMY);
        addressBook.addRequest(validRequest);
        addressBook.removeRequest(validRequest);
        assertTrue(!addressBook.hasRequest(validRequest));
    }

    @Test
    public void addTag_nullTag_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> addressBook.addTag(null));
    }

    @Test
    public void addTag_tagInAddressBook_returnsTrue() {
        Tag validTag = new Tag(VALID_TAG_HUSBAND);
        assertTrue(!addressBook.hasTag(validTag));
        addressBook.addTag(validTag);
        assertTrue(addressBook.hasTag(validTag));
    }

    @Test
    public void removeTag_nullTag_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> addressBook.removeTag(null));
    }

    @Test
    public void removeTag_tagInAddressBook_returnsTrue() {
        Tag validTag = new Tag(VALID_TAG_HUSBAND);
        assertTrue(!addressBook.hasTag(validTag));
        addressBook.addTag(validTag);
        addressBook.removeTag(validTag);
        assertTrue(!addressBook.hasTag(validTag));
    }

    @Test
    public void hasTag_nullTag_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> addressBook.hasTag(null));
    }

    @Test
    public void hasTag_requestInAddressBook_returnsTrue() {
        Tag validTag = new Tag(VALID_TAG_HUSBAND);
        addressBook.addTag(validTag);
        assertTrue(addressBook.hasTag(validTag));
        addressBook.removeTag(validTag);
        assertTrue(!addressBook.hasTag(validTag));
    }


    @Test
    public void hasPerson_personWithSameIdentityFieldsInAddressBook_returnsTrue() {
        addressBook.addPerson(ALICE);
        Person editedAlice = new PersonBuilder(ALICE).withAddress(VALID_ADDRESS_BOB).withTags(VALID_TAG_HUSBAND)
                .build();
        assertTrue(addressBook.hasPerson(editedAlice));
    }

    @Test
    public void getPersonList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> addressBook.getPersonList().remove(0));
    }

    @Test
    public void equals() {
        // same object -> returns true
        assertTrue(addressBook.equals(addressBook));

        // null value -> returns false
        assertFalse(addressBook.equals(null));
    }

    @Test
    public void toStringMethod() {
        String expected = AddressBook.class.getCanonicalName() + "{persons=" + addressBook.getPersonList() + "}";
        assertEquals(expected, addressBook.toString());
    }

    /**
     * A stub ReadOnlyAddressBook whose persons list can violate interface constraints.
     */
    private static class AddressBookStub implements ReadOnlyAddressBook {
        private final ObservableList<Person> persons = FXCollections.observableArrayList();
        private final ObservableList<Request> requests = FXCollections.observableArrayList();
        private final ObservableList<Tag> tags = FXCollections.observableArrayList();

        AddressBookStub(Collection<Person> persons) {
            this.persons.setAll(persons);
        }

        @Override
        public ObservableList<Person> getPersonList() {
            return persons;
        }

        @Override
        public ObservableList<Request> getRequestList() {
            return requests;
        }

        @Override
        public ObservableList<Tag> getTagList() {
            return tags;
        }
    }

}
