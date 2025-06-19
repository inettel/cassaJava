package ru.inettel.cassa;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ru.inettel.cassa.kkm.AtolKkm;
import ru.inettel.cassa.kkm.KkmExeption;


public class CashInFormController {

    @FXML
    private TextField cashTf;
    @FXML
    private Button cancelBtn;
    @FXML
    private Button cashInBtn;

    @FXML
    public void initialize(){
        cashTf.textProperty().addListener((observable, oldValue, newValue) ->{
            if (!newValue.matches("\\d*")) {
                cashTf.setText(newValue.replaceAll("[^\\d]", ""));
            }
            cashInBtn.setDisable(cashTf.getText().isEmpty());
        });
        cancelBtn.setOnAction((event) -> close());
        cashInBtn.setOnAction((event) -> cashIn());
    }

    private void cashIn(){
        double cash = Double.parseDouble(cashTf.getText());
        try {
            AtolKkm.cashIn(cash);
        } catch (KkmExeption e){
            ErrorMessage.show(e);
        }
        close();
    }

    private void close(){
        Stage stage = (Stage) cancelBtn.getScene().getWindow();
        stage.close();
    }
}
