package com.devfesthackathon.devfesthackathon.app.windows.mainwindow;

import com.devfesthackathon.devfesthackathon.app.Window;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;


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
            FXMLLoader fxmlLoader = new FXMLLoader(MainWindow.class.getResource("mainwindow.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 1200, 800);

            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/styles/styles.css")).toExternalForm());

            this.stage = stage;
            stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/icons/cropsense_icon.png"))));
            stage.setScene(scene);
            stage.setResizable(false);
            Window.centerOnScreen(Window.MAIN_WINDOW);

            stage.show();

        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

}
