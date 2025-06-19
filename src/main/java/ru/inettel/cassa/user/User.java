package ru.inettel.cassa.user;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Created by ksork on 29.06.17.
 */
public abstract class User {

    private long fiscalNum;

    private SimpleStringProperty name = new SimpleStringProperty();
    private SimpleStringProperty street = new SimpleStringProperty();
    private SimpleStringProperty house = new SimpleStringProperty();
    private SimpleStringProperty flat = new SimpleStringProperty();
    private SimpleStringProperty account = new SimpleStringProperty();
    private SimpleDoubleProperty balance = new SimpleDoubleProperty();
    private SimpleStringProperty tariff = new SimpleStringProperty();
    private SimpleStringProperty service = setService();
    private SimpleDoubleProperty cashIn = new SimpleDoubleProperty();

    public void setFiscalNum(long fiscalNum){
        this.fiscalNum = fiscalNum;
    }

    public long getFiscalNum() {
        return fiscalNum;
    }

    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getStreet() {
        return street.get();
    }

    public SimpleStringProperty streetProperty() {
        return street;
    }

    public void setStreet(String street) {
        this.street.set(street);
    }

    public String getHouse() {
        return house.get();
    }

    public SimpleStringProperty houseProperty() {
        return house;
    }

    public void setHouse(String house) {
        this.house.set(house);
    }

    public String getFlat() {
        return flat.get();
    }

    public SimpleStringProperty flatProperty() {
        return flat;
    }

    public void setFlat(String flat) {
        this.flat.set(flat);
    }

    public String getAccount() {
        return account.get();
    }

    public SimpleStringProperty accountProperty() {
        return account;
    }

    public void setAccount(String account) {
        this.account.set(account);
    }

    public double getBalance() {
        return balance.get();
    }

    public SimpleDoubleProperty balanceProperty() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance.set(balance);
    }

    public String getService() {
        return service.get();
    }

    public SimpleStringProperty serviceProperty() {
        return service;
    }


    public String getTariff() {
        return tariff.get();
    }

    public SimpleStringProperty tariffProperty() {
        return tariff;
    }

    public void setTariff(String tariff) {
        this.tariff.set(tariff);
    }

    abstract SimpleStringProperty setService();

    public double getCashIn() {
        return cashIn.get();
    }

    public SimpleDoubleProperty cashInProperty() {
        return cashIn;
    }

    public void setCashIn(double cashIn) {
        this.cashIn.set(cashIn);
    }

    public abstract void pay(double sum);

    @Override
    public String toString() {
        return String.format("%-8s лс=%-4d %s", getService(), getAccount(), getName());
    }
}
