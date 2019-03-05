package ua.com.juja.sqlcmd.model;

import java.util.Set;

public interface DatabaseManager {
    DataSet[] getTableData(String tableName);

    Set<String> getTablesNames();

    void connect(String database, String userName, String password);

    void clear(String tableName);

    void create(String tableName, DataSet input);

    void update(String tableName, int id, DataSet newValue);

    Set <String> getTableColumns(String tableName);

    boolean isConnected();
}
