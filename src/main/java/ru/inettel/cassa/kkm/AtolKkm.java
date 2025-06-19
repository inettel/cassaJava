package ru.inettel.cassa.kkm;

import ru.atol.drivers10.fptr.Fptr;
import ru.atol.drivers10.fptr.IFptr;
import ru.inettel.cassa.user.User;

import java.util.List;

public class AtolKkm {

    private static IFptr fptr;
    private static String tvPayment = "Кабельное ТВ, лицевой счет - ";
    private static String internetPayment = "Интернет, лицевой счет - ";

    public static void pay(List<User> users, double sum, int paymentType, String phone) throws KkmExeption {
        connect();
        // Открытие электронного чека (с передачей телефона получателя)
        fptr.setParam(IFptr.LIBFPTR_PARAM_RECEIPT_TYPE, IFptr.LIBFPTR_RT_SELL);
        if (!phone.isEmpty())
            fptr.setParam(1008, phone);
        fptr.openReceipt();
        for (User user: users){
            String serviceName = (user.getService().equals("Интернет")) ? internetPayment : tvPayment;
            // Регистрация позиции
            fptr.setParam(IFptr.LIBFPTR_PARAM_COMMODITY_NAME, serviceName + user.getAccount());
            fptr.setParam(IFptr.LIBFPTR_PARAM_PRICE, user.getCashIn());
            fptr.setParam(IFptr.LIBFPTR_PARAM_QUANTITY, 1);
            fptr.setParam(IFptr.LIBFPTR_PARAM_TAX_TYPE, IFptr.LIBFPTR_TAX_NO);
            // Услуга
            fptr.setParam(1212, 4);
            // Полный расчет
            fptr.setParam(1214, 0);
            fptr.registration();
        }
        fptr.receiptTotal();
        // Оплата наличными
        fptr.setParam(IFptr.LIBFPTR_PARAM_PAYMENT_TYPE, paymentType);
        fptr.setParam(IFptr.LIBFPTR_PARAM_PAYMENT_SUM, sum);
        fptr.payment();
        // Закрытие чека
        fptr.closeReceipt();
        fptr.setParam(IFptr.LIBFPTR_PARAM_FN_DATA_TYPE, IFptr.LIBFPTR_FNDT_LAST_DOCUMENT);
        fptr.fnQueryData();
        long fiscalNum =  fptr.getParamInt(IFptr.LIBFPTR_PARAM_DOCUMENT_NUMBER);
        disconnect();
        for (User user: users){
            user.setFiscalNum(fiscalNum);
            user.pay(user.getCashIn());
        }
    }

    public static void pay(User user, String account, double price, double cash, String service, String phone) throws KkmExeption {
        String serviceName = tvPayment;
        if (service.equals("Интернет"))
            serviceName = internetPayment;
        connect();
        // Открытие электронного чека (с передачей телефона получателя)
        fptr.setParam(IFptr.LIBFPTR_PARAM_RECEIPT_TYPE, IFptr.LIBFPTR_RT_SELL);
        if (!phone.isEmpty())
            fptr.setParam(1008, phone);
        fptr.openReceipt();
        // Регистрация позиции
        fptr.setParam(IFptr.LIBFPTR_PARAM_COMMODITY_NAME, serviceName + account);
        fptr.setParam(IFptr.LIBFPTR_PARAM_PRICE, price);
        fptr.setParam(IFptr.LIBFPTR_PARAM_QUANTITY, 1);
        fptr.setParam(IFptr.LIBFPTR_PARAM_TAX_TYPE, IFptr.LIBFPTR_TAX_NO);
        // Услуга
        fptr.setParam(1212, 4);
        // Полный расчет
        fptr.setParam(1214, 0);
        fptr.registration();
        // Регистрация итога (отрасываем копейки)
//        fptr.setParam(IFptr.LIBFPTR_PARAM_SUM, 369.0);
        fptr.receiptTotal();
        // Оплата наличными
        fptr.setParam(IFptr.LIBFPTR_PARAM_PAYMENT_TYPE, IFptr.LIBFPTR_PT_CASH);
        fptr.setParam(IFptr.LIBFPTR_PARAM_PAYMENT_SUM, cash);
        fptr.payment();
        // Закрытие чека
        fptr.closeReceipt();
        // Запрос информации о закрытом чеке
        fptr.setParam(IFptr.LIBFPTR_PARAM_FN_DATA_TYPE, IFptr.LIBFPTR_FNDT_LAST_DOCUMENT);
        fptr.fnQueryData();
//        System.out.println(String.format("Fiscal Sign = %s", fptr.getParamString(IFptr.LIBFPTR_PARAM_FISCAL_SIGN)));
        long fiscalNum =  fptr.getParamInt(IFptr.LIBFPTR_PARAM_DOCUMENT_NUMBER);
        user.setFiscalNum(fiscalNum);
        disconnect();
    }

