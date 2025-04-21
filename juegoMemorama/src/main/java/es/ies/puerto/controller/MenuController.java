package es.ies.puerto.controller;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class MenuController {

    @FXML
    private Button playEasy;

    @FXML
    private Button playNormal;

    @FXML
    private Button playHard;

private void startGame(int pairCount, Button button) {
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/es/ies/puerto/jugar.fxml"));
        Parent root = loader.load();

        JugarController controller = loader.getController();
        controller.setPairCount(pairCount);

        Stage stage = (Stage) button.getScene().getWindow(); 
        stage.setScene(new Scene(root));
        stage.show();
    } catch (Exception e) {
        e.printStackTrace();
    }
}


    @FXML private void startEasy() { startGame(4, playEasy); }
    @FXML private void startNormal() { startGame(8, playNormal); }
    @FXML private void startHard() { startGame(18, playHard); }
}
