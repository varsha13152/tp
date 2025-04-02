package seedu.innsync.model;

import static java.util.Objects.requireNonNull;
import static seedu.innsync.commons.util.CollectionUtil.requireAllNonNull;

import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import seedu.innsync.commons.core.GuiSettings;
import seedu.innsync.commons.core.LogsCenter;
import seedu.innsync.model.person.Person;
import seedu.innsync.model.request.Request;

/**
 * Represents the in-memory model of the address book data.
 */
public class ModelManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final AddressBook addressBook;
    private final AddressBook backupAddressBook;
    private final UserPrefs userPrefs;
    private final FilteredList<Person> filteredPersons;

    /**
     * Initializes a ModelManager with the given addressBook and userPrefs.
     */
    public ModelManager(ReadOnlyAddressBook addressBook, ReadOnlyUserPrefs userPrefs) {
        requireAllNonNull(addressBook, userPrefs);

        logger.fine("Initializing with address book: " + addressBook + " and user prefs " + userPrefs);

        this.addressBook = new AddressBook(addressBook);
        this.backupAddressBook = new AddressBook(addressBook);
        this.userPrefs = new UserPrefs(userPrefs);
        filteredPersons = new FilteredList<>(this.addressBook.getPersonList());
    }

    public ModelManager() {
        this(new AddressBook(), new UserPrefs());
    }

    //=========== UserPrefs ==================================================================================

    @Override
    public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
        requireNonNull(userPrefs);
        this.userPrefs.resetData(userPrefs);
    }

    @Override
    public ReadOnlyUserPrefs getUserPrefs() {
        return userPrefs;
    }

    @Override
    public GuiSettings getGuiSettings() {
        return userPrefs.getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        requireNonNull(guiSettings);
        userPrefs.setGuiSettings(guiSettings);
    }

    @Override
    public Path getAddressBookFilePath() {
        return userPrefs.getAddressBookFilePath();
    }

    @Override
    public void setAddressBookFilePath(Path addressBookFilePath) {
        requireNonNull(addressBookFilePath);
        userPrefs.setAddressBookFilePath(addressBookFilePath);
    }

    //=========== AddressBook ================================================================================

    @Override
    public void setAddressBook(ReadOnlyAddressBook addressBook) {
        this.backupAddressBook.resetData(this.addressBook);
        this.addressBook.resetData(addressBook);
    }

    @Override
    public ReadOnlyAddressBook getAddressBook() {
        return addressBook;
    }

    @Override
    public boolean hasPerson(Person person) {
        requireNonNull(person);
        return addressBook.hasPerson(person);
    }

    @Override
    public void deletePerson(Person target) {
        this.backupAddressBook.resetData(this.addressBook);
        this.addressBook.removePerson(target);
    }

    @Override
    public void addPerson(Person person) {
        this.backupAddressBook.resetData(this.addressBook);
        addressBook.addPerson(person);
        updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
    }

    @Override
    public void setPerson(Person target, Person editedPerson) {
        requireAllNonNull(target, editedPerson);
        this.backupAddressBook.resetData(this.addressBook);
        addressBook.setPerson(target, editedPerson);
    }

    @Override
    public boolean revertToLastModified() {
        if (this.backupAddressBook.equals(this.addressBook)) {
            return false;
        }
        AddressBook prevAddressBook = new AddressBook(this.addressBook);
        this.addressBook.resetData(this.backupAddressBook);
        this.backupAddressBook.resetData(prevAddressBook);
        return true;
    }

    @Override
    public Request getRequestElseCreate(Request request) {
        requireNonNull(request);
        return this.addressBook.getRequestElseCreate(request);
    }

    @Override
    public Request getRequest(Request request) {
        requireNonNull(request);
        return this.addressBook.getRequest(request);
    }

    public void setRequests(List<Request> requests) {
        requireNonNull(requests);
        this.addressBook.setRequests(requests);
    }

    //=========== Filtered Person List Accessors =============================================================

    /**
     * Returns an unmodifiable view of the sorted filtered list of {@code Person} backed by the internal list of
     * {@code versionedAddressBook}
     */
    @Override
    public ObservableList<Person> getPersonList() {
        return getSortedFilteredPersonList(COMPARATOR_SHOW_STARRED_FIRST);
    }

    /**
     * Returns an unmodifiable view of the list of {@code Person} backed by the internal list of
     * {@code versionedAddressBook}
     */
    private ObservableList<Person> getFilteredPersonList() {
        return filteredPersons;
    }

    /**
     * Returns a sorted unmodifiable view of the list of {@code Person} backed by the internal list of
     * {@code versionedAddressBook}
     */
    private ObservableList<Person> getSortedFilteredPersonList(Comparator<Person> comparator) {
        ObservableList<Person> filteredList = getFilteredPersonList();
        return filteredList.sorted(comparator);
    }

    @Override
    public void updateFilteredPersonList(Predicate<Person> predicate) {
        requireNonNull(predicate);
        filteredPersons.setPredicate(predicate);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof ModelManager)) {
            return false;
        }

        ModelManager otherModelManager = (ModelManager) other;
        return addressBook.equals(otherModelManager.addressBook)
                && userPrefs.equals(otherModelManager.userPrefs)
                && filteredPersons.equals(otherModelManager.filteredPersons);
    }

}
