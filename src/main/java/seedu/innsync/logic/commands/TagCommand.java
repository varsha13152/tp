package seedu.innsync.logic.commands;

import static java.util.Objects.requireNonNull;
import static java.util.Objects.requireNonNullElse;
import static seedu.innsync.logic.parser.CliSyntax.PREFIX_BOOKINGTAG;
import static seedu.innsync.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.innsync.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import seedu.innsync.commons.core.index.Index;
import seedu.innsync.commons.util.ToStringBuilder;
import seedu.innsync.logic.Emoticons;
import seedu.innsync.logic.Messages;
import seedu.innsync.logic.commands.exceptions.CommandException;
import seedu.innsync.model.Model;
import seedu.innsync.model.person.Person;
import seedu.innsync.model.tag.BookingTag;
import seedu.innsync.model.tag.Tag;
import seedu.innsync.model.tag.exceptions.DuplicateTagException;

/**
 * Adds a (booking) tag into a person to the address book.
 */
public class TagCommand extends Command {

    public static final String COMMAND_WORD = "tag";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Adds tag to the contact identified by the index number in the displayed person list.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + PREFIX_TAG + "TAG or\n"
            + PREFIX_BOOKINGTAG + "{property} from/{start-date} to/{end-date}\n"
            + "Example: " + COMMAND_WORD + " 1 t/friends t/handsome\n"
            + "Example: " + COMMAND_WORD + " 1 b/BeachHouse from/2025-06-01 to/2025-06-10";
    public static final String MESSAGE_SUCCESS = "Tag(s) successfully added to `%s`!" + Emoticons.PROUD;
    public static final String MESSAGE_FAILURE_INVALID_INDEX = Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX + "\n"
            + MESSAGE_USAGE;
    public static final String MESSAGE_DUPLICATE_TAG = "This contact already has this tag! " + Emoticons.SAD + "\n"
            + MESSAGE_USAGE;
    public static final String MESSAGE_FAILURE = "Failed to add booking tag! "
            + "The booking tag %s overlaps with an existing tag. " + Emoticons.SAD + "\n"
            + MESSAGE_USAGE;

    private final Index index;
    private final Set<Tag> tagList;
    private final Set<BookingTag> bookingTagList;

    /**
     * Creates an TagCommand to add the specified {@code index}
     * and {@code tag}
     */
    public TagCommand(Index index, Set<Tag> tagList, Set<BookingTag> bookingTagList) {
        requireNonNull(index);
        this.index = index;
        this.tagList = requireNonNullElse(tagList, new HashSet<>());
        this.bookingTagList = requireNonNullElse(bookingTagList, new HashSet<>());
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        List<Person> lastShownList = model.getPersonList();
        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(MESSAGE_FAILURE_INVALID_INDEX);
        }

        Person personToEdit = lastShownList.get(this.index.getZeroBased());

        Set<Tag> modelTags = new HashSet<>();
        for (Tag tag : tagList) {
            modelTags.add(model.getTagElseCreate(tag));
        }

        Person editedPerson = addTagsPerson(personToEdit, bookingTagList);

        model.setPerson(personToEdit, editedPerson);

        for (Tag tag : modelTags) {
            try {
                editedPerson.addTag(tag);
            } catch (DuplicateTagException e) {
                throw new CommandException(String.format(Messages.MESSAGE_DUPLICATE_FIELD, tag));
            }
        }

        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        return new CommandResult(String.format(MESSAGE_SUCCESS, editedPerson.getName()), editedPerson);
    }

    private Person addTagsPerson(Person personToCopy, Set<BookingTag> bookingTags)
            throws CommandException {
        requireNonNull(personToCopy);

        Set<BookingTag> updatedBookingTags = new HashSet<>(personToCopy.getBookingTags());

        for (BookingTag bookingTag : bookingTags) {
            for (BookingTag existingTag : updatedBookingTags) {
                if (isOverlapping(existingTag, bookingTag)) {
                    throw new CommandException(String.format(MESSAGE_FAILURE, bookingTag.toPrettier()));
                }
            }
            updatedBookingTags.add(bookingTag);
        }

        return new Person(
                personToCopy.getName(),
                personToCopy.getPhone(),
                personToCopy.getEmail(),
                personToCopy.getAddress(),
                personToCopy.getMemo(),
                personToCopy.getRequests(),
                updatedBookingTags,
                personToCopy.getTags(),
                personToCopy.getStarred());
    }

    /**
     * Helper method to check if two booking tags overlap
     */
    private boolean isOverlapping(BookingTag tag1, BookingTag tag2) {
        return !(tag1.endDate.isBefore(tag2.startDate) || tag2.endDate.isBefore(tag1.startDate));
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
        if (!(other instanceof TagCommand)) {
            return false;
        }

        TagCommand otherTag = (TagCommand) other;
        return index.equals(otherTag.index) && tagList.equals(otherTag.tagList)
                && bookingTagList.equals(otherTag.bookingTagList);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("index", index)
                .add("tagList", tagList.toString())
                .add("bookingTagList", bookingTagList.toString())
                .toString();
    }
}
