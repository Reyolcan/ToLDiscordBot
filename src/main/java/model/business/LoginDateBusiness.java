package model.business;

import model.DBManager;
import model.pojo.LoginDate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class LoginDateBusiness implements Business<LoginDate> {
    private String database;
    private ArrayList<LoginDate> results = new ArrayList<>();

    public LoginDateBusiness(String guild) {
        DBManager.getConnection(guild);
        this.database = guild;
    }
        
    @Override
    public void createFromResultSet(ResultSet resultset) throws SQLException {
        LoginDate loginDate = new LoginDate();
        loginDate.setId(resultset.getInt(1));
        loginDate.setName(resultset.getString(2));
        loginDate.setStart(LocalDate.parse(resultset.getString(3)));
        loginDate.setEnd(LocalDate.parse(resultset.getString(4)));
        results.add(loginDate);
    }

    @Override
    public LoginDate getById(String id) throws SQLException {
        String sql = "SELECT * FROM logindates WHERE id = " + id;
        DBManager.executeSearchQuery(sql, this);
        return results.size() == 1 ? results.get(0) : null;
    }

    public LoginDate getByName(String name) throws SQLException {
        String sql = "SELECT * FROM logindates WHERE name = '" + name + "'";
        DBManager.executeSearchQuery(sql, this);
        return results.size() == 1 ? results.get(0) : null;
    }

    @Override
    public ArrayList<LoginDate> getAll() throws SQLException {
        String sql = "SELECT * FROM logindates";
        DBManager.executeSearchQuery(sql, this);
        return results;
    }

    @Override
    public int add(LoginDate loginDate) throws SQLException {
        String sql = "INSERT INTO loginDates(id, name, start, end) VALUES ('"
                + loginDate.getId() + "', '"
                + loginDate.getName() + "', '"
                + loginDate.getStart() + "', '"
                + loginDate.getEnd() + "')";
        return DBManager.executeSQLUpdate(sql, database);
    }

    @Override
    public int update(LoginDate loginDate) throws SQLException {
        String sql = "update loginDates set name='" + loginDate.getName() + "', "
                + "start='" + loginDate.getStart() + "', end='" + loginDate.getEnd() + "' "
                + "where id=" + loginDate.getId();
        return DBManager.executeSQLUpdate(sql, database);
    }

    @Override
    public int delete(LoginDate loginDate) throws SQLException {
        String sql = "delete from loginDates where id=" + loginDate.getId();
        return DBManager.executeSQLUpdate(sql, database);
    }

    @Override
    public int numberOf() throws SQLException {
        String sql = "SELECT COUNT(*) FROM logindatess";
        return 0;
    }

    @Override
    public String getDatabase() {
        results.clear();
        return database;
    }
}
