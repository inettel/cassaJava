package ru.inettel.cassa;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ru.inettel.cassa.kkm.AtolKkm;
import ru.inettel.cassa.kkm.KkmExeption;

public class CashOutFormController {
    @FXML
    private TextField cashTf;
    @FXML
    private Button cancelBtn;
    @FXML
    private Button cashOutBtn;

    public CashOutFormController() {
    }

    @FXML
    public void initialize() {
        this.cashTf.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                this.cashTf.setText(newValue.replaceAll("[^\\d]", ""));
            }

            this.cashOutBtn.setDisable(this.cashTf.getText().isEmpty());
        });
        this.cancelBtn.setOnAction((event) -> {
            this.close();
        });
        this.cashOutBtn.setOnAction((event) -> {
            this.cashOut();
        });
    }

    private void cashOut() {
        double cash = Double.parseDouble(this.cashTf.getText());

        try {
            AtolKkm.cashOut(cash);
        } catch (KkmExeption var4) {
            KkmExeption e = var4;
            ErrorMessage.show(e);
        }

        this.close();
    }

    private void close() {
        Stage stage = (Stage)this.cancelBtn.getScene().getWindow();
        stage.close();
    }
}
