package seedu.innsync.model.person;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.innsync.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class PhoneTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Phone(null));
    }

    @Test
    public void constructor_invalidPhone_throwsIllegalArgumentException() {
        String invalidPhone = "";
        assertThrows(IllegalArgumentException.class, () -> new Phone(invalidPhone));
    }

    @Test
    public void isValidPhone() {
        // null phone number
        assertThrows(NullPointerException.class, () -> Phone.isValidPhone(null));

        // invalid phone numbers
        assertFalse(Phone.isValidPhone("")); // empty string
        assertFalse(Phone.isValidPhone(" ")); // spaces only
        assertFalse(Phone.isValidPhone("65 919283")); // doesnt start with +
        assertFalse(Phone.isValidPhone("+65919283")); // no space between country code and phone
        assertFalse(Phone.isValidPhone("+65 919283")); // less than 7 numbers
        assertFalse(Phone.isValidPhone("+68435 919283")); // country code limit exceeded
        assertFalse(Phone.isValidPhone("phone")); // non-numeric
        assertFalse(Phone.isValidPhone("9011p041")); // alphabets within digits
        assertFalse(Phone.isValidPhone("+010 9312 1534")); // invalid country code

        // valid phone numbers
        assertTrue(Phone.isValidPhone("+65 1234567")); // exactly 7 numbers
        assertTrue(Phone.isValidPhone("+65 123456789012345")); // exactly 15 numbers
        assertTrue(Phone.isValidPhone("+65 91318172")); // exactly 1 digit code
        assertTrue(Phone.isValidPhone("+677 9118172")); // exactly 3 digit code
        assertTrue(Phone.isValidPhone("+65 9 3 1 2 1 5 3 4")); // spaces between digits
    }

    @Test
    public void equals() {
        Phone phone = new Phone("+65 9999 9999");

        // same values -> returns true
        assertTrue(phone.equals(new Phone("+65 9999 9999")));

        // same object -> returns true
        assertTrue(phone.equals(phone));

        // null -> returns false
        assertFalse(phone.equals(null));

        // different types -> returns false
        assertFalse(phone.equals(5.0f));

        // different values -> returns false
        assertFalse(phone.equals(new Phone("+65 9999 9995")));
    }
}
