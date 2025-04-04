package seedu.innsync.model.tag;

import static java.util.Objects.requireNonNull;

import seedu.innsync.logic.Messages;

/**
 * Represents a Tag in the address book.
 * Guarantees: immutable; name is valid as declared in {@link #isValidTagName(String)}
 */
public class Tag {
    public static final String MESSAGE_CONSTRAINTS =
            "Tag names should not be blank and should not exceed 170 characters.";
    public static final String MESSAGE_EMPTY = String.format(Messages.MESSAGE_EMPTY_FIELD, "Tag");
    public static final String MESSAGE_LENGTH = String.format(Messages.MESSAGE_MAX_LENGTH_EXCEEDED, "Tag", 170);

    public static final String REGEX_NOT_EMPTY = "^.+$"; // Ensures non-empty string
    public static final String REGEX_MAX_LENGTH = "^.{1,170}$"; // Ensures length <= 170

    public final String tagName;
    /**
     * Constructs a {@code Tag}.
     *
     * @param tagName A valid tag name.
     */
    public Tag(String tagName) {
        requireNonNull(tagName);
        checkValidTag(tagName);
        this.tagName = tagName;
    }

    /**
     * Checks if a given string is a valid tag name.
     *
     * @param test The string to be validated.
     * @throws IllegalArgumentException if the string is not a valid tag name.
     */
    public static void checkValidTag(String test) {
        requireNonNull(test);

        if (!test.matches(REGEX_NOT_EMPTY)) {
            throw new IllegalArgumentException(MESSAGE_EMPTY);
        }
        if (!test.matches(REGEX_MAX_LENGTH)) {
            throw new IllegalArgumentException(MESSAGE_LENGTH);
        }
    }

    /**
     * Returns true if a given string is a valid tag name.
     *
     * @param test The string to be validated.
     * @return true if the string is a valid tag name.
     */
    public static boolean isValidTag(String test) {
        requireNonNull(test);
        try {
            checkValidTag(test);
        } catch (IllegalArgumentException e) {
            return false;
        }
        return true;
    }

    public String getTagName() {
        return tagName;
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
     * Format state as text for storage.
     */
    public String toString() {
        return "[" + tagName + "]";
    }
}
