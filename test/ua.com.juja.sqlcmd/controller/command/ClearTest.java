package ua.com.juja.sqlcmd.controller.command;

import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import ua.com.juja.sqlcmd.model.DataSet;
import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.view.View;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;


public class ClearTest {

    private View view;
    private DatabaseManager manager;
    private Command command;

    @Before
    public void setup() {
        manager = mock(DatabaseManager.class);
        view = mock(View.class);
        command = new Clear(manager, view);
    }

    @Test
    public void testClearTable() {
        // given

        // when
        command.process("clear|users");

        // then
        verify(manager).clear("users");
        verify(view).write("Таблица users была успешно очищена");
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

    @Test
    public void testValidationErrorParametersIsLessThan2() {
        // when
        try {
            command.canProcess("clear");
        } catch (IllegalArgumentException e) {
            // then
            assertEquals("Формат команды 'clear|tableName', а получено clear", e.getMessage());
        }
    }

    @Test
    public void testValidationErrorParametersIsMoreThan2() {
        // when
        try {
            command.canProcess("clear|users|qwer");
        } catch (IllegalArgumentException e) {
            // then
            assertEquals("Формат команды 'clear|tableName', а получено clear|users|qwer", e.getMessage());
        }
    }
}
