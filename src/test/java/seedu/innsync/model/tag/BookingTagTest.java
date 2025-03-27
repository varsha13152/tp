package seedu.innsync.model.tag;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.innsync.testutil.Assert.assertThrows;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

public class BookingTagTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new BookingTag(null));
    }

    @Test
    public void isValidBookingTagName() {
        // null booking tag name
        assertThrows(NullPointerException.class, () -> BookingTag.isValidBookingTagName(null));
    }

    @Test
    public void invalidBookingTagName() {
        // invalid date booking tag name
        assertFalse(() -> BookingTag.isValidBookingTagName("test to/020-10-10 to/2020-10-10"));
    }

    @Test
    void constructor_validBookingTagName_success() {
        BookingTag bookingTag = new BookingTag("Villa from/2025-06-01 to/2025-06-10");
        assertEquals("Villa", bookingTag.bookingTag);
        assertEquals(LocalDateTime.of(2025, 6, 1, 0, 0), bookingTag.startDate);
        assertEquals(LocalDateTime.of(2025, 6, 10, 0, 0), bookingTag.endDate);
    }

    @Test
    void constructor_invalidBookingTagName_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new BookingTag("InvalidTag"));
        assertThrows(IllegalArgumentException.class, () -> new BookingTag("Hotel from/2025-06-10 to/2025-06-01"));
        assertThrows(IllegalArgumentException.class, () -> new BookingTag("Resort from/2025-06-01"));
    }

    @Test
    void isValidBookingTagName_validFormat_returnsTrue() {
        assertTrue(BookingTag.isValidBookingTagName("Cottage from/2025-07-01 to/2025-07-05"));
    }

    @Test
    void isValidBookingTagName_invalidFormat_returnsFalse() {
        assertFalse(BookingTag.isValidBookingTagName("House 2025-07-01 to/2025-07-05"));
        assertFalse(BookingTag.isValidBookingTagName("Apartment from/2025-07-05 to/2025-07-01"));
        assertFalse(BookingTag.isValidBookingTagName("Bungalow from/2025-07-01"));
    }

    @Test
    void equals_sameBookingTag_returnsTrue() {
        BookingTag tag1 = new BookingTag("Mansion from/2025-08-01 to/2025-08-10");
        BookingTag tag2 = new BookingTag("Mansion from/2025-08-01 to/2025-08-10");
        assertEquals(tag1, tag2);
    }

    @Test
    void equals_differentBookingTag_returnsFalse() {
        BookingTag tag1 = new BookingTag("Mansion from/2025-08-01 to/2025-08-10");
        BookingTag tag2 = new BookingTag("Cabin from/2025-08-01 to/2025-08-10");
        assertNotEquals(tag1, tag2);
    }

}
