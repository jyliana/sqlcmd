package ua.com.juja.sqlcmd.controller.command;

import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.view.View;

public class Connect implements Command {
    private static String COMMAND_SAMPLE = "connect|sqlcmd|postgres|123456";

    private DatabaseManager manager;
    private View view;

    public Connect(DatabaseManager manager, View view) {
        this.manager = manager;
        this.view = view;
    }

    @Override
    public boolean canProcess(String command) {
        return command.startsWith("connect|");
    }

    @Override
    public void process(String command) {
        try {
            String[] data = command.split("[|]");

            if (data.length != count()) {
                throw new IllegalArgumentException(
                        String.format("Неверное количество параметров, разделенных " +
                                        "знаком |, ожидается %s, но есть: %s",
                                count(), data.length));
            }
            String databaseName = data[1];
            String userName = data[2];
            String password = data[3];
            manager.connect(databaseName, userName, password);
            view.write("Успешно подключились.");
        } catch (Exception e) {
            printError(e);
        }
    }

    private int count() {
        return COMMAND_SAMPLE.split("\\|").length;
    }

    private void printError(Exception e) {
        String message = e.getMessage();
        if (e.getCause() != null) {
            message += e.getCause().getMessage();
        }
        view.write("Неудача по причине: " + message);
        view.write("Повторите попытку.");
    }
}
