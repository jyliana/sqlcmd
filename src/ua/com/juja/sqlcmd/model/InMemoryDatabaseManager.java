package ua.com.juja.sqlcmd.model;

import java.util.*;

public class InMemoryDatabaseManager implements DatabaseManager {
    public static final String TABLE_NAME = "news, users, test";//TODO
    private LinkedList<DataSet> data = new LinkedList<>();

    @Override
    public List<DataSet> getTableData(String tableName) {
        validateTable(tableName);
        return data;
    }

    private void validateTable(String tableName) {
        if (!"users".equals(tableName)) {
            throw new UnsupportedOperationException("Only for 'users' table, but you try to work with: " + tableName);
        }
    }

    @Override
    public Set<String> getTablesNames() {
        return new LinkedHashSet<>(Arrays.asList(TABLE_NAME));
    }

    @Override
    public void connect(String database, String userName, String password) {

    }

    @Override
    public void clear(String tableName) {
        validateTable(tableName);
        data.clear();
    }

    @Override
    public void create(String tableName, DataSet input) {
        validateTable(tableName);
        data.add(input);
    }

    @Override
    public void update(String tableName, int id, DataSet newValue) {
        for (DataSet dataSet : data) {
            if (dataSet.get("id").equals(id)) {
                dataSet.updateFrom(newValue);
            }
        }
    }

    @Override
    public Set<String> getTableColumns(String tableName) {
        return new LinkedHashSet<>(Arrays.asList("name", "password", "id"));
    }

    @Override
    public boolean isConnected() {
        return true;
    }
}
