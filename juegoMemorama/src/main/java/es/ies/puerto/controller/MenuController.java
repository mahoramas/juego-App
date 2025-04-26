package es.ies.puerto.controller;

import es.ies.puerto.PrincipalApplication;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.stage.Stage;

public class MenuController {

    @FXML
    private CheckBox modoContrareloj;

    @FXML
    private Button playEasy;

    @FXML
    private Button playNormal;

    @FXML
    private Button playHard;

    @FXML
    private Button onVolverButton;


    /**
     * Iniciador del contador
     * @param pairCount numero de pares de cartas de cada dificultad
     * @param button boton de dificultad que se pulsa
     */
    private void startGame(int pairCount, Button button) {
        try {
            FXMLLoader loader = new FXMLLoader(PrincipalApplication.class.getResource("jugar.fxml"));
            Parent root = loader.load();
    
            JugarController controller = loader.getController();
            controller.setPairCount(pairCount);
            controller.setContrareloj(modoContrareloj.isSelected());
            controller.inicializarJuego();
            double width = 450;
            double height = 800;
    
            switch (pairCount) {
                case 4: 
                    width = 550;
                    height = 300;
                    break;
                case 8: 
                    width = 550;
                    height = 500;
                    break;
                case 18: 
                    width = 750;
                    height = 750;
                    break;
            }
    
            Stage stage = (Stage) button.getScene().getWindow();
            stage.setScene(new Scene(root, width, height));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Metodo para volver a la pantalla de perfil
     */
    @FXML
    protected void openVolverClick() {
        try {
            Stage stage = (Stage) onVolverButton.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(PrincipalApplication.class.getResource("perfil.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 450, 760);
            stage.setTitle("Pantalla Inicio");
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML private void startEasy() { 
        startGame(4, playEasy); 
    }

    @FXML private void startNormal() { 
        startGame(8, playNormal); 
    }

    @FXML private void startHard() { 
        startGame(18, playHard); 
    }
}
