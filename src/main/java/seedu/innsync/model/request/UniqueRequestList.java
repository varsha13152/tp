package seedu.innsync.model.request;

import static java.util.Objects.requireNonNull;
import static seedu.innsync.commons.util.CollectionUtil.requireAllNonNull;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.innsync.model.request.exceptions.DuplicateRequestException;
import seedu.innsync.model.request.exceptions.RequestNotFoundException;

/**
 * A list of request that enforces uniqueness between its elements and does not allow nulls.
 * A request is considered unique by comparing using {@code Request#isSameRequest(Request)}. As such,
 * adding and updating of requests uses Request#isSameRequest(Request) for equality so as to ensure that the
 * request being added or updated is unique in terms of identity in the UniqueRequestList. However, the
 * removal of a request uses Request#equals(Object) so as to ensure that the request with exactly
 * the same fields will be removed.
 *
 * Supports a minimal set of list operations.
 *
 * @see Request#isSameRequest(Request)
 */
public class UniqueRequestList {

    private final ObservableList<Request> internalList = FXCollections.observableArrayList();
    private final ObservableList<Request> internalUnmodifiableList =
            FXCollections.unmodifiableObservableList(internalList);
    /**
     * Returns true if the list contains an equivalent person as the given argument regardless of casing.
     */
    public boolean contains(Request toCheck) {
        requireNonNull(toCheck);
        return internalList.stream().anyMatch(toCheck::equals);
    }

    /**
     * Gets request with the same name as the given argument.
     * Returns null if no such request exists.
     */
    public Request getRequest(String name) {
        requireNonNull(name);
        return internalList.stream()
                .filter(request -> request.requestName.equals(name))
                .findFirst()
                .orElse(null);
    }

    /**
     * Gets request from list if it exists.
     * Returns null if no such request exists.
     */
    public Request getRequest(Request request) {
        requireNonNull(request);
        return internalList.stream()
                .filter(r -> r.equals(request))
                .findFirst()
                .orElse(null);
    }

    /**
     * Adds a request to the list.
     * The request must not already exist in the list.
     */
    public void add(Request toAdd) {
        requireNonNull(toAdd);
        if (contains(toAdd)) {
            throw new DuplicateRequestException();
        }
        internalList.add(toAdd);
    }

    /**
     * Removes the equivalent request from the list.
     * The request must exist in the list.
     */
    public void remove(Request toRemove) {
        requireNonNull(toRemove);
        if (!internalList.remove(toRemove)) {
            throw new RequestNotFoundException();
        }
    }

    /**
     * Returns the backing list as an unmodifiable {@code ObservableList}.
     */
    public ObservableList<Request> asUnmodifiableObservableList() {
        return internalUnmodifiableList;
    }

    /**
     * Replaces the contents of this list with {@code requests}.
     * {@code requests} must not contain duplicate requests.
     */
    public void setRequests(List<Request> requests) {
        requireAllNonNull(requests);
        if (!requestsAreUnique(requests)) {
            throw new DuplicateRequestException();
        }

        internalList.setAll(requests);
    }


    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof UniqueRequestList)) {
            return false;
        }

        UniqueRequestList otherUniqueRequestList = (UniqueRequestList) other;
        return internalList.equals(otherUniqueRequestList.internalList);
    }

    @Override
    public String toString() {
        return internalList.toString();
    }

    private boolean requestsAreUnique(List<Request> requests) {
        Set<Request> requestSet = requests.stream().collect(Collectors.toSet());
        return requestSet.size() == requests.size();
    }
}
