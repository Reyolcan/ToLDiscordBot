package model.business;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public interface Business<Pojo> {
    void createFromResultSet(ResultSet resultset) throws SQLException;
    Pojo getById(String id) throws SQLException;
    ArrayList<Pojo> getAll() throws SQLException;
    int add(Pojo pojo) throws SQLException;
    int update(Pojo pojo) throws SQLException;
    int delete(Pojo pojo) throws SQLException;
    int numberOf() throws SQLException;
    String getDatabase();
}
