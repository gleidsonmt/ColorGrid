module io.github.gleidsonmt.colorgrid {
    requires javafx.controls;
    requires javafx.fxml;
    requires colors;

    opens io.github.gleidsonmt.colorgrid to javafx.fxml;
    exports io.github.gleidsonmt.colorgrid;
}