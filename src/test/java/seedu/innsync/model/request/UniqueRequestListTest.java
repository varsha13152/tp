package seedu.innsync.model.request;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.innsync.logic.commands.CommandTestUtil.VALID_REQUEST_AMY;
import static seedu.innsync.logic.commands.CommandTestUtil.VALID_REQUEST_BOB;
import static seedu.innsync.testutil.Assert.assertThrows;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.innsync.model.request.exceptions.DuplicateRequestException;
import seedu.innsync.model.request.exceptions.RequestNotFoundException;


public class UniqueRequestListTest {

    private static final Request VALID_REQUEST_STUB = new Request(VALID_REQUEST_AMY);
    private final UniqueRequestList uniqueRequestList = new UniqueRequestList();

    @Test
    public void contains_nullPerson_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueRequestList.contains(null));
    }

    @Test
    public void contains_requestNotInList_returnsFalse() {
        assertFalse(uniqueRequestList.contains(VALID_REQUEST_STUB));
    }

    @Test
    public void contains_requestInList_returnsTrue() {
        uniqueRequestList.add(VALID_REQUEST_STUB);
        assertTrue(uniqueRequestList.contains(VALID_REQUEST_STUB));
    }

    @Test
    public void add_nullRequest_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueRequestList.add(null));
    }

    @Test
    public void add_duplicateRequest_throwsDuplicateRequestException() {
        uniqueRequestList.add(VALID_REQUEST_STUB);
        assertThrows(DuplicateRequestException.class, () -> uniqueRequestList.add(VALID_REQUEST_STUB));
    }

    @Test
    public void setRequest_nullTargetRequest_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueRequestList.setRequests(List.of(null,
                VALID_REQUEST_STUB)));
    }

    @Test
    public void remove_nullPerson_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueRequestList.remove(null));
    }

    @Test
    public void remove_requestDoesNotExist_throwsRequestNotFoundException() {
        assertThrows(RequestNotFoundException.class, () -> uniqueRequestList.remove(VALID_REQUEST_STUB));
    }

    @Test
    public void remove_existingRequest_removesRequest() {
        uniqueRequestList.add(VALID_REQUEST_STUB);
        uniqueRequestList.remove(VALID_REQUEST_STUB);
        UniqueRequestList expectedUniquerRequestList = new UniqueRequestList();
        assertEquals(expectedUniquerRequestList, uniqueRequestList);
    }

    @Test
    public void setRequests_uniqueRequestList_replacesOwnListWithProvidedUniqueRequestList() {
        uniqueRequestList.add(VALID_REQUEST_STUB);
        UniqueRequestList expectedUniquerRequestList = new UniqueRequestList();
        expectedUniquerRequestList.add(new Request(VALID_REQUEST_BOB));
        uniqueRequestList.setRequests(expectedUniquerRequestList.asUnmodifiableObservableList());
        assertEquals(expectedUniquerRequestList, uniqueRequestList);
    }

    @Test
    public void setRequests_nullList_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueRequestList.setRequests((List<Request>) null));
    }

    @Test
    public void setRequests_list_replacesOwnListWithProvidedList() {
        uniqueRequestList.add(VALID_REQUEST_STUB);
        List<Request> requestList = Collections.singletonList(new Request(VALID_REQUEST_AMY));
        uniqueRequestList.setRequests(requestList);
        UniqueRequestList expectedUniqueRequestList = new UniqueRequestList();
        expectedUniqueRequestList.add(VALID_REQUEST_STUB);
        assertEquals(expectedUniqueRequestList, uniqueRequestList);
    }

    @Test
    public void setRequests_listWithDuplicateRequests_throwsDuplicatePersonException() {
        List<Request> listWithDuplicateRequest = Arrays.asList(VALID_REQUEST_STUB, VALID_REQUEST_STUB);
        assertThrows(DuplicateRequestException.class, () -> uniqueRequestList.setRequests(listWithDuplicateRequest));
    }

    @Test
    public void asUnmodifiableObservableList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, ()
            -> uniqueRequestList.asUnmodifiableObservableList().remove(0));
    }

    @Test
    public void toStringMethod() {
        assertEquals(uniqueRequestList.asUnmodifiableObservableList().toString(), uniqueRequestList.toString());
    }
}
