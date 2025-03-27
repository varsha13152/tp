package seedu.innsync.logic.parser;

import static seedu.innsync.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import seedu.innsync.logic.commands.FindCommand;
import seedu.innsync.logic.commands.FindCommand.SearchType;
import seedu.innsync.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new FindCommand object
 */
public class FindCommandParser implements Parser<FindCommand> {

    // Regex patterns for different field validations (relaxed for partial matching)
    private static final Pattern NAME_VALIDATION_REGEX =
            Pattern.compile("^[a-zA-Z\\s'\\-]+$");
    private static final Pattern PHONE_VALIDATION_REGEX =
            Pattern.compile("^[0-9]+$");
    private static final Pattern EMAIL_VALIDATION_REGEX =
            Pattern.compile("^[a-zA-Z0-9.@_\\-]+$");
    private static final Pattern ADDRESS_VALIDATION_REGEX =
            Pattern.compile("^[a-zA-Z0-9\\s\\-#]+$");
    private static final Pattern TAG_VALIDATION_REGEX =
            Pattern.compile("^[a-zA-Z0-9]+$");
    private static final Pattern BOOKING_VALIDATION_REGEX =
            Pattern.compile("^\\d{4}-\\d{2}-\\d{2}$");
    private static final Pattern BOOKING_DATE_VALIDATION_REGEX =
            Pattern.compile("^\\d{4}-\\d{2}-\\d{2}$");
    private static final Pattern BOOKING_PROPERTY_VALIDATION_REGEX =
            Pattern.compile("^[a-zA-Z0-9\\s\\-]+$");
    private static final Pattern MEMO_VALIDATION_REGEX =
            Pattern.compile("^[a-zA-Z0-9\\s.;:,!?\\-'\"]+$");
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    // Pattern to identify prefix and its keywords
    private static final Pattern FIELD_PATTERN = Pattern.compile("([a-z]{1,2}/)((?:(?!\\s[a-z]{1,2}/).)*)");

    // Pattern to extract content before the first prefix
    private static final Pattern PREFIX_PATTERN = Pattern.compile("^(.*?)\\s*([a-z]{1,2}/.*)$");

    private static final String VALID_FLAGS = "Valid prefixes are: \n name: n/ \n phone: p/ \n email: e/ \n"
            + "address: a/ \n tag: t/ \n memo: m/ \n booking date: bd/ \n booking property: bp/";

    /**
     * Parses the given {@code String} of arguments in the context of the FindCommand
     * and returns a FindCommand object for execution.
     * @throws ParseException if the user input does not conform to the expected format
     * @throws NullPointerException if args is null
     */
    public FindCommand parse(String args) throws ParseException {
        validateArgsNotNull(args);

        String trimmedArgs = args.trim();
        if (trimmedArgs.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        }

        // Legacy support for the old format: find KEYWORD [MORE_KEYWORDS]...
        if (!trimmedArgs.contains("/")) {
            return handleLegacyFormat(trimmedArgs);
        }

        // Check for keywords before first prefix (e.g., "bob a/clementi")
        return handleModernFormat(trimmedArgs);
    }

    /**
     * Validates that the arguments are not null.
     */
    private void validateArgsNotNull(String args) {
        if (args == null) {
            throw new NullPointerException("Arguments string cannot be null");
        }
    }

    /**
     * Handles the legacy format of the find command (e.g., "find bob alice")
     */
    private FindCommand handleLegacyFormat(String args) throws ParseException {
        List<String> keywords = Arrays.asList(args.split("\\s+"));
        validateKeywords(keywords, FindCommand.SearchType.NAME);
        Map<SearchType, List<String>> searchCriteria = new HashMap<>();
        searchCriteria.put(SearchType.NAME, keywords);
        return new FindCommand(searchCriteria);
    }

    /**
     * Handles the modern format of the find command with prefixes.
     */
    private FindCommand handleModernFormat(String trimmedArgs) throws ParseException {
        Matcher prefixMatcher = PREFIX_PATTERN.matcher(trimmedArgs);
        if (prefixMatcher.matches()) {
            String beforePrefix = prefixMatcher.group(1).trim();
            String afterPrefix = prefixMatcher.group(2);

            // If there are keywords before the first prefix, treat them as name search
            if (!beforePrefix.isEmpty()) {
                return handleMixedFormat(beforePrefix, afterPrefix);
            }
        }

        // Parse multi-field format without leading unprefixed keywords
        return parseMultiFieldSearch(trimmedArgs);
    }

