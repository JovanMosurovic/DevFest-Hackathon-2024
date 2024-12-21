package com.devfesthackathon.devfesthackathon.app.windows.aboutwindow;

import com.devfesthackathon.devfesthackathon.app.ControllerBase;
import com.devfesthackathon.devfesthackathon.app.Window;

import java.net.URL;
import java.util.ResourceBundle;

public class AboutWindowController extends ControllerBase {
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Window.getWindowAt(Window.ABOUT_WINDOW).setController(this);
    }
}
