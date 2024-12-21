package com.devfesthackathon.devfesthackathon.app.windows.mainwindow;

import com.devfesthackathon.devfesthackathon.app.Window;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;


public class MainWindow extends Window {

    /**
     * Initializes the Main Window and sets its properties, such as title, icon, and modality.
     * <p>This method loads the FXML layout, applies the stylesheet, and configures the window's
     * appearance and behavior.</p>
     * <p>The window is centered on the screen and is set to be non-resizable.</p>
     *
     * @param stage The primary {@link Stage} on which this window is displayed.
     */
    @Override
    public void init(Stage stage) {
        try {
            System.out.println(MainWindow.class.getResource(""));
            FXMLLoader fxmlLoader = new FXMLLoader(MainWindow.class.getResource("mainwindow.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 320, 240);

        //    scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/styles/styles.css")).toExternalForm());

            this.stage = stage;
            stage.setTitle("SQL Editor");
            stage.setScene(scene);
            Window.centerOnScreen(Window.MAIN_WINDOW);

            stage.show();

        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

}
