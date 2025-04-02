package seedu.innsync.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.innsync.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.innsync.logic.parser.CliSyntax.PREFIX_BOOKINGTAG;
import static seedu.innsync.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.innsync.logic.parser.CliSyntax.PREFIX_MEMO;
import static seedu.innsync.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.innsync.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.innsync.logic.parser.CliSyntax.PREFIX_REQUEST;
import static seedu.innsync.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.innsync.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import seedu.innsync.commons.core.index.Index;
import seedu.innsync.commons.util.CollectionUtil;
import seedu.innsync.commons.util.ToStringBuilder;
import seedu.innsync.logic.Emoticons;
import seedu.innsync.logic.Messages;
import seedu.innsync.logic.commands.exceptions.CommandException;
import seedu.innsync.model.Model;
import seedu.innsync.model.person.Address;
import seedu.innsync.model.person.Email;
import seedu.innsync.model.person.Memo;
import seedu.innsync.model.person.Name;
import seedu.innsync.model.person.Person;
import seedu.innsync.model.person.Phone;
import seedu.innsync.model.request.Request;
import seedu.innsync.model.tag.BookingTag;
import seedu.innsync.model.tag.Tag;

/**
 * Edits the details of an existing person in the address book.
 */
public class EditCommand extends Command {

    public static final String COMMAND_WORD = "edit";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the details of the person identified "
            + "by the index number used in the displayed person list. "
            + "Existing values will be overwritten by the input values.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + "[" + PREFIX_NAME + "NAME] "
            + "[" + PREFIX_PHONE + "PHONE] "
            + "[" + PREFIX_EMAIL + "EMAIL] "
            + "[" + PREFIX_ADDRESS + "ADDRESS] "
            + "[" + PREFIX_MEMO + "MEMO] "
            + "[" + PREFIX_REQUEST + "REQUEST] "
            + "[" + PREFIX_BOOKINGTAG + "BOOKING_TAG] "
            + "[" + PREFIX_TAG + "TAG]...\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_PHONE + "+65 91234567 "
            + PREFIX_EMAIL + "johndoe@example.com";

    public static final String MESSAGE_EDIT_PERSON_SUCCESS = "Edited `%1$s`! " + Emoticons.PROUD;
    public static final String MESSAGE_FAILURE_INVALID_INDEX = Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX + "\n"
            + MESSAGE_USAGE;
    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided! " + Emoticons.SAD 
            + "\n" + MESSAGE_USAGE;
    public static final String MESSAGE_DUPLICATE_PERSON = "This person already exists in the address book! "
            + Emoticons.SAD +  "\n" + MESSAGE_USAGE;

    private final Index index;
    private final EditPersonDescriptor editPersonDescriptor;

    /**
     * @param index of the person in the filtered person list to edit
     * @param editPersonDescriptor details to edit the person with
     */
    public EditCommand(Index index, EditPersonDescriptor editPersonDescriptor) {
        requireNonNull(index);
        requireNonNull(editPersonDescriptor);

        this.index = index;
        this.editPersonDescriptor = new EditPersonDescriptor(editPersonDescriptor);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getPersonList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(MESSAGE_FAILURE_INVALID_INDEX);
        }

        Person personToEdit = lastShownList.get(index.getZeroBased());
        Person editedPerson = createEditedPerson(personToEdit, editPersonDescriptor);

        if (!personToEdit.isSamePerson(editedPerson) && model.hasPerson(editedPerson)) {
            throw new CommandException(MESSAGE_DUPLICATE_PERSON);
        }

