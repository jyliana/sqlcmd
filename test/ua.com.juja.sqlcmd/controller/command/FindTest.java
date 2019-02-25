package ua.com.juja.sqlcmd.controller.command;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import ua.com.juja.sqlcmd.model.DataSet;
import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.view.View;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class FindTest {
    private View view;
    private DatabaseManager manager;

    @Before
    public void setup() {
        manager = Mockito.mock(DatabaseManager.class);
        view = Mockito.mock(View.class);
    }

    @Test
    public void testPrintTableData() {
        // given
        Command command = new Find(manager, view);
        Mockito.when(manager.getTableColumns("users"))
                .thenReturn(new String[]{"id", "name", "password"});

        DataSet user1 = new DataSet();
        user1.put("id", 1);
        user1.put("name", "Stiven");
        user1.put("password", "*****");

        DataSet user2 = new DataSet();
        user2.put("id", 2);
        user2.put("name", "Eva");
        user2.put("password", "+++++");

        DataSet[] data = new DataSet[]{user1, user2};
        Mockito.when(manager.getTableData("users"))
                .thenReturn(data);

        // when
        command.process("find|users");

        // then
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        Mockito.verify(view, Mockito.atLeastOnce()).write(captor.capture());
        assertEquals("[--------------------------,  " +
                "| id | name | password | , " +
                "--------------------------,  " +
                "| 1 | Stiven | ***** | ,  " +
                "| 2 | Eva | +++++ | , " +
                "--------------------------]", captor.getAllValues().toString());
    }

    @Test
    public void testCanProcessFindWithParametersString() {
        //given
        Command command = new Find(manager, view);

        //when
        boolean canProcess = command.canProcess("find|users");

        //then
        assertTrue(canProcess);
    }

    @Test
    public void testCantProcessFindWithoutParamateresString() {
        // given
        Command command = new Find(manager, view);

        // when
        boolean canProcess = command.canProcess("find");

        // then
        assertFalse(canProcess);
    }

    @Test
    public void testCantProcessFindQweString() {
        // given
        Command command = new Find(manager, view);

        // when
        boolean canProcess = command.canProcess("qwe|users");

        // then
        assertFalse(canProcess);
    }

    @Test
    public void testPrintEmptyTableData() {
        // given
        Command command = new Find(manager, view);
        Mockito.when(manager.getTableColumns("users"))
                .thenReturn(new String[]{"id", "name", "password"});

        DataSet[] data = new DataSet[0];
        Mockito.when(manager.getTableData("users"))
                .thenReturn(data);

        // when
        command.process("find|users");

        // then
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        Mockito.verify(view, Mockito.atLeastOnce()).write(captor.capture());
        assertEquals("[--------------------------,  " +
                "| id | name | password | , " +
                "--------------------------, " +
                "--------------------------]", captor.getAllValues().toString());
    }
}
