package seedu.innsync.model.request;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.innsync.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class RequestTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Request(null));
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
        assertDoesNotThrow(() -> new Request("Valid Request"));
        assertDoesNotThrow(() -> new Request("Another Valid Request"));
    }

    // Test for invalid request names
    @Test
    void constructor_invalidRequestName_throwsIllegalArgumentException() {
        // Invalid request names should throw IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> new Request("Invalid/Request"));
        assertThrows(IllegalArgumentException.class, () -> new Request("Request with / slash"));
        assertThrows(IllegalArgumentException.class, () -> new Request("/leadingSlash"));
        assertThrows(IllegalArgumentException.class, () -> new Request("trailingSlash/"));
    }

    // Test for isValidRequest method
    @Test
    void isValidRequest_validRequestNames() {
        assertTrue(Request.isValidRequest("!@#$%^&*()_+ Valid Request"));
        assertTrue(Request.isValidRequest("ValidRequestWithoutSpaces"));
        assertTrue(Request.isValidRequest("Request with spaces"));
        assertTrue(Request.isValidRequest("Another Valid Request"));
    }

    @Test
    void isValidRequest_invalidRequestNames() {
        // Test invalid names that contain '/'
        assertFalse(Request.isValidRequest("Invalid/Request"));
        assertFalse(Request.isValidRequest("Request with / slash"));
        assertFalse(Request.isValidRequest("/leadingSlash"));
        assertFalse(Request.isValidRequest("trailingSlash/"));
    }

    // Test for equals method
    @Test
    void equals_sameRequest_returnsTrue() {
        Request request1 = new Request("!@#$%^&*()_+ Valid Request");
        Request request2 = new Request("!@#$%^&*()_+ Valid Request");

        assertTrue(request1.equals(request2));
    }

    @Test
    void equals_differentRequest_returnsFalse() {
        Request request1 = new Request("Valid Request");
        Request request2 = new Request("Different Request");

        assertFalse(request1.equals(request2));
    }

    // Test hashCode
    @Test
    void hashCode_sameRequest_returnsSameHashCode() {
        Request request1 = new Request("!@#$%^&*()_+ Valid Request");
        Request request2 = new Request("!@#$%^&*()_+ Valid Request");

        assertEquals(request1.hashCode(), request2.hashCode());
    }

    @Test
    void hashCode_differentRequest_returnsDifferentHashCode() {
        Request request1 = new Request("Valid Request");
        Request request2 = new Request("Different Request");

        assertNotEquals(request1.hashCode(), request2.hashCode());
    }

    // Test toString method
    @Test
    void toString_correctFormat() {
        Request request = new Request("Valid Request");
        assertEquals("[Valid Request]", request.toString());
    }
}

