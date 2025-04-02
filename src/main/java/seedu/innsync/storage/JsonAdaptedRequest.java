package seedu.innsync.storage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

import seedu.innsync.commons.exceptions.IllegalValueException;
import seedu.innsync.model.request.Request;

/**
 * Jackson-friendly version of {@link Request}.
 */
class JsonAdaptedRequest {

    private final String requestName;
    private final boolean isCompleted;

    /**
     * Constructs a {@code JsonAdaptedRequest} with the given {@code requestName}.
     */
    @JsonCreator
    public JsonAdaptedRequest(@JsonProperty("requestName") String requestName,
            @JsonProperty("isCompleted") boolean isCompleted) {
        this.requestName = requestName;
        this.isCompleted = isCompleted;
    }

    /**
     * Converts a given {@code Request} into this class for Jackson use.
     */
    public JsonAdaptedRequest(Request source) {
        requestName = source.requestName;
        isCompleted = source.isCompleted();
    }

    @JsonProperty("requestName")
    public String getRequestName() {
        return requestName;
    }

    @JsonProperty("isCompleted")
    public boolean getIsCompleted() {
        return isCompleted;
    }

    /**
     * Converts this Jackson-friendly adapted request object into the model's {@code Request} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted request.
     */
    public Request toModelType() throws IllegalValueException {
        if (!Request.isValidRequest(requestName)) {
            throw new IllegalValueException(Request.getErrorMessage(requestName));
        }
        Request request = new Request(requestName);
        if (isCompleted) {
            request.markAsCompleted();
        }
        return request;
    }

}
