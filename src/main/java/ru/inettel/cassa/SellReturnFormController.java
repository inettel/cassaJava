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

    @FXML
    public void initialize() {
        abonentInfoPane.setCollapsible(false);
        user = MainFormController.getSelectedUser();
        nameLabel.setText(user.getName());
        addressLabel.setText(user.getStreet() + " " + user.getHouse() + "-" + user.getFlat());
        accountLabel.setText(String.valueOf(user.getAccount()));
        serviceLabel.setText(user.getService());

        // Делаем cashTextField numeric-only
        cashTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                cashTextField.setText(newValue.replaceAll("[^\\d]", ""));
            }
            doReturnBtn.setDisable(cashTextField.getText().isEmpty());
        });

        doReturnBtn.setOnAction(event -> doReturn());
        closeBtn.setOnAction(event -> close());
    }

    private void doReturn() {
        String account = user.getAccount();
        String service = user.getService();
        String phone = phoneTf.getText();
        double cash = Double.parseDouble(cashTextField.getText());
        try {
            AtolKkm.sellReturn(user, account, cash, service, phone);
            user.pay(0 - cash);
        } catch (KkmExeption e) {
            ErrorMessage.show(e);
        } finally {
            close();
        }
    }

    private void close() {
        Stage stage = (Stage) closeBtn.getScene().getWindow();
        stage.close();
    }
}
