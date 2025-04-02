package seedu.innsync.model.person;

import static seedu.innsync.commons.util.CollectionUtil.requireAllNonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Logger;

import seedu.innsync.commons.core.LogsCenter;
import seedu.innsync.commons.util.ToStringBuilder;
import seedu.innsync.model.request.Request;
import seedu.innsync.model.request.exceptions.DuplicateRequestException;
import seedu.innsync.model.request.exceptions.RequestNotFoundException;
import seedu.innsync.model.tag.BookingTag;
import seedu.innsync.model.tag.Tag;
import seedu.innsync.model.tag.exceptions.DuplicateTagException;
import seedu.innsync.model.tag.exceptions.TagNotFoundException;

/**
 * Represents a Person in the address book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Person {

    // Logger
    private static final Logger logger = LogsCenter.getLogger(Person.class);

    // Identity fields
    private final Name name;
    private final Phone phone;
    private final Email email;

    // Data fields
    private final Address address;
    private final Memo memo;

    private final List<Request> requests = new ArrayList<>();
    private final Set<BookingTag> bookingTags = new HashSet<>();
    private final Set<Tag> tags = new HashSet<>();
    private final boolean starred;

    /**
     * Every field must be present and not null.
     */
    public Person(Name name, Phone phone, Email email, Address address, Memo memo, List<Request> requests,
                  Set<BookingTag> bookingTags, Set<Tag> tags) {
        requireAllNonNull(name, phone, email, address, memo, requests, bookingTags, tags);
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.memo = memo;
        this.requests.addAll(requests);
        this.bookingTags.addAll(bookingTags);
        this.tags.addAll(tags);
        this.starred = false;
    }

    /**
     * Every field must be present and not null.
     */
    public Person(Name name, Phone phone, Email email, Address address, Memo memo, List<Request> requests,
                  Set<BookingTag> bookingTags, Set<Tag> tags, boolean starred) {
        requireAllNonNull(name, phone, email, address, bookingTags, tags);
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.memo = memo;
        this.requests.addAll(requests);
        this.bookingTags.addAll(bookingTags);
        this.tags.addAll(tags);
        this.starred = starred;
    }

    public Name getName() {
        return name;
    }

    public Phone getPhone() {
        return phone;
    }

    public Email getEmail() {
        return email;
    }

    public Address getAddress() {
        return address;
    }

    public boolean getStarred() {
        return starred;
    }

    public Memo getMemo() {
        return memo;
    }

    public List<Request> getRequests() {
        return Collections.unmodifiableList(requests);
    }

    public Set<BookingTag> getBookingTags() {
        return Collections.unmodifiableSet(bookingTags);
    }

    /**
     * Returns an immutable tag set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Tag> getTags() {
        return Collections.unmodifiableSet(tags);
    }

    /**
     * Adds a tag to the list of tags of the person.
     *
     * @param tag the tag to be added
     */
    public void addTag(Tag tag) throws DuplicateTagException {
        requireAllNonNull(tag);
        if (tags.contains(tag)) {
            throw new DuplicateTagException();
        }
        tags.add(tag);
        logger.info("Tag added to person's tag list.");
    }

    /**
     * Removes a tag from the list of tags of the person.
     *
     * @param tag the tag to be removed
     */
    public void removeTag(Tag tag) throws TagNotFoundException {
        requireAllNonNull(tag);
        if (!tags.contains(tag)) {
            logger.warning("Tag not found in person's tag list.");
            throw new TagNotFoundException();
        }
        tags.remove(tag);
        logger.info("Tag removed from person's tag list.");
    }

    /**
     * Clears the list of tags of the person.
     * Used in testing.
     */
    public void clearTags() {
        tags.clear();
    }

    /**
     * Returns true if both persons have the same name.
     * This defines a weaker notion of equality between two persons.
     */
    public boolean isSamePerson(Person otherPerson) {
        if (otherPerson == this) {
            return true;
        }

        return otherPerson != null
                && otherPerson.getName().equals(getName());
    }

    /**
     * Adds a request to the list of requests of the person.
     *
     * @param request the tag to be added
     */
    public void addRequest(Request request) throws DuplicateRequestException {
        requireAllNonNull(request);
        if (requests.contains(request)) {
            throw new DuplicateRequestException();
        }
        requests.add(request);
        logger.info("Request added to person's request list.");
    }

    /**
     * Removes a request from the list of requests of the person.
     *
     * @param request the tag to be removed
     */
    public void removeRequest(Request request) {
        requireAllNonNull(request);
        if (!requests.contains(request)) {
            logger.warning("Request not found in person's request list.");
            throw new RequestNotFoundException();
        }
        requests.remove(request);
        logger.info("Request removed from person's request list.");
    }

    /**
     * Clears the list of requests of the person.
     * Used in testing.
     */
    public void clearRequests() {
        requests.clear();
    }

    /**
     * Returns true if both persons have the same identity and data fields.
     * This defines a stronger notion of equality between two persons.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Person)) {
            return false;
        }

        Person otherPerson = (Person) other;
        return name.equals(otherPerson.name)
                && phone.equals(otherPerson.phone)
                && email.equals(otherPerson.email)
                && address.equals(otherPerson.address)
                && starred == otherPerson.starred
                && memo.equals(otherPerson.memo)
                && requests.equals(otherPerson.requests)
                && bookingTags.equals(otherPerson.bookingTags)
                && tags.equals(otherPerson.tags);
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(name, phone, email, address, memo, requests, bookingTags, tags);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("name", name)
                .add("phone", phone)
                .add("email", email)
                .add("address", address)
                .add("memo", memo)
                .add("requests", requests)
                .add("bookingTags", bookingTags)
                .add("tags", tags)
                .add("starred", starred)
                .toString();
    }

}
