package seedu.innsync.logic.commands;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static seedu.innsync.logic.commands.CommandTestUtil.assertCommandSuccess;

import org.junit.jupiter.api.Test;

import seedu.innsync.model.Model;
import seedu.innsync.model.ModelManager;

public class CancelConfirmCommandTest {
    private Model model = new ModelManager();
    private Model expectedModel = new ModelManager();

    @Test
    public void execute_confirm_success() {
        CommandResult expectedCommandResult = new CommandResult(CancelConfirmCommand.MESSAGE_CANCEL);
        assertCommandSuccess(new CancelConfirmCommand(), model, expectedCommandResult, expectedModel);
    }

    @Test
    public void hasConfirmationTest_returnsFalse() {
        CancelConfirmCommand cancelConfirmCommand = new CancelConfirmCommand();
        assertFalse(cancelConfirmCommand.requireConfirmation());
    }
}
