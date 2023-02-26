package io.github.gleidsonmt.colorgrid;

import eu.hansolo.colors.ColorHelper;
import eu.hansolo.colors.MaterialDesign;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Gleidson Neves da Silveira | gleidisonmt@gmail.com
 * Version 0.0.1
 * Create on  26/02/2023
 */
public class ColorsController implements Initializable {

    private static final Pattern PATTERN    = Pattern.compile("(_[A]?[0-9]{2,3})");
    private static final Matcher MATCHER    = PATTERN.matcher("");
    private static final int     BOX_WIDTH  = 100;
    private static final int     BOX_HEIGHT = 60;

    private final ToggleGroup group = new ToggleGroup();

    @FXML
    private GridPane grid;
    @FXML private VBox boxColor;
    @FXML private TextField textColor;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        int  col  = 0;
        int  row  = 1;

        for (MaterialDesign color : MaterialDesign.values()) {
            String name     = color.name().replace("_", " ");
            String strWeb   = ColorHelper.web(color.get());
            String strRgb   = ColorHelper.rgb(color.get());
            String text     = String.join("", name, "\n", strWeb, "\n", strRgb);

            MATCHER.reset(color.name());
            String brightness = "";
            while (MATCHER.find()) {
                brightness = MATCHER.group(1).replace("_", " ");
            }

            ToggleButton toggle = new ToggleButton(
                    color.name().contains("0") ? "" : color.name() + "\n" + brightness);
            toggle.setToggleGroup(group);
            toggle.setAlignment(Pos.CENTER);
            toggle.setTextFill(Color.WHITE);
            toggle.setCursor(Cursor.HAND);
            toggle.setAlignment(Pos.CENTER);
            toggle.setPrefSize(BOX_WIDTH, BOX_HEIGHT);


            boolean isBox = false;

            if(!color.name().contains("0")){
                toggle.setMouseTransparent(true);
                toggle.setStyle("-fx-background-color : white; -fx-text-fill : -gray;" +
                        "-fx-border-color : transparent transparent -light-gray transparent;" +
                        "-fx-background-radius : 0;");
//                toggle.setMinWidth(200);
//                toggle.setPrefWidth(150D);
            } else {
                toggle.setStyle("-fx-background-color : " +  formatHexString(color.get()) + ";" +
                        "-fx-background-radius : 0;"

                );

                isBox = true;
            }

            toggle.setOnMousePressed(event -> {
                String clipboardContent = "Color.web(\"" +
                        strWeb +
                        "\");\n" +
                        "Color." +
                        strRgb +
                        ";";

                Clipboard clipboard  = Clipboard.getSystemClipboard();
                ClipboardContent content = new ClipboardContent();
                content.putString(clipboardContent);
                clipboard.setContent(content);


//                System.out.println(color.get().getBrightness());

                if(color.get().getBrightness() > 0.75){
                    boxColor.setStyle("-fx-background-color : " + formatHexString(color.get()) + "; -inner-text : -dark-gray;");

                } else {
                    boxColor.setStyle("-fx-background-color : " + formatHexString(color.get()) + "; -inner-text : white;");
                }
                textColor.setText(formatHexString(color.get()));

//                if(color.get().getBrightness() > 0.75){
//                    App.getDecorator().getScene().getRoot().lookup(".drawer")
//                            .setStyle("-drawer-color : "  +  formatHexString(color.get()) + ";" +
//                                    "-inner-text : black;"
//                            );
//                } else {
//                    App.getDecorator().getScene().getRoot().lookup(".drawer")
//                            .setStyle("-drawer-color : "  +  formatHexString(color.get()) + ";" +
//                                    "-inner-text : white;"
//                            );
//                }



//                App.getDecorator().getScene().getRoot().
////                toggle.getScene().getRoot().
//                        setStyle("-drawer-color : "  +  formatHexString(color.get()) + ";");
            });

            boolean finalIsBox = isBox;
            toggle.setOnMouseEntered(event -> {
                if(finalIsBox){
                    toggle.setBackground(new Background(new BackgroundFill(color.get(), new CornerRadii(10), Insets.EMPTY)));
                    toggle.setStyle("-fx-background-radius : 10px; -fx-background-color : " + formatHexString(color.get())
                            + ";");
                }
            });

            toggle.setOnMouseExited(event -> {
                if(finalIsBox){
                    toggle.setStyle("-fx-background-radius : 0px; -fx-background-color : " + formatHexString(color.get()) + ";");
                }
            });

            if(finalIsBox){
                toggle.setToggleGroup(group);
            }

            Tooltip tooltip = new Tooltip(text);
            Tooltip.install(toggle, tooltip);
            grid.add(toggle, row, col);
            col++;

            if (row > 16) {
                if (col == 11) {
                    col = 0;
                    row++;
                }
            } else {
                if (col == 15) {
                    col = 0;
                    row++;
                }
            }
        }
        grid.setHgap(1);
        grid.setVgap(1);
        grid.getRowConstraints().clear();
        grid.getColumnConstraints().clear();

    }

    private static String formatHexString(Color c) {
        if (c != null) {
            return String.format((Locale) null, "#%02x%02x%02x",
                    Math.round(c.getRed() * 255),
                    Math.round(c.getGreen() * 255),
                    Math.round(c.getBlue() * 255));
        } else {
            return null;
        }
    }
}
