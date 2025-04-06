package seedu.innsync.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.innsync.logic.commands.CommandTestUtil.VALID_REQUEST_AMY;

import org.junit.jupiter.api.Test;

import seedu.innsync.model.request.Request;

public class JsonAdaptedRequestTest {
    @Test
    public void toModelType_validRequestDetails_returnsRequest() throws Exception {
        Request request = new Request(VALID_REQUEST_AMY);
        JsonAdaptedRequest jsonRequest = new JsonAdaptedRequest(request);
        assertEquals(request, jsonRequest.toModelType());
    }
}
