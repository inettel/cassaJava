package ru.inettel.cassa.dao;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import ru.inettel.cassa.SearchData;
import ru.inettel.cassa.user.User;
import ru.inettel.cassa.user.UtmUser;

public class UtmDAO extends BillingDAO {
    public UtmDAO() {
    }

    void startConnect() throws ClassNotFoundException, SQLException {
        String url = "jdbc:mysql://192.168.0.17:3306/UTM5?useUnicode=true&characterEncoding=utf-8";
        String username = "guest";
        String password = "8845svmg";
        Class.forName("com.mysql.cj.jdbc.Driver");
        this.connection = DriverManager.getConnection(url, username, password);
    }

    String prepareSql(SearchData searchData) {
        String sql = "SELECT users.basic_account, ROUND(accounts.balance, 2) AS balance, houses.street, users.actual_address, houses.number, users.flat_number, users.full_name FROM users INNER JOIN accounts ON users.basic_account = accounts.id INNER JOIN houses ON users.house_id = houses.id WHERE users.is_deleted = 0 AND accounts.is_deleted = 0";
        if (!searchData.getAccount().equalsIgnoreCase("")) {
            sql = sql + " AND users.basic_account = '" + searchData.getAccount() + "'";
        }

        if (!searchData.getHouse().equalsIgnoreCase("")) {
            sql = sql + " AND houses.number = '" + searchData.getHouse() + "'";
        }

        if (!searchData.getFlat().equalsIgnoreCase("")) {
            sql = sql + " AND users.flat_number = '" + searchData.getFlat() + "'";
        }

        if (!searchData.getName().equalsIgnoreCase("")) {
            sql = sql + " AND users.full_name LIKE '%" + searchData.getName() + "%'";
        }

        if (!searchData.getStreet().equalsIgnoreCase("")) {
            if (searchData.getStreet().toLowerCase().contains("мал")) {
                sql = sql + " AND houses.street LIKE '%" + searchData.getStreet() + "%'";
            } else {
                sql = sql + " AND users.actual_address LIKE '%" + searchData.getStreet() + "%'";
            }
        }

        sql = sql + ";";
        return sql;
    }

    List<User> parseDbResponse(ResultSet resultSet) throws SQLException {
        List<User> userList = new ArrayList();

        while(resultSet.next()) {
            User user = new UtmUser();
            String[] userAddress = this.parseActualAddress(resultSet.getString("users.actual_address"));
            ((User)user).setAccount(resultSet.getString("users.basic_account"));
            ((User)user).setBalance(resultSet.getDouble("balance"));
            ((User)user).setStreet(userAddress[0]);
            ((User)user).setHouse(userAddress[1]);
            ((User)user).setFlat(resultSet.getString("users.flat_number"));
            ((User)user).setName(resultSet.getString("users.full_name"));
            userList.add(user);
        }

        return userList;
    }

    String prepareTarifSql(User user) {
        String sql = "SELECT tariffs.name FROM tariffs INNER JOIN account_tariff_link ON account_tariff_link.tariff_id = tariffs.id WHERE account_tariff_link.is_deleted = 0 AND account_tariff_link.account_id = " + user.getAccount() + ";";
        return sql;
    }

    void setTarifs(List<User> users) throws SQLException {
        for(int i = 0; i < users.size(); ++i) {
            String sql = this.prepareTarifSql((User)users.get(i));
            Statement statement = this.connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            while(resultSet.next()) {
                String tariff = resultSet.getString(1);
                ((User)users.get(i)).setTariff(tariff);
            }

            resultSet.close();
            statement.close();
        }

    }

    String selectNamesSql() {
        return "SELECT users.full_name FROM users WHERE users.is_deleted = 0;";
    }

    private String[] parseActualAddress(String actualAddress) {
        List<String> addressData = Arrays.asList(actualAddress.split("\\s"));
        List<String> resultList = (List)addressData.stream().filter((x) -> {
            return !x.isEmpty();
        }).filter((x) -> {
            return !x.equals(",");
        }).filter((x) -> {
            return !x.equals("ул.");
        }).filter((x) -> {
            return !x.equals("пгт");
        }).filter((x) -> {
            return !x.equals("пгт.");
        }).map((x) -> {
            return x.replaceAll(",", "");
        }).map((x) -> {
            return x.replaceAll("ул\\.", "");
        }).map((x) -> {
            return x.replaceAll("марта", "8 Марта");
        }).collect(Collectors.toList());
        if (resultList.size() == 1) {
            resultList.add("");
        }

        String[] result = new String[]{(String)resultList.get(resultList.size() - 2), (String)resultList.get(resultList.size() - 1)};
        return result;
    }
}
