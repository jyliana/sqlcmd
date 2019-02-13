package ua.com.juja.sqlcmd.controller;

import ua.com.juja.sqlcmd.controller.command.Command;
import ua.com.juja.sqlcmd.controller.command.Exit;
import ua.com.juja.sqlcmd.controller.command.Help;
import ua.com.juja.sqlcmd.model.DataSet;
import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.view.View;

import java.util.Arrays;

public class MainController {
    private Command[] commands;
    private DatabaseManager manager;
    private View view;

    public MainController(View view, DatabaseManager manager) {
        this.view = view;
        this.manager = manager;
        this.commands = new Command[]{new Exit(view), new Help(view), new List(manager, view)};
    }

    public void run() {
        connectToDb();

        while (true) {
            view.write("Введи команду (или help для помощи):");
            String command = view.read();
            if (commands[2].canProcess(command)) {
                commands[2].process(command);
            } else if (commands[1].canProcess(command)) {
                commands[1].process(command);
            } else if (commands[0].canProcess(command)) {
                commands[0].process(command);
            } else if (command.startsWith("find|")) {
                doFind(command);
            } else {
                view.write("Несуществующая команда: " + command);
            }
        }
    }

    private void doFind(String command) {
        String[] data = command.split("\\|");
        String tableName = data[1];

        DataSet[] tableData = manager.getTableData(tableName);
        String[] tableColumns = manager.getTableColumns(tableName);
        printHeader(tableColumns);
        printTable(tableData);
    }

    private void printTable(DataSet[] tableData) {
        for (DataSet row : tableData) {
            printRow(row);
        }
        view.write("--------------------------");
    }

    private void printRow(DataSet row) {
        Object[] values = row.getValues();
        String result = " | ";
        for (Object value : values) {
            result += value + " | ";
        }
        view.write(result);
    }

    private void printHeader(String[] tableColumns) {
        String result = " | ";
        for (String name : tableColumns) {
            result += name + " | ";
        }
        view.write("--------------------------");
        view.write(result);
        view.write("--------------------------");
    }

    private void connectToDb() {
        view.write("Привет, юзер!");
        view.write("Введи, пожалуйста, имя базы данных, имя пользователя и пароль в формате: database|userName|password");
        while (true) {
            try {
                String string = view.read();
                String[] data = string.split("[|]");
                if (data.length != 3) {
                    throw new IllegalArgumentException("Неверное количество параметров, разделенных знаком |, ожидается 3, но есть " + data.length);
                }
                String databaseName = data[0];
                String userName = data[1];
                String password = data[2];
                manager.connect(databaseName, userName, password);
                break;
            } catch (Exception e) {
                printError(e);
            }
        }
        view.write("Успешно подключились.");
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
