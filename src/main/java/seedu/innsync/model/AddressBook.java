package seedu.innsync.model;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import seedu.innsync.commons.core.LogsCenter;
import seedu.innsync.commons.util.ToStringBuilder;
import seedu.innsync.model.person.Person;
import seedu.innsync.model.person.UniquePersonList;
import seedu.innsync.model.request.Request;
import seedu.innsync.model.request.UniqueRequestList;


/**
 * Wraps all data at the address-book level
 * Duplicates are not allowed (by .isSamePerson comparison)
 */
public class AddressBook implements ReadOnlyAddressBook {

    private static final Logger logger = LogsCenter.getLogger(AddressBook.class);

    private final UniquePersonList persons;
    private final UniqueRequestList requests;

    /*
     * The 'unusual' code block below is a non-static initialization block, sometimes used to avoid duplication
     * between constructors. See https://docs.oracle.com/javase/tutorial/java/javaOO/initial.html
     *
     * Note that non-static init blocks are not recommended to use. There are other ways to avoid duplication
     *   among constructors.
     */
    {
        persons = new UniquePersonList();
        requests = new UniqueRequestList();
    }

    public AddressBook() {}

    /**
     * Creates an AddressBook using the Persons in the {@code toBeCopied}
     */
    public AddressBook(ReadOnlyAddressBook toBeCopied) {
        this();
        resetData(toBeCopied);
    }

    //// list overwrite operations

    /**
     * Replaces the contents of the person list with {@code persons}.
     * {@code persons} must not contain duplicate persons.
     */
    public void setPersons(List<Person> persons) {
        this.persons.setPersons(persons);
    }

    /**
     * Replaces the contents of the requests list with {@code requests}.
     * {@code requests} must not contain duplicate requests.
     */
    public void setRequests(List<Request> requests) {
        this.requests.setRequests(requests);
    }

    /**
     * Resets the existing data of this {@code AddressBook} with {@code newData}.
     */
    public void resetData(ReadOnlyAddressBook newData) {
        requireNonNull(newData);
        setPersons(newData.getPersonList());
        setRequests(newData.getRequestList());
    }

    //// person-level operations

    /**
     * Returns true if a person with the same identity as {@code person} exists in the address book.
     */
    public boolean hasPerson(Person person) {
        requireNonNull(person);
        return persons.contains(person);
    }

    /**
     * Adds a person to the address book.
     * The person must not already exist in the address book.
     */
    public void addPerson(Person p) {
        persons.add(p);
    }

    /**
     * Replaces the given person {@code target} in the list with {@code editedPerson}.
     * {@code target} must exist in the address book.
     * The person identity of {@code editedPerson} must not be the same as another existing person in the address book.
     */
    public void setPerson(Person target, Person editedPerson) {
        requireNonNull(editedPerson);

        persons.setPerson(target, editedPerson);
    }

    /**
     * Removes {@code key} from this {@code AddressBook}.
     * {@code key} must exist in the address book.
     */
    public void removePerson(Person key) {
        persons.remove(key);
    }

    /// request-level operations

    /**
     * Returns true if the person already have the same request as {@code request} in the address book.
     */
    public boolean hasRequest(Request request) {
        requireNonNull(request);
        return requests.contains(request);
    }

    /**
     * Gets request from unique request list or creates a new request if it does not exist.
     *
     * @param request
     * @return request from unique request list or a new request if it does not exist.
     */
    public Request getRequestElseCreate(Request request) {
        requireNonNull(request);
        Request existingRequest = requests.getRequest(request);
        if (existingRequest != null) {
            return existingRequest;
        } else {
            Request newRequest = new Request(request);
            requests.add(newRequest);
            return newRequest;
        }
    }

    /**
     * Gets request from unique request list.
     * Returns null if no such request exists.
     */
    public Request getRequest(Request request) {
        requireNonNull(request);
        return requests.getRequest(request);
    }

    /**
     * Adds request to the unique request list.
     * The request must not already exist in the unique request list.
     */
    public void addRequest(Request request) {
        requireNonNull(request);
        requests.add(request);
    }

    /**
     * Removes the equivalent tag from the list.
     * The tag must exist in the list.
     */
    public void removeRequest(Request request) {
        requireNonNull(request);
        requests.remove(request);
    }

    //// util methods

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("persons", persons)
                .toString();
    }

    @Override
    public ObservableList<Person> getPersonList() {
        return persons.asUnmodifiableObservableList();
    }

    @Override
    public ObservableList<Request> getRequestList() {
        return requests.asUnmodifiableObservableList();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof AddressBook)) {
            return false;
        }

        AddressBook otherAddressBook = (AddressBook) other;
        return persons.equals(otherAddressBook.persons);
    }

    @Override
    public int hashCode() {
        return persons.hashCode();
    }
}
