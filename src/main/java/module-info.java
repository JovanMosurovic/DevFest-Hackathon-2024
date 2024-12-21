module com.devfesthackathon.devfesthackathon {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.api.client.http.apache.v2;
    requires com.google.api.client;
    requires com.google.api.client.json.gson;
    requires com.google.gson;
    requires java.logging;


    opens com.devfesthackathon.devfesthackathon.app to javafx.fxml;
    exports com.devfesthackathon.devfesthackathon.app;
    exports com.devfesthackathon.devfesthackathon.app.windows.mainwindow;
    exports com.devfesthackathon.devfesthackathon.app.windows.closewindow;
    exports com.devfesthackathon.devfesthackathon.app.windows.aboutwindow;
    exports com.devfesthackathon.devfesthackathon.app.windows.forcequitwindow;
    opens com.devfesthackathon.devfesthackathon.app.windows.mainwindow to javafx.fxml;
}