package seedu.innsync.storage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.innsync.commons.exceptions.IllegalValueException;
import seedu.innsync.model.person.Address;
import seedu.innsync.model.person.Email;
import seedu.innsync.model.person.Memo;
import seedu.innsync.model.person.Name;
import seedu.innsync.model.person.Person;
import seedu.innsync.model.person.Phone;
import seedu.innsync.model.request.Request;
import seedu.innsync.model.tag.BookingTag;
import seedu.innsync.model.tag.Tag;

/**
 * Jackson-friendly version of {@link Person}.
 */
class JsonAdaptedPerson {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Person's %s field is missing!";

    private final String name;
    private final String phone;
    private final String email;
    private final String address;
    private final String memo;
    private final boolean starred;
    private final List<JsonAdaptedRequest> requests = new ArrayList<>();
    private final List<JsonAdaptedBookingTag> bookingTags = new ArrayList<>();
    private final List<JsonAdaptedTag> tags = new ArrayList<>();

    /**
     * Constructs a {@code JsonAdaptedPerson} with the given person details.
     */
    @JsonCreator
    public JsonAdaptedPerson(@JsonProperty("name") String name, @JsonProperty("phone") String phone,
                             @JsonProperty("email") String email, @JsonProperty("address") String address,
                             @JsonProperty("memo") String memo,
                             @JsonProperty("requests") List<JsonAdaptedRequest> requests,
                             @JsonProperty("bookingTags") List<JsonAdaptedBookingTag> bookingTags,
                             @JsonProperty("tags") List<JsonAdaptedTag> tags,
                             @JsonProperty("starred") boolean starred) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.memo = memo;
        this.starred = starred;
        if (requests != null) {
            this.requests.addAll(requests);
        }
        if (bookingTags != null) {
            this.bookingTags.addAll(bookingTags);
        }
        if (tags != null) {
            this.tags.addAll(tags);
        }
    }

    /**
     * Converts a given {@code Person} into this class for Jackson use.
     */
    public JsonAdaptedPerson(Person source) {
        name = source.getName().fullName;
        phone = source.getPhone().value;
        email = source.getEmail().value;
        address = source.getAddress().value;
        memo = source.getMemo().value;
        starred = source.getStarred();
        requests.addAll(source.getRequests().stream()
                .map(JsonAdaptedRequest::new)
                .collect(Collectors.toList()));
        bookingTags.addAll(source.getBookingTags().stream()
                .map(JsonAdaptedBookingTag::new)
                .collect(Collectors.toList()));
        tags.addAll(source.getTags().stream()
                .map(JsonAdaptedTag::new)
                .collect(Collectors.toList()));
    }

    /**
     * Converts this Jackson-friendly adapted person object into the model's {@code Person} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted person.
     */
    public Person toModelType() throws IllegalValueException {
        final List<Tag> personTags = new ArrayList<>();
        for (JsonAdaptedTag tag : tags) {
            personTags.add(tag.toModelType());
        }
        final List<BookingTag> personBookingTags = new ArrayList<>();
        for (JsonAdaptedBookingTag bookingTag : bookingTags) {
            personBookingTags.add(bookingTag.toModelType());
        }
        final List<Request> personRequests = new ArrayList<>();
        for (JsonAdaptedRequest request : requests) {
            personRequests.add(request.toModelType());
        }

        if (name == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName()));
        }
        if (!Name.isValidName(name)) {
            throw new IllegalValueException(Name.getErrorMessage(name));
        }
        final Name modelName = new Name(name);

        if (phone == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Phone.class.getSimpleName()));
        }
        if (!Phone.isValidPhone(phone)) {
            throw new IllegalValueException(Phone.MESSAGE_CONSTRAINTS);
        }
        final Phone modelPhone = new Phone(phone);

        if (email == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Email.class.getSimpleName()));
        }
        if (!Email.isValidEmail(email)) {
            throw new IllegalValueException(Email.MESSAGE_CONSTRAINTS);
        }
        final Email modelEmail = new Email(email);

        if (address == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Address.class.getSimpleName()));
        }
        if (!Address.isValidAddress(address)) {
            throw new IllegalValueException(Address.getErrorMessage(address));
        }
        final Address modelAddress = new Address(address);

        if (memo == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Memo.class.getSimpleName()));
        }
        final Memo modelMemo = new Memo(memo);

        final List<Request> modelRequests = personRequests;

        final Set<BookingTag> modelBookingTags = new HashSet<>(personBookingTags);

        final Set<Tag> modelTags = new HashSet<>(personTags);

        final boolean modelStarred = starred;

        return new Person(modelName, modelPhone, modelEmail, modelAddress, modelMemo, modelRequests, modelBookingTags,
                modelTags, modelStarred);
    }

}
