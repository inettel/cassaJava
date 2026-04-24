package ru.inettel.cassa.kkm;

import java.util.Iterator;
import java.util.List;
import ru.atol.drivers10.fptr.Fptr;
import ru.atol.drivers10.fptr.IFptr;
import ru.inettel.cassa.user.User;

public class AtolKkm {
    private static IFptr fptr;
    private static String tvPayment = "Кабельное ТВ, лицевой счет - ";
    private static String internetPayment = "Интернет, лицевой счет - ";

    public AtolKkm() {
    }

    public static void pay(List<User> users, double sum, int paymentType, String phone) throws KkmExeption {
        connect();
        fptr.setParam(65545, 1L);
        if (!phone.isEmpty()) {
            fptr.setParam(1008, phone);
        }

        fptr.openReceipt();
        Iterator var5 = users.iterator();

        while (var5.hasNext()) {
            User user = (User) var5.next();
            String serviceName = user.getService().equals("Интернет") ? internetPayment : tvPayment;
            fptr.setParam(65631, serviceName + user.getAccount());
            fptr.setParam(65632, user.getCashIn());
            fptr.setParam(65633, 1L);
            fptr.setParam(65569, 6L);
            fptr.setParam(1212, 4L);
            fptr.setParam(1214, 0L);
            fptr.registration();
        }

        fptr.receiptTotal();
        fptr.setParam(65564, (long) paymentType);
        fptr.setParam(65565, sum);
        fptr.payment();
        fptr.closeReceipt();
        fptr.setParam(65622, 5L);
        fptr.fnQueryData();
        long fiscalNum = fptr.getParamInt(65598);
        disconnect();
        Iterator var10 = users.iterator();

        while (var10.hasNext()) {
            User user = (User) var10.next();
            user.setFiscalNum(fiscalNum);
            user.pay(user.getCashIn());
        }

    }

    public static void pay(User user, String account, double price, double cash, String service, String phone) throws KkmExeption {
        String serviceName = tvPayment;
        if (service.equals("Интернет")) {
            serviceName = internetPayment;
        }

        connect();
        fptr.setParam(65545, 1L);
        if (!phone.isEmpty()) {
            fptr.setParam(1008, phone);
        }

        fptr.openReceipt();
        fptr.setParam(65631, serviceName + account);
        fptr.setParam(65632, price);
        fptr.setParam(65633, 1L);
        fptr.setParam(65569, 6L);
        fptr.setParam(1212, 4L);
        fptr.setParam(1214, 0L);
        fptr.registration();
        fptr.receiptTotal();
        fptr.setParam(65564, 0L);
        fptr.setParam(65565, cash);
        fptr.payment();
        fptr.closeReceipt();
        fptr.setParam(65622, 5L);
        fptr.fnQueryData();
        long fiscalNum = fptr.getParamInt(65598);
        user.setFiscalNum(fiscalNum);
        disconnect();
    }

    public static void sellReturn(User user, String account, double cash, String service, String phone) throws KkmExeption {
        String serviceName = tvPayment;
        if (service.equals("Интернет")) {
            serviceName = internetPayment;
        }

        connect();
        fptr.setParam(65545, 2L);
        if (!phone.isEmpty()) {
            fptr.setParam(1008, phone);
        }

        fptr.openReceipt();
        fptr.setParam(65631, serviceName + account);
        fptr.setParam(65632, cash);
        fptr.setParam(65633, 1L);
        fptr.setParam(65569, 6L);
        fptr.setParam(1212, 4L);
        fptr.setParam(1214, 0L);
        fptr.registration();
        fptr.receiptTotal();
        fptr.setParam(65564, 0L);
        fptr.setParam(65565, cash);
        if (fptr.payment() < 0) {
            String error = fptr.errorDescription();
            fptr.cancelReceipt();
            disconnect();
            throw new KkmExeption(error);
        } else {
            fptr.closeReceipt();
            fptr.setParam(65622, 5L);
            fptr.fnQueryData();
            long fiscalNum = fptr.getParamInt(65598);
            user.setFiscalNum(fiscalNum);
            disconnect();
        }
    }

    public static void xReport() throws KkmExeption {
        connect();
        fptr.setParam(65546, 1L);
        if (fptr.report() < 0) {
            throw new KkmExeption(fptr.errorDescription());
        } else {
            disconnect();
        }
    }

    public static void closeAndShiftReport() throws KkmExeption {
        connect();
        fptr.setParam(65546, 0L);
        if (fptr.report() < 0) {
            throw new KkmExeption(fptr.errorDescription());
        } else {
            disconnect();
        }
    }

    public static void cashIn(double sum) throws KkmExeption {
        connect();
        fptr.setParam(65613, sum);
        fptr.cashIncome();
        disconnect();
    }

    public static void cashOut(double sum) throws KkmExeption {
        connect();
        fptr.setParam(65613, sum);
        if (fptr.cashOutcome() < 0) {
            throw new KkmExeption(fptr.errorDescription());
        } else {
            disconnect();
        }
    }

    private static void connect() throws KkmExeption {
        fptr = new Fptr();
        fptr.setSingleSetting("Port", String.valueOf(2));
        fptr.setSingleSetting("IPAddress", "192.168.100.11");
        fptr.setSingleSetting("IPPort", "5555");
        fptr.setSingleSetting("Model", String.valueOf(57));
        fptr.setSingleSetting("LibraryPath", System.getProperty("java.library.path"));
        fptr.applySingleSettings();
        if (fptr.open() == 0) {
            fptr.setParam(1021, "Норошкин В.А.");
            fptr.setParam(1203, "660300348241");
            fptr.operatorLogin();
            fptr.beep();
        } else {
            throw new KkmExeption(fptr.errorDescription());
        }
    }

    static void disconnect() {
        fptr.close();
    }
}