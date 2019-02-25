package ua.com.juja.sqlcmd.controller.command;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.view.View;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;

public class ClearTest {
    private View view;
    private DatabaseManager manager;
    private Command command;

    @Before
    public void setup() {
        manager = Mockito.mock(DatabaseManager.class);
        view = Mockito.mock(View.class);
        command = new Clear(manager, view);
    }

    @Test
    public void testClearTable() {
        // given

        // when
        command.process("clear|users");

        // then
        Mockito.verify(manager).clear("users");
        Mockito.verify(view).write("Таблица users была успешно очищена");
    }

    @Test
    public void testCanProcessClearWithParametersString() {
        //given

        //when
        boolean canProcess = command.canProcess("clear|users");

        //then
        assertTrue(canProcess);
    }

    @Test
    public void testCantProcessClearWithoutParamateresString() {
        // given

        // when
        boolean canProcess = command.canProcess("clear");

        // then
        assertFalse(canProcess);
    }

    @Test
    public void testCantProcessClearQweString() {
        // given

        // when
        boolean canProcess = command.canProcess("qwe|users");

        // then
        assertFalse(canProcess);
    }
}
