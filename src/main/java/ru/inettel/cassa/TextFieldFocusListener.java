package ru.inettel.cassa;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;

public class TextFieldFocusListener implements ChangeListener {

    private TextField tf;

    public TextFieldFocusListener(TextField tf) {
        super();
        this.tf = tf;
    }

    @Override
    public void changed(ObservableValue observable, Object oldValue, Object newValue) {

        Platform.runLater(() -> {
            if (tf.isFocused() && !tf.getText().isEmpty()) tf.selectAll();
        });
    }
}
