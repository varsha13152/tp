package seedu.innsync.commons.core.rule;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.function.Predicate;

import javafx.util.Pair;

/**
 * Represents a validation rule for a string.
 * Guarantees: immutable; regex and message are valid as declared in {@link #isValidRule(String, String)}
 */
public class Rule<T> {
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Rule.class.getName());

    private final Predicate<T> rule; // Predicate to validate the object

    // Error message to be displayed if the rule is violated
    private final String errorMessage;

    /**
     * Constructs a {@code Rule}.
     *
     * @param rule A valid regex rule.
     * @param errorMessage A valid error message.
     */
    public Rule(Predicate<T> rule, String errorMessage) {
        this.rule = rule;
        this.errorMessage = errorMessage;
    }

    /**
     * Factory method to create a regex-based rule.
     *
     * @param regex The regex pattern to be used for validation.
     * @param errorMessage The error message to be displayed if the rule is violated.
     * @return A new {@code Rule<String>} instance.
     */
    public static Rule<String> ofRegex(String regex, String errorMessage) {
        return new Rule<>(input -> input.matches(regex), errorMessage);
    }

    /**
     * Factory method to create a date validation rule.
     *
     * @param predicate The predicate to validate the date.
     * @param formatter The {@code DateTimeFormatter} to parse the date.
     * @param errorMessage The error message if validation fails.
     * @return A new {@code Rule<String>} for date validation.
     */
    public static Rule<String> dateRule(Predicate<String> predicate, DateTimeFormatter formatter, String errorMessage) {
        return new Rule<>(date -> {
            try {
                LocalDate.parse(date, formatter);
                return predicate.test(date);
            } catch (DateTimeParseException e) {
                return false;
            }
        }, errorMessage);
    }
    /**
     * Factory method to create a comparison rule for two objects.
     *
     * @param comparison The predicate to compare two objects.
     * @param errorMessage The error message if validation fails.
     * @return A new {@code Rule<Pair<T, T>>} for comparison validation.
     */
    public static <T> Rule<Pair<T, T>> comparisonRule(Predicate<Pair<T, T>> comparison, String errorMessage) {
        return new Rule<>(comparison, errorMessage);
    }

    /**
     * Returns rule.
     */
    public Predicate<T> getRule() {
        return rule;
    }

    /**
     * Returns errorMessage.
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * Checks if the given object passes the validation rule.
     *
     * @param test The object to be validated.
     * @throws IllegalArgumentException if the object does not pass the validation rule.
     */
    public void checkValidation(T test) {
        if (!rule.test(test)) {
            logger.warning("Validation failed for: " + test + " with error message: " + errorMessage);
            logger.info("Rule: " + rule.toString());
            throw new IllegalArgumentException(errorMessage);
        }
    }
}
