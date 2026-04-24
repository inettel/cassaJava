package ru.inettel.cassa;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.stage.Stage;
import ru.inettel.cassa.kkm.AtolKkm;
import ru.inettel.cassa.kkm.KkmExeption;
import ru.inettel.cassa.user.User;

public class SellReturnFormController {
    User user;
    @FXML
    private TitledPane abonentInfoPane;
    @FXML
    private Label nameLabel;
    @FXML
    private Label addressLabel;
    @FXML
    private Label accountLabel;
    @FXML
    private Label serviceLabel;
    @FXML
    private TextField phoneTf;
    @FXML
    private TextField cashTextField;
    @FXML
    private Button doReturnBtn;
    @FXML
    private Button closeBtn;

    public SellReturnFormController() {
    }

    @FXML
    public void initialize() {
        this.abonentInfoPane.setCollapsible(false);
        this.user = MainFormController.getSelectedUser();
        this.nameLabel.setText(this.user.getName());
        this.addressLabel.setText(this.user.getStreet() + " " + this.user.getHouse() + "-" + this.user.getFlat());
        this.accountLabel.setText(String.valueOf(this.user.getAccount()));
        this.serviceLabel.setText(this.user.getService());
        this.cashTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                this.cashTextField.setText(newValue.replaceAll("[^\\d]", ""));
            }

            this.doReturnBtn.setDisable(this.cashTextField.getText().isEmpty());
        });
        this.doReturnBtn.setOnAction((event) -> {
            this.doReturn();
        });
        this.closeBtn.setOnAction((event) -> {
            this.close();
        });
    }

    private void doReturn() {
        String account = this.user.getAccount();
        String service = this.user.getService();
        String phone = this.phoneTf.getText();
        double cash = Double.parseDouble(this.cashTextField.getText());

        try {
            AtolKkm.sellReturn(this.user, account, cash, service, phone);
            this.user.pay(0.0 - cash);
        } catch (KkmExeption var10) {
            KkmExeption e = var10;
            ErrorMessage.show(e);
        } finally {
            this.close();
        }

    }

    private void close() {
        Stage stage = (Stage)this.closeBtn.getScene().getWindow();
        stage.close();
    }

}
