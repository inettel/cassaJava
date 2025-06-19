package ru.inettel.cassa.dao;

import ru.inettel.cassa.SearchData;
import ru.inettel.cassa.user.AtirraUser;
import ru.inettel.cassa.user.User;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Created by ksork on 03.07.17.
 */
public class AtirraDAO extends BillingDAO {
    @Override
    void startConnect() throws SQLException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        Properties props = new Properties();
        props.setProperty("user", "SYSDBA");
        props.setProperty("password", "masterkey");
        props.setProperty("encoding", "UTF8");
        String url = "jdbc:firebirdsql:192.168.0.13:/var/lib/firebird/2.1/data/ATIRRA_DB.FDB";
        connection = DriverManager.getConnection(url, props);
    }

    @Override
    String prepareSql(SearchData searchData) {
        String sql = "SELECT customer.account_no, customer.debt_sum, street.street_name, house.house_no, " +
                "customer.flat_no, customer.firstname, customer.surname, customer.midlename, " +
                "customer.cust_state, customer.cust_state_descr FROM customer " +
                "INNER JOIN house ON customer.house_id = house.house_id " +
                "INNER JOIN street ON  house.street_id = street.street_id ";

        String sqlSearch = "";
        if (!searchData.getAccount().equalsIgnoreCase(""))
            sqlSearch = " customer.account_no = '" + searchData.getAccount() + "'";
        if (!searchData.getStreet().equalsIgnoreCase("")) {
            String sqlAnd = sqlSearch.isEmpty() ? " " : " AND ";
            sqlSearch = sqlSearch + sqlAnd + "street.street_name CONTAINING '" + searchData.getStreet() + "'";
        }
        if (!searchData.getHouse().equalsIgnoreCase("")) {
            String sqlAnd = sqlSearch.isEmpty() ? " " : " AND ";
            sqlSearch = sqlSearch + sqlAnd + "house.house_no = '" + searchData.getHouse() + "'";
        }
        if (!searchData.getFlat().equalsIgnoreCase("")) {
            String sqlAnd = sqlSearch.isEmpty() ? " " : " AND ";
            sqlSearch = sqlSearch + sqlAnd + "customer.flat_no = '" + searchData.getFlat() + "'";
        }
        if (!searchData.getName().equalsIgnoreCase("")) {
            String sqlAnd = sqlSearch.isEmpty() ? " " : " AND ";
//            sqlSearch = sqlSearch + sqlAnd + "(customer.surname CONTAINING '" + searchData.getName() + "'" +
//            "OR customer.midlename CONTAINING '" + searchData.getName() + "'" +
//            "OR customer.firstname CONTAINING '" + searchData.getName() + "')";
            sqlSearch = sqlSearch + sqlAnd + "(customer.surname || ' ' || customer.firstname || ' ' || customer.midlename) " +
                    "CONTAINING '" + searchData.getName() + "'";
        }
        if (!sqlSearch.isEmpty())
            sql = sql + " WHERE " + sqlSearch;

        sql = sql + ";";
        return sql;
    }

    @Override
    String selectNamesSql() {
        return "SELECT customer.surname || ' ' || customer.firstname || ' ' || customer.midlename FROM customer;";
    }

    @Override
    void setTarifs(List<User> users) throws SQLException {

    }

    @Override
    String prepareTarifSql(User user) {
        return null;
    }

    @Override
    List<User> parseDbResponse(ResultSet resultSet) throws SQLException {
        List<User> userList = new ArrayList<>();
        while (resultSet.next()){
            User user = new AtirraUser();
            user.setAccount(resultSet.getString("account_no"));
            user.setBalance(0 - resultSet.getDouble("debt_sum"));
            user.setStreet(resultSet.getString("street_name"));
            user.setHouse(resultSet.getString("house_no"));
            user.setFlat(resultSet.getString("flat_no"));
            user.setName(String.format("%s %s %s", resultSet.getString("surname"), resultSet.getString("firstname"),
                    resultSet.getString("midlename")));
            if (resultSet.getInt("cust_state") > 0)
                try {
                    user.setTariff(resultSet.getString("cust_state_descr").trim().split("\\s")[0]);
                }catch (NullPointerException e){
                    user.setTariff("???");
                }
                userList.add(user);
        }
        return userList;
    }
}
