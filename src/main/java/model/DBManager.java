package model;

import controller.ErrorController;
import model.business.Business;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.concurrent.Semaphore;

public class DBManager {
    private static HashMap<String, DBConnect> connections = new HashMap<>();
    private static HashMap<String, Semaphore> semaphores = new HashMap<>();

    public static void getConnection(String guild) {
        if(!connections.containsKey(guild)) {
            if(guild == "wiki") {
                connections.put("wiki", new DBConnect());
                semaphores.put("wiki", new Semaphore(5));
            }
            else {
                connections.put(guild, new DBConnect(guild));
                semaphores.put(guild, new Semaphore(5));
            }
        }
    }

    public static void executeSearchQuery(String sql, Business business) throws SQLException {
        String database = business.getDatabase();
        try {
            semaphores.get(database).acquire(1);
            try(Statement statement = connections.get(database).getInstance().createStatement()) {
                try(ResultSet resultSet = statement.executeQuery(sql)) {
                    while(resultSet.next()) {
                        business.createFromResultSet(resultSet);
                    }
                }
            }
        } catch (InterruptedException e) {
            ErrorController.logStackTrace(e);
        } finally {
            semaphores.get(database).release(1);
        }
    }

    public static int executeSQLUpdate(String sql, String database) throws SQLException {
        int updates = 0;
        try {
            semaphores.get(database).acquire(5);
            try(Statement statement = connections.get(database).getInstance().createStatement()) {
                updates = statement.executeUpdate(sql);
            }
            semaphores.get(database).release(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return updates;
    }
}
