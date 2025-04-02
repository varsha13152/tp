package seedu.innsync.logic.parser;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import seedu.innsync.commons.core.index.Index;
import seedu.innsync.commons.util.StringUtil;
import seedu.innsync.logic.parser.exceptions.ParseException;
import seedu.innsync.model.person.Address;
import seedu.innsync.model.person.Email;
import seedu.innsync.model.person.Memo;
import seedu.innsync.model.person.Name;
import seedu.innsync.model.person.Phone;
import seedu.innsync.model.request.Request;
import seedu.innsync.model.tag.BookingTag;
import seedu.innsync.model.tag.Tag;

/**
 * Contains utility methods used for parsing strings in the various *Parser classes.
 */
public class ParserUtil {

    public static final String MESSAGE_INVALID_INDEX = "Index is not a non-zero unsigned integer.";

    /**
     * Parses {@code oneBasedIndex} into an {@code Index} and returns it. Leading and trailing whitespaces will be
     * trimmed.
     * @throws ParseException if the specified index is invalid (not non-zero unsigned integer).
     */
    public static Index parseIndex(String oneBasedIndex) throws ParseException {
        String trimmedIndex = oneBasedIndex.trim();
        if (!StringUtil.isNonZeroUnsignedInteger(trimmedIndex)) {
            throw new ParseException(MESSAGE_INVALID_INDEX);
        }
        return Index.fromOneBased(Integer.parseInt(trimmedIndex));
    }

    /**
     * Parses a {@code String name} into a {@code Name}.
     * Leading and trailing whitespaces will be trimmed, and consecutive spaces will be normalized.
     *
     * @throws ParseException if the given {@code name} is invalid.
     */
    public static Name parseName(String name) throws ParseException {
        requireNonNull(name);
        String normalizedName = StringUtil.normalizeWhitespace(name).replace("$", "");
        if (!Name.isValidName(normalizedName)) {
            throw new ParseException(Name.getErrorMessage(name));
        }
        return new Name(normalizedName);
    }

    /**
     * Parses a {@code String phone} into a {@code Phone}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code phone} is invalid.
     */
    public static Phone parsePhone(String phone) throws ParseException {
        requireNonNull(phone);
        String trimmedPhone = phone.trim();
        if (!Phone.isValidPhone(trimmedPhone)) {
            throw new ParseException(Phone.MESSAGE_CONSTRAINTS);
        }
        return new Phone(trimmedPhone);
    }

    /**
     * Parses a {@code String address} into an {@code Address}.
     * Leading and trailing whitespaces will be trimmed, and consecutive spaces will be normalized.
     *
     * @throws ParseException if the given {@code address} is invalid.
     */
    public static Address parseAddress(String address) throws ParseException {
        requireNonNull(address);
        String normalizedAddress = StringUtil.normalizeWhitespace(address);
        if (!Address.isValidAddress(normalizedAddress)) {
            throw new ParseException(Address.getErrorMessage(address));
        }
        return new Address(normalizedAddress);
    }

    /**
     * Parses a {@code String email} into an {@code Email}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code email} is invalid.
     */
    public static Email parseEmail(String email) throws ParseException {
        requireNonNull(email);
        String trimmedEmail = email.trim();
        if (!Email.isValidEmail(trimmedEmail)) {
            throw new ParseException(Email.MESSAGE_CONSTRAINTS);
        }
        return new Email(trimmedEmail);
    }

    /**
     * Parses a {@code String Memo} into an {@code Memo}.
     * Leading and trailing whitespaces will be trimmed.
     *
     */
    public static Memo parseMemo(String memo) throws ParseException {
        requireNonNull(memo);
        String trimmedMemo = memo.trim();
        if (trimmedMemo.isEmpty() || Memo.isValidMemo(trimmedMemo)) {
            return new Memo(trimmedMemo);
        }
        throw new ParseException(Memo.MESSAGE_LENGTH);
    }


    /**
     * Parses a {@code String request} into a {@code request}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code request} is invalid.
     */
    public static Request parseRequest(String request) throws ParseException {
        requireNonNull(request);
        String trimmedRequest = request.trim();
        if (!Request.isValidRequest(trimmedRequest)) {
            throw new ParseException(Request.getErrorMessage(trimmedRequest));
        }
        return new Request(trimmedRequest);
    }

    /**
     * Parses {@code Collection<String> requests} into a {@code RequestList}.
     */
    public static List<Request> parseRequests(Collection<String> requests) throws ParseException {
        requireNonNull(requests);
        final List<Request> requestList = new ArrayList<>();
        for (String requestName : requests) {
            requestList.add(parseRequest(requestName));
        }
        return requestList;
    }


    /**
     * Parses a {@code String bookingTag} into a {@code bookingTag}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code bookingTag} is invalid.
     */
    public static BookingTag parseBookingTag(String bookingTag) throws ParseException {
        requireNonNull(bookingTag);
        String trimmedBookingTag = bookingTag.trim();
        if (!BookingTag.isValidBookingTagName(trimmedBookingTag)) {
            throw new ParseException(BookingTag.MESSAGE_CONSTRAINTS);
        }
        return new BookingTag(trimmedBookingTag);
    }

    /**
     * Parses {@code Collection<String> bookingTags} into a {@code Set<BookingTag>}.
     */
    public static Set<BookingTag> parseBookingTags(Collection<String> bookingTags) throws ParseException {
        requireNonNull(bookingTags);
        final Set<BookingTag> bookingTagSet = new HashSet<>();
        for (String bookingTagName : bookingTags) {
            bookingTagSet.add(parseBookingTag(bookingTagName));
        }
        return bookingTagSet;
    }

    /**
     * Parses a {@code String tag} into a {@code Tag}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code tag} is invalid.
     */
    public static Tag parseTag(String tag) throws ParseException {
        requireNonNull(tag);
        String trimmedTag = tag.trim();
        if (!Tag.isValidTagName(trimmedTag)) {
            throw new ParseException(Tag.MESSAGE_CONSTRAINTS);
        }
        return new Tag(trimmedTag);
    }

    /**
     * Parses {@code Collection<String> tags} into a {@code Set<Tag>}.
     */
    public static Set<Tag> parseTags(Collection<String> tags) throws ParseException {
        requireNonNull(tags);
        final Set<Tag> tagSet = new HashSet<>();
        for (String tagName : tags) {
            tagSet.add(parseTag(tagName));
        }
        return tagSet;
    }
}
