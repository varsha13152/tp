package seedu.innsync.testutil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import seedu.innsync.model.person.Address;
import seedu.innsync.model.person.Email;
import seedu.innsync.model.person.Memo;
import seedu.innsync.model.person.Name;
import seedu.innsync.model.person.Person;
import seedu.innsync.model.person.Phone;
import seedu.innsync.model.request.Request;
import seedu.innsync.model.tag.BookingTag;
import seedu.innsync.model.tag.Tag;
import seedu.innsync.model.util.SampleDataUtil;

/**
 * A utility class to help with building Person objects.
 */
public class PersonBuilder {

    public static final String DEFAULT_NAME = "Amy Bee";
    public static final String DEFAULT_PHONE = "+65 85355255";
    public static final String DEFAULT_EMAIL = "amy@gmail.com";
    public static final String DEFAULT_ADDRESS = "123, Jurong West Ave 6, #08-111";
    public static final boolean DEFAULT_STARRED = false;
    public static final String DEFAULT_MEMO = "";

    private Name name;
    private Phone phone;
    private Email email;
    private Address address;
    private Memo memo;
    private boolean starred;
    private List<Request> requests;
    private Set<BookingTag> bookingTags;
    private Set<Tag> tags;

    /**
     * Creates a {@code PersonBuilder} with the default details.
     */
    public PersonBuilder() {
        name = new Name(DEFAULT_NAME);
        phone = new Phone(DEFAULT_PHONE);
        email = new Email(DEFAULT_EMAIL);
        address = new Address(DEFAULT_ADDRESS);
        memo = new Memo(DEFAULT_MEMO);
        starred = DEFAULT_STARRED;
        requests = new ArrayList<>();
        bookingTags = new HashSet<>();
        tags = new HashSet<>();
    }

    /**
     * Initializes the PersonBuilder with the data of {@code personToCopy}.
     */
    public PersonBuilder(Person personToCopy) {
        name = personToCopy.getName();
        phone = personToCopy.getPhone();
        email = personToCopy.getEmail();
        address = personToCopy.getAddress();
        memo = personToCopy.getMemo();
        starred = personToCopy.getStarred();
        requests = new ArrayList<>(personToCopy.getRequests());
        bookingTags = new HashSet<>(personToCopy.getBookingTags());
        tags = new HashSet<>(personToCopy.getTags());
    }

    /**
     * Sets the {@code Name} of the {@code Person} that we are building.
     */
    public PersonBuilder withName(String name) {
        this.name = new Name(name);
        return this;
    }

    /**
     * Sets the {@code Request} of the {@code Person} that we are building.
     */
    public PersonBuilder withRequests(String... requests) {
        this.requests = SampleDataUtil.getRequestList(requests);
        return this;
    }

    /**
     * Sets the {@code Request} of the {@code Person} that we are building.
     */
    public PersonBuilder withRequests(List<Request> requests) {
        this.requests = requests;
        return this;
    }

    /**
     * Parses the {@code bookingTags} into a {@code Set<BookingTag>}
     * and set it to the {@code Person} that we are building.
     */
    public PersonBuilder withBookingTags(String ... bookingTags) {
        this.bookingTags = SampleDataUtil.getBookingTagSet(bookingTags);
        return this;
    }

    /**
     * Parses the {@code tags} into a {@code Set<Tag>} and set it to the {@code Person} that we are building.
     */
    public PersonBuilder withTags(String ... tags) {
        this.tags = SampleDataUtil.getTagSet(tags);
        return this;
    }

    /**
     * Sets the {@code BookingTag} of the {@code Person} that we are building.
     */
    public PersonBuilder withTags(Set<Tag> tags) {
        this.tags = tags;
        return this;
    }

    /**
     * Sets the {@code Address} of the {@code Person} that we are building.
     */
    public PersonBuilder withAddress(String address) {
        this.address = new Address(address);
        return this;
    }

    /**
     * Sets the {@code Memo} of the {@code Person} that we are building.
     */
    public PersonBuilder withMemo(String memo) {
        this.memo = new Memo(memo);
        return this;
    }

    /**
     * Sets the {@code Phone} of the {@code Person} that we are building.
     */
    public PersonBuilder withPhone(String phone) {
        this.phone = new Phone(phone);
        return this;
    }

    /**
     * Sets the {@code Email} of the {@code Person} that we are building.
     */
    public PersonBuilder withEmail(String email) {
        this.email = new Email(email);
        return this;
    }

    /**
     * Sets the {@code Starred} of the {@code Person} that we are building.
     */
    public PersonBuilder withStarred(boolean starred) {
        this.starred = starred;
        return this;
    }

    public Person build() {
        return new Person(name, phone, email, address, memo, requests, bookingTags, tags, starred);
    }

}
