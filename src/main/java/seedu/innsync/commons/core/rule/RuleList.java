package seedu.innsync.commons.core.rule;

import java.util.List;

/**
 * Represents a list of validation rules.
 * Guarantees: immutable; rules are valid as declared in {@link #isValidRule(String, String)}
 */
public class RuleList {

    private final List<Rule<?>> rules;

    /**
     * Constructs a {@code RuleList}.
     *
     * @param rules A list of valid rules.
     */
    public RuleList(List<Rule<?>> rules) {
        this.rules = rules;
    }

    /**
     * Returns the list of rules.
     */
    public List<Rule<?>> getRules() {
        return rules;
    }

    /**
     * Checks if the given object passes all validation rules in the list.
     *
     * @param test The object to be validated.
     * @throws IllegalArgumentException if the object does not pass any of the validation rules.
     */
    public void checkAllRules(Object test) {
        for (Rule<?> rule : rules) {
            // Warning is suppressed as test is confirmed to be of type Object
            @SuppressWarnings("unchecked")
            Rule<Object> castedRule = (Rule<Object>) rule;
            castedRule.checkValidation(test);
        }
    }
}
