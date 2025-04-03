package seedu.innsync.model.tag;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.innsync.logic.commands.CommandTestUtil.VALID_BOOKINGTAG_BEACHHOUSE;
import static seedu.innsync.testutil.Assert.assertThrows;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

public class BookingTagTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new BookingTag(null));
    }

    @Test
    public void isValidBookingTag() {
        // null booking tag
        assertThrows(NullPointerException.class, () -> BookingTag.isValidBookingTag(null));
    }

    @Test
    void constructor_validBookingTag_success() {
        BookingTag bookingTag = new BookingTag(VALID_BOOKINGTAG_BEACHHOUSE);
        assertEquals("BeachHouse", bookingTag.bookingTagName);
        assertEquals(LocalDate.of(2025, 6, 1), bookingTag.startDate);
        assertEquals(LocalDate.of(2025, 6, 10), bookingTag.endDate);
    }

    @Test
    void constructor_validLeapYearBookingTag_success() {
        BookingTag bookingTag = new BookingTag("LeapYear from/2024-02-29 to/2024-03-01");
        assertEquals("LeapYear", bookingTag.bookingTagName);
        assertEquals(LocalDate.of(2024, 2, 29), bookingTag.startDate);
        assertEquals(LocalDate.of(2024, 3, 1), bookingTag.endDate);
    }

    @Test
    void constructor_invalidLeapYearBookingTag_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new BookingTag("LeapYear from/2024-02-30 to/2024-03-01"));
        assertThrows(IllegalArgumentException.class, () -> new BookingTag("LeapYear from/2024-01-29 to/2024-02-30"));
    }

    @Test
    void constructor_invalidBookingTag_throwsIllegalArgumentException() {
        // missing tokens
        assertThrows(IllegalArgumentException.class, () -> new BookingTag("InvalidTag"));
        assertThrows(IllegalArgumentException.class, () -> new BookingTag("Resort from/2025-06-01"));
        assertThrows(IllegalArgumentException.class, () -> new BookingTag("Resort to/2025-06-10"));

        // months without 31 days
        assertThrows(IllegalArgumentException.class, () -> new BookingTag("Hotel from/2025-06-31 to/2025-07-01"));
        assertThrows(IllegalArgumentException.class, () -> new BookingTag("Hotel from/2025-03-10 to/2025-04-31"));

        // start date after end date
        assertThrows(IllegalArgumentException.class, () -> new BookingTag("Hotel from/2025-06-10 to/2025-06-01"));
    }

    @Test
    void isValidBookingTagName_validFormat_returnsTrue() {
        assertTrue(BookingTag.isValidBookingTag(VALID_BOOKINGTAG_BEACHHOUSE));
    }

    @Test
    void isValidBookingTagName_invalidFormat_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> BookingTag.isValidBookingTag("Apartment from/2025-07-05 to/2025-07-01"));
        assertThrows(IllegalArgumentException.class,
                () -> BookingTag.isValidBookingTag("Bungalow from/2025-07-01"));
    }

    @Test
    void equals_sameBookingTag_returnsTrue() {
        final BookingTag tag1 = new BookingTag(VALID_BOOKINGTAG_BEACHHOUSE);
        final BookingTag tag2 = new BookingTag("BeachHouse from/2025-06-01 to/2025-06-10");
        assertEquals(tag1, tag2);
    }

    @Test
    void equals_exactlyTheSameBookingTag_returnsTrue() {
        final BookingTag tag = new BookingTag(VALID_BOOKINGTAG_BEACHHOUSE);
        assertEquals(tag, tag);
    }

    @Test
    void equals_null_returnsFalse() {
        final BookingTag tag = new BookingTag(VALID_BOOKINGTAG_BEACHHOUSE);
        assertNotEquals(tag, null);
    }


    @Test
    void equals_differentBookingTag_returnsFalse() {
        BookingTag tag1 = new BookingTag("Mansion from/2025-08-01 to/2025-08-10");
        BookingTag tag2 = new BookingTag("Cabin from/2025-08-01 to/2025-08-10");
        assertNotEquals(tag1, tag2);
    }

}
