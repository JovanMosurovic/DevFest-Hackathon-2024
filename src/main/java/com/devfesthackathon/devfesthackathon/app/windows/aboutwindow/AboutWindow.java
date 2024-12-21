package com.devfesthackathon.devfesthackathon.app.windows.aboutwindow;
import com.devfesthackathon.devfesthackathon.app.Window;
import com.devfesthackathon.devfesthackathon.app.windows.mainwindow.MainWindow;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
public class AboutWindow extends Window{
    @Override
    public void init(Stage stage) {
        try {
            System.out.println(AboutWindow.class.getResource(""));
            FXMLLoader fxmlLoader = new FXMLLoader(AboutWindow.class.getResource("aboutwindow.fxml"));
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
