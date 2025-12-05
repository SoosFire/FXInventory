package app.fxinventory;

import app.fxinventory.Inventory.Inventory;
import app.fxinventory.Shop.Shop;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.text.Font;

import java.io.IOException;

public class Main extends Application {

    private static final Inventory INVENTORY = new Inventory();
    private static final Shop SHOP = new Shop();

    public static Inventory getInventory() {
        return INVENTORY;
    }

    public static Shop getShop() {
        return SHOP;
    }

    @Override
    public void start(Stage stage) throws IOException {


        var url = getClass().getResource("/app/fxinventory/Fonts/Garet-Book.ttf");
        System.out.println("Font URL: " + url); // should NOT be null

        Font f = Font.loadFont(url.toExternalForm(), 20);
        System.out.println("Loaded font: " + f);
        if (f != null) {
            System.out.println("Font name to use in CSS: " + f.getName());
        }

        FXMLLoader loader = new FXMLLoader(
                Main.class.getResource("Main.fxml"));

        Scene scene = new Scene(loader.load(), 1000, 750);
        stage.setTitle("Legend of CodeCraft");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
}
