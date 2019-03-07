package ua.com.juja.sqlcmd.model;

import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Set;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

public abstract class DatabaseManagerTest {

    protected DatabaseManager manager;

    public abstract DatabaseManager getDatabaseManager();

    @Before
    public void setup() {
        manager = getDatabaseManager();
        manager.connect("sqlcmd", "postgres", "123456");
    }

    @Test
    public void testGetAllTablesNames() {
        // given
        manager.getTableData("news");
        manager.getTableData("users");
        manager.getTableData("test");

        // when
        Set<String> tablesNames = manager.getTablesNames();

        // then
        assertEquals("[news, users, test]", tablesNames.toString());
    }

    @Test
    public void testGetTableData() {
        //given
        manager.clear("users");

        //when
        DataSet input = new DataSetImpl();
        input.put("name", "Stiven");
        input.put("password", "Pupkin");
        input.put("id", 1);
        manager.create("users", input);

        //then
        List<DataSet> users = manager.getTableData("users");
        assertEquals(1, users.size());

        DataSet user = users.get(0);
        assertEquals("[name, password, id]", user.getNames().toString());
        assertEquals("[Stiven, Pupkin, 1]", user.getValues().toString());
    }

    @Test
    public void testUpdateTableData() {
        //given
        manager.clear("users");

        DataSet input = new DataSetImpl();
        input.put("name", "Stiven");
        input.put("password", "Pupkin");
        input.put("id", 1);
        manager.create("users", input);

        //when
        DataSet newValue = new DataSetImpl();
        newValue.put("password", "pass2");
        newValue.put("name", "Pup");
        manager.update("users", 1, newValue);

        //then
        List<DataSet> users = manager.getTableData("users");
        assertEquals(1, users.size());

        DataSet user = users.get(0);
        assertEquals("[name, password, id]", user.getNames().toString());
        assertEquals("[Pup, pass2, 1]", user.getValues().toString());
    }

    @Test
    public void testGetColumnNames() {
        //given
        manager.clear("users");

        //when
        Set<String> columnNames = manager.getTableColumns("users");

        //then
        assertEquals("[name, password, id]", columnNames.toString());
    }

    @Test
    public void testIsConnected() {
        //given
        //when
        //then
        assertTrue(manager.isConnected());
    }
}
