package ru.inettel.cassa;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    public Main() {
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) {
        Parent root = null;

        try {
            root = (Parent)FXMLLoader.load(this.getClass().getClassLoader().getResource("main_form.fxml"));
        } catch (IOException var4) {
            IOException e = var4;
            e.printStackTrace();
        }

        primaryStage.setTitle("Inettel Касса");
        primaryStage.setMinWidth(800.0);
        primaryStage.setMinHeight(400.0);
        primaryStage.setScene(new Scene(root, 900.0, 500.0));
        primaryStage.show();
    }

    public void stop() throws Exception {
        super.stop();
        System.exit(0);
    }
}
