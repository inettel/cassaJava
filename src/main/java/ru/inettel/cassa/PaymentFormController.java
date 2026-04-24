package ru.inettel.cassa;

import java.util.Iterator;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ru.inettel.cassa.kkm.AtolKkm;
import ru.inettel.cassa.kkm.KkmExeption;
import ru.inettel.cassa.user.User;

public class PaymentFormController {
    User user;
    double price;
    double cash;
    double delivery;
    @FXML
    private TextField phoneTf;
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

    public PaymentFormController() {
    }

    @FXML
    public void initialize() {
        User user;
        for(Iterator var1 = MainFormController.getPaymentList().iterator(); var1.hasNext(); this.price += user.getCashIn()) {
            user = (User)var1.next();
        }

        this.priceLabel.setText(this.price + " руб.");
        this.cashTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                this.cashTextField.setText(newValue.replaceAll("[^\\d]", ""));
            }

            if (this.cashTextField.getText().isEmpty()) {
                this.cash = 0.0;
            } else {
                this.cash = Double.parseDouble(this.cashTextField.getText());
            }

            this.delivery = this.cash - this.price;
            this.deliveryLabel.setText(String.valueOf(this.delivery));
            this.payBtn.setDisable(this.delivery < 0.0);
        });
        Platform.runLater(() -> {
            this.cashTextField.requestFocus();
        });
    }

    @FXML
    private void doPay() {
        if (!(this.delivery < 0.0) && this.cash != 0.0) {
            this.payBtn.setDisable(true);
            String phone = this.phoneTf.getText();

            try {
                AtolKkm.pay(MainFormController.getPaymentList(), this.cash, 0, phone);
            } catch (KkmExeption var3) {
                KkmExeption e = var3;
                ErrorMessage.show(e);
                this.payBtn.setDisable(false);
            }

        }
    }

    @FXML
    private void close() {
        Stage stage = (Stage)this.closeBtn.getScene().getWindow();
        stage.close();
    }
}
