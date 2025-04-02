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
import seedu.innsync.model.tag.Tag;
import seedu.innsync.model.tag.UniqueTagList;

/**
 * Wraps all data at the address-book level
 * Duplicates are not allowed (by .isSamePerson comparison)
 */
public class AddressBook implements ReadOnlyAddressBook {

    // Logger
    private static final Logger logger = LogsCenter.getLogger(AddressBook.class);

    private final UniquePersonList persons;
    private final UniqueRequestList requests;
    private final UniqueTagList tags;

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
        tags = new UniqueTagList();
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
     * Replaces the contents of the tag list with {@code tags}.
     * {@code tags} must not contain duplicate tags.
     */
    public void setTags(List<Tag> tags) {
        this.tags.setTags(tags);
    }

    /**
     * Resets the existing data of this {@code AddressBook} with {@code newData}.
     */
    public void resetData(ReadOnlyAddressBook newData) {
        requireNonNull(newData);
        setPersons(newData.getPersonList());
        setRequests(newData.getRequestList());
        setTags(newData.getTagList());

        logger.info("Address book backed up");
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
     * Removes the equivalent request from the list.
     * The request must exist in the list.
     */
    public void removeRequest(Request request) {
        requireNonNull(request);
        requests.remove(request);
    }

    ///// tag-level operations

    /**
     * Replaces the contents of the tag list with {@code tags}.
     * {@code tags} must not contain duplicate tags.
     */
    public boolean hasTag(Tag tag) {
        requireNonNull(tag);
        return tags.contains(tag);
    }

    /**
     * Gets tag from unique tag list or creates a new tag if it does not exist.
     *
     * @param tag
     * @return tag from unique tag list or a new tag if it does not exist.
     */
    public Tag getTagElseCreate(Tag tag) {
        requireNonNull(tag);
        Tag existingTag = tags.getTag(tag);
        if (existingTag != null) {
            logger.info("Existing tag found: " + existingTag);
            return existingTag;
        } else {
            Tag newTag = new Tag(tag.getTagName());
            tags.add(newTag);
            logger.info("New tag created: " + newTag);
            return newTag;
        }
    }

    /**
     * Gets tag from unique tag list.
     * Returns null if no such tag exists.
     */
    public Tag getTag(Tag tag) {
        requireNonNull(tag);
        return tags.getTag(tag);
    }

    /**
     * Adds tag to the unique tag list.
     * The tag must not already exist in the unique tag list.
     */
    public void addTag(Tag tag) {
        requireNonNull(tag);
        tags.add(tag);
    }

    /**
     * Removes the equivalent tag from the list.
     * The tag must exist in the list.
     */
    public void removeTag(Tag tag) {
        requireNonNull(tag);
        tags.remove(tag);
    }

    //// util methods

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("persons", persons)
                .toString();
    }

    @Override
    public ObservableList<Tag> getTagList() {
        return tags.asUnmodifiableObservableList();
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
