package model.business;

import controller.WikiController;
import model.DBManager;
import model.pojo.Unit;
import org.w3c.dom.Document;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class UnitBusiness implements Business<Unit> {
    private String database;
    private ArrayList<Unit> results = new ArrayList<>();

    public UnitBusiness(String guild) {
        DBManager.getConnection(guild);
        this.database = guild;
    }

    @Override
    public void createFromResultSet(ResultSet resultset) throws SQLException {
        Unit unit = new Unit();
        unit.setId(resultset.getInt(1));
        unit.setName(resultset.getString(2));
        unit.setCharacter(resultset.getString(3));
        unit.setRarity(resultset.getInt(4));
        unit.setCustom(resultset.getBoolean(5));
        results.add(unit);
    }

    public Unit createFromWikia(String id) {
        WikiController wikia = new WikiController();
        Document doc = wikia.consult("action=query&prop=revisions&rvlimit=1&rvprop=content&pageids=" + id);
        String data = doc.getElementsByTagName("rev").item(0).getTextContent();
        Unit unit = new Unit();
        unit.setId(Integer.parseInt(id));
        unit.setName(wikia.getUnitData("name", data).replace("'", "''"));
        unit.setCharacter(wikia.getUnitData("char", data).replace("'", "''"));
        unit.setRarity(Integer.parseInt(wikia.getUnitData("rarity", data).replace("'", "''")));
        System.out.println(unit.getId() + "|" + unit.getName());
        unit.setCustom(false);
        return unit;
    }

    public Unit selectRandomUnit(int rarity) throws SQLException {
        String sql = "SELECT * FROM units WHERE rarity = " + rarity
                + " ORDER BY RANDOM() LIMIT 1";
        DBManager.executeSearchQuery(sql, this);
        return results.size() == 1 ? results.get(0) : null;
    }

    @Override
    public Unit getById(String id) throws SQLException {
        String sql = "SELECT * FROM units WHERE id = " + id;
        DBManager.executeSearchQuery(sql, this);
        return results.size() == 1 ? results.get(0) : null;
    }

    @Override
    public ArrayList getAll() throws SQLException {
        String sql = "SELECT * FROM units";
        DBManager.executeSearchQuery(sql, this);
        return results;
    }

    @Override
    public int add(Unit unit) throws SQLException {
        String sql = "INSERT INTO units(id, name, character, rarity, custom) VALUES ('"
                + unit.getId() + "', '"
                + unit.getName() + "', '"
                + unit.getCharacter() + "', '"
                + unit.getRarity() + "', '"
                + unit.isCustom() + "')";
        return DBManager.executeSQLUpdate(sql, database);
    }

    @Override
    public int update(Unit unit) throws SQLException {
        String sql = "update units set name='" + unit.getName() + "', character='" + unit.getCharacter() + "', "
                + "rarity='" + unit.getRarity() + "', custom='" + unit.isCustom() + "' "
                + "where id=" + unit.getId();
        return DBManager.executeSQLUpdate(sql, database);
    }

    @Override
    public int delete(Unit unit) throws SQLException {
        String sql = "delete from units where id=" + unit.getId();
        return DBManager.executeSQLUpdate(sql, database);
    }

    @Override
    public int numberOf() throws SQLException {
        String sql = "SELECT COUNT(*) FROM units";
        return 0;
    }

    @Override
    public String getDatabase() {
        results.clear();
        return database;
    }
}
