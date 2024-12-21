package com.devfesthackathon.devfesthackathon.app.windows.closewindow;

import com.devfesthackathon.devfesthackathon.app.ControllerBase;
import com.devfesthackathon.devfesthackathon.app.Window;

import java.net.URL;
import java.util.ResourceBundle;


public class CloseWindowController extends ControllerBase {
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Window.getWindowAt(Window.CLOSE_WINDOW).setController(this);
    }
}
