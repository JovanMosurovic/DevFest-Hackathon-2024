package com.devfesthackathon.devfesthackathon.app.windows.forcequitwindow;

import com.devfesthackathon.devfesthackathon.app.ControllerBase;
import com.devfesthackathon.devfesthackathon.app.Window;

import java.net.URL;
import java.util.ResourceBundle;

public class ForceQuitWindowController extends ControllerBase {
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Window.getWindowAt(Window.FORCEQUIT_WINDOW).setController(this);
    }
}
