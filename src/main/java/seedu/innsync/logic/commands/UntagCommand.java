package seedu.innsync.logic.commands;

import static java.util.Objects.requireNonNull;
import static java.util.Objects.requireNonNullElse;
import static seedu.innsync.logic.parser.CliSyntax.PREFIX_BOOKINGTAG;
import static seedu.innsync.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.innsync.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.List;
import java.util.Set;

import seedu.innsync.commons.core.index.Index;
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
            + PREFIX_BOOKINGTAG + "{property} from/{start-date} to/{end-date}\n"
            + "Example: " + COMMAND_WORD + " 1 t/friends";
    public static final String MESSAGE_SUCCESS = "Tag has been successfully removed!: %s";
    public static final String MESSAGE_FAILURE = "Contact does not have the tag!: %s";

    private final Index index;
    private final List<String> tags;
    private final List<String> bookingTags;

    /**
     * @param index of the person in the filtered person list to edit
     * @param tag to be removed from the contact
     */
    public UntagCommand(Index index, List<String> tagList, List<String> bookingTagList) {
        requireNonNull(index);
        this.index = index;
        this.tags = requireNonNullElse(tagList, List.of());
        this.bookingTags = requireNonNullElse(bookingTagList, List.of());
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        List<Person> lastShownList = model.getPersonList();
        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToEdit = lastShownList.get(index.getZeroBased());
        Person editedPerson = removeTagsPerson(personToEdit, tags, bookingTags);

        model.setPerson(personToEdit, editedPerson);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);

        return new CommandResult(String.format(MESSAGE_SUCCESS, editedPerson));
    }

    /**
     * Removes the tag from the person
     * @param personToEdit the person to remove the tag from
     * @param tag the tag to be removed
     * @return the person with the tag removed
     */
    public static Person removeTagsPerson(Person personToCopy, List<String> tags, List<String> bookingTags) {
        Set<Tag> tagList = personToCopy.getTags();
        Set<BookingTag> bookingTagList = personToCopy.getBookingTags();

        for (String tag : tags) {
            Tag tagToRemove = new Tag(tag);
            if (tagList.contains(tagToRemove)) {
                tagList.remove(tagToRemove);
            }
        }

        for (String bookingTag : bookingTags) {
            BookingTag bookingTagToRemove = null;
            for (BookingTag existingTag : bookingTagList) {
                if (existingTag.bookingTagName.equals(bookingTag)) {
                    bookingTagToRemove = existingTag;
                    break;
                }
                bookingTagList.remove(bookingTagToRemove);
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
}
