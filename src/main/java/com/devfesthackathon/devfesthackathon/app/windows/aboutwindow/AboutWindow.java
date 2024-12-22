package com.devfesthackathon.devfesthackathon.app.windows.aboutwindow;

import com.devfesthackathon.devfesthackathon.app.Window;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

/**
 * Represents the About Window of the application.
 * <p>This window displays information about the application,
 * such as why it was created, the author, version, and other relevant details.</p>
 * <p>The About Window is displayed when the user clicks the "About" button in the Menu.</p>
 * <p>The window provides links to the project's GitHub repository, the author's GitHub page
 * and button to close the window.</p>
 *
 * @see Window
 */
public class AboutWindow extends Window {

    @Override
    public void init(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(AboutWindow.class.getResource("aboutwindow.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 320, 240);

            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/styles/styles.css")).toExternalForm());

            this.stage = stage;
            stage.setTitle("About");
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            Window.centerOnScreen(Window.CLOSE_WINDOW);

        } catch (IOException e) {
            throw new RuntimeException();
        }
    }
}
