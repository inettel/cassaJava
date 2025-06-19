package ru.inettel.cassa.dao;

import ru.inettel.cassa.SearchData;
import ru.inettel.cassa.user.User;
import ru.inettel.cassa.user.UtmUser;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 * Created by ksork on 30.06.17.
 */
public class UtmDAO extends BillingDAO {
    @Override
    void startConnect() throws ClassNotFoundException, SQLException {
        String url = "jdbc:mysql://192.168.0.17:3306/UTM5?useUnicode=true&characterEncoding=utf-8";
        String username = "guest";
        String password = "8845svmg";
        Class.forName("com.mysql.cj.jdbc.Driver");
        connection = DriverManager.getConnection(url, username, password);

    }

    @Override
    String prepareSql(SearchData searchData) {
//        String sql = "SELECT users.basic_account, ROUND(accounts.balance, 2) AS balance, houses.street, " +
//                "users.actual_address, houses.number, users.flat_number, users.full_name, tariffs.name, " +
//                "account_tariff_link.is_deleted, account_tariff_link.id FROM users " +
//                "INNER JOIN accounts ON users.basic_account = accounts.id " +
//                "INNER JOIN houses ON users.house_id = houses.id " +
//                "INNER JOIN account_tariff_link ON accounts.id = account_tariff_link.account_id " +
//                "AND account_tariff_link.id = " +
//                "(SELECT MAX(account_tariff_link.id) FROM account_tariff_link WHERE account_tariff_link.account_id = users.basic_account) " +
//                "INNER JOIN tariffs ON account_tariff_link.tariff_id = tariffs.id " +
//                "WHERE users.is_deleted = 0 AND accounts.is_deleted = 0";
        String sql = "SELECT users.basic_account, ROUND(accounts.balance, 2) AS balance, houses.street, " +
                "users.actual_address, houses.number, users.flat_number, users.full_name FROM users " +
                "INNER JOIN accounts ON users.basic_account = accounts.id " +
                "INNER JOIN houses ON users.house_id = houses.id " +
                "WHERE users.is_deleted = 0 AND accounts.is_deleted = 0";
        if (!searchData.getAccount().equalsIgnoreCase(""))
            sql = sql + " AND users.basic_account = '" + searchData.getAccount() + "'";
        if (!searchData.getHouse().equalsIgnoreCase(""))
            sql = sql + " AND houses.number = '" + searchData.getHouse() + "'";
        if (!searchData.getFlat().equalsIgnoreCase(""))
            sql = sql + " AND users.flat_number = '" + searchData.getFlat() + "'";
        if (!searchData.getName().equalsIgnoreCase(""))
            sql = sql + " AND users.full_name LIKE '%" + searchData.getName() + "%'";
        if (!searchData.getStreet().equalsIgnoreCase("")) {
            if (searchData.getStreet().toLowerCase().contains("мал"))
                sql = sql + " AND houses.street LIKE '%" + searchData.getStreet() + "%'";
            else
                sql = sql + " AND users.actual_address LIKE '%" + searchData.getStreet() + "%'";
            }
        sql = sql + ";";
        return sql;
    }

    @Override
    List<User> parseDbResponse(ResultSet resultSet) throws SQLException {
        List<User> userList = new ArrayList<>();
        while (resultSet.next()) {
            User user = new UtmUser();
            String[] userAddress = parseActualAddress(resultSet.getString("users.actual_address"));
            user.setAccount(resultSet.getString("users.basic_account"));
            user.setBalance(resultSet.getDouble("balance"));
//            user.setStreet(resultSet.getString("houses.street"));
            user.setStreet(userAddress[0]);
            user.setHouse(userAddress[1]);
            user.setFlat(resultSet.getString("users.flat_number"));
            user.setName(resultSet.getString("users.full_name"));
//            if (resultSet.getInt("account_tariff_link.is_deleted") == 0)
//                user.setTariff(resultSet.getString("tariffs.name"));
//            else
//                user.setTariff("");
            userList.add(user);
        }
        return userList;
    }

    @Override
    String prepareTarifSql(User user) {
        String sql = "SELECT tariffs.name FROM tariffs " +
                "INNER JOIN account_tariff_link ON account_tariff_link.tariff_id = tariffs.id " +
                "WHERE account_tariff_link.is_deleted = 0 AND account_tariff_link.account_id = " + user.getAccount() +";";
        return sql;
    }

    @Override
    void setTarifs(List<User> users) throws SQLException {
        for (int i=0; i<users.size(); i++){
            String sql = prepareTarifSql(users.get(i));
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()){
                String tariff = resultSet.getString(1);
                users.get(i).setTariff(tariff);
            }
            resultSet.close();
            statement.close();
        }
    }

    @Override
    String selectNamesSql() {
        return "SELECT users.full_name FROM users WHERE users.is_deleted = 0;";
    }

    private String[] parseActualAddress(String actualAddress){
//        System.out.println(actualAddress);
        List<String> addressData = Arrays.asList(actualAddress.split("\\s"));
        List<String> resultList = addressData.stream()
                .filter(x -> !x.isEmpty())
                .filter(x -> !x.equals(","))
                .filter(x -> !x.equals("ул."))
                .filter(x -> !x.equals("пгт"))
                .filter(x -> !x.equals("пгт."))
                .map(x -> x.replaceAll(",", ""))
                .map(x -> x.replaceAll("ул\\.", ""))
                .map(x -> x.replaceAll("марта", "8 Марта"))
                .collect(Collectors.toList());
        if (resultList.size() == 1)
            resultList.add("");
//        System.out.println(resultList);
        String[] result = {resultList.get(resultList.size()-2), resultList.get(resultList.size()-1)};
        return result;
    }
}
