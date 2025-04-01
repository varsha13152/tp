package seedu.innsync.model.request;

import static java.util.Objects.requireNonNull;
import static seedu.innsync.commons.util.AppUtil.checkArgument;

/**
 * Represents a Request in the system.
 * Guarantees: immutable; name is valid as declared in {@link #isValidRequest(String)}
 */
public class Request {

    public static final String MESSAGE_EMPTY = "Request name should not be empty.";
    public static final String MESSAGE_LENGTH = "Request name must not exceed 255 characters.";

    public static final String REGEX_NOT_EMPTY = "^.+$"; // Ensures non-empty string
    public static final String REGEX_MAX_LENGTH = "^.{1,255}$"; // Ensures length <= 255

    public final String requestName;
    private boolean isCompleted = false;

    /**
     * Constructs a {@code Request}.
     *
     * @param requestName A valid request content.
     */
    public Request(String requestName) {
        requireNonNull(requestName);
        checkArgument(isValidRequest(requestName), getErrorMessage(requestName));
        this.requestName = requestName;
    }

    /**
     * Returns true if a given string matches all validation rules.
     */
    public static boolean isValidRequest(String test) {
        return test.matches(REGEX_NOT_EMPTY) && test.matches(REGEX_MAX_LENGTH);
    }

    /**
     * Determines the specific error message based on the invalid request name.
     */
    public static String getErrorMessage(String test) {
        if (!test.matches(REGEX_NOT_EMPTY)) {
            return MESSAGE_EMPTY;
        }
        return MESSAGE_LENGTH;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof Request)) {
            return false;
        }
        Request otherRequest = (Request) other;
        return requestName.equals(otherRequest.requestName);
    }

    /**
     * Returns whether the request is completed.
     *
     * @return True if the request is completed, false otherwise.
     */
    public boolean isCompleted() {
        return this.isCompleted;
    }

    /**
     * Marks the request as completed.
     */
    public void markAsCompleted() {
        this.isCompleted = true;
    }

    /**
     * Marks the request as incomplete.
     */
    public void markAsIncomplete() {
        this.isCompleted = false;
    }

    @Override
    public int hashCode() {
        return requestName.hashCode();
    }

    /**
     * Format state as text for storage.
     */
    @Override
    public String toString() {
        return '[' + requestName + ']';
    }
}
