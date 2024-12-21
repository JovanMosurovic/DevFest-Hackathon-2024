package com.devfesthackathon.devfesthackathon.app.windows.forcequitwindow;

import com.devfesthackathon.devfesthackathon.app.Window;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ForceQuitWindow extends Window {
    @Override
    public void init(Stage stage) {
        try {
            System.out.println(ForceQuitWindow.class.getResource(""));
            FXMLLoader fxmlLoader = new FXMLLoader(ForceQuitWindow.class.getResource("forcequitwindow.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 320, 240);

            //    scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/styles/styles.css")).toExternalForm());

            this.stage = stage;
            stage.setTitle("SQL Editor");
            stage.setScene(scene);
            Window.centerOnScreen(Window.ABOUT_WINDOW);

            stage.show();

        } catch (IOException e) {
            throw new RuntimeException();
        }
    }
}
