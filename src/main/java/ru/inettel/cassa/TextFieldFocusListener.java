package ru.inettel.cassa;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;

public class TextFieldFocusListener implements ChangeListener {
    private TextField tf;

    public TextFieldFocusListener(TextField tf) {
        this.tf = tf;
    }

    public void changed(ObservableValue observable, Object oldValue, Object newValue) {
        Platform.runLater(() -> {
            if (this.tf.isFocused() && !this.tf.getText().isEmpty()) {
                this.tf.selectAll();
            }

        });
    }
}
