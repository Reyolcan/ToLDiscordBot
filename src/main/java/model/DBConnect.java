package model;

import controller.ErrorController;

import java.sql.*;
import java.util.ArrayList;

public class DBConnect {
    private Connection connection;
    private Statement statement;

    public DBConnect() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:databases/wiki.db");
        } catch (ClassNotFoundException e) {
            ErrorController.logStackTrace(e);
        } catch (SQLException e) {
            ErrorController.logStackTrace(e);
        }
        try {
            statement = getInstance().createStatement();
            statement.closeOnCompletion();
            ResultSet rs = statement.executeQuery( "SELECT name FROM sqlite_master WHERE type='table'" );
            ArrayList<String> dbs = new ArrayList<String>();
            while(!rs.isAfterLast()) {
                dbs.add(rs.getString("name"));
                rs.next();
            }
            if(!dbs.contains("data")) {
                statement.executeUpdate("CREATE TABLE data " +
                        "(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                        " name VARCHAR(250) NOT NULL," +
                        " date VARCHAR(30) NOT NULL)");
            }
            if(!dbs.contains("units")) {
                statement.executeUpdate("CREATE TABLE units " +
                        "(id INT PRIMARY KEY NOT NULL," +
                        " name VARCHAR(250) NOT NULL," +
                        " character VARCHAR(100) NOT NULL," +
                        " rarity TINYINT NOT NULL," +
                        " custom TINYINT NOT NULL)");
            }
        } catch (SQLException e) {
            ErrorController.logStackTrace(e);
        }
    }

    public DBConnect(String name) {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:databases/" + name + ".db");
        } catch ( Exception e ) {
            ErrorController.logStackTrace(e);
        }
        try {
            statement = getInstance().createStatement();
            statement.closeOnCompletion();
            ResultSet rs = statement.executeQuery( "SELECT name FROM sqlite_master WHERE type='table'" );
            ArrayList<String> dbs = new ArrayList<String>();
            while(!rs.isAfterLast()) {
                dbs.add(rs.getString("name"));
                rs.next();
            }
            if(!dbs.contains("logindates")) {
                statement.executeUpdate("CREATE TABLE logindates " +
                        "(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                        " name VARCHAR(250) NOT NULL," +
                        " start VARCHAR(20) NOT NULL," +
                        " end VARCHAR(20) NOT NULL)");
            }
            if(!dbs.contains("users")) {
                statement.executeUpdate("CREATE TABLE users " +
                        "(id VARCHAR(30) PRIMARY KEY NOT NULL," +
                        " stones INT," +
                        " login TINYINT NOT NULL," +
                        " boss TINYINT NOT NULL)");
            }
            if(!dbs.contains("units")) {
                statement.executeUpdate("CREATE TABLE units " +
                        "(id INT PRIMARY KEY NOT NULL," +
                        " name VARCHAR(250) NOT NULL," +
                        " character VARCHAR(100) NOT NULL," +
                        " rarity TINYINT NOT NULL," +
                        " custom TINYINT NOT NULL)");
            }
        } catch (SQLException e) {
            ErrorController.logStackTrace(e);
        }
    }

    public synchronized java.sql.Connection getInstance() throws SQLException {
        return connection;
    }
}
