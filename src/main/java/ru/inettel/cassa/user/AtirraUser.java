package ru.inettel.cassa.user;

import javafx.beans.property.SimpleStringProperty;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by ksork on 29.06.17.
 */
public class AtirraUser extends User {

    @Override
    SimpleStringProperty setService() {
        return new SimpleStringProperty("ТВ");
    }

    @Override
    public void pay(double sum) {
        String payCommand = String.format("http://192.168.0.10/tv-nal.php?command=pay&account=%s&sum=%.2f&txn_id=%d",
                this.getAccount(), sum, this.getFiscalNum()).replaceAll(",", ".");
        try {
            URL url = new URL(payCommand);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.getResponseCode();
            connection.disconnect();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
