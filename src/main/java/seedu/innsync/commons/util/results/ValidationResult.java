package seedu.innsync.commons.util.results;

/**
 * Represents the result of a validation check.
 * <p>
 * This class encapsulates the result of a validation check, indicating whether the check was successful or not,
 * and providing an error message if applicable.
 */
public class ValidationResult {
    public final boolean isValid;
    public final String errorMessage;

    /**
     * Constructor for an invalid result.
     *
     * @param isValid true if the result is valid, false otherwise.
     * @param errorMessage the error message to be displayed if the rule is violated.
     */
    public ValidationResult(String errorMessage) {
        this.isValid = false;
        this.errorMessage = errorMessage;
    }

    /**
     * Constructor for a valid result.
     *
     * @param isValid true if the result is valid, false otherwise.
     */
    public ValidationResult(boolean isValid) {
        this.isValid = true;
        this.errorMessage = null;
    }
}
