package seedu.innsync.model.tag;

import static java.util.Objects.requireNonNull;
import static seedu.innsync.commons.util.AppUtil.checkArgument;

/**
 * Represents a Tag in the address book.
 * Guarantees: immutable; name is valid as declared in {@link #isValidTagName(String)}
 */
public class Tag {

    public static final String MESSAGE_EMPTY = "Error: Request value should not be empty.";
    public static final String MESSAGE_LENGTH = "Error: Request value must not exceed 170 characters.";

    public static final String REGEX_NOT_EMPTY = "^.+$"; // Ensures non-empty string
    public static final String REGEX_MAX_LENGTH = "^.{1,170}$"; // Ensures length <= 170

    public final String tagName;
    private int tagCount;

    /**
     * Constructs a {@code Tag}.
     *
     * @param tagName A valid tag name.
     */
    public Tag(String tagName) {
        requireNonNull(tagName);
        checkArgument(isValidTagName(tagName), getErrorMessage(tagName));
        this.tagName = tagName;
        this.tagCount = 0;
    }

    /**
     * Returns true if a given string matches all validation rules.
     */
    public static boolean isValidTagName(String test) {
        return test.matches(REGEX_NOT_EMPTY) && test.matches(REGEX_MAX_LENGTH);
    }

    /**
     * Determines the specific error message based on the invalid tag name.
     */
    public static String getErrorMessage(String test) {
        if (!test.matches(REGEX_NOT_EMPTY)) {
            return MESSAGE_EMPTY;
        }
        return MESSAGE_LENGTH;
    }

    public String getTagName() {
        return tagName;
    }

    public int getTagCount() {
        return tagCount;
    }

    public void addTagCount() {
        tagCount++;
    }

    public void removeTagCount() {
        tagCount--;
    }

    public boolean isInUse() {
        return tagCount > 0;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Tag)) {
            return false;
        }

        Tag otherTag = (Tag) other;
        return tagName.equals(otherTag.tagName);
    }

    @Override
    public int hashCode() {
        return tagName.hashCode();
    }

    /**
     * Format state as text for viewing.
     */
    public String toString() {
        return tagName;
    }
}
