package ua.com.juja.sqlcmd.model;

import java.sql.SQLException;
import java.util.List;
import java.util.Set;

public interface DatabaseManager {
    List<DataSet> getTableData(String tableName);

    int getSize(String tableName) throws SQLException;

    Set<String> getTablesNames();

    void connect(String database, String userName, String password);

    void clear(String tableName);

    void create(String tableName, DataSet input);

    void update(String tableName, int id, DataSet newValue);

    Set <String> getTableColumns(String tableName);

    boolean isConnected();
}
