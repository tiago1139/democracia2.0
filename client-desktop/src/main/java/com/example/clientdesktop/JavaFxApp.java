package com.example.clientdesktop;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class JavaFxApp extends Application {


    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(JavaFxApp.class.getResource("MainController.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.resizableProperty().setValue(false);
        stage.setTitle("Democracia 2.0 - JavaFX App");

        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() {
        Platform.exit();
    }

    public static void main(String[] args) {
        launch();
    }
}
