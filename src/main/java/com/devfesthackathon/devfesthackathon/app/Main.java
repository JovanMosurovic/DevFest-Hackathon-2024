package com.devfesthackathon.devfesthackathon.app;


import com.devfesthackathon.devfesthackathon.app.windows.closewindow.CloseWindow;
import com.devfesthackathon.devfesthackathon.app.windows.mainwindow.MainWindow;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * The main class that launches the JavaFX application.
 */
public class Main extends Application {

    /**
     * Starts the JavaFX application by setting up the main window and showing the welcome window.
     *
     * @param primaryStage the primary {@link Stage} of the application
     */
    @Override
    public void start(Stage primaryStage) {
        Window.setWindowAt(Window.CLOSE_WINDOW, new CloseWindow());
        Window.setWindowAt(Window.MAIN_WINDOW, new MainWindow());

        Window.initAllWindows();
    }

    /**
     * Launches the JavaFX application.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}