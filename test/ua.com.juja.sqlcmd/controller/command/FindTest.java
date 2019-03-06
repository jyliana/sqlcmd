package ua.com.juja.sqlcmd.controller.command;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import ua.com.juja.sqlcmd.model.DataSet;
import ua.com.juja.sqlcmd.model.DataSetImpl;
import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.view.View;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class FindTest {
    private View view;
    private DatabaseManager manager;
    private Command command;

    @Before
    public void setup() {
        manager = Mockito.mock(DatabaseManager.class);
        view = Mockito.mock(View.class);
        command = new Find(manager, view);
    }

    @Test
    public void testPrintTableData() {
        // given
        setupTableColumns("users", "id", "name", "password");

        DataSet user1 = new DataSetImpl();
        user1.put("id", 1);
        user1.put("name", "Stiven");
        user1.put("password", "*****");

        DataSet user2 = new DataSetImpl();
        user2.put("id", 2);
        user2.put("name", "Eva");
        user2.put("password", "+++++");

        Mockito.when(manager.getTableData("users"))
                .thenReturn(Arrays.asList(user1, user2));

        // when
        command.process("find|users");

        // then
        shouldPrint("[--------------------------,  " +
                "| id | name | password | , " +
                "--------------------------,  " +
                "| 1 | Stiven | ***** | ,  " +
                "| 2 | Eva | +++++ | , " +
                "--------------------------]");
    }

    private void setupTableColumns(String tableName, String... columns) {
        Mockito.when(manager.getTableColumns(tableName))
                .thenReturn(new LinkedHashSet<>(Arrays.asList(columns)));
    }

    @Test
    public void testPrintTableDataWithOneColumn() {
        // given
        setupTableColumns("users", "id", "name", "password");

        DataSet user1 = new DataSetImpl();
        user1.put("id", 1);

        DataSet user2 = new DataSetImpl();
        user2.put("id", 2);

        Mockito.when(manager.getTableData("users")).thenReturn(Arrays.asList(user1, user2));

        // when
        command.process("find|users");

        // then
        shouldPrint("[--------------------------,  " +
                "| id | name | password | , " +
                "--------------------------,  " +
                "| 1 | ,  " +
                "| 2 | , " +
                "--------------------------]");
    }

    private void shouldPrint(String expected) {
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        Mockito.verify(view, Mockito.atLeastOnce()).write(captor.capture());
        assertEquals(expected, captor.getAllValues().toString());
    }

    @Test
    public void testCanProcessFindWithParametersString() {
        //given

        //when
        boolean canProcess = command.canProcess("find|users");

        //then
        assertTrue(canProcess);
    }

    @Test
    public void testCantProcessFindWithoutParamateresString() {
        // given

        // when
        boolean canProcess = command.canProcess("find");

        // then
        assertFalse(canProcess);
    }

    @Test
    public void testCantProcessFindQweString() {
        // given

        // when
        boolean canProcess = command.canProcess("qwe|users");

        // then
        assertFalse(canProcess);
    }

    @Test
    public void testPrintEmptyTableData() {
        // given
        setupTableColumns("users", "id", "name", "password");
        Mockito.when(manager.getTableData("users"))
                .thenReturn(new ArrayList<DataSet>());

        // when
        command.process("find|users");

        // then
        shouldPrint("[--------------------------,  " +
                "| id | name | password | , " +
                "--------------------------, " +
                "--------------------------]");
    }
}
