package ua.com.juja.sqlcmd.model;

import java.sql.*;
import java.util.Random;

public class Main {

    public static void main(String[] argv) throws ClassNotFoundException, SQLException {
        Class.forName("org.postgresql.Driver");
        Connection connection = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/sqlcmd", "postgres",
                "123456");

        //insert
        String sql = "INSERT INTO users " + "VALUES ('Tara', 'Frost')";
        update(connection, sql);

        //select
        String sql1 = "SELECT * FROM users WHERE id > 1";
        select(connection, sql1);

        //delete
        String delete = "DELETE FROM users WHERE id > 5";
        update(connection, delete);

        //update
        PreparedStatement ps = connection.prepareStatement(
                "UPDATE users SET password = ? WHERE id > 4");
        ps.setString(1, "password_" + new Random().nextInt());
        ps.executeUpdate();
        ps.close();

        connection.close();
    }

    private static void select(Connection connection, String sql1) throws SQLException {
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sql1);
        while (rs.next()) {
            System.out.println("Column 1 returned ");
            System.out.println("id: " + rs.getString("id"));
            System.out.println("name: " + rs.getString("name"));
            System.out.println("password: " + rs.getString("password"));
            System.out.println("______");
        }
        rs.close();
        stmt.close();
    }

    private static void update(Connection connection, String sql) throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.executeUpdate(sql);
        stmt.close();
    }
}