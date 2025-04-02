package seedu.innsync.model.request;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.innsync.logic.commands.CommandTestUtil.VALID_REQUEST_AMY;
import static seedu.innsync.logic.commands.CommandTestUtil.VALID_REQUEST_BOB;
import static seedu.innsync.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class RequestTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Request((String) null));
        assertThrows(NullPointerException.class, () -> new Request((Request) null));
    }

    @Test
    public void isValidRequestName() {
        // null tag name
        assertThrows(NullPointerException.class, () -> Request.isValidRequest(null));
    }

    // Test for valid request names
    @Test
    void constructor_validRequestName_noExceptions() {
        // Valid request names should not throw exceptions
        assertDoesNotThrow(() -> new Request(VALID_REQUEST_AMY));
        assertDoesNotThrow(() -> new Request(VALID_REQUEST_BOB));
    }

    // Test for isValidRequest method
    @Test
    void isValidRequest_validRequestNames() {
        assertTrue(Request.isValidRequest("!@#$%^&*()_+ Valid Request"));
        assertTrue(Request.isValidRequest("ValidRequestWithoutSpaces"));
        assertTrue(Request.isValidRequest("Request with spaces"));
        assertTrue(Request.isValidRequest("Another Valid Request"));
    }

    // Test for equals method
    @Test
    void equals_sameRequest_returnsTrue() {
        Request request1 = new Request("!@#$%^&*()_+ Valid Request");
        Request request2 = new Request("!@#$%^&*()_+ Valid Request");
        Request request3 = new Request(VALID_REQUEST_AMY);
        Request request4 = new Request(VALID_REQUEST_AMY);

        assertTrue(request1.equals(request2));
        assertTrue(request3.equals(request4));
    }

    @Test
    void equals_differentRequest_returnsFalse() {
        Request request1 = new Request(VALID_REQUEST_AMY);
        Request request2 = new Request(VALID_REQUEST_BOB);

        assertFalse(request1.equals(request2));
    }

    // Test hashCode
    @Test
    void hashCode_sameRequest_returnsSameHashCode() {
        Request request1 = new Request("!@#$%^&*()_+ Valid Request");
        Request request2 = new Request("!@#$%^&*()_+ Valid Request");
        Request request3 = new Request(VALID_REQUEST_AMY);
        Request request4 = new Request(VALID_REQUEST_AMY);

        assertEquals(request1.hashCode(), request2.hashCode());
        assertEquals(request3.hashCode(), request4.hashCode());
    }

    @Test
    void hashCode_differentRequest_returnsDifferentHashCode() {
        Request request1 = new Request(VALID_REQUEST_AMY);
        Request request2 = new Request(VALID_REQUEST_BOB);

        assertNotEquals(request1.hashCode(), request2.hashCode());
    }

    // Test toString method
    @Test
    void toString_correctFormat() {
        Request request = new Request(VALID_REQUEST_AMY);
        assertEquals("[" + VALID_REQUEST_AMY + "]", request.toString());
    }

    // Test default completion state
    @Test
    void isCompleted_defaultState_false() {
        Request request = new Request(VALID_REQUEST_AMY);
        assertFalse(request.isCompleted());
    }

    // Test markAsCompleted()
    @Test
    void markAsCompleted_setsToTrue() {
        Request request = new Request(VALID_REQUEST_AMY);
        request.markAsCompleted();
        assertTrue(request.isCompleted());
    }

    // Test markAsIncomplete()
    @Test
    void markAsIncomplete_setsToFalse() {
        Request request = new Request(VALID_REQUEST_AMY);
        request.markAsCompleted(); // First, mark it as completed
        assertTrue(request.isCompleted()); // Verify it's marked
        request.markAsIncomplete(); // Now, unmark it
        assertFalse(request.isCompleted()); // Verify it's unmarked
    }

    // Test isSameRequest()
    @Test
    void isSameRequest_returnsTrue() {
        Request request = new Request(VALID_REQUEST_AMY);
        Request sameRequest = new Request(VALID_REQUEST_AMY);
        assertTrue(request.isSameRequest(request));
        assertTrue(request.isSameRequest(sameRequest));
    }

    @Test
    public void equals() {
        Request request = new Request(VALID_REQUEST_AMY);
        Request sameRequest = new Request(VALID_REQUEST_AMY);

        // same values -> returns true
        assertTrue(request.equals(sameRequest));

        // null -> returns false
        assertFalse(request.equals(null));
    }
}
