package seedu.innsync.model.request;

import static java.util.Objects.requireNonNull;

import seedu.innsync.logic.Messages;

/**
 * Represents a Request in the system.
 * Guarantees: immutable; name is valid as declared in {@link #isValidRequest(String)}
 */
public class Request {

    public static final String MESSAGE_EMPTY = String.format(Messages.MESSAGE_EMPTY_FIELD, "Request");
    public static final String MESSAGE_LENGTH = String.format(Messages.MESSAGE_MAX_LENGTH_EXCEEDED,
            "Request", 170);

    public static final String REGEX_NOT_EMPTY = "^.+$"; // Ensures non-empty string
    public static final String REGEX_MAX_LENGTH = "^.{1,170}$"; // Ensures length <= 170

    public final String requestName;
    private boolean isCompleted = false;

    /**
     * Constructs a {@code Request}.
     *
     * @param requestName A valid request content.
     */
    public Request(String requestName) {
        requireNonNull(requestName);
        checkValidRequest(requestName);
        this.requestName = requestName;
    }

    /**
     * Clones a {@code Request}.
     *
     * @param requestToCopy A valid request.
     */
    public Request(Request requestToCopy) {
        requireNonNull(requestToCopy);
        checkValidRequest(requestToCopy.requestName);
        this.requestName = requestToCopy.requestName;
        this.isCompleted = requestToCopy.isCompleted;
    }

    /**
     * Checks if a given string is a valid request.
     *
     * @param test The string to be validated.
     * @throws IllegalArgumentException if the string is not a valid request.
     */
    public static void checkValidRequest(String test) {
        requireNonNull(test);

        if (!test.matches(REGEX_NOT_EMPTY)) {
            throw new IllegalArgumentException(MESSAGE_EMPTY);
        }
        if (!test.matches(REGEX_MAX_LENGTH)) {
            throw new IllegalArgumentException(MESSAGE_LENGTH);
        }
    }

    /**
     * Returns true if a given string is a valid request.
     *
     * @param test The string to be validated.
     * @return true if the string is a valid request.
     */
    public static boolean isValidRequest(String test) {
        requireNonNull(test);
        try {
            checkValidRequest(test);
        } catch (IllegalArgumentException e) {
            return false;
        }
        return true;
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
