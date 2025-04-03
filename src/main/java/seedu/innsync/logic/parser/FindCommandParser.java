package seedu.innsync.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.innsync.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.innsync.logic.parser.CliSyntax.PREFIX_BOOKING_DATE;
import static seedu.innsync.logic.parser.CliSyntax.PREFIX_BOOKING_PROPERTY;
import static seedu.innsync.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.innsync.logic.parser.CliSyntax.PREFIX_MEMO;
import static seedu.innsync.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.innsync.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.innsync.logic.parser.CliSyntax.PREFIX_TAG;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import seedu.innsync.logic.Emoticons;
import seedu.innsync.logic.Messages;
import seedu.innsync.logic.commands.FindCommand;
import seedu.innsync.logic.commands.FindCommand.SearchType;
import seedu.innsync.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new FindCommand object
 */
public class FindCommandParser implements Parser<FindCommand> {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * Maps prefixes to their corresponding search types
     */
    private static final Map<Prefix, SearchType> PREFIX_TO_SEARCH_TYPE = Map.of(
            PREFIX_NAME, SearchType.NAME,
            PREFIX_PHONE, SearchType.PHONE,
            PREFIX_EMAIL, SearchType.EMAIL,
            PREFIX_ADDRESS, SearchType.ADDRESS,
            PREFIX_TAG, SearchType.TAG,
            PREFIX_MEMO, SearchType.MEMO,
            PREFIX_BOOKING_DATE, SearchType.BOOKING_DATE,
            PREFIX_BOOKING_PROPERTY, SearchType.BOOKING_PROPERTY
    );

    /**
     * Parses the given {@code String} of arguments in the context of the FindCommand
     * and returns a FindCommand object for execution.
     * @throws ParseException if the user input does not conform to the expected format
     */
    public FindCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(
                args,
                PREFIX_NAME,
                PREFIX_PHONE,
                PREFIX_EMAIL,
                PREFIX_ADDRESS,
                PREFIX_TAG,
                PREFIX_MEMO,
                PREFIX_BOOKING_DATE,
                PREFIX_BOOKING_PROPERTY);

        Map<SearchType, List<String>> searchCriteria = new HashMap<>();

        for (Map.Entry<Prefix, SearchType> entry : PREFIX_TO_SEARCH_TYPE.entrySet()) {
            Prefix prefix = entry.getKey();
            SearchType searchType = entry.getValue();

            List<String> prefixValues = argMultimap.getAllValues(prefix);
            if (!prefixValues.isEmpty()) {
                processPrefix(prefixValues, prefix, searchType, searchCriteria);
            }
        }

        if (searchCriteria.isEmpty()) {
            throw new ParseException(String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        }

        return new FindCommand(searchCriteria);
    }

    /**
     * Processes all values for a given prefix and adds them to the search criteria.
     * Each prefix value is kept as a complete string without splitting.
     *
     * @param prefixValues The list of values for a specific prefix
     * @param prefix The prefix being processed
     * @param searchType The search type corresponding to this prefix
     * @param searchCriteria The map to store search criteria
     * @throws ParseException if validation fails
     */
    private void processPrefix(List<String> prefixValues, Prefix prefix,
                               SearchType searchType, Map<SearchType, List<String>> searchCriteria)
            throws ParseException {
        requireNonNull(prefixValues);
        requireNonNull(prefix);
        requireNonNull(searchType);
        requireNonNull(searchCriteria);

        List<String> keywords = new ArrayList<>();

        // Add each complete prefix value as a keyword without splitting
        for (String value : prefixValues) {
            String trimmedValue = value.trim();
            if (!trimmedValue.isEmpty()) {
                keywords.add(trimmedValue);
            }
        }

        validateKeywords(keywords, prefix);
        searchCriteria.put(searchType, keywords);
    }

    /**
     * Extracts keywords from a string, splitting by whitespace.
     * This method is kept for backward compatibility but is no longer used
     * in the main parsing flow.
     *
     * @param value The string to extract keywords from
     * @return A list of keywords extracted from the string
     */
    private List<String> extractKeywords(String value) {
        requireNonNull(value);

        List<String> keywords = new ArrayList<>();
        String[] keywordArray = value.trim().split("\\s+");
        for (String keyword : keywordArray) {
            if (!keyword.isEmpty()) {
                keywords.add(keyword);
            }
        }
        return keywords;
    }

