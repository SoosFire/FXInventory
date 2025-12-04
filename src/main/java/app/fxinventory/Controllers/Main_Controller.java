package app.fxinventory.Controllers;

import app.fxinventory.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class Main_Controller {

    private Parent root;
    private Stage stage;
    private Scene scene;

    @FXML
    protected void onStartGame(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Main.class.getResource("Game_Home.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root,1000,750);
        stage.setScene(scene);
        stage.show();

    }
}
