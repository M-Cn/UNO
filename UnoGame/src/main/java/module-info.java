module org.example.unogame {
    requires javafx.controls;
    requires javafx.fxml;

    exports Main to javafx.graphics;
    opens Scene to javafx.fxml;
}