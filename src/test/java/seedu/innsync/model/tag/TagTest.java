package seedu.innsync.model.tag;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static seedu.innsync.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.innsync.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class TagTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Tag(null));
    }

    @Test
    public void constructor_invalidTagName_throwsIllegalArgumentException() {
        String invalidTagName = "";
        assertThrows(IllegalArgumentException.class, () -> new Tag(invalidTagName));
    }

    @Test
    public void isValidTagName() {
        // null tag name
        assertThrows(NullPointerException.class, () -> Tag.isValidTagName(null));
    }

    @Test
    public void equal_tag_returnTrue() {
        final Tag validTag1 = new Tag(VALID_TAG_HUSBAND);
        final Tag validTag2 = new Tag(VALID_TAG_HUSBAND);
        assertEquals(validTag2, validTag1);
    }

    @Test
    public void same_tag_returnTrue() {
        final Tag validTag1 = new Tag(VALID_TAG_HUSBAND);
        assertEquals(validTag1, validTag1);
    }

    @Test
    public void null_tag_returnFalse() {
        final Tag validTag1 = new Tag(VALID_TAG_HUSBAND);
        assertNotEquals(validTag1, null);
    }
}
