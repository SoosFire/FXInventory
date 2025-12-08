module app.fxinventory {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.base;


    opens app.fxinventory to javafx.fxml;
    opens app.fxinventory.Controllers to javafx.fxml;

    exports app.fxinventory;
    exports app.fxinventory.Controllers;

}