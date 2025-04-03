package seedu.innsync.model.tag;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.innsync.model.tag.exceptions.DuplicateTagException;
import seedu.innsync.model.tag.exceptions.TagNotFoundException;

/**
 * A list of tags that enforces uniqueness between its elements and does not allow nulls.
 * A tag is considered unique by comparing using {@code Tag#equals(Object)}. As such, adding and updating of
 * persons uses Tag#isSameTag(Tag) for equality so as to ensure that the tag being added or updated is
 * unique in terms of identity in the UniqueTagList.
 *
 * Supports a minimal set of list operations.
 *
 * @see Tag#isSameTag(Tag)
 */
public class UniqueTagList {

    private final ObservableList<Tag> internalList = FXCollections.observableArrayList();
    private final ObservableList<Tag> internalUnmodifiableList =
            FXCollections.unmodifiableObservableList(internalList);

    /**
     * Returns true if the list contains an equivalent tag as the given argument.
     */
    public boolean contains(Tag toCheck) {
        requireNonNull(toCheck);
        return internalList.stream().anyMatch(toCheck::equals);
    }

    /**
     * Gets tag with the same name as the given argument.
     * Returns null if no such tag exists.
     */
    public Tag getTag(String name) {
        requireNonNull(name);
        return internalList.stream()
                .filter(tag -> tag.getTagName().equals(name))
                .findFirst()
                .orElse(null);
    }

    /**
     * Gets tag from list if it exists.
     * Returns null if no such tag exists.
     */
    public Tag getTag(Tag tag) {
        requireNonNull(tag);
        return internalList.stream()
                .filter(t -> t.equals(tag))
                .findFirst()
                .orElse(null);
    }

    /**
     * Adds a tag to the list.
     * The tag must not already exist in the list.
     */
    public void add(Tag toAdd) {
        requireNonNull(toAdd);
        if (contains(toAdd)) {
            throw new DuplicateTagException();
        }
        internalList.add(toAdd);
    }

    /**
     * Removes the equivalent tag from the list.
     * The tag must exist in the list.
     * @param toRemove
     */
    public void remove(Tag toRemove) {
        requireNonNull(toRemove);
        if (!internalList.remove(toRemove)) {
            throw new TagNotFoundException();
        }
    }

    public void setTags(UniqueTagList replacement) {
        requireNonNull(replacement);
        internalList.setAll(replacement.internalList);
    }

    /**
     * Replaces the contents of this list with {@code tags}.
     * {@code tags} must not contain duplicate tags.
     */
    public void setTags(List<Tag> tags) {
        requireNonNull(tags);
        if (!tagsAreUnique(tags)) {
            throw new DuplicateTagException();
        }
        internalList.setAll(tags);
    }

    /**
     * Returns the backing list as an unmodifiable {@code ObservableList}.
     */
    public ObservableList<Tag> asUnmodifiableObservableList() {
        return internalUnmodifiableList;
    }

    @Override
    public String toString() {
        return internalList.toString();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        } else if (!(other instanceof UniqueTagList)) {
            return false;
        } else {
            UniqueTagList otherList = (UniqueTagList) other;
            return internalList.equals(otherList.internalList);
        }
    }

    private boolean tagsAreUnique(List<Tag> tags) {
        Set<Tag> tagSet = tags.stream().collect(Collectors.toSet());
        return tagSet.size() == tags.size();
    }
}