    /**
     * Handles mixed format where unprefixed keywords appear before prefixed ones.
     */
    private FindCommand handleMixedFormat(String beforePrefix, String afterPrefix) throws ParseException {
        // Process the name keywords
        List<String> nameKeywords = Arrays.asList(beforePrefix.split("\\s+"));
        validateKeywords(nameKeywords, FindCommand.SearchType.NAME);

        // Process the rest with prefixes
        Map<SearchType, List<String>> searchCriteria = parseFieldsWithPrefixes(afterPrefix);

        // Add name keywords to criteria
        searchCriteria.put(SearchType.NAME, nameKeywords);

        return new FindCommand(searchCriteria);
    }

    /**
     * Parses fields with explicit prefixes (e.g., "a/clementi p/12345")
     */
    private Map<SearchType, List<String>> parseFieldsWithPrefixes(String args) throws ParseException {
        Map<SearchType, List<String>> searchCriteria = new HashMap<>();
        Matcher matcher = FIELD_PATTERN.matcher(args + " ");

        if (!matcher.find()) {
            throw new ParseException(
                    "At least one valid search keyword must be provided. "
                            + VALID_FLAGS);
        }

        matcher.reset();
        processAllPrefixMatches(matcher, searchCriteria);

        return searchCriteria;
    }

    /**
     * Processes all prefix matches found by the matcher.
     */
    private void processAllPrefixMatches(Matcher matcher, Map<SearchType, List<String>> searchCriteria)
            throws ParseException {
        while (matcher.find()) {
            String prefix = validatePrefix(matcher.group(1));
            String keywordsString = validateKeywordsString(prefix, matcher.group(2));

            SearchType searchType = getSearchTypeFromPrefix(prefix);
            checkForDuplicateField(searchCriteria, prefix, searchType);

            List<String> keywords = processKeywords(prefix, keywordsString);
            validateKeywords(keywords, searchType);

            // Add to search criteria map
            searchCriteria.put(searchType, keywords);
        }

        if (searchCriteria.isEmpty()) {
            throw new ParseException("No valid search field found. "
                    + "Please provide at least one search field with a valid keyword.");
        }
    }

    /**
     * Validates the prefix and returns it if valid.
     */
    private String validatePrefix(String prefix) throws ParseException {
        if (prefix == null) {
            throw new ParseException("Invalid search format. Please specify search fields. "
                    + VALID_FLAGS);
        }

        // Validate prefix before proceeding
        if (!isValidPrefix(prefix)) {
            throw new ParseException("Invalid search field: '" + prefix + "'. " + VALID_FLAGS);
        }

        return prefix;
    }

    /**
     * Validates the keywords string for a given prefix.
     */
    private String validateKeywordsString(String prefix, String keywordsString) throws ParseException {
        if (keywordsString == null) {
            throw new ParseException("Search terms missing for search flag: " + prefix);
        }

        keywordsString = keywordsString.trim();
        if (keywordsString.isEmpty()) {
            throw new ParseException("Search term cannot be empty for " + getFieldNameFromPrefix(prefix)
                    + ". Please provide at least one search term after " + prefix);
        }

        return keywordsString;
    }

    /**
     * Checks for duplicate fields in the search criteria.
     */
    private void checkForDuplicateField(Map<SearchType, List<String>> searchCriteria,
                                        String prefix, SearchType searchType) throws ParseException {
        if (searchCriteria.containsKey(searchType)) {
            throw new ParseException("Duplicate search field: " + prefix
                    + ". Each field can only be specified once.");
        }
    }

    /**
     * Processes the keywords string into a list of keywords.
     */
    private List<String> processKeywords(String prefix, String keywordsString) throws ParseException {
        List<String> keywords = Arrays.asList(keywordsString.split("\\s+"));

        for (String keyword : keywords) {
            if (keyword.isEmpty()) {
                throw new ParseException("Invalid keyword format. Multiple spaces between keywords "
                        + "are not allowed for " + getFieldNameFromPrefix(prefix));
            }
        }

        return keywords;
    }

    /**
     * Parses search for multiple fields
     *
     * @param args the command arguments to parse
     * @return a FindCommand object with the parsed search criteria
     * @throws ParseException if the input does not conform to the expected format or contains invalid keywords
     * @throws IllegalArgumentException if args is null or unsupported search type is encountered
     */
    private FindCommand parseMultiFieldSearch(String args) throws ParseException {
        assert args != null : "Arguments string cannot be null";
        assert !args.trim().isEmpty() : "Arguments string cannot be empty";

        Map<SearchType, List<String>> searchCriteria = parseFieldsWithPrefixes(args);

        // Final check to ensure we have search criteria
        if (searchCriteria.isEmpty()) {
            throw new ParseException("No valid search field found. "
                    + "Please provide at least one search field with a valid keyword.");
        }

        return new FindCommand(searchCriteria);
    }

