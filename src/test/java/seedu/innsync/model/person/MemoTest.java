package seedu.innsync.model.person;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.innsync.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class MemoTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Memo(null));
    }

    @Test
    public void equals() {
        Memo memo1 = new Memo("This is a memo.");
        Memo memo2 = new Memo("This is a memo.");
        Memo memo3 = new Memo("Different memo");

        // same values -> returns true
        assertTrue(memo1.equals(memo2));

        // same object -> returns true
        assertTrue(memo1.equals(memo1));

        // null -> returns false
        assertFalse(memo1.equals(null));

        // different types -> returns false
        assertFalse(memo1.equals(5.0f));

        // different values -> returns false
        assertFalse(memo1.equals(memo3));
    }

    @Test
    public void hashCodeTest() {
        Memo memo1 = new Memo("Sample memo");
        Memo memo2 = new Memo("Sample memo");
        Memo memo3 = new Memo("Different memo");

        // same content -> same hashcode
        assertTrue(memo1.hashCode() == memo2.hashCode());

        // different content -> different hashcode
        assertFalse(memo1.hashCode() == memo3.hashCode());
    }
}
