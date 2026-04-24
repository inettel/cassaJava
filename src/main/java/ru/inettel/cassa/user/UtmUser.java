package ru.inettel.cassa.user;

import java.io.IOException;
import javafx.beans.property.SimpleStringProperty;

public class UtmUser extends User {
    private static final String UTM5_PAYMENT_TOOL_WIN = "utm5_payment_tool/utm5_payment_tool.exe";
    private static final String UTM5_PAYMENT_TOOL_NIX = "utm5_payment_tool/utm5_payment_tool";
    String os = System.getProperty("os.name").toLowerCase();

    public UtmUser() {
    }

    SimpleStringProperty setService() {
        return new SimpleStringProperty("Интернет");
    }

    public void pay(double sum) {
        String binName = this.os.contains("win") ? "utm5_payment_tool/utm5_payment_tool.exe" : "utm5_payment_tool/utm5_payment_tool";
        String payCommand = String.format("%s -m 1 -a %s -b %.2f -L %d", binName, this.getAccount(), sum, this.getFiscalNum()).replaceAll(",", ".");
        System.out.println(payCommand);
        System.out.println(this.os);
        System.out.println(binName);
        try {
            Runtime.getRuntime().exec(payCommand);
        } catch (IOException var6) {
            IOException e = var6;
            e.printStackTrace();
            System.out.println("Ошибка при выполнении команды оплаты " + e);
        }

    }
}
