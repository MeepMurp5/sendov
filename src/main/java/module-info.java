module org.example.sendov {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.sendov to javafx.fxml;
    exports org.example.sendov;
}