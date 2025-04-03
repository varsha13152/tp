package seedu.innsync.model.tag;

import static java.util.Objects.requireNonNull;
import static seedu.innsync.commons.util.AppUtil.checkAllRules;

import java.util.List;

import seedu.innsync.commons.core.rule.Rule;
import seedu.innsync.commons.core.rule.RuleList;
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

    public static final RuleList VALIDATION_RULES = new RuleList(List.of(
            Rule.ofRegex(REGEX_NOT_EMPTY, MESSAGE_EMPTY),
            Rule.ofRegex(REGEX_MAX_LENGTH, MESSAGE_LENGTH)
    ));

    public final String tagName;
    private int tagCount;

    /**
     * Constructs a {@code Tag}.
     *
     * @param tagName A valid tag name.
     */
    public Tag(String tagName) {
        requireNonNull(tagName);
        checkAllRules(tagName, VALIDATION_RULES);
        this.tagName = tagName;
        this.tagCount = 0;
    }

    /**
     * Returns true if a given string is a valid tag name.
     *
     * @param test The string to be validated.
     * @throws IllegalArgumentException if the string does not pass any of the validation rules.
     * @return true if the string is a valid tag name.
     */
    public static boolean isValidTagName(String test) throws IllegalArgumentException {
        requireNonNull(test);
        checkAllRules(test, VALIDATION_RULES);
        return true;
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