        model.setPerson(personToEdit, editedPerson);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        return new CommandResult(String.format(MESSAGE_EDIT_PERSON_SUCCESS, editedPerson.getName()),
                editedPerson);
    }

    /**
     * Creates and returns a {@code Person} with the details of {@code personToEdit}
     * edited with {@code editPersonDescriptor}.
     */
    private static Person createEditedPerson(Person personToEdit, EditPersonDescriptor editPersonDescriptor) {
        assert personToEdit != null;

        Name updatedName = editPersonDescriptor.getName().orElse(personToEdit.getName());
        Phone updatedPhone = editPersonDescriptor.getPhone().orElse(personToEdit.getPhone());
        Email updatedEmail = editPersonDescriptor.getEmail().orElse(personToEdit.getEmail());
        Address updatedAddress = editPersonDescriptor.getAddress().orElse(personToEdit.getAddress());
        Memo updatedMemo = editPersonDescriptor.getMemo().orElse(personToEdit.getMemo());
        List<Request> updatedRequestList =
                editPersonDescriptor.getRequests().orElse(personToEdit.getRequests());
        Set<BookingTag> updatedBookingTags =
            editPersonDescriptor.getBookingTags().orElse(personToEdit.getBookingTags());
        Set<Tag> updatedTags = editPersonDescriptor.getTags().orElse(personToEdit.getTags());

        return new Person(updatedName, updatedPhone, updatedEmail, updatedAddress, updatedMemo, updatedRequestList,
                updatedBookingTags, updatedTags, personToEdit.getStarred());
    }

    @Override
    public boolean requireConfirmation() {
        return false;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof EditCommand)) {
            return false;
        }

        EditCommand otherEditCommand = (EditCommand) other;
        return index.equals(otherEditCommand.index)
                && editPersonDescriptor.equals(otherEditCommand.editPersonDescriptor);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("index", index)
                .add("editPersonDescriptor", editPersonDescriptor)
                .toString();
    }

    /**
     * Stores the details to edit the person with. Each non-empty field value will replace the
     * corresponding field value of the person.
     */
    public static class EditPersonDescriptor {
        private Name name;
        private Phone phone;
        private Email email;
        private Address address;
        private Memo memo;
        private List<Request> requests;
        private Set<BookingTag> bookingTags;
        private Set<Tag> tags;

        public EditPersonDescriptor() {}

        /**
         * Copy constructor.
         * A defensive copy of {@code tags} is used internally.
         */
        public EditPersonDescriptor(EditPersonDescriptor toCopy) {
            setName(toCopy.name);
            setPhone(toCopy.phone);
            setEmail(toCopy.email);
            setAddress(toCopy.address);
            setMemo(toCopy.memo);
            setRequests(toCopy.requests);
            setBookingTags(toCopy.bookingTags);
            setTags(toCopy.tags);
        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(name, phone, email, address, memo, requests, bookingTags, tags);
        }

        public void setName(Name name) {
            this.name = name;
        }

        public Optional<Name> getName() {
            return Optional.ofNullable(name);
        }

        public void setPhone(Phone phone) {
            this.phone = phone;
        }

        public Optional<Phone> getPhone() {
            return Optional.ofNullable(phone);
        }

        public void setEmail(Email email) {
            this.email = email;
        }

        public Optional<Email> getEmail() {
            return Optional.ofNullable(email);
        }

        public void setAddress(Address address) {
            this.address = address;
        }

        public Optional<Address> getAddress() {
            return Optional.ofNullable(address);
        }

        public void setMemo(Memo memo) {
            this.memo = memo;
        }

        public Optional<Memo> getMemo() {
            return Optional.ofNullable(memo);
        }

        public void setRequests(List<Request> requests) {
            this.requests = (requests != null) ? new ArrayList<>(requests) : null;
        }

        public Optional<List<Request>> getRequests() {
            return (requests != null) ? Optional.of(Collections.unmodifiableList(requests)) : Optional.empty();
        }

        public void setBookingTags(Set<BookingTag> bookingTags) {
            this.bookingTags = (bookingTags != null) ? new HashSet<>(bookingTags) : null;
        }

        public Optional<Set<BookingTag>> getBookingTags() {
            return (bookingTags != null) ? Optional.of(Collections.unmodifiableSet(bookingTags)) : Optional.empty();
        }

        /**
         * Sets {@code tags} to this object's {@code tags}.
         * A defensive copy of {@code tags} is used internally.
         */
        public void setTags(Set<Tag> tags) {
            this.tags = (tags != null) ? new HashSet<>(tags) : null;
        }

        /**
         * Returns an unmodifiable tag set, which throws {@code UnsupportedOperationException}
         * if modification is attempted.
         * Returns {@code Optional#empty()} if {@code tags} is null.
         */
        public Optional<Set<Tag>> getTags() {
            return (tags != null) ? Optional.of(Collections.unmodifiableSet(tags)) : Optional.empty();
        }

        @Override
        public boolean equals(Object other) {
            if (other == this) {
                return true;
            }

            // instanceof handles nulls
            if (!(other instanceof EditPersonDescriptor)) {
                return false;
            }

            EditPersonDescriptor otherEditPersonDescriptor = (EditPersonDescriptor) other;
            return Objects.equals(name, otherEditPersonDescriptor.name)
                    && Objects.equals(phone, otherEditPersonDescriptor.phone)
                    && Objects.equals(email, otherEditPersonDescriptor.email)
                    && Objects.equals(address, otherEditPersonDescriptor.address)
                    && Objects.equals(memo, otherEditPersonDescriptor.memo)
                    && Objects.equals(requests, otherEditPersonDescriptor.requests)
                    && Objects.equals(bookingTags, otherEditPersonDescriptor.bookingTags)
                    && Objects.equals(tags, otherEditPersonDescriptor.tags);
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this)
                    .add("name", name)
                    .add("phone", phone)
                    .add("email", email)
                    .add("address", address)
                    .add("memo", memo)
                    .add("requests", requests)
                    .add("bookingTags", bookingTags)
                    .add("tags", tags)
                    .toString();
        }
    }
}
