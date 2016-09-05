package database;

import org.w3c.dom.Document;
import utils.WikiCall;

import java.sql.*;
import java.time.OffsetDateTime;
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
            if(!dbs.contains("logindates")) {
                stmt.executeUpdate("CREATE TABLE logindates " +
                        "(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                        " name VARCHAR(250) NOT NULL," +
                        " date VARCHAR(20) NOT NULL)");
            }
            if(!dbs.contains("users")) {
                stmt.executeUpdate("CREATE TABLE users " +
                        "(id INT PRIMARY KEY NOT NULL," +
                        " stones INT," +
                        " login TINYINT NOT NULL," +
                        " boss TINYINT NOT NULL)");
            }
            if(!dbs.contains("units")) {
                stmt.executeUpdate("CREATE TABLE units " +
                        "(id INT PRIMARY KEY NOT NULL," +
                        " name VARCHAR(250) NOT NULL," +
                        " character VARCHAR(100) NOT NULL," +
                        " rarity INT NOT NULL)");
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
            stmt.executeUpdate("INSERT INTO users (id, stones, boss, login) VALUES (" + userId + ", 0, " + (isBoss ? "1" : "0") + ", 0)");
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

    public boolean checkLogin(OffsetDateTime time) {
        try {
            ResultSet rs = stmt.executeQuery("SELECT date FROM logindates WHERE name = 'Daily Login'");
            if(rs.next()) {
                String[] date = rs.getString("date").split("-");
                if(time.getDayOfMonth() != Integer.parseInt(date[0]) ||
                        time.getMonthValue() != Integer.parseInt(date[1]) ||time.getYear() != Integer.parseInt(date[2])) {
                    stmt.executeUpdate("UPDATE users SET login = 0");
                    stmt.executeUpdate("INSERT INTO logindates (name, date) VALUES ('Daily Login', '" +
                            time.getDayOfMonth() + "-" + time.getMonthValue() + "-" + time.getYear() + "')");
                }
            }
            else {
                stmt.executeUpdate("INSERT INTO logindates (name, date) VALUES ('Daily Login', '" +
                        time.getDayOfMonth() + "-" + time.getMonthValue() + "-" + time.getYear() + "')");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean checkUserLogin(String userId) {
        try {
            ResultSet rs = stmt.executeQuery("SELECT login FROM users WHERE id = " + userId);
            rs.next();
            if(!rs.getBoolean("login")) {
                setStones(1, userId);
                stmt.executeUpdate("UPDATE users SET login = 1 WHERE id = " + userId);
                return true;
            }
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

    public String getDate(String name) {
        try {
            ResultSet rs = stmt.executeQuery("SELECT date FROM logindates WHERE name = '" + name + "'");
            if(rs.next()) {
                return rs.getString("date");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean setDate(String name, String date) {
        try {
            if(getDate(name) != null) {
                stmt.executeUpdate("UPDATE date FROM logindates WHERE name = '" + name + "'");
                return true;
            }
            else {
                stmt.executeUpdate("INSERT INTO logindates (name, date) VALUES (" + name + ", " + date + ")");
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void addUnit(String id, String name, String character, String rarity) {
        try {
            stmt.executeUpdate("INSERT INTO units (id, name, character, rarity) VALUES " +
                    "(" + id + ", " + name + ", " + character + ", " + rarity + ")");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addUnit(String id) {
        /*WikiCall wikia = new WikiCall();
        Document doc = wikia.consult("action=query&prop=revisions&rvlimit=1&rvprop=content&pageids=" + id);
        try {
            stmt.executeUpdate("INSERT INTO units (id, name, character, rarity) VALUES " +
                    "(" + id + ", " + name + ", " + character + ", " + rarity + ")");
        } catch (SQLException e) {
            e.printStackTrace();
        }*/
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
