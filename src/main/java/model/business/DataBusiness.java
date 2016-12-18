package model.business;

import model.DBManager;
import model.pojo.Data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class DataBusiness implements Business<Data> {
    private String database;
    private ArrayList<Data> results = new ArrayList<>();

    public DataBusiness(String guild) {
        DBManager.getConnection(guild);
        this.database = guild;
    }
    
    @Override
    public void createFromResultSet(ResultSet resultset) throws SQLException {
        Data data = new Data();
        data.setId(resultset.getInt(1));
        data.setName(resultset.getString(2));
        data.setDate(LocalDateTime.parse(resultset.getString(3)));
        results.add(data);
    }

    @Override
    public Data getById(String id) throws SQLException {
        String sql = "SELECT * FROM data WHERE id = " + id;
        DBManager.executeSearchQuery(sql, this);
        return results.size() == 1 ? results.get(0) : null;
    }

    public Data getByName(String name) throws SQLException {
        String sql = "SELECT * FROM data WHERE name = '" + name +"'";
        DBManager.executeSearchQuery(sql, this);
        return results.size() == 1 ? results.get(0) : null;
    }

    @Override
    public ArrayList<Data> getAll() throws SQLException {
        String sql = "SELECT * FROM logindates";
        DBManager.executeSearchQuery(sql, this);
        return results;
    }

    @Override
    public int add(Data data) throws SQLException {
        String sql = "INSERT INTO data(name, date) VALUES ('"
                + data.getName() + "', '"
                + data.getDate() + "')";
        return DBManager.executeSQLUpdate(sql, database);
    }

    @Override
    public int update(Data data) throws SQLException {
        String sql = "update data set name='" + data.getName() + "', date='" + data.getDate() + "' "
                + "where id=" + data.getId();
        return DBManager.executeSQLUpdate(sql, database);
    }

    @Override
    public int delete(Data data) throws SQLException {
        String sql = "delete from data where id=" + data.getId();
        return DBManager.executeSQLUpdate(sql, database);
    }

    @Override
    public int numberOf() throws SQLException {
        String sql = "SELECT COUNT(*) FROM data";
        return 0;
    }

    @Override
    public String getDatabase() {
        results.clear();
        return this.database;
    }
}