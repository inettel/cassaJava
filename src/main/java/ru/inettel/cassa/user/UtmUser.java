package ru.inettel.cassa.user;

import javafx.beans.property.SimpleStringProperty;

import java.io.IOException;

/**
 * Created by ksork on 29.06.17.
 */
public class UtmUser extends User {

    private static final String UTM5_PAYMENT_TOOL_WIN = "utm5_payment_tool/utm5_payment_tool.exe";
    private static final String UTM5_PAYMENT_TOOL_NIX = "utm5_payment_tool/utm5_payment_tool";
    String os = System.getProperty("os.name").toLowerCase();

    @Override
    SimpleStringProperty setService() {
        return new SimpleStringProperty("Интернет");
    }

    @Override
    public void pay(double sum) {
        String binName = os.contains("win") ? UTM5_PAYMENT_TOOL_WIN : UTM5_PAYMENT_TOOL_NIX;
        String payCommand = String.format("%s -m 1 -a %s -b %.2f -L %d", binName, this.getAccount(), sum, this.getFiscalNum())
                .replaceAll(",", ".");
//        System.out.println(payCommand);
        try {
            Runtime.getRuntime().exec(payCommand);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