    /**
     * Checks if a prefix is valid
     */
    private boolean isValidPrefix(String prefix) {
        return prefix.equals("n/")
                || prefix.equals("p/")
                || prefix.equals("e/")
                || prefix.equals("a/")
                || prefix.equals("t/")
                || prefix.equals("bd/")
                || prefix.equals("bp/")
                || prefix.equals("m/");
    }

    /**
     * Gets a human-readable field name from a prefix for error messages
     */
    private String getFieldNameFromPrefix(String prefix) {
        switch (prefix) {
        case "n/":
            return "name";
        case "p/":
            return "phone";
        case "e/":
            return "email";
        case "a/":
            return "address";
        case "t/":
            return "tag";
        case "bd/":
            return "booking date";
        case "bp/":
            return "booking property";
        case "m/":
            return "memo";
        default:
            return "field";
        }
    }

    /**
     * Gets the search type from the prefix
     *
     * @param prefix the prefix string to convert to a SearchType
     * @return the corresponding SearchType enum value
     * @throws IllegalArgumentException if prefix is null or empty
     */
    private SearchType getSearchTypeFromPrefix(String prefix) {
        if (prefix == null) {
            throw new IllegalArgumentException("Search field cannot be null");
        }
        if (prefix.isEmpty()) {
            throw new IllegalArgumentException("Search field cannot be empty");
        }

        // Assert prefix is not null or empty
        assert prefix != null && !prefix.isEmpty() : "Prefix must be non-null and non-empty";
        // Assert prefix is valid (already checked by isValidPrefix)
        assert isValidPrefix(prefix) : "Prefix must be valid";

        switch (prefix) {
        case "n/":
            return SearchType.NAME;
        case "p/":
            return SearchType.PHONE;
        case "e/":
            return SearchType.EMAIL;
        case "a/":
            return SearchType.ADDRESS;
        case "t/":
            return SearchType.TAG;
        case "bd/":
            return SearchType.BOOKING_DATE;
        case "bp/":
            return SearchType.BOOKING_PROPERTY;
        case "m/":
            return SearchType.MEMO;
        default:
            // This should never happen because we validate with isValidPrefix first
            throw new IllegalArgumentException("Unsupported search field: " + prefix);
        }
    }

    /**
     * Validates keywords based on the search type
     *
     * @param keywords list of keywords to validate
     * @param searchType the type of search being performed
     * @throws ParseException if any keywords are invalid for the specified search type
     * @throws IllegalArgumentException if keywords list or searchType is null
     */
    private void validateKeywords(List<String> keywords, FindCommand.SearchType searchType) throws ParseException {
        checkKeywordsNotNullOrEmpty(keywords, searchType);

        List<String> invalidKeywords = findInvalidKeywords(keywords, searchType);

        if (!invalidKeywords.isEmpty()) {
            throw createValidationException(searchType, invalidKeywords);
        }
    }

    /**
     * Checks that keywords are not null or empty.
     */
    private void checkKeywordsNotNullOrEmpty(List<String> keywords, SearchType searchType) throws ParseException {
        // Check for null parameters
        if (keywords == null) {
            throw new IllegalArgumentException("Search term cannot be null");
        }
        if (searchType == null) {
            throw new IllegalArgumentException("Search field cannot be null");
        }

        // Ensure keywords list is not empty
        if (keywords.isEmpty()) {
            throw new ParseException("At least one search term must be provided for " + searchType + " search");
        }

        // Assert parameters are valid
        assert keywords != null : "Search term list must be non-null";
        assert searchType != null : "Search type must be non-null";
        assert !keywords.isEmpty() : "Keywords list must not be empty";
    }

    /**
     * Finds invalid keywords in the list.
     */
    private List<String> findInvalidKeywords(List<String> keywords, SearchType searchType) {
        List<String> invalidKeywords = new ArrayList<>();

        for (String keyword : keywords) {
            // Check for null keywords
            if (keyword == null) {
                throw new IllegalArgumentException("Search term cannot be null");
            }

            if (!isValidKeyword(keyword, searchType)) {
                invalidKeywords.add(keyword);
            }
        }

        return invalidKeywords;
    }

    /**
     * Checks if a keyword is valid for the given search type
     *
     * @param keyword the keyword to validate
     * @param searchType the type of search being performed
     * @return true if the keyword is valid for the search type, false otherwise
     * @throws IllegalArgumentException if keyword is null or searchType is null
     */
    private boolean isValidKeyword(String keyword, FindCommand.SearchType searchType) {
        checkKeywordAndSearchTypeNotNull(keyword, searchType);

        // Check for empty keyword (which would match everything)
        if (keyword.isEmpty()) {
            return false;
        }

        return validateKeywordFormat(keyword, searchType);
    }

