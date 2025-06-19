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

    @FXML
    public void initialize(){
        cashTf.textProperty().addListener((observable, oldValue, newValue) ->{
            if (!newValue.matches("\\d*")) {
                cashTf.setText(newValue.replaceAll("[^\\d]", ""));
            }
            cashOutBtn.setDisable(cashTf.getText().isEmpty());
        });
        cancelBtn.setOnAction((event) -> close());
        cashOutBtn.setOnAction((event) -> cashOut());
    }

    private void cashOut(){
        double cash = Double.parseDouble(cashTf.getText());
        try {
            AtolKkm.cashOut(cash);
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
