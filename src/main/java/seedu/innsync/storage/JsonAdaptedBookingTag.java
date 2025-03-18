package seedu.innsync.storage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import seedu.innsync.commons.exceptions.IllegalValueException;
import seedu.innsync.model.tag.BookingTag;
import seedu.innsync.model.tag.Tag;

/**
 * Jackson-friendly version of {@link Tag}.
 */
class JsonAdaptedBookingTag {

    private final String bookingTagName;

    /**
     * Constructs a {@code JsonAdaptedTag} with the given {@code tagName}.
     */
    @JsonCreator
    public JsonAdaptedBookingTag(String bookingTagName) {
        this.bookingTagName = bookingTagName;
    }

    /**
     * Converts a given {@code Tag} into this class for Jackson use.
     */
    public JsonAdaptedBookingTag(BookingTag source) {
        bookingTagName = source.bookingTagName;
    }

    @JsonValue
    public String getBookingTagName() {
        return bookingTagName;
    }

    /**
     * Converts this Jackson-friendly adapted tag object into the model's {@code Tag} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted tag.
     */
    public BookingTag toModelType() throws IllegalValueException {
        if (!BookingTag.isValidBookingTagName(bookingTagName)) {
            throw new IllegalValueException(Tag.MESSAGE_CONSTRAINTS);
        }
        return new BookingTag(bookingTagName);
    }

}
