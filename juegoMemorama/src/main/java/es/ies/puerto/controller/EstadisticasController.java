package es.ies.puerto.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import es.ies.puerto.model.UsuarioSesion;
import es.ies.puerto.model.UsuarioEntity;

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
    private Button cerrarButton;

    /**
     * Método que se ejecuta al inicializar la ventana de estadísticas.
     * Muestra las estadísticas del usuario actual en la interfaz gráfica.
     */
    @FXML
    public void initialize() {
        UsuarioEntity usuario = UsuarioSesion.getInstancia().getUsuario();

        victoriasFacilLabel.setText("Victorias en fácil: " + usuario.getVictoriasFacil());
        victoriasNormalLabel.setText("Victorias en normal: " + usuario.getVictoriasNormal());
        victoriasDificilLabel.setText("Victorias en difícil: " + usuario.getVictoriasDificil());

        if (usuario.getMejorTiempoNormal() != null) {
            mejorTiempoNormalLabel.setText("Mejor tiempo (normal): " + usuario.getMejorTiempoNormal() + " s");
        } else {
            mejorTiempoNormalLabel.setText("Mejor tiempo (normal): No disponible");
        }

        victoriasContrarelojLabel.setText("Victorias en contrarreloj: " + usuario.getVictoriasContrareloj());
        derrotasLabel.setText("Derrotas totales: " + usuario.getDerrotasTotales());
    }

    /**
     * Método que se ejecuta al hacer clic en el botón de cerrar.
     * Cierra la ventana de estadísticas.
     */
    @FXML
    private void onCerrarClick() {
        Stage stage = (Stage) cerrarButton.getScene().getWindow();
        stage.close();
    }
}
