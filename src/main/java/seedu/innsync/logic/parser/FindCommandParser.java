package seedu.innsync.logic.parser;

import static seedu.innsync.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import seedu.innsync.logic.commands.FindCommand;
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

    /**
     * Parses the given {@code String} of arguments in the context of the FindCommand
     * and returns a FindCommand object for execution.
     * @throws ParseException if the user input does not conform to the expected format
     */
    public FindCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();
        if (trimmedArgs.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        }

        if (trimmedArgs.startsWith("n/")) {
            return parseFieldSearch(trimmedArgs, FindCommand.SearchType.NAME);
        } else if (trimmedArgs.startsWith("p/")) {
            return parseFieldSearch(trimmedArgs, FindCommand.SearchType.PHONE);
        } else if (trimmedArgs.startsWith("e/")) {
            return parseFieldSearch(trimmedArgs, FindCommand.SearchType.EMAIL);
        } else if (trimmedArgs.startsWith("a/")) {
            return parseFieldSearch(trimmedArgs, FindCommand.SearchType.ADDRESS);
        } else if (trimmedArgs.startsWith("t/")) {
            return parseFieldSearch(trimmedArgs, FindCommand.SearchType.TAG);
        }

        List<String> keywords = Arrays.asList(trimmedArgs.split("\\s+"));
        validateKeywords(keywords, FindCommand.SearchType.NAME);
        return new FindCommand(keywords, FindCommand.SearchType.NAME);
    }

    /**
     * Parses search for a specific field
     */
    private FindCommand parseFieldSearch(String args, FindCommand.SearchType searchType) throws ParseException {
        String keywordsPart = args.substring(2).trim();

        if (keywordsPart.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        }
        List<String> keywords = Arrays.asList(keywordsPart.split("\\s+"));

        validateKeywords(keywords, searchType);
        return new FindCommand(keywords, searchType);
    }

    /**
     * Validates keywords based on the search type
     */
    private void validateKeywords(List<String> keywords, FindCommand.SearchType searchType) throws ParseException {
        List<String> invalidKeywords = new ArrayList<>();

        for (String keyword : keywords) {
            if (!isValidKeyword(keyword, searchType)) {
                invalidKeywords.add(keyword);
            }
        }

        if (!invalidKeywords.isEmpty()) {
            throw createValidationException(searchType, invalidKeywords);
        }
    }

    /**
     * Checks if a keyword is valid for the given search type
     */
    private boolean isValidKeyword(String keyword, FindCommand.SearchType searchType) {
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
        default:
            return false;
        }
    }

    /**
     * Creates a specific ParseException based on the search type
     */
    private ParseException createValidationException(FindCommand.SearchType searchType, List<String> invalidKeywords) {
        String errorMessage;
        switch (searchType) {
        case NAME:
            errorMessage = "Invalid name format. Names should only contain alphabets, spaces, apostrophes,"
                    + " and/or hyphens.";
            break;
        case PHONE:
            errorMessage = "Invalid phone format. Phone should only contain digits.";

            break;
        case EMAIL:
            errorMessage = "Invalid email format. Email should only contain alphanumeric characters, dots, '@',"
                    + " underscores, and hyphens.";
            break;
        case ADDRESS:
            errorMessage = "Invalid address format. Address should only contain alphanumeric characters, spaces,"
                    + " hyphens, and hashes.";
            break;
        case TAG:
            errorMessage = "Invalid tag format. Tags should only contain alphanumeric characters.";
            break;
        default:
            errorMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE);
        }

        return new ParseException(errorMessage + " Invalid keywords: " + String.join(", ", invalidKeywords));
    }
}