    public static void sellReturn(User user, String account, double cash, String service, String phone) throws KkmExeption {
        String serviceName = tvPayment;
        if (service.equals("Интернет"))
            serviceName = internetPayment;
        connect();
        // Открытие электронного чека (с передачей телефона получателя)
        fptr.setParam(IFptr.LIBFPTR_PARAM_RECEIPT_TYPE, IFptr.LIBFPTR_RT_SELL_RETURN);
        if (!phone.isEmpty())
            fptr.setParam(1008, phone);
        fptr.openReceipt();
        // Регистрация позиции
        fptr.setParam(IFptr.LIBFPTR_PARAM_COMMODITY_NAME, serviceName + account);
        fptr.setParam(IFptr.LIBFPTR_PARAM_PRICE, cash);
        fptr.setParam(IFptr.LIBFPTR_PARAM_QUANTITY, 1);
        fptr.setParam(IFptr.LIBFPTR_PARAM_TAX_TYPE, IFptr.LIBFPTR_TAX_NO);
        // Услуга
        fptr.setParam(1212, 4);
        // Полный расчет
        fptr.setParam(1214, 0);
        fptr.registration();
        fptr.receiptTotal();
        // Оплата наличными
        fptr.setParam(IFptr.LIBFPTR_PARAM_PAYMENT_TYPE, IFptr.LIBFPTR_PT_CASH);
        fptr.setParam(IFptr.LIBFPTR_PARAM_PAYMENT_SUM, cash);
        if(fptr.payment() < 0){
            String error = fptr.errorDescription();
            fptr.cancelReceipt();
            disconnect();
            throw new KkmExeption(error);
        }
        // Закрытие чека
        fptr.closeReceipt();
        // Запрос информации о закрытом чеке
        fptr.setParam(IFptr.LIBFPTR_PARAM_FN_DATA_TYPE, IFptr.LIBFPTR_FNDT_LAST_DOCUMENT);
        fptr.fnQueryData();
//        System.out.println(String.format("Fiscal Sign = %s", fptr.getParamString(IFptr.LIBFPTR_PARAM_FISCAL_SIGN)));
        long fiscalNum =  fptr.getParamInt(IFptr.LIBFPTR_PARAM_DOCUMENT_NUMBER);
        user.setFiscalNum(fiscalNum);
        disconnect();
    }

    public static void xReport() throws KkmExeption {
        connect();
        // Отчет Без гашения
        fptr.setParam(IFptr.LIBFPTR_PARAM_REPORT_TYPE, IFptr.LIBFPTR_RT_X);
        if(fptr.report() < 0)
            throw new KkmExeption(fptr.errorDescription());
        disconnect();
    }

    public static void closeAndShiftReport() throws KkmExeption {
        connect();
        // Отчет о закрытии смены
        fptr.setParam(IFptr.LIBFPTR_PARAM_REPORT_TYPE, IFptr.LIBFPTR_RT_CLOSE_SHIFT);
        if(fptr.report() < 0)
            throw new KkmExeption(fptr.errorDescription());
        disconnect();
    }

    public static void cashIn(double sum) throws KkmExeption {
        connect();
        fptr.setParam(IFptr.LIBFPTR_PARAM_SUM, sum);
        fptr.cashIncome();
        disconnect();
    }

    public static void cashOut(double sum) throws KkmExeption {
        connect();
        fptr.setParam(IFptr.LIBFPTR_PARAM_SUM, sum);
        if (fptr.cashOutcome() < 0)
            throw new KkmExeption(fptr.errorDescription());
        disconnect();
    }

    private static void connect() throws KkmExeption {
        fptr = new Fptr();
        fptr.setSingleSetting(IFptr.LIBFPTR_SETTING_PORT, String.valueOf(IFptr.LIBFPTR_PORT_TCPIP));
        fptr.setSingleSetting(IFptr.LIBFPTR_SETTING_IPADDRESS, "192.168.100.11");
        fptr.setSingleSetting(IFptr.LIBFPTR_SETTING_IPPORT, "5555");
        fptr.setSingleSetting(IFptr.LIBFPTR_SETTING_MODEL, String.valueOf(IFptr.LIBFPTR_MODEL_ATOL_25F));
        fptr.setSingleSetting(IFptr.LIBFPTR_SETTING_LIBRARY_PATH, System.getProperty("java.library.path"));
        fptr.applySingleSettings();
        // Соединение с ККТ
        if (fptr.open() == 0) {
            // Регистрация кассира
            fptr.setParam(1021, "Норошкин В.А.");
            fptr.setParam(1203, "660300348241");
            fptr.operatorLogin();
            fptr.beep();
        } else {
            throw new KkmExeption(fptr.errorDescription());
        }
    }

    static void disconnect(){
        fptr.close();
    }
}
