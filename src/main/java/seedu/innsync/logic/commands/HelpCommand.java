package seedu.innsync.logic.commands;

import seedu.innsync.logic.Emoticons;
import seedu.innsync.model.Model;

/**
 * Format full help instructions for every command for display.
 */
public class HelpCommand extends Command {

    public static final String COMMAND_WORD = "help";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Shows program usage instructions.\n"
            + "Example: " + COMMAND_WORD;

    public static final String SHOWING_HELP_MESSAGE = "Opened help window! " + Emoticons.PROUD;

    @Override
    public CommandResult execute(Model model) {
        return new CommandResult(SHOWING_HELP_MESSAGE, true, false, null);
    }

    @Override
    public boolean requireConfirmation() {
        return false;
    }
}
