package com.devfesthackathon.devfesthackathon.app.windows.forcequitwindow;

import com.devfesthackathon.devfesthackathon.app.Window;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

/**
 * Represents the Force Quit Window of the application.
 * <p>This window is displayed when the user clicks the "Force Quit" button in the Menu</p>
 * The window prompts the user to confirm the action and provides options to cancel the operation.
 *
 * @see Window
 */
public class ForceQuitWindow extends Window {
    @Override
    public void init(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(ForceQuitWindow.class.getResource("forcequitwindow.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 400, 200);

            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/styles/styles.css")).toExternalForm());

            this.stage = stage;
            stage.setTitle("Warning");
            stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/icons/cropsense_icon.png"))));
            stage.setScene(scene);
            stage.setResizable(false);
            stage.initModality(Modality.APPLICATION_MODAL);
            Window.centerOnScreen(Window.FORCE_QUIT_WINDOW);

        } catch (IOException e) {
            throw new RuntimeException();
        }
    }
}
