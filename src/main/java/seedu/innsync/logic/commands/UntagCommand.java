package seedu.innsync.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.innsync.logic.parser.CliSyntax.PREFIX_BOOKINGTAG;
import static seedu.innsync.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.innsync.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import seedu.innsync.commons.core.index.Index;
import seedu.innsync.commons.util.ToStringBuilder;
import seedu.innsync.logic.Messages;
import seedu.innsync.logic.commands.exceptions.CommandException;
import seedu.innsync.model.Model;
import seedu.innsync.model.person.Person;
import seedu.innsync.model.tag.BookingTag;
import seedu.innsync.model.tag.Tag;

/**
 * Removes a tag from a contact in the addressbook
 */
public class UntagCommand extends Command {
    public static final String COMMAND_WORD = "untag";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Removes the tag from the contact identified by the index number used in the displayed person list.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + PREFIX_TAG + "TAG\n"
            + "or "
            + PREFIX_BOOKINGTAG + "{property} from/{start-date} to/{end-date}\n"
            + "Example: " + COMMAND_WORD + " 1 t/friends";
    public static final String MESSAGE_SUCCESS = "Tag has been successfully removed!: %s";
    public static final String MESSAGE_FAILURE_TAG = "Contact does not have the tag!: %s";
    public static final String MESSAGE_FAILURE_BOOKINGTAG = "Contact does not have the booking tag!: %s";

    private final Index index;
    private final String tag;
    private final String bookingTag;

    /**
     * @param index of the person in the filtered person list to edit
     * @param tag to be removed from the contact
     * @param bookingTag to be removed from the contact
     *
     */
    public UntagCommand(Index index, String tag, String bookingTag) {
        requireNonNull(index);
        this.index = index;
        this.tag = requireNonNull(tag);
        this.bookingTag = requireNonNull(bookingTag);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        List<Person> lastShownList = model.getPersonList();
        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToEdit = lastShownList.get(index.getZeroBased());
        Person editedPerson = removeTagsPerson(personToEdit, tag, bookingTag);

        model.setPerson(personToEdit, editedPerson);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);

        return new CommandResult(String.format(MESSAGE_SUCCESS, editedPerson));
    }

    /**
     * Removes the tag from the person
     * @param personToCopy the person to have its tag remove
     * @param toRemoveBookingTag the bookingTag to be removed
     * @param toRemoveTag the tag to be removed
     * @return the person with the tags or bookingTags removed
     */
    public static Person removeTagsPerson(Person personToCopy,
                                          String toRemoveTag, String toRemoveBookingTag)
            throws CommandException {
        Set<Tag> tagList = new HashSet<>(personToCopy.getTags());
        Set<BookingTag> bookingTagList = new HashSet<>(personToCopy.getBookingTags());

        if (!toRemoveTag.isEmpty()) {
            Tag tagToRemove = new Tag(toRemoveTag);
            if (!tagList.contains(tagToRemove)) {
                throw new CommandException(String.format(MESSAGE_FAILURE_TAG, toRemoveBookingTag));
            } else {
                tagList.remove(tagToRemove);
            }
        } else {
            BookingTag bookingTagToRemove = null;
            boolean found = false;
            for (BookingTag existingTag : bookingTagList) {
                if (existingTag.bookingTagName.equals(toRemoveBookingTag)) {
                    bookingTagToRemove = existingTag;
                    found = true;
                    break;
                }
            }
            if (found) {
                bookingTagList.remove(bookingTagToRemove);
            } else {
                throw new CommandException(String.format(MESSAGE_FAILURE_BOOKINGTAG, toRemoveBookingTag));
            }
        }

        return new Person(
                personToCopy.getName(),
                personToCopy.getPhone(),
                personToCopy.getEmail(),
                personToCopy.getAddress(),
                personToCopy.getMemo(),
                bookingTagList,
                tagList,
                personToCopy.getStarred());
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof UntagCommand)) {
            return false;
        }

        UntagCommand otherUntagCommand = (UntagCommand) other;
        return index.equals(otherUntagCommand.index) && tag.equals(otherUntagCommand.tag)
                && bookingTag.equals(otherUntagCommand.bookingTag);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("index", index)
                .toString();
    }
}
