package database;

import java.sql.*;
import java.util.ArrayList;

public class DBConnect {
    private Connection c;
    private Statement stmt;

    public DBConnect(String name) {
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:" + name + ".db");
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
        try {
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery( "SELECT name FROM sqlite_master WHERE type='table'" );
            ArrayList<String> dbs = new ArrayList<String>();
            while(!rs.isAfterLast()) {
                dbs.add(rs.getString("name"));
                rs.next();
            }
            if(!dbs.contains("users")) {
                stmt.executeUpdate("CREATE TABLE users " +
                        "(id INT PRIMARY KEY NOT NULL," +
                        " stones INT," +
                        " boss TINYINT)");
            }
            if(!dbs.contains("units")) {
                stmt.executeUpdate("CREATE TABLE units " +
                        "(id INT PRIMARY KEY NOT NULL," +
                        " name VARCHAR(250) NOT NULL," +
                        " character VARCHAR(100) NOT NULL," +
                        " rarity INT)");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<String> getAllUsers() {
        ArrayList<String> users = new ArrayList<String>();
        try {
            ResultSet rs = stmt.executeQuery("SELECT id FROM users");
            while(!rs.isAfterLast()) {
                users.add(rs.getString("id"));
                rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    public void addUser(String userId, boolean isBoss) {
        try {
            stmt.executeUpdate("INSERT INTO users (id, stones, boss) VALUES (" + userId + ", 0, " + (isBoss ? "1" : "0") + ")");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean isBoss(String userId) {
        try {
            ResultSet rs = stmt.executeQuery("SELECT boss FROM users WHERE id = " + userId);
            rs.next();
            return rs.getBoolean("boss");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public int getStones(String userId) {
        try {
            ResultSet rs = stmt.executeQuery("SELECT stones FROM users WHERE id =" + userId);
            rs.next();
            return rs.getInt("stones");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public boolean setStones(int quantity, String userId) {
        int hs = getStones(userId) + quantity;
        if(hs >= 0) {
            try {
                stmt.executeUpdate("UPDATE users SET stones = " + hs + " WHERE id = " + userId);
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }
        else {
            return false;
        }
        return true;
    }

    public void close() {
        try {
            stmt.close();
            c.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
