package seedu.innsync.logic.commands;

import static seedu.innsync.logic.commands.CommandTestUtil.assertCommandSuccess;

import org.junit.jupiter.api.Test;

import seedu.innsync.model.Model;
import seedu.innsync.model.ModelManager;

public class ConfirmCommandTest {
    private Model model = new ModelManager();
    private Model expectedModel = new ModelManager();

    @Test
    public void execute_confirm_success() {
        CommandResult expectedCommandResult = new CommandResult(ConfirmCommand.MESSAGE_CONFIRMATION);
        assertCommandSuccess(new ConfirmCommand(), model, expectedCommandResult, expectedModel);
    }
}