    /**
     * Checks that keyword and search type are not null.
     */
    private void checkKeywordAndSearchTypeNotNull(String keyword, SearchType searchType) {
        // Check for null parameters
        if (keyword == null) {
            throw new IllegalArgumentException("Search term cannot be null");
        }
        if (searchType == null) {
            throw new IllegalArgumentException("Search type cannot be null");
        }

        // Assert parameters are valid
        assert keyword != null : "Search term must be non-null";
        assert searchType != null : "Search type must be non-null";
    }

    /**
     * Validates the keyword format based on the search type.
     * @param keyword the keyword to validate
     * @param searchType the type of search being performed
     * @return true if the keyword is valid for the search type, false otherwise
     * @throws IllegalArgumentException if searchType is not supported
     */
    private boolean validateKeywordFormat(String keyword, SearchType searchType) {
        // Assert parameters have been checked by caller
        assert keyword != null : "Keyword must be non-null";
        assert searchType != null : "Search type must be non-null";
        assert !keyword.isEmpty() : "Keyword must be non-empty";

        try {
            switch (searchType) {
            case NAME:
                return NAME_VALIDATION_REGEX.matcher(keyword).matches();
            case PHONE:
                return PHONE_VALIDATION_REGEX.matcher(keyword).matches();
            case EMAIL:
                return EMAIL_VALIDATION_REGEX.matcher(keyword).matches();
            case ADDRESS:
                return ADDRESS_VALIDATION_REGEX.matcher(keyword).matches();
            case TAG:
                return TAG_VALIDATION_REGEX.matcher(keyword).matches();
            case BOOKING_DATE:
                return BOOKING_DATE_VALIDATION_REGEX.matcher(keyword).matches() && validateDateFormat(keyword);
            case BOOKING_PROPERTY:
                return BOOKING_PROPERTY_VALIDATION_REGEX.matcher(keyword).matches();
            case MEMO:
                return MEMO_VALIDATION_REGEX.matcher(keyword).matches();
            default:
                throw new IllegalArgumentException("Unsupported search type: " + searchType);
            }
        } catch (Exception e) {
            // Log the exception for debugging
            System.err.println("Error validating search term: " + e.getMessage());
            return false;
        }
    }

    /**
     * Additional validation for date format to ensure it's a valid date
     * @param dateString the date string to validate in yyyy-MM-dd format
     * @return true if the date is valid, false otherwise
     */
    private boolean validateDateFormat(String dateString) {
        try {
            // Attempt to parse the date to validate it
            LocalDate.parse(dateString, DATE_FORMAT);
            return true;
        } catch (DateTimeParseException e) {
            // Invalid date format
            System.err.println("Invalid date format: " + e.getMessage());
            return false;
        }
    }

    /**
     * Creates a specific ParseException based on the search type
     */
    private ParseException createValidationException(FindCommand.SearchType searchType, List<String> invalidKeywords) {
        String errorMessage = getErrorMessageForSearchType(searchType);
        return new ParseException(errorMessage + " Invalid search terms(s): " + String.join(", ", invalidKeywords));
    }

    /**
     * Gets the appropriate error message for a given search type.
     */
    private String getErrorMessageForSearchType(SearchType searchType) {
        switch (searchType) {
        case NAME:
            return "Invalid name format. Names should only contain alphabets, spaces, apostrophes,"
                    + " and/or hyphens.";
        case PHONE:
            return "Invalid phone format. Phone numbers should only contain digits.";
        case EMAIL:
            return "Invalid email format. Emails should only contain alphanumeric characters, dots, '@',"
                    + " underscores, and hyphens.";
        case ADDRESS:
            return "Invalid address format. Addresses should only contain alphanumeric characters, spaces,"
                    + " hyphens, and hashes.";
        case TAG:
            return "Invalid tag format. Tags should only contain alphanumeric characters.";
        case BOOKING_DATE:
            return "Invalid booking date format. Dates should be in the format yyyy-MM-dd "
                    + "(e.g., 2024-10-15).";
        case BOOKING_PROPERTY:
            return "Invalid booking property format. Property names should only contain alphanumeric characters,"
                    + " spaces, and hyphens.";
        case MEMO:
            return "Invalid memo format. Memos should only contain alphanumeric characters, spaces, punctuation,"
                    + " and basic symbols.";
        default:
            return String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE);
        }
    }
}
