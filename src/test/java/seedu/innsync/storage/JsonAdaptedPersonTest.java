package seedu.innsync.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.innsync.storage.JsonAdaptedPerson.MISSING_FIELD_MESSAGE_FORMAT;
import static seedu.innsync.testutil.Assert.assertThrows;
import static seedu.innsync.testutil.TypicalPersons.BENSON;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import seedu.innsync.commons.exceptions.IllegalValueException;
import seedu.innsync.model.person.Address;
import seedu.innsync.model.person.Email;
import seedu.innsync.model.person.Name;
import seedu.innsync.model.person.Phone;

public class JsonAdaptedPersonTest {
    public static final String LONG_STRING = "A".repeat(171);
    private static final String INVALID_NAME = "";
    private static final String INVALID_PHONE = "+651234";
    private static final String INVALID_ADDRESS = "";
    private static final String INVALID_EMAIL = "example.com";
    private static final String INVALID_REQUESTS = "";
    private static final String INVALID_BOOKING_TAG = "house";
    private static final String INVALID_TAG = LONG_STRING;

    private static final String VALID_NAME = BENSON.getName().toString();
    private static final String VALID_PHONE = BENSON.getPhone().toString();
    private static final String VALID_EMAIL = BENSON.getEmail().toString();
    private static final String VALID_ADDRESS = BENSON.getAddress().toString();
    private static final String VALID_MEMO = BENSON.getMemo().toString();
    private static final List<JsonAdaptedRequest> VALID_REQUESTS = BENSON.getRequests().stream()
            .map(JsonAdaptedRequest::new)
            .collect(Collectors.toList());
    private static final List<JsonAdaptedBookingTag> VALID_BOOKING_TAGS = BENSON.getBookingTags().stream()
            .map(JsonAdaptedBookingTag::new)
            .collect(Collectors.toList());
    private static final List<JsonAdaptedTag> VALID_TAGS = BENSON.getTags().stream()
            .map(JsonAdaptedTag::new)
            .collect(Collectors.toList());

    @Test
    public void toModelType_validPersonDetails_returnsPerson() throws Exception {
        JsonAdaptedPerson person = new JsonAdaptedPerson(BENSON);
        assertEquals(BENSON, person.toModelType());
    }

    @Test
    public void toModelType_invalidName_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(INVALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS, VALID_MEMO,
                        VALID_REQUESTS, VALID_BOOKING_TAGS, VALID_TAGS, false);
        String expectedMessage = Name.getErrorMessage(INVALID_NAME);
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullName_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson(null, VALID_PHONE, VALID_EMAIL,
                VALID_ADDRESS, VALID_MEMO, VALID_REQUESTS, VALID_BOOKING_TAGS, VALID_TAGS, false);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidPhone_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(VALID_NAME, INVALID_PHONE, VALID_EMAIL,
                        VALID_ADDRESS, VALID_MEMO, VALID_REQUESTS, VALID_BOOKING_TAGS, VALID_TAGS, false);
        String expectedMessage = Phone.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullPhone_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson(VALID_NAME, null, VALID_EMAIL,
                VALID_ADDRESS, VALID_MEMO, VALID_REQUESTS, VALID_BOOKING_TAGS, VALID_TAGS, false);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Phone.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidEmail_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(VALID_NAME, VALID_PHONE, INVALID_EMAIL,
                        VALID_ADDRESS, VALID_MEMO, VALID_REQUESTS, VALID_BOOKING_TAGS, VALID_TAGS, false);
        String expectedMessage = Email.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullEmail_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson(VALID_NAME, VALID_PHONE, null,
                VALID_ADDRESS, VALID_MEMO, VALID_REQUESTS, VALID_BOOKING_TAGS, VALID_TAGS, false);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Email.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidAddress_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(VALID_NAME, VALID_PHONE, VALID_EMAIL,
                        INVALID_ADDRESS, VALID_MEMO, VALID_REQUESTS, VALID_BOOKING_TAGS, VALID_TAGS, false);
        String expectedMessage = Address.MESSAGE_EMPTY;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullAddress_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson(VALID_NAME, VALID_PHONE, VALID_EMAIL,
                null, VALID_MEMO, VALID_REQUESTS, VALID_BOOKING_TAGS, VALID_TAGS, false);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Address.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidRequests_throwsIllegalValueException() {
        List<JsonAdaptedRequest> invalidRequests = new ArrayList<>(VALID_REQUESTS);
        invalidRequests.add(new JsonAdaptedRequest(INVALID_REQUESTS, false));
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(VALID_NAME, VALID_PHONE, VALID_EMAIL,
                        VALID_MEMO, VALID_ADDRESS, invalidRequests, VALID_BOOKING_TAGS, VALID_TAGS, false);
        assertThrows(IllegalValueException.class, person::toModelType);
    }

    @Test
    public void toModelType_invalidBookingTags_throwsIllegalValueException() {
        List<JsonAdaptedBookingTag> invalidBookingTags = new ArrayList<>(VALID_BOOKING_TAGS);
        invalidBookingTags.add(new JsonAdaptedBookingTag(INVALID_BOOKING_TAG));
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(VALID_NAME, VALID_PHONE, VALID_EMAIL,
                        VALID_MEMO, VALID_ADDRESS, VALID_REQUESTS, invalidBookingTags, VALID_TAGS, false);
        assertThrows(IllegalValueException.class, person::toModelType);
    }

    @Test
    public void toModelType_invalidTags_throwsIllegalValueException() {
        List<JsonAdaptedTag> invalidTags = new ArrayList<>(VALID_TAGS);
        invalidTags.add(new JsonAdaptedTag(INVALID_TAG));
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(VALID_NAME, VALID_PHONE, VALID_EMAIL,
                        VALID_MEMO, VALID_ADDRESS, VALID_REQUESTS, VALID_BOOKING_TAGS, invalidTags, false);
        assertThrows(IllegalValueException.class, person::toModelType);
    }

}
