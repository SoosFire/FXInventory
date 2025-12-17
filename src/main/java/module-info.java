module app.fxinventory {
    // JavaFX moduler, der bruges i projektet (UI, FXML, grafik og base-klasser).
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.base;

    // JDBC / database-adgang (bruges til MySQL).
    requires java.sql;

    // Gør pakkestrukturen tilgængelig for JavaFX' FXML-loader (reflection).
    opens app.fxinventory to javafx.fxml;
    opens app.fxinventory.Controllers to javafx.fxml;

    // Eksporterer disse pakker, så de kan bruges af andre moduler.
    exports app.fxinventory;
    exports app.fxinventory.Controllers;

}
