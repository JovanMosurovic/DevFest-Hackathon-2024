module com.devfesthackathon.devfesthackathon {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.devfesthackathon.devfesthackathon.app to javafx.fxml;
    exports com.devfesthackathon.devfesthackathon.app;
    exports com.devfesthackathon.devfesthackathon.app.windows.mainwindow;
    exports com.devfesthackathon.devfesthackathon.app.windows.closewindow;
    opens com.devfesthackathon.devfesthackathon.app.windows.mainwindow to javafx.fxml;
}