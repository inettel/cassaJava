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

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getHouse() {
        return house;
    }

    public void setHouse(String house) {
        this.house = house;
    }

    public String getFlat() {
        return flat;
    }

    public void setFlat(String flat) {
        this.flat = flat;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public boolean isInet() {
        return inet;
    }

    public void setInet(boolean inet) {
        this.inet = inet;
    }

    public boolean isTv() {
        return tv;
    }

    public void setTv(boolean tv) {
        this.tv = tv;
    }
}
