package com.devfesthackathon.devfesthackathon.app.windows.closewindow;

import com.devfesthackathon.devfesthackathon.app.Window;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class CloseWindow extends Window {
    @Override
    public void init(Stage stage) {
        try {
            System.out.println(CloseWindow.class.getResource(""));
            FXMLLoader fxmlLoader = new FXMLLoader(CloseWindow.class.getResource("closewindow.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 320, 240);

            //    scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/styles/styles.css")).toExternalForm());

            this.stage = stage;
            stage.setTitle("SQL Editor");
            stage.setScene(scene);
            Window.centerOnScreen(Window.CLOSE_WINDOW);

            stage.show();

        } catch (IOException e) {
            throw new RuntimeException();
        }
    }
}
