package com.devfesthackathon.devfesthackathon.app.windows.mainwindow;



import com.devfesthackathon.devfesthackathon.app.ControllerBase;
import com.devfesthackathon.devfesthackathon.app.Window;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for the Main Window in the application.
 * <p>This class handles the user interactions within the Main Window,
 * such as executing SQL queries, importing databases, saving changes,
 * and displaying the results in the console and result tabs.</p>
 *
 * @see ControllerBase
 * @see Window
 */
public class MainWindowController extends ControllerBase {

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Window.getWindowAt(Window.MAIN_WINDOW).setController(this);
    }

}
