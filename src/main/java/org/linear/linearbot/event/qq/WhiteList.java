package org.linear.linearbot.event.qq;

import org.linear.linearbot.LinearBot;

import java.io.File;
import java.sql.*;

public class WhiteList {

    private static final LinearBot INSTANCE = LinearBot.INSTANCE;

    public static Connection connection;

    public static void initializeSQLite() throws SQLException, ClassNotFoundException{
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection("jdbc:sqlite:" + new File(INSTANCE.getDataFolder(),"database.db").getPath());
    }

    public static void closeSQLite() throws SQLException {
        connection.close();
    }


    public static void addBind(String id, long qq) {
        String createTable = "CREATE TABLE IF NOT EXISTS whitelist (id TINYTEXT NOT NULL, qq long NOT NULL);";
        String selectid = "SELECT * FROM whitelist WHERE id='" + id + "' LIMIT 1";
        String selectqq = "SELECT * FROM whitelist WHERE qq=" + qq + " LIMIT 1";
        String updateid = "UPDATE whitelist SET id='" + id + "' WHERE qq=" + qq + ";";
        String updateqq = "UPDATE whitelist SET qq=" + qq + " WHERE id='" + id + "';";
        String insert = "insert into whitelist values('" + id + "', " + qq + ");";

        try {
            Statement statement = connection.createStatement();
            Statement statement1 = connection.createStatement();
            statement.executeUpdate(createTable);

            ResultSet resultSetid = statement.executeQuery(selectid);
            ResultSet resultSetqq = statement1.executeQuery(selectqq);

            if (!resultSetid.isBeforeFirst() && resultSetqq.isBeforeFirst()) {
                statement.executeUpdate(updateid);
            } else if (resultSetid.isBeforeFirst() && !resultSetqq.isBeforeFirst()) {
                statement.executeUpdate(updateqq);
            } else if (!resultSetid.isBeforeFirst() && !resultSetqq.isBeforeFirst()) {
                statement.executeUpdate(insert);
            }

            resultSetid.close();
            resultSetqq.close();
            statement.close();
            statement1.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public static void removeBind(String id) {
        String createTable = "CREATE TABLE IF NOT EXISTS whitelist (id TINYTEXT NOT NULL, qq long NOT NULL);";
        String select = "SELECT * FROM whitelist WHERE id='" + id + "' LIMIT 1;";
        String delete = "DELETE FROM whitelist WHERE id='" + id + "';";

        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(createTable);

            ResultSet resultSet = statement.executeQuery(select);

            if (resultSet.isBeforeFirst()) {
                resultSet.next();
                statement.executeUpdate(delete);
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void removeBind(long qq) {
        String createTable = "CREATE TABLE IF NOT EXISTS whitelist (id TINYTEXT NOT NULL, qq long NOT NULL);";
        String select = "SELECT * FROM whitelist WHERE qq=" + qq + " LIMIT 1;";
        String delete = "DELETE FROM whitelist WHERE qq=" + qq+";";

        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(createTable);

            // 如果没有找到记录为false，找到就是true
            ResultSet resultSet = statement.executeQuery(select);

            if (resultSet.isBeforeFirst()) {
                resultSet.next();
                statement.executeUpdate(delete);
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static long getBind(String id){
        long qq = 0L;

        String createTable = "CREATE TABLE IF NOT EXISTS whitelist (id TINYTEXT NOT NULL, qq long NOT NULL);";
        String select = "SELECT * FROM whitelist WHERE id='" + id + "' LIMIT 1;";

        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(createTable);

            ResultSet resultSet = statement.executeQuery(select);

            if (resultSet.isBeforeFirst()) {
                resultSet.next();
                qq = resultSet.getLong("qq");
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return qq;
    }

    public static String getBind(long qq) {
        String id = null;

        String createTable = "CREATE TABLE IF NOT EXISTS whitelist (id TINYTEXT NOT NULL, qq long NOT NULL);";
        String select = "SELECT * FROM whitelist WHERE qq=" + qq + " LIMIT 1;";

        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(createTable);

            ResultSet resultSet = statement.executeQuery(select);

            if (resultSet.isBeforeFirst()) {
                resultSet.next();
                id = resultSet.getString("id");
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return id;
    }

}
