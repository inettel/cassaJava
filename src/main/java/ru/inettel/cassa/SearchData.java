package ru.inettel.cassa;

/**
 * Используется, как контейнер, для передачи данных из MainFormController в DAOHelper
 */

public class SearchData {
    private String street;
    private String house;
    private String flat;
    private String name;
    private String account;
    private boolean inet;
    private boolean tv;

    public SearchData() {
    }

    public String getStreet() {
        return this.street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getHouse() {
        return this.house;
    }

    public void setHouse(String house) {
        this.house = house;
    }

    public String getFlat() {
        return this.flat;
    }

    public void setFlat(String flat) {
        this.flat = flat;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAccount() {
        return this.account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public boolean isInet() {
        return this.inet;
    }

    public void setInet(boolean inet) {
        this.inet = inet;
    }

    public boolean isTv() {
        return this.tv;
    }

    public void setTv(boolean tv) {
        this.tv = tv;
    }
}
