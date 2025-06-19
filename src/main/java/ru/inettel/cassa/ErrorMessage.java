package ru.inettel.cassa;

import javafx.scene.control.Alert;

public class ErrorMessage {

    public static void show(Exception e){
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setHeaderText("Ошибка кассы !!!");
        errorAlert.setContentText(e.getMessage());
        errorAlert.showAndWait();
    }
}
