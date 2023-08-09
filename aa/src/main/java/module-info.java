module aa {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;
    requires javafx.media;

    exports view;
    exports model;
    opens view to javafx.fxml;
    opens model to com.google.gson;
}