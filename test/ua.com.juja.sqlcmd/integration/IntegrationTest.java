package ua.com.juja.sqlcmd.integration;

import org.junit.Before;
import org.junit.Test;
import ua.com.juja.sqlcmd.controller.Main;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

import static org.junit.Assert.assertEquals;


public class IntegrationTest {

    private ConfigurableInputStream in;
    private ByteArrayOutputStream out;

    @Before
    public void setup() {
        out = new ByteArrayOutputStream();
        in = new ConfigurableInputStream();

        System.setIn(in);
        System.setOut(new PrintStream(out));
    }


    public String getData() {
        try {
            String result = new String(out.toByteArray(), "UTF-8");
            out.reset();
            return result;
        } catch (UnsupportedEncodingException e) {
            return e.getMessage();
        }
    }

    @Test
    public void testHelp() {
        //given
        in.add("help");
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Привет, юзер!\r\n" +
                "Введи, пожалуйста, имя базы данных, имя пользователя и пароль в формате: connect|database|userName|password\r\n" +
                "Существующие команды: \r\n" +
                "\tconnect|databaseName|userName|password\r\n" +
                "\t\tдля подключения к базе данных, с которой будем работать\r\n" +
                "\tlist\r\n" +
                "\t\tдля получения списка всех таблиц базы, к которой подключились\r\n" +
                "\tfind|tableName\r\n" +
                "\t\tдля получения содержимого таблицы 'tableName'\r\n" +
                "\thelp\r\n" +
                "\t\tдля вывода этого списка на экран\r\n" +
                "\texit\r\n" +
                "\t\tдля выходя из программы\r\n" +
                "Введи команду (или help для помощи):\r\n" +
                "До скорой встречи!\r\n", getData());
    }


    @Test
    public void testExit() {
        //given
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Привет, юзер!\r\n" +
                "Введи, пожалуйста, имя базы данных, имя пользователя и пароль в формате: connect|database|userName|password\r\n" +
                "До скорой встречи!\r\n", getData());
    }


    @Test
    public void testListWithoutConnect() {
        //given
        in.add("list");
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Привет, юзер!\r\n" +
                "Введи, пожалуйста, имя базы данных, имя пользователя и пароль в формате: connect|database|userName|password\r\n" +
                "Вы не можете пользоваться командой 'list', пока не подключитесь к базе с помощью команды connect|databaseName|userName|password\r\n" +
                "Введи команду (или help для помощи):\r\n" +
                "До скорой встречи!\r\n", getData());
    }

    @Test
    public void testFindWithoutConnect() {
        //given
        in.add("find|users");
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Привет, юзер!\r\n" +
                "Введи, пожалуйста, имя базы данных, имя пользователя и пароль в формате: connect|database|userName|password\r\n" +
                "Вы не можете пользоваться командой 'find|users', пока не подключитесь к базе с помощью команды connect|databaseName|userName|password\r\n" +
                "Введи команду (или help для помощи):\r\n" +
                "До скорой встречи!\r\n", getData());
    }

    @Test
    public void testUnsupported() {
        //given
        in.add("unsupported");
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Привет, юзер!\r\n" +
                "Введи, пожалуйста, имя базы данных, имя пользователя и пароль в формате: connect|database|userName|password\r\n" +
                "Вы не можете пользоваться командой 'unsupported', пока не подключитесь к базе с помощью команды connect|databaseName|userName|password\r\n" +
                "Введи команду (или help для помощи):\r\n" +
                "До скорой встречи!\r\n", getData());
    }

    @Test
    public void testUnsupportedAfterConnect() {
        //given
        in.add("connect|sqlcmd|postgres|123456");
        in.add("unsupported");
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Привет, юзер!\r\n" +
                "Введи, пожалуйста, имя базы данных, имя пользователя и пароль в формате: connect|database|userName|password\r\n" +
                // connect
                "Успешно подключились.\r\n" +
                "Введи команду (или help для помощи):\r\n" +
                // unsupported
                "Несуществующая команда: unsupported\r\n" +
                "Введи команду (или help для помощи):\r\n" +
                // exit
                "До скорой встречи!\r\n", getData());
    }

    @Test
    public void testListAfterConnect() {
        //given
        in.add("connect|sqlcmd|postgres|123456");
        in.add("list");
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Привет, юзер!\r\n" +
                "Введи, пожалуйста, имя базы данных, имя пользователя и пароль в формате: connect|database|userName|password\r\n" +
                // connect
                "Успешно подключились.\r\n" +
                "Введи команду (или help для помощи):\r\n" +
                // list
                "[news, users, test]\r\n" +
                "Введи команду (или help для помощи):\r\n" +
                // exit
                "До скорой встречи!\r\n", getData());
    }

    @Test
    public void testFindAfterConnect() {
        //given
        in.add("connect|sqlcmd|postgres|123456");
        in.add("find|test");
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Привет, юзер!\r\n" +
                "Введи, пожалуйста, имя базы данных, имя пользователя и пароль в формате: connect|database|userName|password\r\n" +
                // connect
                "Успешно подключились.\r\n" +
                "Введи команду (или help для помощи):\r\n" +
                // find|test
                "--------------------------\r\n" +
                " | id | \r\n" +
                "--------------------------\r\n" +
                "--------------------------\r\n" +
                "Введи команду (или help для помощи):\r\n" +
                // exit
                "До скорой встречи!\r\n", getData());
    }

    @Test
    public void testConnectAfterConnect() {
        //given
        in.add("connect|sqlcmd|postgres|123456");
        in.add("list");
        in.add("connect|postgres|postgres|123456");
        in.add("list");
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Привет, юзер!\r\n" +
                "Введи, пожалуйста, имя базы данных, имя пользователя и пароль в формате: connect|database|userName|password\r\n" +
                // connect
                "Успешно подключились.\r\n" +
                "Введи команду (или help для помощи):\r\n" +
                // list
                "[news, users, test]\r\n" +
                "Введи команду (или help для помощи):\r\n" +
                // connect to other DB
                "Успешно подключились.\r\n" +
                "Введи команду (или help для помощи):\r\n" +
                // list
                "[test2]\r\n" +
                "Введи команду (или help для помощи):\r\n" +
                "До скорой встречи!\r\n", getData());
    }
}
