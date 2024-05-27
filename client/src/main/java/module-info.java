module com.allyedge {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires org.java_websocket;
    requires javafx.base;

    opens com.allyedge to javafx.fxml;

    exports com.allyedge;
}
