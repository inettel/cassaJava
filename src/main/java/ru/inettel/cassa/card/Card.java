package ru.inettel.cassa.card;

import ru.atol.drivers10.fptr.IFptr;
import ru.inettel.cassa.ErrorMessage;
import ru.inettel.cassa.MainFormController;
import ru.inettel.cassa.kkm.AtolKkm;
import ru.inettel.cassa.kkm.KkmExeption;
import ru.inettel.cassa.user.User;

import java.io.*;

public class Card {

    private static final String BANK_UTILITY_PATH = "C:/SC552/";
    private static final String BANK_PAYMENT_UTILITY = BANK_UTILITY_PATH + "loadparm.exe";
    private static final String BANK_RESPONSE = BANK_UTILITY_PATH + "p";

    public static void pay(){
        int price = 0;
        for (User user: MainFormController.getPaymentList()){
            price += user.getCashIn();
        }
        String payCommand = BANK_PAYMENT_UTILITY + " 1 " + price * 100;
        try {
            Runtime.getRuntime().exec(payCommand).waitFor();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return;
        }

        File f = new File(BANK_RESPONSE);
        if(f.exists() && !f.isDirectory()) {
            try {
                AtolKkm.pay(MainFormController.getPaymentList(), price, IFptr.LIBFPTR_PT_6, "");
            } catch (KkmExeption e){
                ErrorMessage.show(e);
            }
        }
    }
}