    /**
     * Validates keywords based on the provided prefix.
     * Checks if all keywords are valid according to their prefix type.
     *
     * @param keywords The list of keywords to validate
     * @param prefix The prefix that determines validation rules
     * @throws ParseException if any keyword is invalid or if the keywords list is empty
     */
    private void validateKeywords(List<String> keywords, Prefix prefix) throws ParseException {
        requireNonNull(keywords);
        requireNonNull(prefix);

        if (keywords.isEmpty()) {
            throw new ParseException("Error: Please enter a keyword after " + prefix + " when searching by "
                    + getPrefixDescription(prefix) + ". " + Emoticons.ANGRY);
        }

        List<String> invalidKeywords = new ArrayList<>();

        for (String keyword : keywords) {
            if (!isValidKeyword(keyword, prefix)) {
                invalidKeywords.add(keyword);
            }
        }

        if (!invalidKeywords.isEmpty()) {
            throw new ParseException(getErrorMessage(prefix, invalidKeywords));
        }
    }

    /**
     * Checks if a keyword is valid for the given prefix.
     * Different validation rules apply based on the prefix type.
     *
     * @param keyword The keyword to validate
     * @param prefix The prefix that determines validation rules
     * @return true if the keyword is valid for the prefix, false otherwise
     */
    private boolean isValidKeyword(String keyword, Prefix prefix) {
        requireNonNull(keyword);
        requireNonNull(prefix);

        if (keyword.isEmpty()) {
            return false;
        }

        if (keyword.length() > 170) {
            return false;
        }

        if (prefix.equals(PREFIX_PHONE)) {
            return keyword.matches("^\\+?[0-9]+$");
        } else if (prefix.equals(PREFIX_EMAIL)) {
            return keyword.matches("^[A-Za-z0-9+_.@-]*$");
        } else if (prefix.equals(PREFIX_BOOKING_DATE)) {
            if (!keyword.matches("^\\d{4}-\\d{2}-\\d{2}$")) {
                return false;
            }

            try {
                LocalDate.parse(keyword, DATE_FORMAT);
                return true;
            } catch (DateTimeParseException e) {
                return false;
            }
        }

        return true;
    }

    /**
     * Gets a description of the prefix for error messages.
     *
     * @param prefix The prefix to get a description for
     * @return A human-readable description of the prefix
     */
    private String getPrefixDescription(Prefix prefix) {
        if (prefix.equals(PREFIX_NAME)) {
            return "name";
        } else if (prefix.equals(PREFIX_PHONE)) {
            return "phone number";
        } else if (prefix.equals(PREFIX_EMAIL)) {
            return "email";
        } else if (prefix.equals(PREFIX_ADDRESS)) {
            return "address";
        } else if (prefix.equals(PREFIX_TAG)) {
            return "tag";
        } else if (prefix.equals(PREFIX_MEMO)) {
            return "memo";
        } else if (prefix.equals(PREFIX_BOOKING_DATE)) {
            return "booking date";
        } else if (prefix.equals(PREFIX_BOOKING_PROPERTY)) {
            return "booking property";
        } else {
            return "field";
        }
    }

    /**
     * Gets an error message for invalid keywords.
     * The message is customized based on the prefix type.
     *
     * @param prefix The prefix associated with the invalid keywords
     * @param invalidKeywords The list of invalid keywords
     * @return A formatted error message
     */
    private String getErrorMessage(Prefix prefix, List<String> invalidKeywords) {
        String errorMessage;

        if (prefix.equals(PREFIX_NAME)) {
            errorMessage = "Error: Name values should not exceed 170 characters.";
        } else if (prefix.equals(PREFIX_PHONE)) {
            errorMessage = "Error: Invalid phone format. Phone numbers should contain digits, with an optional "
                    + "'+' at the beginning.";
        } else if (prefix.equals(PREFIX_EMAIL)) {
            errorMessage = "Error: Invalid email format. Email values may only contain alphanumeric characters, '@', "
                    + "and these special characters: + _ . -";
        } else if (prefix.equals(PREFIX_ADDRESS)) {
            errorMessage = "Error: Address values should not exceed 500 characters.";
        } else if (prefix.equals(PREFIX_TAG)) {
            errorMessage = "Error: Tag values should not exceed 170 characters.";
        } else if (prefix.equals(PREFIX_MEMO)) {
            errorMessage = "Error: Memo values should not exceed 500 characters.";
        } else if (prefix.equals(PREFIX_BOOKING_DATE)) {
            errorMessage = "Error: Invalid booking date format. "
                    + "Dates should be in the format yyyy-MM-dd (e.g., 2024-10-15).";
        } else if (prefix.equals(PREFIX_BOOKING_PROPERTY)) {
            errorMessage = "Error: Booking property values should not exceed 170 characters.";
        } else {
            errorMessage = "Error: Invalid keyword format.";
        }

        return errorMessage + " Invalid keyword(s): " + String.join(", ", invalidKeywords) + " "
                + Emoticons.ANGRY;
    }
}
