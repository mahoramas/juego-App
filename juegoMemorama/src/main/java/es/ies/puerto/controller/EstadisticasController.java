package es.ies.puerto.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.sql.SQLException;

import es.ies.puerto.model.*;

public class EstadisticasController {
    @FXML
    private Label victoriasFacilLabel;
    @FXML
    private Label victoriasNormalLabel;
    @FXML
    private Label victoriasDificilLabel;
    @FXML
    private Label mejorTiempoNormalLabel;
    @FXML
    private Label victoriasContrarelojLabel;
    @FXML
    private Label derrotasLabel;
    @FXML
    private Label rachaVictoriaLabel;
    @FXML
    private Label rachaDerrotaLabel;
    @FXML
    private Button cerrarButton;

    /**
     * Metodo que inicializa el controlador
     */
    @FXML
    public void initialize() {
        UsuarioEntity usuario = UsuarioSesion.getInstancia().getUsuario();
    
        try {
    
            if (usuario != null && usuario.getMejorTiempoNormal() > 0) {
                mejorTiempoNormalLabel.setText("Mejor tiempo (normal): " + usuario.getMejorTiempoNormal() + " s");
            } else {
                mejorTiempoNormalLabel.setText("Mejor tiempo (normal): No disponible");
            }
          
        } catch (Exception e) {
            e.printStackTrace();
            mejorTiempoNormalLabel.setText("Error al cargar estadísticas");
        }
        victoriasFacilLabel.setText("Victorias en fácil: " + usuario.getVictoriasFacil());
        victoriasNormalLabel.setText("Victorias en medio: " + usuario.getVictoriasMedio());
        victoriasDificilLabel.setText("Victorias en difícil: " + usuario.getVictoriasDificil());
        rachaVictoriaLabel.setText("Racha de victorias: " + usuario.getRachaVictoria());
        rachaDerrotaLabel.setText("Racha de derrotas: " + usuario.getRachaDerrota());
        victoriasContrarelojLabel.setText("Victorias en contrarreloj: " + usuario.getVictoriasContrareloj());
        derrotasLabel.setText("Derrotas totales: " + usuario.getDerrotasTotales());
    }
    

    /**
     * Metodo para cerrar la ventana emergente
     */
    @FXML
    private void onCerrarClick() {
        Stage stage = (Stage) cerrarButton.getScene().getWindow();
        stage.close();
    }
}
