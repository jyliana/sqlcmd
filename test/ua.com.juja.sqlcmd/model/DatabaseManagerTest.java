package ua.com.juja.sqlcmd.model;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static junit.framework.TestCase.assertEquals;

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
        String[] tablesNames = manager.getTablesNames();
        assertEquals("[news, users]", Arrays.toString(tablesNames));
    }

    @Test
    public void testGetTableData() {
        //given
        manager.clear("users");

        //when
        DataSet input = new DataSet();
        input.put("name", "Stiven");
        input.put("password", "Pupkin");
        input.put("id", 1);
        manager.create("users", input);

        //then
        DataSet[] users = manager.getTableData("users");
        assertEquals(1, users.length);

        DataSet user = users[0];
        assertEquals("[name, password, id]", Arrays.toString(user.getNames()));
        assertEquals("[Stiven, Pupkin, 1]", Arrays.toString(user.getValues()));
    }

    @Test
    public void testUpdateTableData() {
        //given
        manager.clear("users");

        DataSet input = new DataSet();
        input.put("name", "Stiven");
        input.put("password", "Pupkin");
        input.put("id", 1);
        manager.create("users", input);

        //when
        DataSet newValue = new DataSet();
        newValue.put("password", "pass2");
        newValue.put("name", "Pup");
        manager.update("users", 1, newValue);

        //then
        DataSet[] users = manager.getTableData("users");
        assertEquals(1, users.length);

        DataSet user = users[0];
        assertEquals("[name, password, id]", Arrays.toString(user.getNames()));
        assertEquals("[Pup, pass2, 1]", Arrays.toString(user.getValues()));
    }
}
