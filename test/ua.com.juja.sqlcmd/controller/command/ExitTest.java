package ua.com.juja.sqlcmd.controller.command;

import org.junit.Test;

import static junit.framework.TestCase.*;
import static org.junit.Assert.assertFalse;

public class ExitTest {

    private FakeView view = new FakeView();

    @Test
    public void testCanProcessExitString() {
        //given
        Command command = new Exit(view);

        //when
        boolean canProcess = command.canProcess("exit");

        //then
        assertTrue(canProcess);
    }

    @Test
    public void testCantProcessQwerString() {
        // given
        Command command = new Exit(view);

        // when
        boolean canProcess = command.canProcess("qwer");

        // then
        assertFalse(canProcess);
    }

    @Test
    public void testProcessExitCommand_throwsExitException() {
        // given
        Command command = new Exit(view);

        // when
        try {
            command.process("exit");
            fail("Expected ExitException");
        } catch (ExitException e) {
            //do nothing
        }

        // then
        assertEquals("До скорой встречи!\n", view.getContent());
        // throws ExitException
    }
}
