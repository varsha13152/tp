package seedu.innsync.commons.util;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.function.Function;

import javafx.scene.image.Image;
import javafx.util.Pair;
import seedu.innsync.MainApp;

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
    public static void checkAllValidationRules(String test,
            List<Pair<Function<String, Boolean>, String>> validationRules) {
        requireNonNull(test);
        for (Pair<Function<String, Boolean>, String> rule : validationRules) {
            if (!rule.getKey().apply(test)) {
                throw new IllegalArgumentException(rule.getValue());
            }
        }
    }
}
