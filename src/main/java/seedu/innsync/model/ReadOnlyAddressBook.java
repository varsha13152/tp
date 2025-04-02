package seedu.innsync.model;

import javafx.collections.ObservableList;
import seedu.innsync.model.person.Person;
import seedu.innsync.model.request.Request;
import seedu.innsync.model.tag.Tag;

/**
 * Unmodifiable view of an address book
 */
public interface ReadOnlyAddressBook {

    /**
     * Returns an unmodifiable view of the persons list.
     * This list will not contain any duplicate persons.
     */
    ObservableList<Person> getPersonList();
  
    /**
     * Returns an unmodifiable view of the requests list.
     * This list will not contain any duplicate requests.
     */
    ObservableList<Request> getRequestList();
  
    /**
     * Returns an unmodifiable view of the tags list.
     * This list will not contain any duplicate tags.
     */
    ObservableList<Tag> getTagList();
}
