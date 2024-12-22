package com.devfesthackathon.devfesthackathon.app.windows.closewindow;

import com.devfesthackathon.devfesthackathon.app.Window;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

/**
 * Represents the Closing Window of the application.
 * <p>This window is displayed when the user attempts to close the application without saving
 * the changes made to the SQL script. </p>
 * The window prompts the user to confirm the action and
 * provides options to save the script, discard the changes, or cancel the operation.
 *
 * @see Window
 */
public class CloseWindow extends Window {
    @Override
    public void init(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(CloseWindow.class.getResource("closewindow.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 320, 240);

            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/styles/styles.css")).toExternalForm());

            this.stage = stage;
            stage.setTitle("CropSense");
            stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/icons/cropsense_icon.png"))));
            stage.setScene(scene);
            stage.setResizable(false);
            stage.initModality(Modality.APPLICATION_MODAL);
            Window.centerOnScreen(Window.CLOSE_WINDOW);

        } catch (IOException e) {
            throw new RuntimeException();
        }
    }
}
