package ru.inettel.cassa.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import ru.inettel.cassa.SearchData;
import ru.inettel.cassa.user.User;

public abstract class BillingDAO {
    protected Connection connection;

    public BillingDAO() {
    }

    abstract void startConnect() throws SQLException, ClassNotFoundException, IllegalAccessException, InstantiationException;

    abstract String prepareSql(SearchData var1);

    abstract List<User> parseDbResponse(ResultSet var1) throws SQLException;

    abstract void setTarifs(List<User> var1) throws SQLException;

    abstract String prepareTarifSql(User var1);

    abstract String selectNamesSql();

    public Set<String> selectNames() throws SQLException {
        Set<String> names = new HashSet();
        String sql = this.selectNamesSql();
        Statement statement = this.connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);

        while(resultSet.next()) {
            String name = resultSet.getString(1);
            name = name == null ? "" : name;
            if (name.trim() != "") {
                names.add(name);
            }
        }

        return names;
    }

    public List<User> selectUsers(SearchData searchData) throws SQLException {
        String sql = this.prepareSql(searchData);
        Statement statement = this.connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        List<User> userList = this.parseDbResponse(resultSet);
        statement.close();
        this.setTarifs(userList);
        return userList;
    }

    void stopConnect() throws SQLException {
        if (this.connection != null) {
            this.connection.close();
        }

    }
}
