package seedu.innsync.model.util;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import seedu.innsync.model.AddressBook;
import seedu.innsync.model.ReadOnlyAddressBook;
import seedu.innsync.model.person.Address;
import seedu.innsync.model.person.Email;
import seedu.innsync.model.person.Name;
import seedu.innsync.model.person.Person;
import seedu.innsync.model.person.Phone;
import seedu.innsync.model.tag.BookingTag;
import seedu.innsync.model.tag.Tag;

/**
 * Contains utility methods for populating {@code AddressBook} with sample data.
 */
public class SampleDataUtil {

    public static Person[] getSamplePersons() {
        return new Person[] {
            new Person(new Name("Alex Yeoh"), new Phone("+65 87438807"), new Email("alexyeoh@example.com"),
                new Address("Blk 30 Geylang Street 29, #06-40"),
                    getBookingTagSet(),
                getTagSet("friends")),
            new Person(new Name("Bernice Yu"), new Phone("+65 99272758"), new Email("berniceyu@example.com"),
                new Address("Blk 30 Lorong 3 Serangoon Gardens, #07-18"),
                    getBookingTagSet(),
                getTagSet("colleagues", "friends")),
            new Person(new Name("Charlotte Oliveiro"), new Phone("+65 93210283"), new Email("charlotte@example.com"),
                new Address("Blk 11 Ang Mo Kio Street 74, #11-04"),
                    getBookingTagSet(),
                getTagSet("neighbours")),
            new Person(new Name("David Li"), new Phone("+65 91031282"), new Email("lidavid@example.com"),
                new Address("Blk 436 Serangoon Gardens Street 26, #16-43"),
                    getBookingTagSet(),
                getTagSet("family")),
            new Person(new Name("Irfan Ibrahim"), new Phone("+65 92492021"), new Email("irfan@example.com"),
                new Address("Blk 47 Tampines Street 20, #17-35"),
                    getBookingTagSet(),
                getTagSet("classmates")),
            new Person(new Name("Roy Balakrishnan"), new Phone("+65 92624417"), new Email("royb@example.com"),
                new Address("Blk 45 Aljunied Street 85, #11-31"),
                    getBookingTagSet(),
                getTagSet("colleagues"))
        };
    }

    public static ReadOnlyAddressBook getSampleAddressBook() {
        AddressBook sampleAb = new AddressBook();
        for (Person samplePerson : getSamplePersons()) {
            sampleAb.addPerson(samplePerson);
        }
        return sampleAb;
    }

    /**
     * Returns a date tag set containing the list of strings given.
     */
    public static Set<BookingTag> getBookingTagSet(String... strings) {
        return Arrays.stream(strings)
                .map(BookingTag::new)
                .collect(Collectors.toSet());
    }

    /**
     * Returns a tag set containing the list of strings given.
     */
    public static Set<Tag> getTagSet(String... strings) {
        return Arrays.stream(strings)
                .map(Tag::new)
                .collect(Collectors.toSet());
    }
}
