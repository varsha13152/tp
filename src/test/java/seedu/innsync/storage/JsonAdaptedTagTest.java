package seedu.innsync.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.innsync.logic.commands.CommandTestUtil.VALID_TAG_FRIEND;

import org.junit.jupiter.api.Test;

import seedu.innsync.model.tag.Tag;


public class JsonAdaptedTagTest {
    @Test
    public void toModelType_validTagDetails_returnsTag() throws Exception {
        Tag tag = new Tag(VALID_TAG_FRIEND);
        JsonAdaptedTag jsonTag = new JsonAdaptedTag(VALID_TAG_FRIEND);
        assertEquals(tag, jsonTag.toModelType());
    }
}
