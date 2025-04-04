package seedu.innsync.storage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import seedu.innsync.commons.exceptions.IllegalValueException;
import seedu.innsync.model.tag.BookingTag;

/**
 * Jackson-friendly version of {@link BookingTag}.
 */
class JsonAdaptedBookingTag {

    private final String bookingTag;

    /**
     * Constructs a {@code JsonAdaptedTag} with the given {@code tagName}.
     */
    @JsonCreator
    public JsonAdaptedBookingTag(String bookingTag) {
        this.bookingTag = bookingTag;
    }

    /**
     * Converts a given {@code Tag} into this class for Jackson use.
     */
    public JsonAdaptedBookingTag(BookingTag source) {
        bookingTag = source.getFullBookingTag();
    }

    @JsonValue
    public String getBookingTagName() {
        return bookingTag;
    }

    /**
     * Converts this Jackson-friendly adapted tag object into the model's {@code Tag} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted tag.
     */
    public BookingTag toModelType() throws IllegalValueException {
        try {
            BookingTag.checkValidBookingTag(bookingTag);
        } catch (IllegalArgumentException e) {
            throw new IllegalValueException(e.getMessage());
        }
        return new BookingTag(bookingTag);
    }

}
