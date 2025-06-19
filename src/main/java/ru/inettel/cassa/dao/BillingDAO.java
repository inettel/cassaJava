package ru.inettel.cassa.dao;

import ru.inettel.cassa.SearchData;
import ru.inettel.cassa.user.User;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by ksork on 29.06.17.
 */
public abstract class BillingDAO {

    protected Connection connection;

    public BillingDAO(){}

    abstract void startConnect() throws SQLException, ClassNotFoundException, IllegalAccessException, InstantiationException;

    abstract String prepareSql(SearchData searchData);

    abstract List<User> parseDbResponse(ResultSet resultSet) throws SQLException;

    abstract void setTarifs(List<User> users) throws SQLException;

    abstract String prepareTarifSql(User user);

    abstract String selectNamesSql();

    public Set<String> selectNames() throws SQLException {
        Set<String> names = new HashSet<>();
        String sql = selectNamesSql();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        while (resultSet.next()){
            String name = resultSet.getString(1);
            name = name == null ? "": name;
            if (name.trim() != "") names.add(name);
        }
        return names;
    }

    public List<User> selectUsers(SearchData searchData) throws SQLException {
        String sql = prepareSql(searchData);
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        List<User> userList = parseDbResponse(resultSet);
        statement.close();
        setTarifs(userList);
        return userList;
    }

     void stopConnect() throws SQLException {
         if (connection != null)
             connection.close();
     }
}
