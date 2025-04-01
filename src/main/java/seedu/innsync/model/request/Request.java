package seedu.innsync.model.request;

import static java.util.Objects.requireNonNull;
import static seedu.innsync.commons.util.AppUtil.checkArgument;

/**
 * Represents a Request in the system.
 * Guarantees: immutable; name is valid as declared in {@link #isValidRequest(String)}
 */
public class Request {

    public static final String MESSAGE_CONSTRAINTS = "Request names should not be empty, contain the '/' character, "
            + "and must not exceed 255 characters.";
    public static final String VALIDATION_REGEX = "^[^/]{1,255}$";

    public final String requestName;
    private boolean isCompleted = false;

    /**
     * Constructs a {@code Request}.
     *
     * @param requestName A valid request content.
     */
    public Request(String requestName) {
        requireNonNull(requestName);
        checkArgument(isValidRequest(requestName), MESSAGE_CONSTRAINTS);
        this.requestName = requestName;
    }

    /**
     * Returns true if a given string is a valid request format.
     */
    public static boolean isValidRequest(String test) {
        return test.matches(VALIDATION_REGEX);
    }

    public String getRequestName() {
        return requestName;
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
        return requestName.equals(otherRequest.requestName) && isCompleted == otherRequest.isCompleted;
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

    /**
     * Returns true if both requests have the same request name.
     * This defines a weaker notion of equality between two requests.
     */
    public boolean isSameRequest(Request otherRequest) {
        if (otherRequest == this) {
            return true;
        }

        return otherRequest != null
                && otherRequest.getRequestName().equals(getRequestName());
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
