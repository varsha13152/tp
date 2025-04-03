package seedu.innsync.model.tag;

import static java.util.Objects.requireNonNull;
import static seedu.innsync.commons.util.AppUtil.checkAllValidationRules;

import java.util.List;
import java.util.function.Function;

import javafx.util.Pair;
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

    public static final List<Pair<Function<String, Boolean>, String>> VALIDATION_RULES = List.of(
            new Pair<>(Tag::notEmptyTagName, MESSAGE_EMPTY),
            new Pair<>(Tag::notMaxLengthTagName, MESSAGE_LENGTH)
    );

    public final String tagName;
    private int tagCount;

    /**
     * Constructs a {@code Tag}.
     *
     * @param tagName A valid tag name.
     */
    public Tag(String tagName) {
        requireNonNull(tagName);
        checkAllValidationRules(tagName, VALIDATION_RULES);
        this.tagName = tagName;
        this.tagCount = 0;
    }

    /**
     * Returns true if a given string is a valid tag name.
     */
    public static boolean isValidTagName(String test) {
        requireNonNull(test);
        try {
            checkAllValidationRules(test, VALIDATION_RULES);
        } catch (IllegalArgumentException e) {
            return false;
        }
        return true;
    }

    private static boolean notEmptyTagName(String test) {
        return test.matches(REGEX_NOT_EMPTY);
    }

    private static boolean notMaxLengthTagName(String test) {
        return test.matches(REGEX_MAX_LENGTH);
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
