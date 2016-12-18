package model.business;

import model.DBManager;
import model.pojo.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class UserBusiness implements Business<User> {
    private String database;
    private ArrayList<User> results = new ArrayList<>();

    public UserBusiness(String guild) {
        DBManager.getConnection(guild);
        this.database = guild;
    }

    @Override
    public void createFromResultSet(ResultSet resultset) throws SQLException {
        User user = new User();
        user.setId(resultset.getString(1));
        user.setStones(resultset.getInt(2));
        user.setLogin(resultset.getBoolean(3));
        user.setBoss(resultset.getBoolean(4));
        results.add(user);
    }

    @Override
    public User getById(String id) throws SQLException {
        String sql = "SELECT * FROM users WHERE id = " + id;
        DBManager.executeSearchQuery(sql, this);
        return results.size() == 1 ? results.get(0) : null;
    }

    @Override
    public ArrayList getAll() throws SQLException {
        String sql = "SELECT * FROM users";
        DBManager.executeSearchQuery(sql, this);
        return results;
    }

    @Override
    public int add(User user) throws SQLException {
        String sql = "INSERT INTO users(id, stones, login, boss) VALUES ('"
                + user.getId() + "', '"
                + user.getStones() + "', '"
                + (user.isLogin() ? 1 : 0) + "', '"
                + (user.isBoss() ? 1 : 0) + "')";
        return DBManager.executeSQLUpdate(sql, database);
    }

    @Override
    public int update(User user) throws SQLException {
        String sql = "update users set stones='" + user.getStones() + "', login='" + (user.isLogin() ? 1 : 0)  + "', "
                + "boss='" + (user.isBoss() ? 1 : 0) + "' "
                + "where id=" + user.getId();
        return DBManager.executeSQLUpdate(sql, database);
    }

    @Override
    public int delete(User user) throws SQLException {
        String sql = "delete from users where id=" + user.getId();
        return DBManager.executeSQLUpdate(sql, database);
    }

    @Override
    public int numberOf() throws SQLException {
        String sql = "SELECT COUNT(*) FROM users";
        return 0;
    }

    @Override
    public String getDatabase() {
        results.clear();
        return database;
    }

    public int renewLoginAll() throws SQLException {
        String sql = "UPDATE users SET login = 0";
        return DBManager.executeSQLUpdate(sql, database);
    }
}
