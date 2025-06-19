package ru.inettel.cassa.kkm;

public class KkmExeption extends Exception {

    public KkmExeption() {
        AtolKkm.disconnect();
    }

    public KkmExeption(String message) {
        super(message);
        AtolKkm.disconnect();
    }
}
