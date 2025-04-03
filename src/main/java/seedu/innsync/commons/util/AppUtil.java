package seedu.innsync.commons.util;

import static java.util.Objects.requireNonNull;

import javafx.scene.image.Image;
import seedu.innsync.MainApp;
import seedu.innsync.commons.core.rule.RuleList;

/**
 * A container for App specific utility functions
 */
public class AppUtil {

    /**
     * Gets an {@code Image} from the specified path.
     */
    public static Image getImage(String imagePath) {
        requireNonNull(imagePath);
        return new Image(MainApp.class.getResourceAsStream(imagePath));
    }

    /**
     * Checks that {@code condition} is true. Used for validating arguments to methods.
     *
     * @throws IllegalArgumentException if {@code condition} is false.
     */
    public static void checkArgument(Boolean condition) {
        if (!condition) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Checks that {@code condition} is true. Used for validating arguments to methods.
     *
     * @throws IllegalArgumentException with {@code errorMessage} if {@code condition} is false.
     */
    public static void checkArgument(Boolean condition, String errorMessage) {
        if (!condition) {
            throw new IllegalArgumentException(errorMessage);
        }
    }

    /**
     * Checks that {@code test} passes all validation rules in {@code validationRules}.
     *
     * @param test
     * @param validationRules
     * @throws IllegalArgumentException if any of the validation rules fail.
     */
    public static void checkAllRules(String test, RuleList validationRules) {
        requireNonNull(test);
        requireNonNull(validationRules);
        validationRules.checkAllRules(test);
    }

    /**
     * Checks that {@code test} passes all validation rules in {@code validationRules}.
     *
     * @param test
     * @param validationRules
     * @throws IllegalArgumentException if any of the validation rules fail.
     */
    public static void checkAllRules(Object test, RuleList validationRules) {
        requireNonNull(test);
        requireNonNull(validationRules);
        validationRules.checkAllRules(test);
    }
}
