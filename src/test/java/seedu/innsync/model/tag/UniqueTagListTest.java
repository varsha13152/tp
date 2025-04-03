package seedu.innsync.model.tag;

import org.junit.jupiter.api.Test;
import seedu.innsync.model.tag.exceptions.DuplicateTagException;
import seedu.innsync.model.tag.exceptions.TagNotFoundException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.innsync.logic.commands.CommandTestUtil.VALID_TAG_FRIEND;
import static seedu.innsync.testutil.Assert.assertThrows;


public class UniqueTagListTest {

    private static final Tag VALID_TAG_STUB = new Tag(VALID_TAG_FRIEND);
    private final UniqueTagList uniqueTagList = new UniqueTagList();

    @Test
    public void contains_nullTag_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueTagList.contains(null));
    }

    @Test
    public void contains_tagNotInList_returnsFalse() {
        assertFalse(uniqueTagList.contains(VALID_TAG_STUB));
    }

    @Test
    public void contains_tagInList_returnsTrue() {
        uniqueTagList.add(VALID_TAG_STUB);
        assertTrue(uniqueTagList.contains(VALID_TAG_STUB));
    }

    @Test
    public void add_nullTag_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueTagList.add(null));
    }

    @Test
    public void add_duplicateTag_throwsDuplicateTagException() {
        uniqueTagList.add(VALID_TAG_STUB);
        assertThrows(DuplicateTagException.class, () -> uniqueTagList.add(VALID_TAG_STUB));
    }

    @Test
    public void setTag_nullTargetTag_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueTagList.setTags(List.of(null,
                VALID_TAG_STUB)));
    }

    @Test
    public void remove_nullTag_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueTagList.remove(null));
    }

    @Test
    public void remove_tagDoesNotExist_throwsTagNotFoundException() {
        assertThrows(TagNotFoundException.class, () -> uniqueTagList.remove(VALID_TAG_STUB));
    }

    @Test
    public void remove_existingTag_removesTag() {
        uniqueTagList.add(VALID_TAG_STUB);
        uniqueTagList.remove(VALID_TAG_STUB);
        UniqueTagList expectedUniqueTagList = new UniqueTagList();
        assertEquals(expectedUniqueTagList, uniqueTagList);
    }

    @Test
    public void setTags_uniqueTagList_replacesOwnListWithProvidedUniqueTagList() {
        uniqueTagList.add(VALID_TAG_STUB);
        UniqueTagList expectedUniquerTagList = new UniqueTagList();
        expectedUniquerTagList.add(new Tag(VALID_TAG_FRIEND));
        uniqueTagList.setTags(expectedUniquerTagList.asUnmodifiableObservableList());
        assertEquals(expectedUniquerTagList, uniqueTagList);
    }

    @Test
    public void setTags_nullList_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueTagList.setTags((List<Tag>) null));
    }

    @Test
    public void setTags_list_replacesOwnListWithProvidedList() {
        uniqueTagList.add(VALID_TAG_STUB);
        List<Tag> tagList = Collections.singletonList(new Tag(VALID_TAG_FRIEND));
        uniqueTagList.setTags(tagList);
        UniqueTagList expectedTagList = new UniqueTagList();
        expectedTagList.add(VALID_TAG_STUB);
        assertEquals(expectedTagList, uniqueTagList);
    }

    @Test
    public void setTags_listWithDuplicateTags_throwsDuplicateTagException() {
        List<Tag> listWithDuplicateTag = Arrays.asList(VALID_TAG_STUB, VALID_TAG_STUB);
        assertThrows(DuplicateTagException.class, () -> uniqueTagList.setTags(listWithDuplicateTag));
    }

    @Test
    public void asUnmodifiableObservableList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, ()
            -> uniqueTagList.asUnmodifiableObservableList().remove(0));
    }

    @Test
    public void toStringMethod() {
        assertEquals(uniqueTagList.asUnmodifiableObservableList().toString(), uniqueTagList.toString());
    }
}
