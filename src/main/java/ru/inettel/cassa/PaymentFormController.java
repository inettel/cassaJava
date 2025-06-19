package ru.inettel.cassa;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import ru.atol.drivers10.fptr.IFptr;
import ru.inettel.cassa.kkm.AtolKkm;
import ru.inettel.cassa.kkm.KkmExeption;
import ru.inettel.cassa.user.User;

/**
 * Created by ksork on 01.07.17.
 */
public class PaymentFormController {

    User user;
    double price;
    double cash;
    double delivery;

    @FXML
    private  TextField phoneTf;
    @FXML
    private Label priceLabel;
    @FXML
    private Label deliveryLabel;
    @FXML
    private TextField cashTextField;
    @FXML
    private Button payBtn;
    @FXML
    private Button closeBtn;

    @FXML
    public void initialize() {
        for (User user: MainFormController.getPaymentList()){
            price += user.getCashIn();
        }
        priceLabel.setText(price + " руб.");

        // Делаем cashTextField numeric-only
        cashTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                cashTextField.setText(newValue.replaceAll("[^\\d]", ""));
            }

            // Расчет сдачи
            if (cashTextField.getText().isEmpty())
                cash = 0;
            else
                cash = Double.parseDouble(cashTextField.getText());

            delivery = cash - price;
            deliveryLabel.setText(String.valueOf(delivery));
            payBtn.setDisable(delivery < 0 );

        });
        Platform.runLater(() -> cashTextField.requestFocus());
    }


    @FXML
    private void doPay(){
        if (delivery < 0 || cash == 0) return;
        payBtn.setDisable(true);
        String phone = phoneTf.getText();
        try {
            AtolKkm.pay(MainFormController.getPaymentList(), cash, IFptr.LIBFPTR_PT_CASH, phone);
        } catch (KkmExeption e){
            ErrorMessage.show(e);
            payBtn.setDisable(false);
        }
// String account = user.getAccount();
//        String service = user.getService();
////        double price = Double.parseDouble(MainFormController.getSum());
//        double cash = Double.parseDouble(cashTextField.getText());
//        try {
////            AtolKkm.pay(user, account, price, cash, service, phone);
////            user.pay(price);
//            close();
//        } catch (KkmExeption e){
//            ErrorMessage.show(e);
//            payBtn.setDisable(false);
//        }
    }

    @FXML
    private void close(){
        Stage stage = (Stage) closeBtn.getScene().getWindow();
        stage.close();
    }
}
